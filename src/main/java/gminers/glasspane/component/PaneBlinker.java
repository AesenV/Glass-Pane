package gminers.glasspane.component;


import gminers.glasspane.GlassPane;
import gminers.glasspane.GlassPaneMirror;
import gminers.kitchensink.Rendering;
import gminers.kitchensink.WaveType;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.gameminers.glasspane.internal.GlassPaneMod;


/**
 * A pulsing component, to bring attention. May be useful to make buttons more apparent in a "first run" scenario.<br>
 * The bounds of the component are used as the 'core' area to pulsate around. The minimum distance the ring will pulsate to is around the
 * inner edge of the bounds of this component. When created, this component will default clipToSize to false due to this behavior.<br>
 * <br>
 * Illustration:<br>
 * <img src="http://dl.gameminers.com/blinker.png"/>
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneBlinker
		extends ColorablePaneComponent {
	/**
	 * Whether or not this component is currently blinking.
	 */
	@Getter @Setter private boolean blinking = true;
	/**
	 * The maximum distance to pulsate to.
	 */
	@Getter @Setter private int distance = 10;
	/**
	 * The wave this component will use for pulsating.
	 */
	@Getter @Setter private WaveType wave = WaveType.ABSOLUTE_TANGENT;
	/**
	 * The speed that this component will pulsate at.
	 */
	@Getter @Setter private double speed = 4;
	
	private int tickCounter = 0;
	
	private PaneComponent target = null;
	
	public PaneBlinker() {
		setColor(0xFF0000);
	}
	
	@Override
	protected void doTick() {
		if (blinking) {
			tickCounter++;
		}
		if (target != null) {
			GlassPane targetPane = target.getGlassPane();
			// be very sure we should be displaying
			// a blinker floating in the middle of nowhere will look strange
			if (target.isVisible()
					&& targetPane != null
					&& (Minecraft.getMinecraft().currentScreen == targetPane.getScreenMirror()
							|| GlassPaneMod.inst.currentOverlays.contains(targetPane) || GlassPaneMod.inst.currentStickyOverlays
								.contains(targetPane))) {
				// mimic it's size, translation, and rotation
				setVisible(true);
				mimic(target);
				setTranslateX(target.getTranslateX());
				setTranslateY(target.getTranslateY());
				setRotationAllowed(target.isRotationAllowed());
				setAngle(target.getAngle());
				setXRot(target.getXRot());
				setYRot(target.getYRot());
				setZRot(target.getZRot());
			} else {
				setVisible(false);
			}
		}
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		if (blinking) {
			final double wv = wave.calculate((tickCounter + partialTicks) / speed);
			final int dist = (int)((float) (wv * distance));
			final int col = color | (((int) ((1 - wv) * 255D) & 0xFF) << 24);
			Rendering.drawRect(-dist, -dist, width + dist, (-dist) + 1, col);
			Rendering.drawRect(-dist, (-dist) + 1, (-dist) + 1, height + dist, col);
			Rendering.drawRect((-dist) + 1, height + dist, width + dist, (height + dist) - 1, col);
			Rendering.drawRect(width + dist, (-dist) + 1, (width + dist) - 1, (height + dist) - 1, col);
		}
	}
	
	/**
	 * Sets the target of this PaneBlinker to the passed component. A targeted blinker will track the position and size of the given
	 * component and mimic it as closely as possible to keep up with the component. If the component becomes orphaned or hidden, the blinker
	 * will go invisible.<br/>
	 * Null is acceptable and disables targeting.
	 */
	public void target(PaneComponent component) {
		this.target = component;
	}
	
	/**
	 * Searches for a component in the current overlay stack, displaying screen, modal overlays, etc, trying to find a component with a
	 * matching name.
	 * If it is found, {@link #target(PaneComponent)} is called with the found component.
	 */
	public void target(String componentName) {
		PaneComponent found = null;
		// we want to process this in the order they're rendered (roughly), to be somewhat intuitive
		
		// look through the overlays first
		for (GlassPane pane : GlassPaneMod.inst.currentOverlays) {
			found = search(componentName, pane);
			if (found != null) {
				break;
			}
		}
		// look through sticky overlays next, if needed
		if (found == null) {
			for (GlassPane pane : GlassPaneMod.inst.currentStickyOverlays) {
				found = search(componentName, pane);
				if (found != null) {
					break;
				}
			}
		}
		// now check the currently displaying screen if we still haven't found it
		if (found == null) {
			GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
			if (currentScreen instanceof GlassPaneMirror) {
				GlassPaneMirror mirror = (GlassPaneMirror) currentScreen;
				found = search(componentName, mirror.getMirrored());
				// still haven't found it? check underneath
				found = searchModal(componentName, mirror);
			}
		}
		// we tried to find your component, but it's not happening
		if (found == null)
			throw new IllegalArgumentException("Cannot find component with name '" + componentName
					+ "' in the current Glass Pane display stack!");
		// otherwise, we're ready
		// les do dis
		target(found);
	}
	
	// yeah, i'm using a stack-based search.
	// there should never be any hierarchies deep enough to cause a stack overflow...
	// but someone's probably going to find a way to do it anyway
	
	private PaneComponent searchModal(String needle, GlassPaneMirror haystack) {
		if (haystack.isModal()) {
			// same order as before
			for (GlassPane pane : haystack.getModalUnderlays()) {
				PaneComponent found = search(needle, pane);
				if (found != null) return found;
			}
			// check the pane itself
			GuiScreen underneath = haystack.getModal();
			if (underneath instanceof GlassPaneMirror) {
				GlassPaneMirror underMirror = (GlassPaneMirror) underneath;
				PaneComponent found = search(needle, underMirror.getMirrored());
				if (found != null) return found;
				// we have to go deeper
				found = searchModal(needle, underMirror);
				if (found != null) return found;
			}
		}
		return null;
	}
	
	private PaneComponent search(String needle, PaneContainer haystack) {
		for (PaneComponent pc : haystack.getComponents()) {
			if (needle.equals(pc.getName()))
				return pc;
			else if (pc instanceof PaneContainer) {
				PaneComponent found = search(needle, (PaneContainer) pc);
				if (found != null) return found;
			}
		}
		return null;
	}
	
}
