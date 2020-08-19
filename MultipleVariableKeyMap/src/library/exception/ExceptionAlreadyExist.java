package library.exception;

public class ExceptionAlreadyExist extends Exception {

	private static final long serialVersionUID = -2144727464335948793L;

	public ExceptionAlreadyExist(String msg){
		super(msg);
	}
}