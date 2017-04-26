import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class Server {
	/** Default port number where the server listens for connections. */
	public static final int PORT = 8012;
	Random m_random = new Random();
	
	//counts for the number of each request
	int count_fib = 1;
	int count_rand = 1;
	int count_prime = 1;
	
	int rand_temp = 0;
	int rand_max = 100000;
	int prime_temp = 1;
	private int value;
	private ServerSocket serverSocket;


	/**
	 * Constructor for a Server
	 * @param port the port number of the server
	 * @throws IOException if there are complications with the ServerSocket
	 */
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		value = 0;
	}

	/**
	 * The running state of the server, listening for connections to handle
	 * @throws IOException if the main server socket is broken
	 */
	private void serve() throws IOException {
		//keep the serving running
		while (true) {
			final Socket socket = serverSocket.accept();
			
			// create a new thread to handle that client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(socket);
						} finally {
							socket.close();
						}
					} 
					
					catch (IOException ioe) {
						// this exception wouldn't terminate serve(),
						// since we're now on a different thread, but
						// we still need to handle it
						ioe.printStackTrace();
					}
				}
			});
			
			// start the thread
			handler.start();
		}
	}

	/**
	 * Handles connected clients.
	 * @param socket socket where client is connected
	 * @throws IOException if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.out.println("client connected");

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		

		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			
		try {
			value = 0;
			while(true) {
				try {
					value = Character.getNumericValue(in.read());
					
					// compute answer and send back to client
					switch(value){
						case 1:
								System.out.println("request for even fibonacci: " + count_fib);
								int w = Integer.valueOf(count_fib);
								int y = nextFibonacciEven(w);
								System.out.println("reply: " + y);
								out.println(y);
								count_fib++;
						
						break;
						
						//go over this. Ask him to explain the random limits.
						case 2: 
								 System.out.println("request for random number: " + count_rand);
					             int o = Integer.valueOf(count_rand);
					             int t = randomWithRange(o, o, o*100);
					             System.out.println("reply: " +t);
					             out.println(t);
					             if(rand_temp > 90000) {
					            	 rand_temp = 1;
					             } else {
					            	 rand_temp = t;
					             }
					             count_rand++;
						
						break;
						case 3:
							  System.out.println("request for prime number: " + count_prime);
		                      int x = Integer.valueOf(prime_temp);
		                      int c = (int) getNextPrime(x);
		                      System.out.println(c);
		                      System.out.println(prime_temp);
		                      if (c == prime_temp) {
		                    	  c += 1;
		                    	  System.out.println(c);
		                    	  for (int b = c; b <= (c * 10); b++) {
		                    		  c = (int) getNextPrime(b);
		                    		  if ( c != prime_temp) {
		                    			  break;
		                    		  }
		                    	  }
		                      }
		                      System.out.println("reply: " + c);
		                      out.println(c);
		                      prime_temp = c;
		                      count_prime++;
						break;
						
					}

				} 
				//in case the client receives a non-integer.
				catch (NumberFormatException e) {
					// complain about ill-formatted request
					System.out.println("reply: err");
					out.print("err\n");
				}
			}

		} finally {
			System.out.print("Client Closed.");
			out.close();
			in.close();
		}
		
	}

	/**
	 * Will return to the user the nth even fibonacci
	 * @param n The number of even fibonacci numbers into the sequence
	 * @return the nth even fibonacci number
	 */
	private int nextFibonacciEven(int n) {
		
		if (n < 1) {
			return n;
		}
		if (n == 1) {
			return 2;
		}
		
		int e = ((4*nextFibonacciEven(n-1)) + nextFibonacciEven(n-2));
		
	    return e;

	}
	
	private int randomWithRange(int number, int min, int max) {
		int rand = (int) ((max - min) * Math.random()) + min;
		do {
			if(rand > number) { 
				if (rand < rand_temp) {
					do {
						rand = (int) ((max - min) * Math.random()) + min;
					} while (rand_temp > rand);
				}
				return (int) rand;
			} else {
				rand = (int) ((max - min) * Math.random()) + min;
			}
		} while (rand > number);
		
		return rand;
	}
	
	/**
	 * 
	 * @param number 
	 * @return
	 */
	private long getNextPrime(long number)
	{
		BigInteger b = new BigInteger(String.valueOf(number));
        return Long.parseLong(b.nextProbablePrime().toString());
	} 

	public static void main(String[] args) {
		try {
			Server server = new Server(
					PORT);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}