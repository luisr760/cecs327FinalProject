import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
public class uThr implements Runnable
{
	
	private int iters = 20;
	private int command;
	private int thrId;
	
	private BufferedReader in;
    private PrintWriter out;
    
	private Random rand;
	private RTdata trt; 
	public runtimeThr runThr; 
	private static Socket socket;
	
	public uThr(int id, runtimeThr r)
	{
		rand = new Random();
		thrId = id;
		trt = new RTdata();
		trt.setThreadId(thrId);
		runThr = r;
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
			command = 1+ (rand.nextInt(3));
			trt.setCommand(command);
			runThr.toReqQue(trt);
		}	
	}
	public void printMessage(String m)
	{
		System.out.println(m);
	}
}

