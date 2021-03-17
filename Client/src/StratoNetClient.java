import Common.Authentication;
import Common.Request;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class StratoNetClient {

    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket socket;
    private Socket authenticatedSocket;
    private int authenticatedPort;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected ObjectOutputStream objectOutputStream;
    protected ObjectInputStream objectInputStream;
    protected InputStream in;
    protected OutputStream out;
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
            socket =new Socket(serverAddress, serverPort);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            dataInputStream = new DataInputStream(in);
            dataOutputStream = new DataOutputStream(out);
            System.out.println("[StratoNetClient.Connect]: Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("[StratoNetClient.Connect]: Error. no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    public Object SendObjectForAnswer(Request message) throws IOException
    {
        objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(in);
        Object response = null;
        try
        {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            response = objectInputStream.readObject();
            //System.out.println(response);
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
            if (dataInputStream != null)
            {
                dataInputStream.close();
            }
            if (dataOutputStream != null)
            {
                dataOutputStream.close();
            }
            if (objectInputStream != null)
            {
                objectInputStream.close();
            }
            if (objectOutputStream != null)
            {
                objectOutputStream.close();
            }
            socket.close();
            System.out.println("[StratoNetClient.Disconnect]: Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void authenticatedSocket() throws IOException {
        //authenticatedSocket = new Socket(serverAddress, authenticatedPort);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public boolean sendCommandLine(byte[] auth_request) throws IOException{
        dataOutputStream.write(auth_request);
        dataOutputStream.flush();

        int wholeSize = dataInputStream.available();
        while (wholeSize < 1)
        {
            wholeSize = dataInputStream.available();
        }
        byte[] data = new byte[wholeSize];
        dataInputStream.readFully(data);
        byte[] header = Arrays.copyOfRange(data, 0,6);
        int size = Authentication.checkAuthHeader(header);
        if (size == -1)
        {
            System.out.println("Failed");
            return false;
        }
        System.out.println("Succeded.");
        if (size > 0)
        {
            data = Arrays.copyOfRange(data, 6,6+size);
            authenticatedPort = Integer.parseInt(new String(data));
            System.out.println("Authenticated.");
        }

        return true;
    }
}
