/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package instantmessenger;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* Whenever you connect to someone else's cpu , you communicate through streams
 * output stream is sent from your computer to your friend's cpu, input is opposite
 *  
 */
public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    //constructor
    public Server() {

        super("Paul's Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);  //doesn't allow user to type while they are not connected to anyone
        userText.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent event) {

                        sendMessage(event.getActionCommand());// passes the string typed into the text field to the method
                        userText.setText(""); // reset message area back to blank for you to type new message

                    }
                });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);

    }

    //sets up and runs the server
    public void startRunning() {
        try {

            server = new ServerSocket(6789, 100);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eofException) {
                    showMessage("\n Server Ended the connection! ");
                } finally {
                    closeCrap();
                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    //wait for connection, then display connection information
    private void waitForConnection() throws IOException {

        showMessage("Waiting for someone to connect...\n");
        connection = server.accept();  //opens port once a person is there to connect to
        showMessage("Now connected to " + connection.getInetAddress().getHostAddress());


    }

    //get stream to send and recieve data
    private void setupStreams() throws IOException {

        output = new ObjectOutputStream(connection.getOutputStream()); // sets up pathway to send messages out
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());  // sets up pathway to recieve messages
        showMessage("\n Streams are now setup \n");
    }

    //during the chat
    private void whileChatting() throws IOException {

        String message = " You are now connected! ";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);

            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n idk what that user sent!");
            }

        } while (!message.equals("CLIENT - END"));
    }

    //close streams and sockets after you're done chatting
    private void closeCrap() {
        showMessage("\n Closing connections... \n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //send a message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message); // sends them the message
            output.flush();
            showMessage("\nSERVER - " + message);  //displays to us our own message
        } catch (IOException ioException) {
            chatWindow.append("\n ERROR: Can't send that message! ");
        }
    }

    //updates chat window
    private void showMessage(final String text) {

        SwingUtilities.invokeLater( // this updates a small portion of GUI to show message
                new Runnable() {

            public void run() {

                chatWindow.append(text);


            }
        });
    }

    //let the user type text into their box; when true user is able to type
    private void ableToType(final boolean tof) {

        SwingUtilities.invokeLater( // this updates a small portion of GUI to show message
                new Runnable() {

            public void run() {

                userText.setEditable(tof);


            }
        });

    }
}