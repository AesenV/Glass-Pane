package gminers.glasspane;


/**
 * By default, a GlassPane will pause the game when it is displayed. Extend this class instead to suppress this behavior.<br/>
 * This only affects GlassPanes displayed with show() - panes shown with overlay() never pause the game.
 * 
 * @author Aesen Vismea
 * 
 */
public abstract class NonPausingGlassPane
		extends GlassPane {
	
}
