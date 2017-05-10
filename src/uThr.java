import java.util.*;

/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This class generates 20 random commands
 * of nextEven, nextOdd, nextRand, nextPrime, or
 * nextEvenFibb. Then puts it in a requestQue un runtimeThr
 */
public class uThr implements Runnable
{
	
	private int iters = 20;
	private int command;
	private  int thrId;
    
	private Random rand;
	private RTdata trt; 
	/**
	 * uThr Constructor will set the id of the uThr
	 * @param id Sets id of the uThr
	 */
	public uThr(int id)
	{
		thrId = id;
	}
	
	public void run()
	{
		/**
		 * for loop will iterate 20 times for 20 commands. 
		 * and will send a request to runtime to grab an answer from server 
		 * using networkThr or localThr.
		 */
		for(int i = 0; i < iters; i++)
		{
			trt = new RTdata();
			trt.setThreadId(thrId);
			rand = new Random();
			command = 1 + (rand.nextInt(5));
			trt.setCommand(command);
			runtimeThr.toReqQue(trt);
		}
		runtimeThr.increment();
	}
	
	
	/**
	 * printMessage will print out the result of the command it wanted.
	 * @param m it is the message from the returnQue in the runtime
	 */
	public void printMessage(RTdata m)
	{
		System.out.println(m);
	}
}