package NasaConnection;

import java.io.File;
import java.net.http.*;
import java.util.Scanner;

public abstract class NasaConnection {

    protected static final String NasaApiBaseAddress = "https://api.nasa.gov";
    protected static final String apiKeyString = "?api_key=";
    HttpClient client;
    HttpRequest request;
    HttpResponse<String> response;
    protected String apiKey = null;

    public NasaConnection ()
    {
        readKey();
        try {
            client = HttpClient.newHttpClient();
            buildRequest();
            sendSyncRequest();
            System.out.println(response.body());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    public abstract void convertResponse();
    public abstract boolean validateParameters();
    public abstract NasaConnection buildRequest();

    public int returnCode(){
        return response.statusCode();
    }
    public void sendSyncRequest(){
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            convertResponse();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void readKey()
    {
        try
        {
            File myObj = new File("src/NasaConnection/NasaAPI.env");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                String[] parts = data.split("=");
                if (parts[0].equals("NASA_API_KEY"))
                {
                    apiKey = parts[1];
                }
            }
            myReader.close();

            if (apiKey == null)
            {
                throw new Exception("Nasa API key could not found");
            }

            System.out.println("[NasaConnection]: Nasa API Key is read.");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

