package NasaConnection;

import Types.ConnectionType;
import Utils.ConvertFromJson;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public abstract class NasaConnection {

    private static final String NasaApiBaseAddress = "https://api.nasa.gov";
    private static final String Apod = "/planetary/apod";
    private static final String InsightWeather = "/insight_weather/";
    private static final String apiKeyString = "?api_key=";

    private String apiKey = null;

    public NasaConnection (ConnectionType type)
    {
        readKey();
        String apiAddress = type == ConnectionType.APOD ?
                NasaApiBaseAddress + Apod + apiKeyString + apiKey :
                NasaApiBaseAddress + InsightWeather + apiKeyString + apiKey + "&feedtype=json&ver=1.0";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(apiAddress))
                    .header("accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
            System.out.println(ConvertFromJson.convertInsightWeatherResponse(response).toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    private void readKey()
    {
        try
        {
            File myObj = new File("NasaAPI.env");
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

    public abstract boolean validateParameters();
}

