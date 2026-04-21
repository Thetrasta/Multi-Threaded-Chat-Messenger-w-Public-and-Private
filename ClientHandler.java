import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {

        try {
            // Set up streams
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            // First message = username
            username = in.readLine();
            System.out.println(username + " joined.");

            Server.broadcast(username + " joined the chat.");

            String message;

            // Message loop
            while ((message = in.readLine()) != null) {

                // Quit check
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }

                // Format message
                String fullMessage = username + ": " + message;

                System.out.println(fullMessage);

                // Broadcast to all
                Server.broadcast(fullMessage);
            }

        } catch (IOException e) {
            System.out.println("Connection error.");
        } finally {
            // Cleanup
            Server.removeClient(this);
            Server.broadcast(username + " left the chat.");

            try {
                client.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    // Send message to client
    public void sendMessage(String message) {
        out.println(message);
    }
}