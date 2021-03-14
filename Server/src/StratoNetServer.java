import java.io.*;
import java.net.*;

public class StratoNetServer {

    private ServerSocket serverSocket;
    public static final int DEFAULT_SERVER_PORT = 4444;

    public StratoNetServer(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("[StratoNetServer]: Opened up a server socket on " + Inet4Address.getLocalHost());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("[StratoNetServer]: Constructor exception on opening a server socket");
        }
        while (true)
        {
            ListenAndAccept();
        }
    }

    private void ListenAndAccept()
    {
        Socket s;
        try
        {
            s = serverSocket.accept();
            System.out.println("[StratoNetServer.ListenAndAccept]: A connection was established with a client on the address of " + s.getRemoteSocketAddress());
            ServerConnection st = new ServerConnection(s);
            st.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("[StratoNetServer.ListenAndAccept]: Connection establishment error inside listen and accept function");
        }
    }
}