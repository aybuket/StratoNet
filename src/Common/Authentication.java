package Common;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static Common.MessageType.*;

public class Authentication {

    private ApplicationType type;
    private final int failureLimit = 3;
    private int failureCount = 0;
    private final byte initialization = (byte)0;
    private final byte querying = (byte)1;
    private final Map<MessageType, Byte> messageTypeByteMap = Map.of(
        Auth_Request, (byte)0,
        Auth_Challenge, (byte)1,
        Auth_Fail, (byte)2,
        Auth_Success, (byte)3
    );

    public Authentication(ApplicationType type)
    {
        this.type = type;
    }

    public int getFailureLimit()
    {
        return failureLimit;
    }

    public int getFailureCount()
    {
        return failureCount;
    }

    public byte[] Auth_Request(String requestString){
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        int size = requestString.length();
        byte[] header = new byte[]{initialization, messageTypeByteMap.get(Auth_Request), (byte) size};
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    public byte[] Auth_Challenge_Password()
    {
        return Auth_Challenge("Password?");
    }

    private byte[] Auth_Challenge(String requestString){
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        int size = requestString.length();
        byte[] header = new byte[]{initialization, messageTypeByteMap.get(Auth_Challenge), (byte) size};
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }


    public byte[] Auth_Success(int port, String username){
        String token = constractToken(port, username);
        byte[] requestBytes = token.getBytes(StandardCharsets.UTF_8);
        int size = token.length();
        byte[] header = new byte[]{initialization, messageTypeByteMap.get(Auth_Success), (byte) size};
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    private String constractToken(int port, String username)
    {
        String longToken = (port+username+"72").hashCode()+"";
        return longToken.substring(0,5);
    }

    public byte[] Auth_Fail_Timeout(){
        return Auth_Fail("Timeout");
    }

    public byte[] Auth_Fail_User(){
        return Auth_Fail("User does not exist.");
    }

    public byte[] Auth_Fail_Password(){
        return Auth_Fail("Incorrect Password.");
    }

    private byte[] Auth_Fail(String requestString){
        this.failureCount++;
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        int size = requestString.length();
        byte[] header = new byte[]{initialization, messageTypeByteMap.get(Auth_Fail), (byte) size};
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    public boolean checkUsername(String username) {
        return readSecretFile().containsKey(username);
    }

    private HashMap<String, String> readSecretFile() {
        HashMap<String, String> clients = new HashMap<String, String>();
        try {
            File myObj = new File("src/server/clients.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(":");
                clients.put(parts[0], parts[1]);
            }
            myReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean checkPassword(String username, String password) {
        return readSecretFile().get(username).equals(password);

    }
}
