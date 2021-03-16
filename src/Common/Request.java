package Common;

import java.io.Serializable;

public class Request implements Serializable {



    private MessageType type;
    private String payload;

    public Request(MessageType type)
    {
        this.type = type;
    }

    public Request(MessageType type, String payload)
    {
        this.type = type;
        this.payload = payload;
    }
}
