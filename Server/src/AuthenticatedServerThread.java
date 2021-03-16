import java.io.*;
import java.net.Socket;

public class AuthenticatedServerThread extends ServerThread{
    public AuthenticatedServerThread(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try
        {
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
            objectInputStream = new ObjectInputStream(s.getInputStream());
            objectOutputStream = new ObjectOutputStream(s.getOutputStream());
        }
        catch (IOException e)
        {
            System.err.println("[ServerConnection]: Run. IO error in server thread");
            e.printStackTrace();
        }
    }

    private void sendToClientFile(Object obj) throws IOException
    {
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
    }
}
