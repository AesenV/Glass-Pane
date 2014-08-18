package gminers.glasspane.exception;


/**
 * Thrown by internal GlassPane methods when something horrible occurs, making it impossible for GlassPane to operate correctly.<br/>
 * Only throw this Error if you feel that an error is severe enough that it should crash Minecraft, such as an important subsystem (like
 * events) failing to operate for a non-recoverable reason.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneCantContinueError
		extends Error {
	private static final long serialVersionUID = -5909212699861507521L;
	
	public PaneCantContinueError() {
		super();
	}
	
	public PaneCantContinueError(final String message) {
		super(message);
	}
	
	public PaneCantContinueError(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public PaneCantContinueError(final Throwable cause) {
		super(cause);
	}
}
