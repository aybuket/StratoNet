package NasaConnection;

import Types.ConnectionType;

public class NasaApodConnection extends NasaConnection{

    public NasaApodConnection() {
        super(ConnectionType.APOD);
    }

    @Override
    public boolean validateParameters() {
        return true;
    }
}
