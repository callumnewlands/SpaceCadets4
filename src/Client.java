import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client
{
    private Socket serverSocket;
    private String name;
    private PrintWriter writer;
    private BufferedReader stdReader;
    private BufferedReader serverReader;

    public static void main(String[] args)
    {
        Client client = new Client();
        client.run();
    }

    private void run()
    {
        System.out.println("TEST GIT");
        name = PublicMethods.getString("Name: ");

        try
        {
            setup();

            while(true)
            {
                tryReceiveFromServer();
                trySendFromUser();

            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void setup() throws IOException
    {
        try
        {
            serverSocket = new Socket("localhost", MultiUserServer.PORT);
            stdReader = new BufferedReader(new InputStreamReader(System.in));
            serverReader = new BufferedReader(new InputStreamReader((serverSocket.getInputStream())));
            writer = new PrintWriter(serverSocket.getOutputStream(), true);
            sendToServer(name.toUpperCase() + " HAS CONNECTED", true);
        }
        catch (IOException e)
        {
            throw new IOException("Cannot connect to sever.", e);
        }

    }

    private void sendToServer(String str)
    {
        sendToServer(str, false);
    }

    private void sendToServer(String str, Boolean anonymous)
    {
        if (anonymous)
        {
            writer.println(str);
        }
        else
        {
            writer.println(name.toUpperCase() + ": " + str);
        }
    }

    private void tryReceiveFromServer() throws IOException
    {
        String serverIn;
        if (serverReader.ready())
        {
            serverIn = serverReader.readLine();
            if (serverIn != null )
            {
                System.out.println(serverIn);
            }
            if (serverIn.equals("YOU HAVE DISCONNECTED"))
            {
                System.exit(0);
            }
        }
    }

    private void trySendFromUser() throws IOException
    {
        String userMessage;
        if (stdReader.ready())
        {
            userMessage = stdReader.readLine();
            if (userMessage != null && !userMessage.equals(""))
            {
                if (!userMessage.equals("bye"))
                {
                    sendToServer(userMessage);
                }
                else
                {
                    sendToServer(name.toUpperCase() + " HAS DISCONNECTED", true);
                }
            }
        }
    }
}
