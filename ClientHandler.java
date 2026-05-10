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
            if (Server.usernameExists(username)){
                sendMessage("This username already exists.");
                client.close();
                return;
            }
            Server.clients.add(this);
            // only adds username if it's new

            
            System.out.println(username + " joined.");

            Server.broadcast(username + " joined the chat.");

            String message;

            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
                
                if (message.startsWith("/w")){

                    // a whisper must be sent like: /w [username] [message]
                    String[] parts = message.split(" ", 3);

                    if (parts.length < 3) {
                        out.println("Invalid whisper format. Use: /w recipient message");
                        continue;
                    }

                    String recipient = parts[1];
                    String msg = parts[2];
                    Server.sendPrivate(username, recipient, msg);
                }
                    else {
                    
                String fullMessage = username + ": " + message;
                System.out.println(fullMessage);
                Server.broadcast(fullMessage);
            }
        
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
    public String getUsername(){
        return username;
    }
}
