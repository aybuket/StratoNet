import Common.*;
import NasaConnection.NasaApodConnection;
import NasaConnection.NasaInsightWeatherConnection;
import Types.Apod;
import Types.InsightWeather;
import Types.Sol;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class ServerThread extends Thread {
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream objectOutputStream;
    protected InputStream in;
    protected OutputStream out;
    protected Socket socket;
    protected Socket authenticatedSocket;
    protected int authenticatedPort;
    protected Phase phase;
    protected AuthenticationInfo info;

    public ServerThread(Socket s)
    {
        this.socket = s;
        this.phase = Phase.INITIALIZATION;
    }

    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            dataInputStream = new DataInputStream(in);
            dataOutputStream = new DataOutputStream(out);
        } catch (IOException e) {
            System.err.println("[ServerConnection]: Run. IO error in server thread");
            e.printStackTrace();
        }

        try {
            while (!phase.equals(Phase.QUIT))
            {
                switch (phase)
                {
                    case INITIALIZATION -> authenticate();
                    case QUERYING -> acceptQuery();
                    default -> connectionFinally();
                }
            }

        } catch (IOException e) {
            System.err.println("[ServerConnection]: Run. IO Error/ Client " + this.getName() + " terminated abruptly");
        } catch (NullPointerException e) {
            System.err.println("[ServerConnection]: Run.Client " + this.getName() + " Closed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectionFinally();
        }
    }

    private void authenticate(){
        System.out.println("Authentication Phase.");
        try {
            int wholeSize = dataInputStream.available();
            while (wholeSize < 1)
            {
                wholeSize = dataInputStream.available();
            }
            byte[] data = new byte[wholeSize];
            dataInputStream.readFully(data);
            System.out.println(data);
            byte[] header = Arrays.copyOfRange(data, 0,6);
            int size = Authentication.checkAuthRequestHeader(header);
            String username = "";
            if (size != -1)
            {
                data = Arrays.copyOfRange(data, 6,6+size);
                username = new String(data, StandardCharsets.UTF_8);
            }
            if (size == -1 || !Authentication.checkUsername(username)) {
                System.out.println("User does not exist.");
                sendToClientCommandLine(Authentication.Auth_Fail_User());
                phase = Phase.QUIT;
                connectionFinally();
                return;
            }

            sendToClientCommandLine(Authentication.Auth_Challenge_Password());

            boolean passwordCheck = false;
            int failureCount = 0;
            while (!passwordCheck && failureCount < Authentication.getFailureLimit())
            {
                wholeSize = dataInputStream.available();
                while (wholeSize < 1)
                {
                    wholeSize = dataInputStream.available();
                }
                data = new byte[wholeSize];
                dataInputStream.readFully(data);
                header = Arrays.copyOfRange(data, 0,6);
                size = Authentication.checkAuthRequestHeader(header);
                String password = "";
                if (size != -1)
                {
                    data = Arrays.copyOfRange(data, 6,6+size);
                    password = new String(data, StandardCharsets.UTF_8);
                }
                passwordCheck = Authentication.checkPassword(username, password);
                if (!passwordCheck)
                {
                    sendToClientCommandLine(Authentication.Auth_Fail_Password());
                    failureCount++;
                }
            }

            if(!passwordCheck)
            {
                sendToClientCommandLine(Authentication.Auth_Fail_Timeout());
                phase = Phase.QUIT;
                connectionFinally();
                return;
            }

            //int port = newPortSocket();
            sendToClientCommandLine(Authentication.Auth_Success(StratoNetServer.DEFAULT_SERVER_PORT));
            //authenticatedStreams();
            phase = Phase.QUERYING;

        } catch (SocketTimeoutException timeoutException)
        {
            System.err.println("[ServerConnection]: Run. Timeout " + this.getName() + " terminated abruptly");
            connectionFinally();
        } catch (IOException e)
        {
            System.err.println("[ServerConnection]: Run. IO Error/ Client " + this.getName() + " terminated abruptly");
        }
    }

    private void acceptQuery() throws IOException, ClassNotFoundException
    {
        objectInputStream = new ObjectInputStream(in);
        objectOutputStream = new ObjectOutputStream(out);
        Request request = (Request) objectInputStream.readObject();
        if (request.getType().equals(RequestType.APOD)) {
            NasaApodConnection connection = new NasaApodConnection();
            connection.setParameters((ApodRequest) request);
            connection.buildRequest().sendSyncRequest();
            connection.convertResponse();
            Apod apodObject = connection.getApodObject();
            objectOutputStream.writeObject(apodObject);
            objectOutputStream.flush();
        }
        else if (request.getType().equals(RequestType.INSIGHT_WEATHER))
        {
            NasaInsightWeatherConnection connection = new NasaInsightWeatherConnection();
            connection.buildRequest().sendSyncRequest();
            connection.convertResponse();
            InsightWeather insightWeatherObject = connection.getInsideWeatherObject();
            int randomKeyIndex = new Random().nextInt(insightWeatherObject.getSolKeys().length);
            Sol randomSol = insightWeatherObject.getSols().get(insightWeatherObject.getSolKeys()[randomKeyIndex]);
            objectOutputStream.writeObject(randomSol.getPre());
            objectOutputStream.flush();
        }
        else {
            phase = Phase.QUIT;
        }

    }
    private void sendToClientCommandLine(byte[] obj) throws IOException
    {
        dataOutputStream.write(obj);
        dataOutputStream.flush();
    }

    private void connectionFinally() {
        try {
            System.out.println("[ServerThread]: Closing the connection");
            if (dataInputStream != null) {
                dataInputStream.close();
                System.err.println("[ServerThread]: Socket Data Input Stream Closed");
            }

            if (objectInputStream != null) {
                objectInputStream.close();
                System.err.println("[ServerThread]: Socket Object Input Stream Closed");
            }

            if (dataOutputStream != null) {
                dataOutputStream.close();
                System.err.println("[ServerThread]: Socket Data Output Stream Closed");
            }

            if (objectOutputStream != null) {
                objectOutputStream.close();
                System.err.println("[ServerThread]: Socket Object Output Stream Closed");
            }

            if (socket != null) {
                socket.close();
                System.err.println("[ServerThread]: Socket Closed");
            }
        } catch (IOException ie) {
            System.err.println("[ServerThread]: Socket Close Error");
        }
    }
}
