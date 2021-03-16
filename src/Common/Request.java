package Common;

import java.io.Serializable;

public class Request implements Serializable {

    public enum MessageType {
        Auth_Request,
        Auth_Challenge,
        Auth_Fail,
        Auth_Success
    }

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
