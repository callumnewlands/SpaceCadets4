import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MultiUserServer
{

    static final int PORT = 60101;
    private ArrayList<ClientThread> activeClientThreads = new ArrayList<>();

    public static void main(String[] args)
    {
        MultiUserServer server = new MultiUserServer();
        server.run();
    }

    private void run()
    {
        System.out.println("SERVER RUNNING");

        try (ServerSocket socket = new ServerSocket(PORT))
        {
            while (true)
            {
                activeClientThreads.add(new ClientThread(socket.accept(), activeClientThreads));
                activeClientThreads.get(numberOfClients() - 1).start();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private int numberOfClients()
    {
        return activeClientThreads.size();
    }

}
