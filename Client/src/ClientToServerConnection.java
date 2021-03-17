import Common.*;
import Types.Apod;
import Types.PREValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ClientToServerConnection {

    private Scanner scanner;
    private String message = "";
    private StratoNetClient client;
    private Phase phase;
    private String token;

    public ClientToServerConnection(){
        client = new StratoNetClient(
                StratoNetClient.DEFAULT_SERVER_ADDRESS,
                StratoNetClient.DEFAULT_SERVER_PORT);
        phase = Phase.INITIALIZATION;
    }

    public void start(){
        client.Connect();
        System.out.println("-----Welcome!-----");
        scanner = new Scanner(System.in);
        while (!message.equals("QUIT")) {
            switch (phase) {
                case INITIALIZATION -> authenticate();
                case QUERYING -> sendQuery();
                case QUIT -> message = "QUIT";
                default -> client.Disconnect();
            }
        }
        client.Disconnect();
    }

    private void sendQuery() {
        chooseAPI();
        Request newRequest = null;
        try {
            if (message.equals("1")) {
                newRequest = useApodAPI();
                Apod apod = (Apod)client.SendObjectForAnswer(newRequest);
                BufferedImage bimg = (BufferedImage) apod.downloadImage();
                File imageFile = new File("Client/Downloads/Apod"+ apod.getDate()+".jpg");
                imageFile.createNewFile();
                ImageIO.write(bimg, "jpg" , imageFile);
                System.out.println("Image is saved.");
            } else {
                newRequest = useInsightAPI();
                PREValue pre = (PREValue) client.SendObjectForAnswer(newRequest);
                System.out.println("PreValues are received for sol: "+ pre.getKey());
                System.out.println(pre.toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        client.Disconnect();
    }

    private void authenticate() {
        System.out.println("Initialization Phase.");
        try {
            System.out.println("Username: ");
            String username = scanner.nextLine();
            System.out.println("You wrote: " + username);

            boolean isValid = client.sendCommandLine(Authentication.Auth_Request(username));
            boolean isSuccess = false;
            int count = 0;
            while (isValid && !isSuccess && count < Authentication.getFailureLimit()) {
                System.out.println("Password: ");
                String password = scanner.nextLine();
                isSuccess = client.sendCommandLine(Authentication.Auth_Request(password));
                count++;
            }

            if (isSuccess) {
                //client.authenticatedSocket();
                phase = Phase.QUERYING;
                System.out.println("Switching Querying Phase.");
            }
            else
            {
                phase = Phase.QUIT;
                System.out.println("Switching Quitting Phase.");
                client.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chooseAPI()
    {
        String chooseAPI = "Choose the NASA Application\n" +
                "[1] Astronomy Picture of the Day (APOD)\n" +
                "[2] InSight: Mars Weather Service" +
                "[QUIT] Close the connection";
        System.out.println(chooseAPI);
        message = scanner.nextLine();
        while (!message.equals("1") && !message.equals("2") && !message.equals("QUIT"))
        {
            System.out.println("Write 1 or 2. \n\n" + chooseAPI);
            message = scanner.nextLine();
        }
    }

    private ApodRequest useApodAPI() throws Exception
    {
        ApodRequest requestParameters = new ApodRequest();
        String parameterString = "Optional parameters. Choose number or ENTER. \n" +
                "[1] date: YYYY-MM-DD\n" +
                "[2] start date: YYYY-MM-DD (and end date: YYYY-MM-DD)\n" +
                "[3] count: int";
        System.out.println(parameterString);
        message = scanner.nextLine();
        while (!message.equals("1") && !message.equals("2") && !message.equals("3") && !message.isBlank())
        {
            System.out.println("Invalid Input \n\n" + parameterString);
            message = scanner.nextLine();
        }

        switch (message) {
            case "1" -> requestParameters.setDate(getDate());
            case "2" -> {
                requestParameters.setStartDate(getDate());
                System.out.println("Choose [E] end date or ENTER");
                message = scanner.nextLine();
                while (!message.equals("E") && !message.isBlank()) {
                    System.out.println("Invalid Input \n\n" + "Choose [E] end date or ENTER");
                    message = scanner.nextLine();
                }
                if (message.equals("E")) {
                    requestParameters.setEndDate(getDate(requestParameters.getStartDate()));
                }
            }
            case "3" -> {
                System.out.println("Count: (max 100)");
                message = scanner.nextLine();
                while (Integer.parseInt(message) < 1 && Integer.parseInt(message) > 100) {
                    System.out.println("Invalid Input \n\n" + "Count: (max 100)");
                    message = scanner.nextLine();
                }
                requestParameters.setCount(Integer.parseInt(message));
            }
        }

        System.out.println(requestParameters);
        return requestParameters;
    }

    private InsightWeatherRequest useInsightAPI()
    {
        return new InsightWeatherRequest();
    }

    private Date getDate() throws Exception
    {
        return getDate(Date.getFirstPictureDay());
    }

    private Date getDate(Date validateDateMin) throws Exception
    {
        Date dateRequested = new Date();
        System.out.println("Year: (YYYY)");
        message = scanner.nextLine();
        while (Integer.parseInt(message) < 1995 || Integer.parseInt(message) > 2021) {
            System.out.println("Invalid Input. Year must be at least 1995 and at most 2021. \n\n" + "Year: (YYYY)");
            message = scanner.nextLine();
        }
        dateRequested.setYear(Integer.parseInt(message));

        System.out.println("Month: (MM)");
        message = scanner.nextLine();
        while (Integer.parseInt(message) < 1 || Integer.parseInt(message) > 12)
        {
            System.out.println("Invalid Input. Month must be at least 1 and at most 12. \n\n" + "Month: (MM)");
            message = scanner.nextLine();
        }
        dateRequested.setMonth(Integer.parseInt(message));

        System.out.println("Day: (DD)");
        message = scanner.nextLine();
        while (Integer.parseInt(message) < 1 || Integer.parseInt(message) > 31) {
            System.out.println("Invalid Input. Day must be at least 1 and at most 31. \n\n" + "Day: (DD)");
            message = scanner.nextLine();
        }
        dateRequested.setDay(Integer.parseInt(message));

        System.out.println(dateRequested.toString());
        if (!dateRequested.validateDate(validateDateMin)) {
            throw new Exception();
        }

        return dateRequested;
    }
}
