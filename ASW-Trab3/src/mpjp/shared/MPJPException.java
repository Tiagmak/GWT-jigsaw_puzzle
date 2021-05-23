package mpjp.shared;

/**
 *An exception raise by MPJP that needs to be sent to (web) clients
 */
public class MPJPException extends Exception{

	private static final long serialVersionUID = 1L;

	public MPJPException(){
		
	}
	
	public MPJPException(String message){
		 super(message);
	}
	
	public MPJPException(String message, Throwable cause){
		super(message, cause);
	}
	
	public MPJPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public MPJPException(Throwable cause){
		super(cause);
	}
}
