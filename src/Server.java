import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{


    private static ServerSocket socket;
    private static PrintWriter writer;
    private static ArrayList<BufferedReader> clientReaders = new ArrayList<>();
    private static ArrayList<String> clientNames = new ArrayList<>();

    public static void main(String[] args)
    {
        try
        {
            boot();

            String clientIn;
            while ((clientIn = clientReaders.get(0).readLine()) != null)
            {
                if (clientIn.contains("__USERNAME__:"))
                {
                    clientNames.set(0, clientIn.substring("__USERNAME__:".length()));
                    continue;
                }
                System.out.println(clientNames.get(0).toUpperCase() + ": " + clientIn);

                //writer.println(clientIn);  // Echo back to the client

                if (clientIn.toLowerCase().equals("bye")) {
                    clientReaders.get(0).close();
                    break;
                }


            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void boot() throws IOException
    {
        socket = new ServerSocket(MultiUserServer.PORT);
        Socket client = socket.accept();
        clientReaders.add(new BufferedReader(new InputStreamReader((client.getInputStream()))));
        clientNames.add("CLIENT");

        writer = new PrintWriter(client.getOutputStream(), true);
    }
}
