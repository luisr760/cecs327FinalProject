
public class localThr implements Runnable
{
	public static long even = 2;
	public static long odd = 3;
	public static int countEven = 1;
	public static int countOdd = 1;
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
				countEven++;
			break;
			case 5: 
				//System.out.println("odd");
				long curr2 = nextOdd();
				data.setMessage("NextOdd: ",curr2);
				countOdd++;
			break;
		}
		
	}
	public RTdata returnData(){
		return data;
	}
	public long nextEven()
	{
		if(countEven == 1)
		{
			return 2;
		}else
		{
			even = even+2;
			return even;
		}
	}
	public long nextOdd()
	{
		if(countOdd == 1)
		{
			return 3;
		}else
		{
			odd = odd+2;
			return odd;
		}
	}

}
