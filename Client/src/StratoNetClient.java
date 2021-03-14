import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StratoNetClient {

    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket s;
    protected BufferedReader is;
    protected PrintWriter os;

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

            System.out.println("[StratoNetClient.Connect]: Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("[StratoNetClient.Connect]: Error. no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    public String SendForAnswer(String message)
    {
        String response = new String();
        try
        {
            os.println(message);
            os.flush();
            response = is.readLine();
        }
        catch(IOException e)
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
