import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This class will was spawned by the runtime thread
 * thus meaning the command was 1-3 and sends a request 
 * to the server to compute the nextLargeRand, nextEvenFibb, or
 * nextPrime based on command waits for reply and then 
 * modifies data that will soon be sent back to uThr
 */
public class networkThr implements Runnable
{
	private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private RTdata data;
    /**
	 * Sets up the netWorkThr in and out reader and writer to communicate
	 * with the server.
	 */
	public networkThr(Socket s, RTdata d) throws IOException
	{
		socket = s;
		data = d;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	public void run()
	{
		/**
		 * Depending on the command it will send a request to the server
		 * and wait for reply
		 * and write the appropriate message based on command
		 */
		try {
			sendRequest(data.getCommand());
			long ans = getReply();
			if(data.getCommand() == 1){
				data.setMessage("NextEvenFib: ",ans);
			}else if(data.getCommand() == 2){
				data.setMessage("NextRand: ",ans);
			}else if(data.getCommand() == 3){
				data.setMessage("NextPrime: ",ans);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Data that was sent that contains info of what uthr it wants.
	 * @return returns the data that it was given
	 */
	public RTdata returnData(){
		return data;
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
}
