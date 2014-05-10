package gminers.glasspane.listener;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotate a method with this annotation to allow it to be registered as an event listener.
 * 
 * @author Aesen Vismea
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.METHOD
})
public @interface PaneEventHandler {
	/**
	 * True to make this handler be skipped if the event has been consumed.
	 */
	public boolean ignoreConsumed() default false;
}
