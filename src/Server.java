import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


public class Server {
	/** Default port number where the server listens for connections. */
	public static final int PORT = 6980;
	Random m_random = new Random();
	public ArrayList primeNumbers = new ArrayList();
	int count_fib = 1;
	int count_rand = 1;
	int count_prime = 1;
	int rand_temp = 0;
	int rand_max = 100000;
	int prime_temp = 1;

	private ServerSocket serverSocket;

	// Rep invariant: serverSocket != null
	//
	// Thread safety argument:
	// TODO FIBONACCI_PORT
	// TODO serverSocket
	// TODO socket objects
	// TODO readers and writers in handle()
	// TODO data in handle()


	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
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
					} catch (IOException ioe) {
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
	 * Handle one client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.err.println("client connected");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and that buffer it so that we can read a line at a time
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the
		// socket output stream, and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it.
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()), true);
		
		int p = 0;
		
		try {
				try {
					// compute answer and send back to client
					for(int j = 1; j <= 5; j++) {
						System.err.println("request: " + count_fib);
						int x = Integer.valueOf(count_fib);
						int y = nextFibonacciEven(x);
						System.err.println("reply: " + y);
						out.println(y);
						count_fib++;
					}
					
					for(int j = 1; j <= 5; j++) { 
						 System.err.println("request: " + count_rand);
			             int o = Integer.valueOf(count_rand);
			             int t = randomWithRange(o, o, o*10000);
			             System.err.println("reply: " +t);
			             out.println(t);
			             if(rand_temp > 90000) {
			            	 rand_temp = 1;
			             } else {
			            	 rand_temp = t;
			             }
			             count_rand++;
					}
					
					for (int j = 1; j <= 5; j++) {
					  System.err.println("request: " + count_prime);
                      int x = Integer.valueOf(prime_temp);
                      int c = (int) getNextPrime(x);
                      if (c == prime_temp) {
                    	  c += 1;
                    	  for ( int b = c; b <= (c * 10); b++) {
                    		  c = (int) getNextPrime(b);
                    		  if ( c != prime_temp) {
                    			  break;
                    		  }
                    	  }
                      }
                      System.err.println("reply: " + c);
                      out.println(c);
                      prime_temp = c;
                      count_prime++;
					}

				} catch (NumberFormatException e) {
					// complain about ill-formatted request
					System.err.println("reply: err");
					out.print("err\n");
				}
				
			// important! our PrintWriter is auto-flushing, but if it were
			// not:
			// out.flush();
		} finally {
			out.close();
			in.close();
		}
	}

	/**
	 * Compute the n^th Fibonacci number
	 * 
	 * @param n
	 *            indicates the Fibonacci number to compute. Requires n > 0.
	 * @return the n^th Fibonacci number
	 */
	public int nextFibonacciEven(int n) {
		
		if (n < 1) {
			return n;
		}
		if (n == 1) {
			return 2;
		}
		
		int e = ((4*nextFibonacciEven(n-1)) + nextFibonacciEven(n-2));
		
	    return e;

	}
	
	public int randomWithRange(int number, int min, int max) {
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
	
	public long getNextPrime(long number)
	{
		BigInteger b = new BigInteger(String.valueOf(number));
        return Long.parseLong(b.nextProbablePrime().toString());
	} 

	/**
	 * Start a FibonacciServerMulti running on the default port.
	 */
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
