import Common.ApodRequest;
import Common.ApplicationType;
import Common.Authentication;
import Common.Request;
import NasaConnection.NasaApodConnection;
import Types.Apod;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class ServerThread extends Thread{
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream objectOutputStream;
    protected Socket s;
    private String line = "";
    private String lines = "";

    public ServerThread(Socket s)
    {
        this.s = s;
    }

    public void run()
    {
        try
        {
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
        }
        catch (IOException e)
        {
            System.err.println("[ServerConnection]: Run. IO error in server thread");
            e.printStackTrace();
        }

        try
        {
            authenticate();
            Request request = (Request) objectInputStream.readObject();
            if (request instanceof ApodRequest)
            {
                NasaApodConnection connection = new NasaApodConnection((ApodRequest)request);
                connection.convertResponse();
                Apod apodObject = connection.getApodObject();
                objectOutputStream.writeObject(apodObject.getHdurl());
                objectOutputStream.flush();
                //os.println(apodObject.getHdurl());
                //os.flush();
            }
            /*
            while (line.compareTo("QUIT") != 0)
            {
                lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
                os.println(lines);
                os.flush();
                System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);
                line = is.readLine();
            }
            */

        }
        catch (IOException e)
        {
            line = this.getName();
            System.err.println("[ServerConnection]: Run. IO Error/ Client " + line + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            line = this.getName();
            System.err.println("[ServerConnection]: Run.Client " + line + " Closed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally
        {
            connectionFinally();
        }
    }

    private void connectionFinally()
    {
        try
        {
            System.out.println("[ServerConnection]: Closing the connection");
            if (dataInputStream != null)
            {
                dataInputStream.close();
                System.err.println("[ServerConnection]: Socket Input Stream Closed");
            }

            if (dataOutputStream != null)
            {
                dataOutputStream.close();
                System.err.println("[ServerConnection]: Socket Out Closed");
            }
            if (s != null)
            {
                s.close();
                System.err.println("[ServerConnection]: Socket Closed");
            }

        }
        catch (IOException ie)
        {
            System.err.println("[ServerConnection]: Socket Close Error");
        }
    }

    private void authenticate(){
        try {
            String username = new String(dataInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Authentication authentication = new Authentication(ApplicationType.SERVER);
            if (authentication.checkUsername(username)) {
                sendToClientCommandLine(authentication.Auth_Fail_User());
                connectionFinally();
                return;
            }

            sendToClientCommandLine(authentication.Auth_Challenge_Password());
            this.s.setSoTimeout(15000);

            boolean passwordCheck = false;
            while (!passwordCheck && authentication.getFailureCount() < authentication.getFailureLimit())
            {
                String password = new String(dataInputStream.readAllBytes(), StandardCharsets.UTF_8);
                passwordCheck = authentication.checkPassword(username, password);
                if (!passwordCheck)
                {
                    sendToClientCommandLine(authentication.Auth_Fail_Password());
                }
            }
            this.s.setSoTimeout(0);

            if(!passwordCheck)
            {
                sendToClientCommandLine(authentication.Auth_Fail_Timeout());
                connectionFinally();
                return;
            }

        } catch (SocketTimeoutException timeoutException)
        {
            line = this.getName();
            System.err.println("[ServerConnection]: Run. Timeout " + line + " terminated abruptly");
            connectionFinally();
        } catch (IOException e)
        {
            line = this.getName();
            System.err.println("[ServerConnection]: Run. IO Error/ Client " + line + " terminated abruptly");
        }
    }

    private void sendToClientCommandLine(byte[] obj) throws IOException
    {
        dataOutputStream.write(obj);
        dataOutputStream.flush();
    }
}
