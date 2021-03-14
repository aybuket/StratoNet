package NasaConnection;

import Types.ConnectionType;

public class NasaInsightWeatherConnection extends NasaConnection{

    public NasaInsightWeatherConnection() {
        super(ConnectionType.InsightWeather);
    }

    @Override
    public boolean validateParameters() {
        return true;
    }
}
