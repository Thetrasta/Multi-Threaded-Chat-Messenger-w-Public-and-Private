import java.io.*;
import java.net.*;

/* this entire class basically describes what the 
client will do/how it interacts with the server/gui.
unlike in the previous server/client assignment the client's
behavior was being describes in its client class, we have multiple clients now.
Everytime a new client joins the chat, ClientHandler is invok */
public class ClientHandler implements Runnable {
/*You need to implement runnable for threads */
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.client = socket;
    }
    /* remember that socket you used in the server? 
    we're using it here. */

    @Override
    public void run() {
    // standard client behavior, honestly

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            username = in.readLine();
            System.out.println(username + " joined.");

            Server.broadcast(username + " joined the chat.");

            String message;

            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
                String fullMessage = username + ": " + message;
                System.out.println(fullMessage);
                Server.broadcast(fullMessage);
            }

        } catch (IOException e) {
            System.out.println("Connection error.");
        } finally {
            Server.removeClient(this);
            Server.broadcast(username + " left the chat.");

            try {
                client.close();
            } catch (IOException e) {
            }
        }
    }
    
    public void sendMessage(String message) {
        out.println(message);
    }
}
