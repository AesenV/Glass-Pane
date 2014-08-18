package gminers.glasspane.event;


import gminers.glasspane.GlassPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a GlassPane is hidden from view.
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class PaneHideEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		GlassPane.class
	};
	/**
	 * The GlassPane being hidden. The same as 'source', but already cast to a GlassPane.
	 */
	GlassPane pane;
	
	public PaneHideEvent(final GlassPane pane) {
		super(pane);
		this.pane = pane;
	}
}
