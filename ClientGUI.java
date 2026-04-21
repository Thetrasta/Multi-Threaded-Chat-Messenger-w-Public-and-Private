import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientGUI {

    private JFrame window;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String username;

    public ClientGUI() {

        username = JOptionPane.showInputDialog("Enter username:");
        window = new JFrame("Chat - " + username);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        inputField = new JTextField(20);
        sendButton = new JButton("Send");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(inputField);
        bottom.add(sendButton);

        panel.add(bottom, BorderLayout.SOUTH);

        window.setContentPane(panel);
        window.setSize(400, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        connect();

        out.println(username);

        sendButton.addActionListener(e -> {
            String msg = inputField.getText();
            out.println(msg);
            inputField.setText("");

        /*Without an ActionListener,
        hitting send wouldn't actually do anything
        */
        });

        new Thread(() -> listen()).start();
        /* this is really what makes this whole program asynchronous.
        It opens a new thread which is listening in the background.
        Without this, the GUI would freeze */
    }

    private void connect() {
        try {
            socket = new Socket("127.0.0.1", 1728);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            chatArea.append("Connection failed\n");
        }
    }

    private void listen() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                chatArea.append(msg + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Disconnected\n");
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}