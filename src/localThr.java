import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This will be spawned by runtime is commad is 
 * 4-5 and will get the nextEven or nextOdd depending 
 * on the command
 */
public class localThr implements Runnable
{
	public static long even = 2;
	public static long odd = 1;
	private int choice;
	private RTdata data;
	private Lock evenLock = new ReentrantLock();
	private Lock oddLock = new ReentrantLock();
	/**
	 * localThr constructor sets the choice
	 * it was sent from network thread and the data to setmessage
	 * to send back
	 * @param c choice of command 4 or 5
	 * @param d data that contains id,command, and message
	 */
	public localThr(int c, RTdata d)
	{
		choice = c;
		data = d;
	}
	
	public void run()
	{
		switch (choice)
		{
			/**
			 * If choice is 4 then
			 * it will get next even
			 */
			case 4: 
				try{
					evenLock.lock();
					long curr = nextEven();
					data.setMessage("NextEven: ",curr);
					even+=2;
				}finally{
					evenLock.unlock();
				}
			break;
			/**
			 * If choice is 5 then it will retrieve the next odd
			 */
			case 5:
				try{
					oddLock.lock();
					long curr2 = nextOdd();
					data.setMessage("NextOdd: ",curr2);
					odd+=2;
				}finally{
					oddLock.unlock();
				}
			break;
		}
		
	}
	/**
	 * Return the data that it manipulated to be sent back to uThr later
	 * by the Runtimethr
	 * @return return the RTdata that holds all info
	 */
	public RTdata returnData(){
		return data;
	}
	/**
	 * Return the nextEven
	 * @return returns the next even number
	 */
	public long nextEven()
	{
		return even;
	}
	/**
	 * Return the nextOdd
	 * @return returns the next odd number
	 */
	public long nextOdd()
	{
		return odd;
	}

}