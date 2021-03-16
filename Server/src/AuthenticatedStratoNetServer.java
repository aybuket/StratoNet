import java.net.Socket;

public class AuthenticatedStratoNetServer extends StratoNetServer{
    public AuthenticatedStratoNetServer(int port) {
        super(port);
    }

    @Override
    protected void ListenAndAccept()
    {
        Socket s;
        try
        {
            s = this.serverSocket.accept();
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
