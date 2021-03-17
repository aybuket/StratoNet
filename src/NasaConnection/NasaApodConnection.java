package NasaConnection;

import Common.ApodRequest;
import Types.Apod;
import Utils.ConvertFromJson;

import java.net.URI;
import java.net.http.HttpRequest;

public class NasaApodConnection extends NasaConnection{

    private Apod apod;
    private static final String ApodAddress = "/planetary/apod";
    private ApodRequest parameters = null;

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
    public NasaConnection buildRequest() {
        String apiAddress = NasaApiBaseAddress + ApodAddress + apiKeyString + apiKey + buildParameters();
        request = HttpRequest.newBuilder(URI.create(apiAddress))
                .header("accept", "application/json")
                .GET()
                .build();
        return this;
    }

    public void setParameters(ApodRequest request) {
        this.parameters = request;
    }

    public String buildParameters(){
        if (parameters != null) {
            if (parameters.hasDate()) {
                return "&date=" + parameters.getDate().dateFormat();
            }

            if (parameters.hasStartDate()) {
                String payload = "&start_date=" + parameters.getStartDate().dateFormat();

                if (parameters.hasEndDate()) {
                    payload += "&end_date=" + parameters.getEndDate().dateFormat();
                }

                return payload;
            }

            if (parameters.hasCount()) {
                return "&count=" + parameters.getCount();
            }
        }

        return "";
    }

    public Apod getApodObject()
    {
        return apod;
    }
}
