package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;


/**
 * Base class for all events that can be fired and listened for in Glass Pane objects.<br/>
 * All classes extending PaneEvent <b>MUST</b> specify a public static Class<?>[] field named SIGNATURE, containing an array of classes
 * matching the signature of the event's constructor.
 * 
 * @author Aesen Vismea
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
@RequiredArgsConstructor
public abstract class PaneEvent {
	/**
	 * The source of this event.
	 */
	PaneComponent source;
	/**
	 * Whether or not this event is consumed.
	 */
	@NonFinal boolean consumed = false;
	
	/**
	 * Consumes this event, preventing it from firing other event behaviors.<br/>
	 * It will still propagate to other event listeners of the same type, but will prevent this low-level event from triggering high-level
	 * behaviors, such as a mouse click triggering a button press.
	 */
	public void consume() {
		consumed = true;
	}
	
}
