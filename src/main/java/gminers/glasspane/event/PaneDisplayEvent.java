package gminers.glasspane.event;


import gminers.glasspane.GlassPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a GlassPane is displayed, either automatically via autoOverride(Class<?>) or manually with show().
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class PaneDisplayEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		GlassPane.class
	};
	/**
	 * The GlassPane being displayed. The same as 'source', but already cast to a GlassPane.
	 */
	GlassPane pane;
	
	public PaneDisplayEvent(final GlassPane pane) {
		super(pane);
		this.pane = pane;
	}
}
