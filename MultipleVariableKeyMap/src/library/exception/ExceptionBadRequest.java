package library.exception;

public class ExceptionBadRequest extends Exception {

	private static final long serialVersionUID = 6123055423369563837L;

	public ExceptionBadRequest(String msg){
		super(msg);
	}
}