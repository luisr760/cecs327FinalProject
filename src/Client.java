
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    // Rep invariant: socket, in, out != null
    
    public Client(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    public void sendRequest(int x) throws IOException {
        out.print(x + "\n");
        out.flush(); // important! make sure x actually gets sent
    }
    
    public Integer getReply() throws IOException {
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
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    
    
    
    
    private static final int N = 5;
    
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", Server.PORT);
           
            
            for (int i = 1; i <= 5; ++i) {
            	client.sendRequest(i);
            	System.out.println("fibonacci("+i+") = ?");
            }
            
            for (int i = 1; i <= 5; ++i) {
            	Integer y = client.getReply();
            	System.out.println("Next Even Fibonacci Number is = "+y);
            }
            
            for (int i = 1; i <= 5; ++i) {
            	client.sendRequest(i);
            	System.out.println("Random Number("+i+") = ?");
            }
            
            for (int i = 1; i <= 5; ++i) {
            	Integer y = client.getReply();
            	System.out.println("Next Larger Random number is = "+y);
            }
            
            for (int i = 1; i <= 5; ++i) {
            	client.sendRequest(i);
            	System.out.println("Number("+i+") = ?");
            }
            
            for (int i = 1; i <= 5; ++i) {
            	Integer y = client.getReply();
            	System.out.println("Next Prime is = "+y);
            }
           
            client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
