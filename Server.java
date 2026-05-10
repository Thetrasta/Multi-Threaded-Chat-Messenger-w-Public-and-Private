import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

    // This is a shared list of clients
    // Because all of the clients are all talking in one chat
    // the static means that it's used for the entire program as a global variable


    static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {

        int port = 1728;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) { // infinite loop that keeps running until an error or until everyone closes their GUI
                Socket client = server.accept();
                System.out.println("Client connected");

                ClientHandler handler = new ClientHandler(client);
                // an object is made to "handle" this client (hence the ClientHandler)
                Thread t = new Thread(handler);
                t.start();
            }

        } catch (IOException e) {
            //IOExceptions are super common with chat messengers like this. 
            // Gotta handle them as they come up

            System.out.println("ERROR: " + e.getMessage());
        }
    }

    
    public static void broadcast(String message) {
        for (ClientHandler c : clients) {
            c.sendMessage(message);
            /* This is an important section.
            If something is tagged as a broadcast,
            everyone in the chat receives the message*/
        }
    }

    public static void sendPrivate(String sender, String recipient, String message){
        boolean exist = false;
        for (ClientHandler c : clients) {
            if (c.getUsername().equalsIgnoreCase(recipient)) {
                c.sendMessage("(Whisper from " + sender + "): " + message);
                exist = true;
                break;
                // the actual private message 
                // exist variable is to make sure that the recipient exists in the client list
            }
        
            if (c.getUsername().equalsIgnoreCase(sender)) {
                c.sendMessage("(Whisper to " + sender + "): " + message);
            }
        }
        if (!exist) {
            for (ClientHandler c : clients) {
                if (c.getUsername().equalsIgnoreCase(sender)) {
                    c.sendMessage("User not found.");
                }
            }
        }
    
    }

    public static boolean usernameExists(String username) {
        for (ClientHandler c : clients) {
            if (c.getUsername() != null &&
            c.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    // this partially handles the problem of identical usernames.
    // clienthandler handles the rest



    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
    /* this portion removes a client if they disconnect
    (which might happen) */
}
