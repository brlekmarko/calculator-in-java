package hr.fer.zemris.java.gui.layouts;

/**
 * Greška koju bacamo prilikom krivog razmještaja elemenata u CalcLayoutu
 * 
 * @author Marko Brlek
 *
 */
public class CalcLayoutException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CalcLayoutException() {
		super();
	}
	
	public CalcLayoutException(String message) {
		super(message);
	}

}
