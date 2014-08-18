package gminers.glasspane.event;


import gminers.glasspane.GlassPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a GlassPane is pushed onto the overlay stack, either automatically via autoOverlay(Class<?>), or manually with overlay().
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class PaneOverlayEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		GlassPane.class
	};
	/**
	 * The GlassPane being displayed. The same as 'source', but already cast to a GlassPane.
	 */
	GlassPane pane;
	
	public PaneOverlayEvent(final GlassPane pane) {
		super(pane);
		this.pane = pane;
	}
}
