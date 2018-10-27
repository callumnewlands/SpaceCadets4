import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread
{
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private ArrayList<ClientThread> activeClientThreads;

    ClientThread(Socket clientSocket, ArrayList<ClientThread> activeClientThreads)
    {
        super();
        this.clientSocket = clientSocket;
        this.activeClientThreads = activeClientThreads;
    }

    public void run()
    {
        try
        {
            setup();
            writer.println("YOU HAVE CONNECTED");

            while (true)
            {

                String clientIn = "";
                if (reader.ready())
                {
                    clientIn = reader.readLine();
                    System.out.println(clientIn);
                    sendToAllOtherClients(clientIn);
                }


                // 
                if (clientIn.toUpperCase().contains(" HAS DISCONNECTED"))
                {
                    writer.println("YOU HAVE DISCONNECTED");
                    reader.close();
                    break;
                }
            }
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            writer.close();
        }
    }

    private void setup() throws IOException
    {
        reader = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    void sendToClient(String str)
    {
        this.writer.println(str);
    }

    private void sendToAllOtherClients(String str)
    {
        for (ClientThread client : activeClientThreads)
        {
            if (client != this)
            {
                client.sendToClient(str);
            }
        }
    }

}
