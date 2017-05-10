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
    //private BufferedReader in;
    //private PrintWriter out;

    /**
     * Constructor for a client
     * @param hostname the hostname for the socket
     * @param port the port number for the socket
     * @throws IOException in case socket encounters an error
     */
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    /**
     * Sends a request to connected server
     * @param option the requested option to send to the server
     * @throws IOException in case socket encounters an error
     */
    /*private void sendRequest(int option) throws IOException {
        out.print(option + "\n");
        out.flush();
    }
    
    
    /**
     * gets the reply from the server
     * @return the servers response 
     * @throws IOException in case theres an error with the connection
     */
    /*
    private long getReply() throws IOException {
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("connection closed");
        }
        
        try {
            return new Long(reply);
        } catch (NumberFormatException nfe) {
            throw new IOException("misformatted reply: " + reply);
        }
    }
*/

    /**
     * Cleanly closes a client cleanly.
     * @throws IOException if an error occurs closing the client.
     */
    private void close() throws IOException  {
        socket.close();
    }
    
    
    
    
    public static void main(String[] args) throws InterruptedException {
    	int uThrNumb= 8;
    
    	try {
            Client client = new Client("localhost", Server.PORT);
            
            runtimeThr rt =new runtimeThr(client.socket);
            Thread runtime = new Thread(rt);
    	    runtime.start();
    	    
	    	for(int i = 0; i < uThrNumb; i++)
	    	{
	    		uThr u = new uThr(i);
	    		Thread uThr = new Thread(u);
	    		rt.addThrList(u);
	    		uThr.start();
	    	}
    	}catch (IOException ioe) {
        	System.err.print(ioe.getMessage());
        }
    }
}