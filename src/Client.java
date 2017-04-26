import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    private void sendRequest(int x) throws IOException {
        out.print(x + "\n");
        out.flush(); // important! make sure x actually gets sent
    }
    
    private Integer getReply() throws IOException {
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("connection terminated unexpectedly");
        }
        
        try {
            return new Integer(reply);
        } catch (NumberFormatException nfe) {
            throw new IOException("misformatted reply: " + reply);
        }
    }


    /**
     * Cleanly closes a client.
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
            int choice = 0;
            try {
	            while (choice != -1) {
	            	System.out.println("Select an option\n1. Next even fibonacci number"
	            			+ "\n2. Next larger random number\n3. Next prime number");
		            choice = in.nextInt();
		            client.sendRequest(choice);
		            System.out.println("");
		    	    Integer y = client.getReply();
		    	    System.out.println(""+y);
	            	
	            }
            }
            //in case a non-integer is attempted to be sent to the server
            catch (InputMismatchException e) {
            	System.err.println("Invalid option. Closing client cleanly.");
            }
            
            client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
        	in.close();
        }
    }
}