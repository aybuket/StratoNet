package Common;

import java.io.Serializable;

public class Request implements Serializable {

    private final RequestType type;
    public Request(RequestType type)
    {
        this.type = type;
    }

    public RequestType getType()
    {
        return type;
    }
}
