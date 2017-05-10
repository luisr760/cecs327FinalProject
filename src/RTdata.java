/**
 * 
 * @author Richard D., Luis R. Howard C. Mukesh S.
 * This class will hold data that is sent to the runtime data que.
 * It holds uThrid, command, and message.
 */
public class RTdata
{
		// Thread (uthr) id
		private int threadId;
		//command 1-5
		private int command;
		//Message that will go back to uThr
		private String message;
		/**
		 * Sets the id of the uThr
		 * @param t uThr id (0-7)
		 */
		public void setThreadId(int t)
		{
			threadId = t;
		}
		/**
		 * Sets the command uThr
		 * @param c
		 */
		public void setCommand(int c)
		{
			command = c;
		}
		/**
		 * Gets the threadId
		 * @return returns the uThrid
		 */
		public int getThreadId()
		{
			return threadId;
		}
		/**
		 * Gets the command that it was given
		 * @return
		 */
		public int getCommand()
		{
			return command;
		}
		/**
		 * return the message of what the uThr asked for 
		 * read to print to console
		 * @return
		 */
		public String getMessage()
		{
			return message;
		}
		/**
		 * Sets the message that will be later used to print 
		 * in the uThr
		 * @param msg a string containing the message of what command it wanted
		 * @param s the calculated answer it wanted 
		 */
		public void setMessage(String msg, long s)
		{
			message =  msg + " " +s ;
		}
		
		public String toString () {
			return message;
		}
	}