import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * @author Richard D., Luis R., Howard C., Mukesh S.
 * This program creates a client that sends requests to a server in the
 * form of integer values. The client does not send any data other than 
 * the integer specifying which response it is requesting. Implemented 
 * in TCP.
 */

public class Client {
    private Socket socket;

    /**
     * Constructor for a client
     * @param hostname the hostname for the socket
     * @param port the port number for the socket
     * @throws IOException in case socket encounters an error
     */
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
    }

    /**
     * Cleanly closes a client cleanly.
     * @throws IOException if an error occurs closing the client.
     */
    private void close() throws IOException  {
        socket.close();
    }
    
    public static void main(String[] args) throws InterruptedException {
    	int uThrNumb= 8;
    	Scanner in = new Scanner(System.in);
    	try {
            Client client = new Client("localhost", Server.PORT);
            
            runtimeThr rt =new runtimeThr(client.socket);
            Thread runtime = new Thread(rt);
    	    runtime.start();
    	    
    	    Thread uThr = null;
	    	for(int i = 0; i < uThrNumb; i++)
	    	{
	    		uThr u = new uThr(i, rt);
	    		uThr = new Thread(u);
	    		rt.addThrList(u);
	    		uThr.start();
	    		uThr.join();
	    	}
	    	runtime.join();
	    	client.close();
    	}catch (IOException ioe) {
        	System.err.print(ioe.getMessage());
        }
        finally {
        	in.close();
        }
    }
}