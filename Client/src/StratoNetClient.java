import Common.Request;
import Types.Apod;

import java.io.*;
import java.net.Socket;

public class StratoNetClient {

    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket s;
    protected BufferedReader is;
    protected PrintWriter os;
    protected ObjectOutputStream outputStream;
    protected ObjectInputStream inputStream;
    protected String serverAddress;
    protected int serverPort;


    public StratoNetClient(String address, int port)
    {
        serverAddress = address;
        serverPort    = port;
    }

    public void Connect()
    {
        try
        {
            s=new Socket(serverAddress, serverPort);
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());
            outputStream = new ObjectOutputStream(s.getOutputStream());
            inputStream = new ObjectInputStream(s.getInputStream());

            System.out.println("[StratoNetClient.Connect]: Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("[StratoNetClient.Connect]: Error. no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    public Object SendForAnswer(Request message)
    {
        Object response = null;
        try
        {
            outputStream.writeObject(message);
            outputStream.flush();
            response = inputStream.readObject();
            System.out.println(response);
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("[StratoNetClient.SendForAnswer]: Socket read Error");
        }
        return response;
    }

    public void Disconnect()
    {
        try
        {
            is.close();
            os.close();
            s.close();
            System.out.println("[StratoNetClient.Disconnect]: Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
