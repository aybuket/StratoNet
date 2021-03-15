package NasaConnection;

import Utils.ConvertFromJson;

import java.net.URI;
import java.net.http.HttpRequest;

public class NasaInsightWeatherConnection extends NasaConnection{
    private static final String InsightWeatherAddress = "/insight_weather/";
    private static final String feedType = "&feedtype=json";
    private static final String version = "&ver=1.0";
    private Types.InsightWeather insightWeather;

    public NasaInsightWeatherConnection() {
        super();
    }

    @Override
    public boolean validateParameters() {
        return true;
    }

    @Override
    public void buildRequest() {
        String apiAddress = NasaApiBaseAddress + InsightWeatherAddress + apiKeyString + apiKey + feedType + version;
        request = HttpRequest.newBuilder(URI.create(apiAddress))
                .header("accept", "application/json")
                .GET()
                .build();
    }

    @Override
    public void convertResponse() {
        insightWeather = ConvertFromJson.convertInsightWeatherResponse(response);
    }
}
