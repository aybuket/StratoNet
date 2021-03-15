package NasaConnection;

import Utils.ConvertFromJson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;

public class NasaApodConnection extends NasaConnection{

    private Types.Apod apod;
    private static final String ApodAddress = "/planetary/apod";

    public NasaApodConnection() {
        super();
    }

    @Override
    public void convertResponse() {
        apod = ConvertFromJson.convertApodResponse(response);
    }

    @Override
    public boolean validateParameters() {
        return true;
    }

    @Override
    public void buildRequest() {
        String apiAddress = NasaApiBaseAddress + ApodAddress + apiKeyString + apiKey;
        request = HttpRequest.newBuilder(URI.create(apiAddress))
                .header("accept", "application/json")
                .GET()
                .build();
    }


    public Image downloadImage()
    {
        Image image = null;
        try {
            URL url = new URL(apod.getHdurl());
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
