import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class StratoNetServer {

    protected ServerSocket serverSocket;
    protected ServerSocket authenticatedServerSocket;
    public static final int DEFAULT_SERVER_PORT = 4444;
    public static int NEW_SERVER_PORT;

    public StratoNetServer(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("[StratoNetServer]: Opened up a server socket on " + Inet4Address.getLocalHost());
            //NEW_SERVER_PORT = 5000+new Random().nextInt(1000);
            //authenticatedServerSocket = new ServerSocket(NEW_SERVER_PORT);
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

    protected void ListenAndAccept()
    {
        Socket s;
        try
        {
            s = serverSocket.accept();
            System.out.println("[StratoNetServer.ListenAndAccept]: A connection was established with a client on the address of " + s.getRemoteSocketAddress());
            ServerThread st = new ServerThread(s);
            st.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("[StratoNetServer.ListenAndAccept]: Connection establishment error inside listen and accept function");
        }
    }
}