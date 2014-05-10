package gminers.glasspane.exception;


/**
 * Thrown by internal GlassPane methods when something horrible occurs, making it impossible for GlassPane to operate correctly.<br/>
 * Only throw this Error if you feel that an error is severe enough that it should crash Minecraft, such as an important subsystem (such as
 * events) failing to operate.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneCantContinueError
		extends Error {
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
