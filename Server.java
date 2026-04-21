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
                clients.add(handler);

                // Start thread (like lecture)
                Thread t = new Thread(handler);
                // wraps the handler in a thread (i remember wrappers!!!)
                t.start();
            }

        } catch (IOException e) {
            //IOExceptions are super common with chat messengers like this. Gotta handle them as they come up

            System.out.println("ERROR: " + e.getMessage());
        }
    }

    
    public static void broadcast(String message) {
        for (ClientHandler c : clients) {
            c.sendMessage(message);
            /* This is an important section.
            it sends a message to ALL the clients, 
            so that everyone can see it*/
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
    /* this portion removes a client if they disconnect
    ( which might happn) */
}