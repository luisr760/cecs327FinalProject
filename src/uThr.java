import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class uThr implements Runnable
{
	
	private int iters = 20;
	private int command;
	private  int thrId;
    
	private Random rand;
	private RTdata trt; 
	private static Socket socket;
	private static Lock lock = new ReentrantLock();
	
	public uThr(int id)
	{
		thrId = id;
	}
	/* command : 1 =  nextEven
				 2 =  nextOdd
				 3 = nextEvenFib
				 4 = Rand
				 5 = nextPrime*/
	public void run()
	{
		
		for(int i = 0; i < iters; i++)
		{
			trt = new RTdata();
			trt.setThreadId(thrId);
			rand = new Random();
			command = 1 + (rand.nextInt(5));
			trt.setCommand(command);
			runtimeThr.toReqQue(trt);
		}
		
	}
	public void printMessage(String m)
	{
		System.out.println("ID: " + thrId + " " + m);
	}
}

