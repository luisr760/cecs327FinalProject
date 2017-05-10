
public class localThr implements Runnable
{
	public static long even = 2;
	public static long odd = 1;
	private int choice;
	private RTdata data;
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
				long curr = nextEven();
				data.setMessage("NextEven: ",curr);
				even+=2;
			break;
			/**
			 * If choice is 5 then it will retrieve the next odd
			 */
			case 5: 
				long curr2 = nextOdd();
				data.setMessage("NextOdd: ",curr2);
				odd+=2;
			break;
		}
		
	}
	/**
	 * Return the data that it manipulated to be sent back to uThr later
	 * by the Runtimethr
	 * @return
	 */
	public RTdata returnData(){
		return data;
	}
	/**
	 * Return the nextEven
	 * @return
	 */
	public long nextEven()
	{
		return even;
	}
	/**
	 * Return the nextOdd
	 * @return
	 */
	public long nextOdd()
	{
		return odd;
	}

}
