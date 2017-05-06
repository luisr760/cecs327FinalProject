public class RTdata
{
		private int threadId;
		private int command;
		private String message;
		
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
		public void setMessage(long s)
		{
			message = s + "";
		}
		
	}