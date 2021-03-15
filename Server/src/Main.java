import NasaConnection.NasaApodConnection;
import NasaConnection.NasaInsightWeatherConnection;

public class Main {
    public static void main(String[] args)
    {
        // StratoNetServer server = new StratoNetServer(StratoNetServer.DEFAULT_SERVER_PORT);
        // NasaApodConnection connection = new NasaApodConnection();
        new NasaInsightWeatherConnection();
        new NasaApodConnection().downloadImage();
        System.out.println("End.");
    }
}
