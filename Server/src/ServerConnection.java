import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection extends Thread{
    protected BufferedReader is;
    protected PrintWriter os;
    protected Socket s;
    private String line = new String();
    private String lines = new String();

    public ServerConnection(Socket s)
    {
        this.s = s;
    }

    public void run()
    {
        try
        {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());
        }
        catch (IOException e)
        {
            System.err.println("[ServerConnection]: Run. IO error in server thread");
        }

        try
        {
            line = is.readLine();
            while (line.compareTo("QUIT") != 0)
            {
                lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId();
                os.println(lines);
                os.flush();
                System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " + lines);
                line = is.readLine();
            }
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
            if (is != null)
            {
                is.close();
                System.err.println("[ServerConnection]: Socket Input Stream Closed");
            }

            if (os != null)
            {
                os.close();
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
}
