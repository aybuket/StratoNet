import java.util.Scanner;

public class Main {
    public static void main()
    {
        StratoNetClient client = new StratoNetClient(
                StratoNetClient.DEFAULT_SERVER_ADDRESS,
                StratoNetClient.DEFAULT_SERVER_PORT);

        client.Connect();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message for the echo");

        String message = scanner.nextLine();

        while (!message.equals("QUIT"))
        {
            System.out.println("Response from server: " + client.SendForAnswer(message));
            message = scanner.nextLine();
        }

        client.Disconnect();
    }
}