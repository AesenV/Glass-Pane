package gminers.glasspane.event;


import gminers.glasspane.GlassPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.GuiScreen;


/**
 * Called when a GlassPane is displayed, either automatically via autoOverride(Class<?>) or manually with show().
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class PaneOverrideEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			GlassPane.class,
			GuiScreen.class
	};
	/**
	 * The GlassPane being displayed. The same as 'source', but already cast to a GlassPane.
	 */
	GlassPane pane;
	/**
	 * The GuiScreen being overridden. If it was a GlassPane, this will be an instance of GlassPaneMirror.
	 */
	GuiScreen orig;
	
	public PaneOverrideEvent(final GlassPane pane, final GuiScreen orig) {
		super(pane);
		this.pane = pane;
		this.orig = orig;
	}
}
