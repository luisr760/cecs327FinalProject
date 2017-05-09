import java.io.IOException;
import java.net.Socket;
import java.util.*;
public class runtimeThr implements Runnable
{
	
	public Queue requestQue;
	public Queue returnQue;
	public LinkedList<uThr> uThrList;
	private Socket socket;
	private networkThr netThr;
	private localThr local;
	private boolean done;
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
		Thread t = null;
		while(true)
		{
			
			if(!requestQue.isEmpty())
			{
				RTdata data = (RTdata) requestQue.poll();
				try 
				{
					if(data.getCommand() == 4 || data.getCommand() == 5)
					{
						local = new localThr(data.getCommand(), data);
						t = new Thread(local);
						t.start();
						t.join();
						returnQue.add(local.returnData());
					}
					else{
						netThr = new networkThr(socket, data);
						t = new Thread(netThr);
						t.start();
						t.join();
						returnQue.add(netThr.returnData());
					}
				} catch (IOException e) 
				{
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!returnQue.isEmpty())
				{
					sendMessageBack();
				}
			}
			
		}
	}
	/*
	 * This will send the message back to the correct thread
	 */
	public void sendMessageBack()
	{
		RTdata d = (RTdata) returnQue.poll();
		uThrList.get(d.getThreadId()).printMessage(d.getMessage());
	}
	public void addThrList(uThr u){
		uThrList.add(u);
	}
	public void toReqQue(RTdata o)
	{
		requestQue.add(o);	
	}
	public void setDone(boolean d)
	{
		done = d;
	}
}
