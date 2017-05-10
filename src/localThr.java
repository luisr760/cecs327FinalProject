
public class localThr implements Runnable
{
	public static long even = 2;
	public static long odd = 1;
	private int choice;
	private RTdata data;
	
	public localThr(int c, RTdata d)
	{
		choice = c;
		data = d;
	}
	public void run()
	{
		switch (choice)
		{
			case 4: 
				//System.out.println("even");
				long curr = nextEven();
				data.setMessage("NextEven: ",curr);
				even+=2;
			break;
			case 5: 
				//System.out.println("odd");
				long curr2 = nextOdd();
				data.setMessage("NextOdd: ",curr2);
				odd+=2;
			break;
		}
		
	}
	public RTdata returnData(){
		return data;
	}
	public long nextEven()
	{
		return even;
	}
	public long nextOdd()
	{
		return odd;
	}

}
