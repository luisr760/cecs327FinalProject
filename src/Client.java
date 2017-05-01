import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This program creates a client that sends requests to a server in the
 * form of integer values. The client does not send any data other than 
 * the integer specifying which response it is requesting. Implemented 
 * in TCP.
 */

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructor for a client
     * @param hostname the hostname for the socket
     * @param port the port number for the socket
     * @throws IOException in case socket encounters an error
     */
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    /**
     * Sends a request to connected server
     * @param option the requested option to send to the server
     * @throws IOException in case socket encounters an error
     */
    private void sendRequest(int option) throws IOException {
        out.print(option + "\n");
        out.flush();
    }
    
    
    /**
     * gets the reply from the server
     * @return the servers response 
     * @throws IOException in case theres an error with the connection
     */
    private Integer getReply() throws IOException {
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("connection closed");
        }
        
        try {
            return new Integer(reply);
        } catch (NumberFormatException nfe) {
            throw new IOException("misformatted reply: " + reply);
        }
    }


    /**
     * Cleanly closes a client cleanly.
     * @throws IOException if an error occurs closing the client.
     */
    private void close() throws IOException  {
        in.close();
        out.close();
        socket.close();
    }
    
    
    
    
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
        try {
            Client client = new Client("localhost", Server.PORT);
            int choice = -1;
            try {
	            while (choice != 0) {
	            	System.out.println("Select an option\n1. Next even fibonacci number"
	            			+ "\n2. Next larger random number\n3. Next prime number");
		            choice = in.nextInt();
		            System.out.println("Sent value " + choice);
		            client.sendRequest(choice);
		            System.out.println("");
		    	    Integer y = client.getReply();
		    	    System.out.println(""+y);
	            	
	            }
            }
            
            //in case a non-integer is attempted to be sent to the server
            catch (InputMismatchException e) {
            	//tell the server to close the client by sending 0
            	client.sendRequest(0);
            	System.err.println("Invalid option. Closing client cleanly.");
            }
            
            client.close();
        } catch (IOException ioe) {
        	System.err.print(ioe.getMessage());
        	
        }
        finally {
        	in.close();
        }
    }
}