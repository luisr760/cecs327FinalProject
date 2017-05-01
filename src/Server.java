import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This program creates a server that creates a thread for each connected client.
 * The server is stateful and concurrent, and responds to requests in the form
 * of numbers (1 - 3 being valid requests). It can return an even fibonacci, a random 
 * number, or a prime number. Implemented in TCP.
 *
 */

public class Server {
	/** Default port number where the server listens for connections. */
	public static final int PORT = 8001;
	
	//counts for the number of each request
	int count_fib = 1;
	int count_rand = 1;
	int count_prime = 1;

	//lower and upper bounds of the random number generator
	int mRandMin = 0;
	long mPrime = 1;
	long mFib = 0;
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
	 * Handles connected clients by waiting for request, and responding appropriately.
	 * @param socket socket where client is connected
	 * @throws IOException if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.out.println("client connected");

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		

		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			
		try {
			value = -1;
			while(value != 0) {
				try {
					value = Character.getNumericValue(in.read());
					
					//remove afterwards
					System.out.println("read value" + value);
					// compute answer and send back to client
					switch(value) {
						case 1:
								long fibTemp = nextFibonacciEven(count_fib);
								
								/*if the current fibonacci is larger than the next fibonacci
								you've overflowed*/
								if (mFib > fibTemp) {
									count_fib = 1;
									mFib = nextFibonacciEven(count_fib);
								}
								else
									mFib = fibTemp;
								System.out.println("request for even fibonacci: " + count_fib);

								System.out.println("reply: " + mFib);
								out.println(mFib);
								count_fib++;
						
						break;
						
						case 2: 
								 System.out.println("request for random number: " + count_rand);
					             int rand = new Random().nextInt(100 + mRandMin) + mRandMin;
					             
					             //if rand is smaller than rand_min, then you overflowed					             
					             if(rand < mRandMin) {
					            	 mRandMin = 1;
					             }
					             //rand_min becomes the new minimum to guarantee next random value is larger
					             else
					            	 mRandMin = rand;
					             System.out.println("reply: " + rand);
					             out.println(rand);
					             

					             count_rand++;
						
						break;
						
						case 3:
							  System.out.println("request for prime number: " + count_prime);
							  long primeTemp = getNextPrime(count_prime);
							  
							  //if you overflow
							  if (primeTemp < mPrime) {
								  count_prime = 1;
								  mPrime = getNextPrime(count_prime);
							  }
							  else
			                      mPrime = primeTemp;
		                      System.out.println("reply: " + mPrime);
		                      out.println(mPrime);
		                      count_prime++;
						break;
						
						//if the client sends a request that isn't 1, 2, or 3, send nothing back
						default:
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

		}
		
		//catch errors having to do with the clients socket (such as force closing it)
		catch(SocketException e) {
			System.err.println("Client closed unexpectedly.");
		}
		
		//we want to close the client to prevent their respective thread from continuing execution
		finally {
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
	private long nextFibonacciEven(int n) {
		
		if (n < 1) {
			return n;
		}
		if (n == 1) {
			return 2;
		}
		
		long e = ((4*nextFibonacciEven(n-1)) + nextFibonacciEven(n-2));
		
	    return e;

	}
	
	/**
	 * 
	 * @param number the number of the nth prime number, where n is number
	 * @return the nth prime number
	 */
	private long getNextPrime(int number)
	{
		long currentNum = mPrime;
		currentNum++;
		while (!(isPrime(currentNum))) {
			currentNum++;
		}
		return currentNum;
	} 

	/**
	 * Will check if a given number is a prime
	 * @param num the number to check
	 * @return True or False whether that number is prime
	 */
	private static boolean isPrime(long num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) return false;
        return true;
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