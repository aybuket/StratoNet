import Common.ApodRequest;
import Common.Request;
import NasaConnection.NasaApodConnection;
import Types.Apod;

import java.io.*;
import java.net.Socket;

public class ServerConnection extends Thread{
    protected BufferedReader is;
    protected PrintWriter os;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream outputStream;
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
            objectInputStream = new ObjectInputStream(s.getInputStream());
            outputStream = new ObjectOutputStream(s.getOutputStream());
        }
        catch (IOException e)
        {
            System.err.println("[ServerConnection]: Run. IO error in server thread");
            e.printStackTrace();
        }

        try
        {
            //new NasaInsightWeatherConnection();
            //new NasaApodConnection().downloadImage();
            //System.out.println("End.");

            Request request = (Request) objectInputStream.readObject();
            if (request instanceof ApodRequest)
            {
                NasaApodConnection connection = new NasaApodConnection((ApodRequest)request);
                connection.convertResponse();
                Apod apodObject = connection.getApodObject();
                outputStream.writeObject(apodObject.getHdurl());
                outputStream.flush();
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
