package Common;

public class Message {

    public enum MessageType {
        Auth_Request,
        Auth_Challenge,
        Auth_Fail,
        Auth_Success
    }

    private MessageType type;
    private String payload;

    public Message(MessageType type, String payload)
    {
        this.type = type;
        this.payload = payload;
    }
}
