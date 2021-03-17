package Common;

import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static Common.MessageType.*;

public class Authentication {

    private final static int failureLimit = 3;
    private static int failureCount = 0;
    private static final byte initialization = (byte)0;
    private static final Map<MessageType, Byte> messageTypeByteMap = Map.of(
        Auth_Request, (byte)0,
        Auth_Challenge, (byte)1,
        Auth_Fail, (byte)2,
        Auth_Success, (byte)3
    );

    public static int getFailureLimit()
    {
        return failureLimit;
    }

    public static int getFailureCount()
    {
        return failureCount;
    }

    public static byte[] Auth_Request(String requestString){
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        int size = requestString.length();
        byte[] sizeByte = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(size).array();
        byte[] headerFirst = new byte[]{ initialization, messageTypeByteMap.get(Auth_Request)};
        byte[] header = Arrays.copyOf(headerFirst, headerFirst.length + sizeByte.length);
        System.arraycopy(sizeByte, 0, header, headerFirst.length, sizeByte.length);
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    public static byte[] Auth_Challenge_Password()
    {
        return Auth_Challenge("Password?");
    }

    private static byte[] Auth_Challenge(String requestString){
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        int size = requestString.length();
        byte[] sizeByte = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(size).array();
        byte[] headerFirst = new byte[]{ initialization, messageTypeByteMap.get(Auth_Challenge)};
        byte[] header = Arrays.copyOf(headerFirst, headerFirst.length + sizeByte.length);
        System.arraycopy(sizeByte, 0, header, headerFirst.length, sizeByte.length);
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    public static byte[] Auth_Success(int port){
        String portString = port+"";
        byte[] requestBytes = portString.getBytes(StandardCharsets.UTF_8);
        int size = portString.length();
        byte[] sizeByte = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(size).array();
        byte[] headerFirst = new byte[]{ initialization, messageTypeByteMap.get(Auth_Success)};
        byte[] header = Arrays.copyOf(headerFirst, headerFirst.length + sizeByte.length);
        System.arraycopy(sizeByte, 0, header, headerFirst.length, sizeByte.length);
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    public static byte[] Auth_Success(int port, String username){
        String token = constractToken(port, username);
        byte[] requestBytes = token.getBytes(StandardCharsets.UTF_8);
        int size = token.length();
        byte[] sizeByte = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(size).array();
        byte[] headerFirst = new byte[]{ initialization, messageTypeByteMap.get(Auth_Success)};
        byte[] header = Arrays.copyOf(headerFirst, headerFirst.length + sizeByte.length);
        System.arraycopy(sizeByte, 0, header, headerFirst.length, sizeByte.length);
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    private static String constractToken(int port, String username)
    {
        String longToken = (port+username+"72").hashCode()+"";
        return longToken.substring(0,5);
    }

    public static byte[] Auth_Fail_Timeout(){
        return Auth_Fail("Timeout");
    }

    public static byte[] Auth_Fail_User(){
        return Auth_Fail("User does not exist.");
    }

    public static byte[] Auth_Fail_Password(){
        return Auth_Fail("Incorrect Password.");
    }

    private static byte[] Auth_Fail(String requestString){
        failureCount++;
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        int size = requestString.length();
        byte[] sizeByte = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(size).array();
        byte[] headerFirst = new byte[]{ initialization, messageTypeByteMap.get(Auth_Fail)};
        byte[] header = Arrays.copyOf(headerFirst, headerFirst.length + sizeByte.length);
        System.arraycopy(sizeByte, 0, header, headerFirst.length, sizeByte.length);
        byte[] payload = Arrays.copyOf(header, header.length + requestBytes.length);
        System.arraycopy(requestBytes, 0, payload, header.length, requestBytes.length);
        return payload;
    }

    public static boolean checkUsername(String username) {
        return readSecretFile().containsKey(username);
    }

    private static HashMap<String, String> readSecretFile() {
        HashMap<String, String> clients = new HashMap<String, String>();
        try {
            File myObj = new File("Server/clients.txt");
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

    public static boolean checkPassword(String username, String password) {
        return readSecretFile().get(username).equals(password);

    }

    public static int checkAuthRequestHeader(byte[] header) {
        if(header[0] != initialization || header[1] != messageTypeByteMap.get(Auth_Request))
        {
            System.out.println("Check Auth Request Header Failed.");
            return -1;
        }
        System.out.println("Check Auth Request Header Succeded.");
        return new BigInteger(Arrays.copyOfRange(header, 2,6)).intValue();
    }

    public static int checkAuthHeader(byte[] header)
    {
        if(header[0] != initialization || header[1] == messageTypeByteMap.get(Auth_Fail))
        {
            return -1;
        }

        if (header[1] == messageTypeByteMap.get(Auth_Challenge))
        {
            return 0;
        }

        if (header[1] == messageTypeByteMap.get(Auth_Success))
        {
            return new BigInteger(Arrays.copyOfRange(header, 2,6)).intValue();
        }
        return -1;
    }
}
