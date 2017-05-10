import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class runtimeThr implements Runnable
{
	
	public static Queue<RTdata> requestQue;
	public static Queue<RTdata> returnQue;
	public static LinkedList<uThr> uThrList;
	
	private Socket socket;
	private networkThr netThr;
	private localThr local;
	
	private boolean done;
	private static Lock lock = new ReentrantLock();
	private static Lock lockMsg = new ReentrantLock();
	private Lock lockReq = new ReentrantLock();
	
	//Receives the socket to send to the network thread 
	public runtimeThr(Socket s)
	{
		socket = s;
		requestQue = new LinkedList<RTdata>();
		returnQue = new LinkedList<RTdata>();
		uThrList = new LinkedList<uThr>();
	}
	public void run()
	{
		
		while(true){
			if(!requestQue.isEmpty())
			{
				try{
				RTdata data = requestQue.poll();
				//System.out.println("ReqQueue-Thread:" +data.getThreadId() + " c:" + data.getCommand() );
				if(data.getCommand() >= 1 && data.getCommand() <= 3)
				{
					try {
						netThr = new networkThr(socket, data);
						Thread t = new Thread(netThr);
						t.start();
						t.join();
					} catch (IOException e) {e.printStackTrace();}
					data = netThr.returnData();
					returnQue.add(data);
				}
				else if(data.getCommand() == 4 || data.getCommand() == 5)
				{
					try {
						local = new localThr(data.getCommand(), data);
						Thread t = new Thread(local);
						t.start();
						t.join();
					} 
					catch (Exception e) {e.printStackTrace();}
					data = local.returnData();
					returnQue.add(data);
				}
				if(!returnQue.isEmpty())
				{
					RTdata dt = returnQue.poll();
					uThr u = uThrList.get(dt.getThreadId());
					u.printMessage(dt.getMessage());
					//System.out.print("ReturnQueue-Thread:" +dt.getThreadId() + " c:" + dt.getCommand() );
					//System.out.println(" Msg: "+ dt.getMessage() );
				}
				}catch(Exception e){
					
				}
			}
		}
	}
	/*
	 * This will send the message back to the correct thread
	 */
	public void sendMessageBack()
	{
		try{
			lockMsg.lock();
			RTdata d = (RTdata) returnQue.poll();
			uThrList.get(d.getThreadId()).printMessage(d.getMessage());
		}finally{
			lockMsg.unlock();
		}
	}
	public void addThrList(uThr u){
		uThrList.add(u);
	}
	public static void toReqQue(RTdata o)
	{
		//System.out.println("In QUEUE - Thread:" +o.getThreadId() + " c:" + o.getCommand() );
		try{
			lock.lock();
			requestQue.add(o);	
		}finally{
			lock.unlock();
		}
	}
}
