public class RTdata
{
		private int threadId;
		private int command;
		private String message;
		private int count;
		
		public void setThreadId(int t)
		{
			threadId = t;
		}
		public void setCommand(int c)
		{
			command = c;
		}
		public int getThreadId()
		{
			return threadId;
		}
		public int getCommand()
		{
			return command;
		}
		public String getMessage()
		{
			return message;
		}
		public void setMessage(String msg, long s)
		{
			message =  msg + " " +s ;
		}
		public void addCount(){
			count++;
		}
		public int getCount()
		{
			return count;
		}
		
	}