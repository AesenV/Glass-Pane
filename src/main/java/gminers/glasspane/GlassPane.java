package gminers.glasspane;


import gminers.glasspane.component.PaneContainer;
import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.event.MouseDownEvent;
import gminers.glasspane.event.PaneDisplayEvent;
import gminers.glasspane.event.PaneHideEvent;
import gminers.glasspane.event.PaneOverlayEvent;
import gminers.glasspane.shadowbox.AdaptivePanoramaShadowbox;
import gminers.glasspane.shadowbox.ImageTileShadowbox;
import gminers.glasspane.shadowbox.PaneShadowbox;
import gminers.glasspane.shadowbox.PanoramaShadowbox;
import gminers.kitchensink.Rendering;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.gameminers.glasspane.internal.GlassPaneMod;
import com.google.common.collect.Lists;


/**
 * GlassPane is the basis of the Glass Pane GUI system, and can be used as either an overlay or standalone UI.<br/>
 * If you need to get a GuiScreen mirror of a GlassPane, use {@link #getScreenMirror}.
 * 
 * @author Aesen Vismea
 */
@ToString
public abstract class GlassPane
		extends PaneContainer {
	/**
	 * A mirror of this GlassPane that can be used for APIs that need a GuiScreen, or for Minecraft itself.
	 */
	@Getter(lazy = true) private final GlassPaneMirror screenMirror = new GlassPaneMirror(this);
	/**
	 * Whether or not the revert() method can be used.
	 */
	@Getter @Setter private boolean revertAllowed = false;
	protected static final ResourceLocation defaultShadowboxTex = new ResourceLocation(
			"textures/gui/options_background.png");
	/**
	 * The shadowbox (background) used by this GlassPane.
	 * 
	 * @see ImageTileShadowbox
	 * @see PanoramaShadowbox
	 * @see AdaptivePanoramaShadowbox
	 */
	@Getter @Setter protected PaneShadowbox shadowbox = new ImageTileShadowbox(defaultShadowboxTex);
	private List<GlassPane> lastOverlays = null;
	private GuiScreen lastScreen = null;
	/**
	 * Whether or not this GlassPane is currently being displayed with takeover mode.
	 */
	@Getter protected boolean takingOver = false;
	/**
	 * Whether or not the shadowbox should be affected by rotation applied to this GlassPane.
	 */
	@Getter @Setter protected boolean shadowboxRotationAllowed = true;
	/**
	 * Whether or not the screen should be cleared before drawing this GlassPane. If the screen size or rotation changes on the fly, this
	 * will remove any artifacts left by the previous frame.
	 */
	@Getter @Setter protected boolean screenClearedBeforeDrawing = false;
	/**
	 * Whether or not this GlassPane will render when the HUD is disabled. (Only applies if this GlassPane is displayed over a GuiIngame)
	 */
	@Getter @Setter protected boolean renderedWhenHUDIsOff = false;
	
	/**
	 * Overrides the currently displaying GuiScreen with a screen dedicated to displaying this GlassPane, and stores the current GUI state
	 * for later use with {@link #revert}.<br/>
	 * Note: the screen displayed is the same as that returned by getScreenMirror.
	 */
	public final void show() {
		unsetModality();
		_show();
	}
	
	/**
	 * Pushes this GlassPane onto the current GuiScreen's overlay stack.
	 */
	public final void overlay() {
		unsetModality();
		// just add ourselves to the overlay list. this will put us on top since overlays are rendered in insertion-order. if modders need
		// something more complex, they can access currentOverlays directly.
		
		// yes, i am approving directly touching currentOverlays if you need it. just don't expect your code to work between Glass Pane
		// versions. it's in the internal package for a reason.
		GlassPaneMod.inst.currentOverlays.add(this);
		// fire an overlay event
		fireEvent(PaneOverlayEvent.class, this);
		// and now let's get the current screen size
		final Minecraft mc = Minecraft.getMinecraft();
		// avoid creating a ScaledResolution if we can
		if (mc.currentScreen != null) {
			setWidth(mc.currentScreen.width);
			setHeight(mc.currentScreen.height);
		} else {
			// well, there's no screen currently displayed. we'll just use a ScaledResolution
			final ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			setWidth(res.getScaledWidth());
			setHeight(res.getScaledHeight());
		}
	}
	
	/**
	 * Pushes this GlassPane onto the global overlay stack.
	 */
	public final void stickyOverlay() {
		unsetModality();
		GlassPaneMod.inst.currentStickyOverlays.add(this);
		// and now let's get the current screen size
		final Minecraft mc = Minecraft.getMinecraft();
		// avoid creating a ScaledResolution if we can
		if (mc.currentScreen != null) {
			setWidth(mc.currentScreen.width);
			setHeight(mc.currentScreen.height);
		} else {
			// well, there's no screen currently displayed. we'll just use a ScaledResolution
			final ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			setWidth(res.getScaledWidth());
			setHeight(res.getScaledHeight());
		}
		// fire an overlay event
		fireEvent(PaneOverlayEvent.class, this);
	}
	
	/**
	 * Performs a modal overlay with this GlassPane.<br/>
	 * A modal overlay renders a darkened background in front of the GUI that was the on top when modalOverlay()
	 * was called. Any shadowboxes that the modal GlassPane has will be skipped.<br/>
	 * It can be used to make dialogs, or similar things.
	 */
	public final void modalOverlay() {
		getScreenMirror().setModalUnderlays(Lists.newArrayList(GlassPaneMod.inst.currentOverlays));
		getScreenMirror().setModal(Minecraft.getMinecraft().currentScreen);
		_show();
	}
	
	private void _show() {
		// we offer a way to disable reverting if it's unnecessary, to save objects and cycles
		if (revertAllowed) {
			// first, save the current state of the gui
			lastScreen = Minecraft.getMinecraft().currentScreen;
			lastOverlays = Lists.newArrayList(GlassPaneMod.inst.currentOverlays); // copy it, since GlassPaneMod never gets rid of it's list
		}
		// then all we really need to do is display our mirror, the rest is handled by GlassPaneMod and GlassPaneMirror
		Minecraft.getMinecraft().displayGuiScreen(getScreenMirror());
	}
	
	/**
	 * Returns <code>this</code> - overridden for efficiency
	 */
	@Override
	public GlassPane getGlassPane() {
		return this;
	}
	
	/**
	 * Uses this GlassPane to completely take over Minecraft's rendering. This can be used to display a GlassPane before Minecraft is
	 * fully initialized.<br/>
	 * <br/>
	 * This method will block until another thread calls {@link #hide}. It is required to call this method from Minecraft's main thread,
	 * since the OpenGL context is only accessible from that thread.
	 * 
	 * @throws IllegalStateException
	 *             if Minecraft has finished initializing
	 */
	// TODO - Update this for new MouseUp, Wheel, etc events
	public final void takeover() {
		// make sure minecraft isn't fully initialized yet
		if (Minecraft.getMinecraft().theWorld != null || Minecraft.getMinecraft().currentScreen != null)
			throw new IllegalStateException("Minecraft is initialized!");
		long lastTick = 0;
		// activate the takeover flag
		takingOver = true;
		// enter a loop
		final Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		fireEvent(PaneDisplayEvent.class, this);
		while (takingOver) {
			// make sure Minecraft doesn't initialize while displaying this screen, as that would cause horrific flickering
			if (Minecraft.getMinecraft().theWorld != null || Minecraft.getMinecraft().currentScreen != null)
				throw new IllegalStateException("Minecraft is initialized!");
			// tick if we should
			if (System.currentTimeMillis() - lastTick >= 50) {
				if (Mouse.isCreated()) {
					while (Mouse.next()) {
						final int mX = Mouse.getEventX() * width / mc.displayWidth;
						final int mY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
						int button = Mouse.getEventButton();
						
						if (Minecraft.isRunningOnMac
								&& button == 0
								&& (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard
										.isKeyDown(Keyboard.KEY_RCONTROL))) {
							button = 1;
						}
						
						if (Mouse.getEventButtonState()) {
							
							fireEvent(MouseDownEvent.class, this, mX, mY, button);
						}
					}
				}
				
				if (Keyboard.isCreated()) {
					while (Keyboard.next()) {
						if (Keyboard.getEventKeyState()) {
							final int kCode = Keyboard.getEventKey();
							final char kChar = Keyboard.getEventCharacter();
							
							fireEvent(KeyTypedEvent.class, this, kChar, kCode);
						}
					}
				}
				tick();
				lastTick = System.currentTimeMillis();
			}
			// render
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			setWidth(res.getScaledWidth());
			setHeight(res.getScaledHeight());
			if (shadowbox != null) {
				boolean winch = false;
				if (shadowbox.getWidth() != res.getScaledWidth() || shadowbox.getHeight() != res.getScaledHeight()) {
					winch = true;
				}
				shadowbox.setWidth(res.getScaledWidth());
				shadowbox.setHeight(res.getScaledHeight());
				if (winch) {
					shadowbox.winch();
				}
			}
			GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, res.getScaledWidth(), res.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			render(Mouse.getX() * res.getScaledWidth() / mc.displayWidth,
					res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / mc.displayHeight - 1,
					(System.currentTimeMillis() - lastTick) / 50f);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glFlush();
			Display.update();
			// resize if we need to
			if (!mc.isFullScreen() && Display.wasResized()) {
				final int oldWidth = mc.displayWidth;
				final int oldHeight = mc.displayHeight;
				final int newWidth = mc.displayWidth = Display.getWidth();
				final int newHeight = mc.displayHeight = Display.getHeight();
				
				if (newWidth != oldWidth || newHeight != oldHeight) {
					if (mc.displayWidth <= 0) {
						mc.displayWidth = 1;
					}
					
					if (mc.displayHeight <= 0) {
						mc.displayHeight = 1;
					}
					
					mc.displayWidth = newWidth <= 0 ? 1 : newWidth;
					mc.displayHeight = newHeight <= 0 ? 1 : newHeight;
					
					final ScaledResolution res1 = new ScaledResolution(mc, newWidth, newHeight);
					setWidth(res1.getScaledWidth());
					setHeight(res1.getScaledHeight());
					
					mc.loadingScreen = new LoadingScreenRenderer(mc);
					if (mc.entityRenderer != null) {
						mc.entityRenderer.updateShaderGroupSize(newWidth, newHeight);
					}
				}
			}
			if (Display.isCloseRequested()) {
				mc.shutdownMinecraftApplet();
			}
			// use LWJGL's sync method to get the desired framerate
			Display.sync(30);
		}
		// draw the mojang logo again
		Rendering.drawFullScreenLogo(Rendering.MOJANG_LOGO, 0xFFFFFF);
	}
	
	/**
	 * Reverts the GUI state to what was stored when this GUI was displayed with <code>show</code>.<br/>
	 * If this GlassPane has never been displayed, does nothing.
	 */
	public final void revert() {
		// make sure we can do reverts
		if (!getScreenMirror().isModal() && !revertAllowed)
			throw new IllegalStateException("Attempt to use revert() on " + getClass().getName()
					+ ", but it's not enabled!");
		// then, make sure we have a state to revert to
		if (!getScreenMirror().isModal() && (lastOverlays == null || lastScreen == null))
			throw new IllegalStateException("Attempt to use revert() on " + getClass().getName()
					+ ", but there is no previous state to revert to!");
		// if we're a modal overlay, let's use our modal metadata to revert instead
		final GuiScreen screen = getScreenMirror().isModal() ? getScreenMirror().getModal() : lastScreen;
		
		final List<GlassPane> overlays = getScreenMirror().isModal() ? getScreenMirror().getModalUnderlays()
				: lastOverlays;
		// now, display the previous screen
		Minecraft.getMinecraft().displayGuiScreen(screen);
		// and restore the overlays
		GlassPaneMod.inst.currentOverlays.clear();
		if (overlays != null) {
			GlassPaneMod.inst.currentOverlays.addAll(overlays);
		}
		// and finally, invalidate our "previous" state since it's now current
		lastOverlays = null;
		lastScreen = null;
		getScreenMirror().setModal(null);
		getScreenMirror().setModalUnderlays(null);
	}
	
	/**
	 * Removes this GlassPane from the overlay stack, if it's on it.<br/>
	 * Also works in takeover mode to finish the takeover.
	 */
	public final void hide() {
		if (takingOver || GlassPaneMod.inst.currentOverlays.remove(this)
				|| GlassPaneMod.inst.currentStickyOverlays.remove(this)) {
			fireEvent(PaneHideEvent.class, this);
			focusedComponent = null;
		}
		takingOver = false;
	}
	
	/**
	 * Makes this GlassPane stop automatically overlaying the passed GlassPane, GuiScreen, or Object.
	 * 
	 * @param screenOrPane
	 *            The class of the GUI to stop overlaying.
	 * @throws IllegalArgumentException
	 *             If screenOrPane is not of a supported type.
	 */
	public final void stopOverlaying(final Class<?> screenOrPane) {
		// do we support the passed object?
		if (GlassPane.class.isAssignableFrom(screenOrPane) || GuiScreen.class.isAssignableFrom(screenOrPane)
				|| screenOrPane == Object.class) {
			// fetch the list for this specific class
			final List<GlassPane> list = GlassPaneMod.inst.overlays.get(screenOrPane);
			if (list == null) // if there's no list, there's no overlays for this class, so just return
				return;
			// remove us from the list
			list.remove(this);
			// if the list is empty, dereference it
			if (list.isEmpty()) {
				GlassPaneMod.inst.overlays.remove(screenOrPane);
			}
		} else
			throw new IllegalArgumentException(screenOrPane.getName() + " is not supported by stopOverlaying!");
	}
	
	/**
	 * Makes this GlassPane automatically get pushed onto the overlay stack when any screen of the passed type is shown.<br/>
	 * Works for GuiScreens or GlassPanes. Object can also be passed to overlay any and every screen, no matter it's type. GuiIngame can be
	 * passed to overlay the ingame GUI.
	 * 
	 * @param screenOrPane
	 *            The class of the GUI to overlay.
	 * @throws IllegalArgumentException
	 *             If screenOrPane is not of a supported type.
	 */
	public final void autoOverlay(final Class<?> screenOrPane) {
		// do we support the passed object? this is to prevent people from overlaying random garbage
		if (GlassPane.class.isAssignableFrom(screenOrPane) || GuiScreen.class.isAssignableFrom(screenOrPane)
				|| GuiIngame.class.isAssignableFrom(screenOrPane) || screenOrPane == Object.class) {
			// fetch the list for this specific class
			List<GlassPane> list;
			if (GlassPaneMod.inst.overlays.containsKey(screenOrPane)
					&& GlassPaneMod.inst.overlays.get(screenOrPane) != null) {
				// if we already have a list, we'll use it
				list = GlassPaneMod.inst.overlays.get(screenOrPane);
			} else {
				// otherwise make a new one
				list = Lists.newArrayList();
				GlassPaneMod.inst.overlays.put(screenOrPane, list);
			}
			// add us to the list
			list.add(this);
		} else
			throw new IllegalArgumentException(screenOrPane.getName() + " is not supported by autoOverlay!");
	}
	
	/**
	 * Makes this GlassPane stop automatically overriding the passed GlassPane or Object.
	 * 
	 * @param screenOrPane
	 *            The class of the GUI to stop overriding.
	 * @throws IllegalArgumentException
	 *             If screenOrPane is not of a supported type.
	 */
	public final void stopOverriding(final Class<?> screenOrPane) {
		// do we support the passed object?
		if (GlassPane.class.isAssignableFrom(screenOrPane) || GuiScreen.class.isAssignableFrom(screenOrPane)) {
			// are we the one overriding?
			if (GlassPaneMod.inst.overrides.get(screenOrPane) == this) {
				// if so, remove
				GlassPaneMod.inst.overrides.remove(screenOrPane);
			} else if (GlassPaneMod.inst.overrides.containsKey(screenOrPane)) {
				// otherwise, print a warning
				GlassPaneMod.inst.getLog().warn(
						"Attempting to stop overriding " + screenOrPane.getName() + " with "
								+ this.getClass().getName() + " but it's actually overridden with "
								+ GlassPaneMod.inst.overrides.get(screenOrPane).getClass().getName() + "!");
			}
		} else
			throw new IllegalArgumentException(screenOrPane.getName() + " is not supported by stopOverriding!");
	}
	
	/**
	 * Makes this GlassPane automatically get displayed when any screen of the passed type is shown, but not this specific GlassPane.<br/>
	 * Works for GuiScreens or GlassPanes.
	 * 
	 * @param screenOrPane
	 *            The class of the GUI to override.
	 * @throws IllegalArgumentException
	 *             If screenOrPane is not of a supported type.
	 */
	public final void autoOverride(final Class<?> screenOrPane) {
		// do we support the passed object? this is to prevent people from overriding random garbage
		if (GlassPane.class.isAssignableFrom(screenOrPane) || GuiScreen.class.isAssignableFrom(screenOrPane)) {
			// is it already being overridden? if so, print a warning
			if (GlassPaneMod.inst.overrides.containsKey(screenOrPane)) {
				GlassPaneMod.inst.getLog().warn(
						"CONFLICT: Overriding " + screenOrPane.getName() + " with " + this.getClass().getName()
								+ " but it is already overridden with "
								+ GlassPaneMod.inst.overrides.get(screenOrPane).getClass().getName() + "!");
			}
			// now put it into the map
			GlassPaneMod.inst.overrides.put(screenOrPane, this);
		} else
			throw new IllegalArgumentException(screenOrPane.getName() + " is not supported by autoOverride!");
	}
	
	/**
	 * Internal method used by GlassPaneMod. Do not touch. Beware of dog.
	 */
	public void unsetModality() {
		getScreenMirror().setModal(null);
		getScreenMirror().setModalUnderlays(null);
	}
	
}
