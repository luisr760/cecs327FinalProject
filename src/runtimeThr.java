import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This class checks its requestque to and it there are
 * elements then it spawns appropriate thread, network or local thread
 * based on the command/data of the element.
 */
public class runtimeThr implements Runnable
{

	public static Queue<RTdata> requestQue;
	public static Queue<RTdata> returnQue;
	public static LinkedList<uThr> uThrList;
	private static int counter = 0;
	
	private Socket socket;
	private networkThr netThr;
	private localThr local;

	private static Lock lock = new ReentrantLock();
	private static Lock lockReq = new ReentrantLock();
	
	/**
	 * runtimeThr Constructor will get instantiate the queues
	 * and the uThrlists
	 * @param s the socket that will be used to connect to the server
	 */
	public runtimeThr(Socket s)
	{
		socket = s;
		requestQue = new LinkedList<RTdata>();
		returnQue = new LinkedList<RTdata>();
		uThrList = new LinkedList<uThr>();
	}
	
	public void run()
	{

		/*the runtimeThr will keep running until the number of completed uThr is 
		equal to or greater than the number of uThr (so until they're all done)
		AND the request queue is finally empty*/
		
		while(counter < Client.numOfUthr || (!requestQue.isEmpty())) {
			/*
			 Will check if the request queue conatains
			 element if it does it grabs the head element
			 then check the command of the data and chooses the 
			 appropriate network or local thread
			 */

			if(!requestQue.isEmpty())
			{
				//try{
					//lockReq.lock();
					RTdata data = requestQue.poll();
					/*
					 If command is 1-3 if call networkthread
					 */
					if(data.getCommand() >= 1 && data.getCommand() <= 3)
					{
						try {
							netThr = new networkThr(socket, data);
							Thread t = new Thread(netThr);
							t.start();
							t.join();
						} catch (IOException e) {e.printStackTrace();} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*
						 gets the new data modified by the networkThread
						 */
						data = netThr.returnData();
						/*
						 adds data with calculated answer to return que
						 */
						returnQue.add(data);
					}
					/*
					 If command is 4-5 if call localThread
					 */
					else if(data.getCommand() == 4 || data.getCommand() == 5)
					{
						try {
							local = new localThr(data.getCommand(), data);
							Thread t = new Thread(local);
							t.start();
							t.join();
						} 
						catch (Exception e) {e.printStackTrace();}
						/*
						 gets the new data modified by the networkThread
						 */
						data = local.returnData();
						/*
						 adds data with calculated answer to return que
						 */
						returnQue.add(data);
					}
					/*
					  if return que isnt empty then it will get the current 
					  head element and tell the correct uThr to print the message 
					  it asked for
					 */
					if(!returnQue.isEmpty())
					{

						RTdata dt = returnQue.poll();
						uThr u = uThrList.get(dt.getThreadId());
						u.printMessage(dt);
					}
				}
				//finally{
				//	lockReq.unlock();
				//}
			//}
		}
	}

	
	/**
	 * A method for incrementing the counter of completed uThr is runtimeThr.
	 */
	public static void increment () {
		counter++;
	}
	
	
	/**
	 * Add a uThr to a list
	 * @param u the uthr added to a list.
	 */
	public void addThrList(uThr u){
		uThrList.add(u);
	}
	/**
	 * This will add the data that a uthr wants to the queue
	 * @param o the Data that uThr wants the runtime to get for it
	 */
	public static void toReqQue(RTdata o)
	{
		try{
			lock.lock();
			requestQue.add(o);	
		}finally{
			lock.unlock();
		}
	}
}