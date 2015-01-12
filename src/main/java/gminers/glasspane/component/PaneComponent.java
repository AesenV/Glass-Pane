package gminers.glasspane.component;


import static lombok.AccessLevel.NONE; // i generally think static imports are bad, but this is used in clear context and shortens lines
import gminers.glasspane.GlassPane;
import gminers.glasspane.GlassPaneMirror;
import gminers.glasspane.PaneBB;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.event.ComponentActivateEvent;
import gminers.glasspane.event.ComponentTickEvent;
import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.event.MouseDownEvent;
import gminers.glasspane.event.MouseUpEvent;
import gminers.glasspane.event.MouseWheelEvent;
import gminers.glasspane.event.PaneComponentPostRenderEvent;
import gminers.glasspane.event.PaneComponentPreRenderEvent;
import gminers.glasspane.event.PaneEvent;
import gminers.glasspane.event.PaneEventListenerRegisterEvent;
import gminers.glasspane.event.PaneEventListenerUnregisterEvent;
import gminers.glasspane.event.WinchEvent;
import gminers.glasspane.exception.PaneCantContinueError;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Rendering;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.PackagePrivate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.gameminers.glasspane.internal.GlassPaneMod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * Root class for all components that can be used in a PaneContainer, and make up the general UI of a GlassPane.<br/>
 * Yes, it extends PaneBB. Deal with it.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {
		"listeners",
		"parent"
})
@Getter
@Setter
// TODO: this class is a bit oversized, needs refactoring
public abstract class PaneComponent
		extends PaneBB {
	
	
	/**
	 * The Z index of this component. Components with higher Z indexes render in front of components with lower.
	 */
	protected int zIndex = 0;
	/**
	 * The angle of this component, in degrees. Only listened to if rotationAllowed is true.
	 */
	float angle = 0f;
	/**
	 * The X multiplier for rotation
	 */
	float xRot = 0f;
	/**
	 * The Y multiplier for rotation
	 */
	float yRot = 0f;
	/**
	 * The Z multiplier for rotation (this points into the screen and is probably what you want)
	 */
	float zRot = 0f;
	/**
	 * Whether or not rotation will be applied to this component.
	 */
	boolean rotationAllowed = false;
	/**
	 * For debugging - if true, renders a solid color box encompassing this entire component, with it's color being this component's
	 * identityHashCode.
	 */
	boolean drawBoundingBox = false;
	/**
	 * Whether or not this component will render.
	 */
	boolean visible = true;
	/**
	 * Whether or not to clip rendering of this component to it's bounding box.<br/>
	 * 
	 * @deprecated Clip To Size is very buggy and not worth the effort required to fix it properly. It is still used and maintained as some
	 *             PaneComponents rely on it, but as such Clip To Size functionality may contain PaneComponent-specific workarounds and
	 *             other such quirks, and it should not be depended upon for third-party components.
	 */
	@Deprecated boolean clipToSize = false;
	/**
	 * Whether or not to listen to the relative size set by relativeWidth. If this is true, width will be updated whenever the parent
	 * container is resized.
	 */
	boolean autoResizeWidth = false;
	/**
	 * Whether or not to listen to the relative size set by relativeHeight. If this is true, height will be updated whenever the parent
	 * container is resized.
	 */
	boolean autoResizeHeight = false;
	/**
	 * The multiplier to be applied to the parent container's width when autoResize is enabled and a WinchEvent is fired.
	 */
	double relativeWidth = 1.0;
	/**
	 * The multiplier to be applied to the parent container's height when autoResize is enabled and a WinchEvent is fired.
	 */
	double relativeHeight = 1.0;
	/**
	 * An offset to be applied to this component's width when it is resized due to autoResize.
	 */
	int relativeWidthOffset = 0;
	/**
	 * An offset to be applied to this component's height when it is resized due to autoResize.
	 */
	int relativeHeightOffset = 0;
	/**
	 * Whether or not to listen to the relative position set by relativeX. If this is true, X will be updated whenever the parent
	 * container is resized.
	 */
	boolean autoPositionX = false;
	/**
	 * Whether or not to listen to the relative position set by relativeY. If this is true, Y will be updated whenever the parent
	 * container is resized.
	 */
	boolean autoPositionY = false;
	/**
	 * The multiplier to be applied to the parent container's width when autoResize is enabled and a WinchEvent is fired.
	 */
	double relativeX = 1.0;
	/**
	 * The multiplier to be applied to the parent container's height when autoResize is enabled and a WinchEvent is fired.
	 */
	double relativeY = 1.0;
	/**
	 * An offset to be applied to this component's width when it is resized due to autoResize.
	 */
	int relativeXOffset = 0;
	/**
	 * An offset to be applied to this component's height when it is resized due to autoResize.
	 */
	int relativeYOffset = 0;
	/**
	 * Whether or not a ComponentActivateEvent should be fired when this component is clicked.
	 */
	boolean activatedOnClick = true;
	/**
	 * The tooltip to show when the mouse hovers over this component for a while.<br/>
	 * Null is acceptable, and suppresses the tooltip. Newlines are allowed.
	 */
	@Setter(NONE) String tooltip = null;
	/**
	 * The font renderer to use for the tooltip.
	 */
	FontRenderer tooltipFontRenderer = Minecraft.getMinecraft().fontRendererObj;
	/**
	 * The distance to translate the position of this component on the X axis, in 'big' pixels.
	 */
	protected float translateX = 0f;
	/**
	 * The distance to translate the position of this component on the Y axis, in 'big' pixels.
	 */
	protected float translateY = 0f;
	
	@Getter(NONE) @Setter(NONE) protected static final ResourceLocation RESOURCE = new ResourceLocation("glasspane",
			"wadjets.png");
	/**
	 * The parent of this component.
	 */
	@PackagePrivate @Setter(NONE) PaneContainer parent = null;
	@Getter(NONE) @Setter(NONE) protected int mouseX;
	@Getter(NONE) @Setter(NONE) protected int mouseY;
	@Getter(NONE) @Setter(NONE) private List<String> tooltipSplit = null;
	@Getter(NONE) @Setter(NONE) protected Map<String, String> metadata = Maps.newHashMap();
	
	@Getter(NONE) @Setter(NONE) protected Map<Class<? extends PaneEvent>, Map<Object, List<Method>>> listeners = new HashMap<Class<? extends PaneEvent>, Map<Object, List<Method>>>();
	
	public PaneComponent() {
		registerListeners(this);
	}
	
	public String getName() {
		return getMetadata("name");
	}
	
	public void setName(String name) {
		putMetadata("name", name);
	}
	
	public void putMetadata(final String k, final String v) {
		metadata.put(k, v);
	}
	
	public void removeMetadata(final String k) {
		metadata.remove(k);
	}
	
	public String getMetadata(final String k) {
		return metadata.get(k);
	}
	
	public boolean hasMetadata(final String k) {
		return metadata.containsKey(k);
	}
	
	/**
	 * Shortcut to set autoResizeWidth and autoResizeHeight at the same time.
	 */
	public void setAutoResize(final boolean autoResize) {
		autoResizeHeight = autoResizeWidth = autoResize;
	}
	
	/**
	 * Shortcut to set autoPositionX and autoPositionY at the same time.
	 */
	public void setAutoPosition(final boolean autoPosition) {
		autoPositionX = autoPositionY = autoPosition;
	}
	
	/**
	 * The tooltip to show when the mouse hovers over this component for a while.<br/>
	 * Null is acceptable, and suppresses the tooltip. Newlines are allowed.
	 */
	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		if (tooltip == null) {
			tooltipSplit = null;
		} else {
			tooltipSplit = Lists.newArrayList(tooltip.split("\n"));
		}
	}
	
	protected final boolean isListeningForEvent(final Class<? extends PaneEvent> eventClass) {
		return listeners.containsKey(eventClass) && listeners.get(eventClass).size() > 0;
	}
	
	/**
	 * Creates a new PaneBB that copies this component's x, y, width, and height. If you do not need to modify the returned PaneBB, it is
	 * more efficient to use PaneComponent as if it were a PaneBB, as it avoids unnecessary object creation.
	 * 
	 * @return A strictly PaneBB copy of this PaneComponent's bounds.
	 */
	public PaneBB getBounds() {
		return new PaneBB(this);
	}
	
	/**
	 * Renders this Component, applying all needed transformations, and then calling doRender. This method will revert all transformations
	 * it does afterward.<br/>
	 * 
	 * @param mouseX
	 *            The X coordinate of the mouse, in 'big' pixels.
	 * @param mouseY
	 *            The Y coordinate of the mouse, in 'big' pixels.
	 * @param partialTicks
	 *            The amount of the way into the next tick we are, since frames do not align with ticks.
	 */
	public final void render(final int mouseX, final int mouseY, final float partialTicks) {
		// just return if we aren't visible
		if (!visible) return;
		// push a matrix so we can easily revert
		GL11.glPushMatrix();
		// draw a bounding box if asked, for debugging purposes
		if (drawBoundingBox) {
			Rendering.drawRect(x, y, x + width, y + height, ~(System.identityHashCode(this) | 0xFF000000));
			Rendering.drawRect(x + 1, y + 1, x + (width - 2), y + (height - 2),
					System.identityHashCode(this) | 0xFF000000);
		}
		// only do position transformations if we're a component
		if (!(this instanceof GlassPane)) {
			// clip to this component's size
			if (clipToSize) {
				final GlassPane pane = getGlassPane();
				GL11.glScissor(getAbsoluteX(pane.getWidth()), getAbsoluteY(pane.getHeight()),
						getAbsoluteWidth(pane.getWidth()), getAbsoluteHeight(pane.getHeight()));
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
			}
			// translate to this component's coordinates
			GL11.glTranslatef(x, y, zIndex);
		} else if (((GlassPane) this).isScreenClearedBeforeDrawing() && currentScreenIsThis()) {
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		}
		GL11.glTranslatef(translateX, translateY, 0);
		// if we're a pane and have a shadowbox, this will be true
		final boolean renderShadowbox = this instanceof GlassPane && !((GlassPane) this).getScreenMirror().isModal()
				&& ((GlassPane) this).getShadowbox() != null
				&& (currentScreenIsThis() || ((GlassPane) this).isTakingOver());
		// if we don't want shadowboxes to be rotated, render it here
		if (renderShadowbox && !((GlassPane) this).isShadowboxRotationAllowed()) {
			((GlassPane) this).getShadowbox().render(mouseX, mouseY, partialTicks);
		}
		// apply rotation, if wanted
		if (angle != 0 && rotationAllowed) {
			GL11.glRotatef(angle, xRot, yRot, zRot);
		}
		// if we do want shadowboxes to be rotated, render it here
		if (renderShadowbox && ((GlassPane) this).isShadowboxRotationAllowed()) {
			((GlassPane) this).getShadowbox().render(mouseX, mouseY, partialTicks);
		}
		// set the fields, for doTick logic
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		// perform the render
		performRender(partialTicks);
		// pop the matrix to revert to the previous state, and disable scissor test
		if (clipToSize) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		if (hoverTime >= 30 && tooltip != null) {
			// render a tooltip if we should
			GL11.glTranslatef(0, 0, 5f);
			Rendering.drawHoveringText(tooltipSplit, mouseX, mouseY, tooltipFontRenderer);
		}
		GL11.glPopMatrix();
	}
	
	/**
	 * Gets the GlassPane ancestor of this Component.
	 * 
	 * @return The GlassPane at the root of the hierarchy this component is within, or null if this component is orphaned (or one of it's
	 *         parents is orphaned)
	 */
	public GlassPane getGlassPane() {
		PaneComponent work = this;
		while (!(work instanceof GlassPane)) {
			work = work.getParent();
			if (work == null) {
				break;
			}
		}
		if (work instanceof GlassPane)
			return (GlassPane) work;
		else
			return null;
	}
	
	public int getChainX() {
		return getX() + (getParent() == null ? 0 : getParent().getPX());
	}
	
	public int getChainY() {
		return getY() + (getParent() == null ? 0 : getParent().getPY());
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected int getAbsoluteX(final int scaledWidth) {
		return getAbsoluteX(getChainX(), scaledWidth);
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected static int getAbsoluteX(final int x, final int scaledWidth) {
		return (int) Math.floor(((double) x / ((double) scaledWidth)) * Minecraft.getMinecraft().displayWidth);
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected int getAbsoluteY(final int scaledHeight) {
		return getAbsoluteY(getChainY(), height, scaledHeight);
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected static int getAbsoluteY(final int y, final int height, final int scaledHeight) {
		return (Minecraft.getMinecraft().displayHeight - getAbsoluteHeight(height, scaledHeight))
				- (int) Math.ceil(((double) y / ((double) scaledHeight)) * Minecraft.getMinecraft().displayHeight);
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected int getAbsoluteEdgeY(final int scaledHeight) {
		return getAbsoluteY(getEdgeY(), height, scaledHeight)
				- (getParent() == null ? 0 : getParent().getAbsoluteEdgeY(scaledHeight));
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected int getAbsoluteWidth(final int scaledWidth) {
		return getAbsoluteWidth(width, scaledWidth);
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected static int getAbsoluteWidth(final int width, final int scaledWidth) {
		return (int) Math.ceil(((double) width / ((double) scaledWidth)) * Minecraft.getMinecraft().displayWidth) + 1;
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected int getAbsoluteHeight(final int scaledHeight) {
		return getAbsoluteHeight(height, scaledHeight);
	}
	
	/**
	 * Internal utility method for transforming GlassPane/Minecraft coords to GL coords
	 */
	protected static int getAbsoluteHeight(final int height, final int scaledHeight) {
		return (int) Math.ceil(((double) height / ((double) scaledHeight)) * Minecraft.getMinecraft().displayHeight);
	}
	
	private boolean currentScreenIsThis() {
		if (((GlassPane) this).isTakingOver()) return true;
		final Minecraft mc = Minecraft.getMinecraft();
		if (mc.currentScreen != null) {
			if (mc.currentScreen instanceof GlassPaneMirror)
				return ((GlassPaneMirror) mc.currentScreen).getMirrored() == this;
		}
		return false;
	}
	
	private void performRender(final float partialTicks) {
		if (clipToSize) {
			// save the scissor box so we can revert it in case the component's render method changes it (e.g. containers)
			GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
		}
		GL11.glPushMatrix();
		// fire a pre-render event
		fireEvent(PaneComponentPreRenderEvent.class, this, mouseX, mouseY, partialTicks);
		// render the component
		doRender(mouseX, mouseY, partialTicks);
		// and fire a post-render event.
		fireEvent(PaneComponentPostRenderEvent.class, this, mouseX, mouseY, partialTicks);
		GL11.glPopMatrix();
		if (clipToSize) {
			// restore the scissor box
			GL11.glPopAttrib();
		}
	}
	
	/**
	 * Renders this Component. When this method is called, a clip and transform have already been applied to the GL context and the receiver
	 * does not have to worry about applying transforms or going out of bounds.
	 * 
	 * @param mouseX
	 *            The X coordinate of the mouse, in 'big' pixels.
	 * @param mouseY
	 *            The Y coordinate of the mouse, in 'big' pixels.
	 * @param partialTicks
	 *            The amount of the way into the next tick we are, since frames do not align with ticks.
	 */
	protected abstract void doRender(final int mouseX, final int mouseY, final float partialTicks);
	
	/**
	 * Registers all methods annotated with {@link PaneEventHandler} as event listeners for their
	 * argument.
	 * 
	 * @param o
	 *            The object to register
	 */
	public final void registerListeners(final Object o) {
		for (final Method m : o.getClass().getMethods()) {
			// first check if we have the @PaneEventHandler annotation
			if (m.getAnnotation(PaneEventHandler.class) != null) {
				// if we do, see if the method has only one parameter
				if (m.getParameterTypes().length == 1) {
					// and that that parameter can be cast to a PaneEvent
					if (PaneEvent.class.isAssignableFrom(m.getParameterTypes()[0])) {
						// first we'll cast the parameter class, which should be safe given the above check
						@SuppressWarnings("unchecked")
						final Class<? extends PaneEvent> eventClass = (Class<? extends PaneEvent>) m
								.getParameterTypes()[0];
						// now we'll grab the objects map
						Map<Object, List<Method>> objects;
						if (listeners.containsKey(eventClass) && listeners.get(eventClass) != null) {
							// if it exists, use it
							objects = listeners.get(eventClass);
						} else {
							// otherwise make a new one
							objects = Maps.newHashMap();
							listeners.put(eventClass, objects);
						}
						// now grab the methods list
						List<Method> methodList;
						if (objects.containsKey(o) && objects.get(o) != null) {
							// use it if it exists
							methodList = objects.get(o);
						} else {
							// or make a new one.
							methodList = Lists.newArrayList();
							objects.put(o, methodList);
						}
						// now add the current method to the list
						methodList.add(m);
						// and fire an event for the registration
						fireEvent(PaneEventListenerRegisterEvent.class, this, o, m);
					} else {
						// not a PaneEvent, print an error and continue
						System.err
								.println("[GlassPane] [EventSystem] Found a method with incorrect parameter types when registering listener "
										+ o.getClass().getName());
					}
				} else {
					// method has more than one parameter, or no parameters. print an error and continue
					System.err
							.println("[GlassPane] [EventSystem] Found a method with an incorrect number of parameters when registering listener "
									+ o.getClass().getName());
				}
			}
		}
	}
	
	/**
	 * Unregisters all methods associated with the passed object.
	 * 
	 * @param o
	 *            The object to unregister
	 */
	public final void unregisterListeners(final Object o) {
		List<Class<? extends PaneEvent>> needsRemoval = null;
		boolean didSomething = false;
		for (final Entry<Class<? extends PaneEvent>, Map<Object, List<Method>>> en : listeners.entrySet()) {
			// do we have a map in this?
			if (en.getValue() != null) {
				// we do. does it contain our object?
				if (en.getValue().containsKey(o)) {
					// it does.
					en.getValue().remove(o);
					didSomething = true;
					// do we still have some entries in here?
					if (en.getValue().isEmpty()) {
						// no, we don't.
						if (needsRemoval == null) {
							needsRemoval = Lists.newArrayList();
						}
						// add this event class to the removal list to prevent having extra maps
						needsRemoval.add(en.getKey());
					}
				}
			}
		}
		// are some entries pending removal?
		if (needsRemoval != null) {
			// yes, so let's remove them
			for (final Class<? extends PaneEvent> clazz : needsRemoval) {
				listeners.remove(clazz);
			}
		}
		// fire an event for the unregistration, if we actually did something.
		if (didSomething) {
			// we only call this once per object instead of once per method like in register because we would get a concurrent modification
			// exception if we did it in the above for loop.
			fireEvent(PaneEventListenerUnregisterEvent.class, o);
		}
	}
	
	/**
	 * Fires an event of type <code>eventClass</code> to all listeners listening for that event type. Does not create an event object if
	 * there are no listeners for this event.
	 * 
	 * @param eventClass
	 *            The class of the event to fire.
	 * @param constructorArgs
	 *            The arguments to pass to the event's constructor.
	 * @return The instantiated event, or <code>null</code> if an error occurred or an event did not need to be instantiated.
	 */
	public <T extends PaneEvent> T fireEvent(final @NonNull Class<T> eventClass, final Object... constructorArgs) {
		// this is a bit hacky, but the only good way to forward into protected from outside the package w/o reflecting
		if (eventClass == KeyTypedEvent.class) {
			keyPressed((Character) constructorArgs[1], (Integer) constructorArgs[2]);
		} else if (eventClass == MouseDownEvent.class) {
			mouseDown((Integer) constructorArgs[1], (Integer) constructorArgs[2], (Integer) constructorArgs[3]);
		} else if (eventClass == MouseUpEvent.class) {
			mouseUp((Integer) constructorArgs[1], (Integer) constructorArgs[2], (Integer) constructorArgs[3]);
		} else if (eventClass == MouseWheelEvent.class) {
			mouseWheel((Integer) constructorArgs[1], (Integer) constructorArgs[2], (Integer) constructorArgs[3]);
		} else if (eventClass == WinchEvent.class) {
			winch((Integer) constructorArgs[1], (Integer) constructorArgs[2], (Integer) constructorArgs[3],
					(Integer) constructorArgs[4]);
		}
		
		// first of all, to save objects, we're going to check if this event is being listened for on this object.
		if (!isListeningForEvent(eventClass)) return null; // if not, just return and don't create any event objects. this is good for
															// high-frequency events.
			
		// now we'll create an instance... i hate how many lines all the exception garbage takes.
		Class<?>[] constructorTypes;
		try {
			constructorTypes = (Class<?>[]) eventClass.getField("SIGNATURE").get(null);
		} catch (final SecurityException e) {
			e.printStackTrace();
			if (System.getSecurityManager() == null) {
				GlassPaneMod.inst
						.getLog()
						.error("[GlassPane] [EventSystem] A SecurityException was thrown, but there's no SecurityManager registered...");
			} else
				throw new PaneCantContinueError("Security manager (" + System.getSecurityManager().getClass().getName()
						+ ") prevents proper operation of the GlassPane event system!", e);
			return null;
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			GlassPaneMod.inst.getLog().error(
					"[GlassPane] [EventSystem] Event class " + eventClass.getName()
							+ "'s SIGNATURE field is non-public!");
			return null;
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
			GlassPaneMod.inst.getLog().error(
					"[GlassPane] [EventSystem] Event class " + eventClass.getName()
							+ " does not declare static field SIGNATURE!");
			return null;
		}
		T event;
		try {
			event = eventClass.getConstructor(constructorTypes).newInstance(constructorArgs);
		} catch (final SecurityException e) {
			e.printStackTrace();
			if (System.getSecurityManager() == null) {
				GlassPaneMod.inst
						.getLog()
						.error("[GlassPane] [EventSystem] A SecurityException was thrown, but there's no SecurityManager registered...");
				return null;
			} else
				throw new PaneCantContinueError("Security manager (" + System.getSecurityManager().getClass().getName()
						+ ") prevents proper operation of the GlassPane event system!", e);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			final StringBuilder types = new StringBuilder();
			for (int i = 0; i < constructorTypes.length; i++) {
				types.append(constructorTypes[i].getName());
				if (i == constructorTypes.length - 2) {
					types.append(" and ");
				} else if (i < constructorTypes.length - 2) {
					types.append(", ");
				}
			}
			GlassPaneMod.inst.getLog().error(
					"[GlassPane] [EventSystem] No constructor for event class " + eventClass.getName()
							+ " matching the call spec of " + types + "!");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// now let's fire it
		for (final Entry<Object, List<Method>> en : listeners.get(eventClass).entrySet()) {
			for (final Method method : en.getValue()) {
				if (method.getAnnotation(PaneEventHandler.class).ignoreConsumed() && event.isConsumed()) {
					continue;
				}
				try {
					method.setAccessible(true);
					// you're fired
					method.invoke(en.getKey(), event);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
					GlassPaneMod.inst.getLog().error(
							"[GlassPane] [EventSystem] Cannot properly invoke method for event class "
									+ eventClass.getName() + " and listener class " + en.getKey().getClass().getName()
									+ "!");
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
					GlassPaneMod.inst.getLog().error(
							"[GlassPane] [EventSystem] No permission to invoke method for event class "
									+ eventClass.getName() + " and listener class " + en.getKey().getClass().getName()
									+ "!");
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
					GlassPaneMod.inst.getLog().error(
							"[GlassPane] [EventSystem] Invocation of method for event class " + eventClass.getName()
									+ " and listener class " + en.getKey().getClass().getName()
									+ " threw an exception!");
				} catch (final SecurityException e) {
					e.printStackTrace();
					if (System.getSecurityManager() == null) {
						GlassPaneMod.inst
								.getLog()
								.error("[GlassPane] [EventSystem] A SecurityException was thrown, but there's no SecurityManager registered...");
						return null;
					} else
						throw new PaneCantContinueError("Security manager ("
								+ System.getSecurityManager().getClass().getName()
								+ ") prevents proper operation of the GlassPane event system!", e);
				}
			}
		}
		// and finally return it
		return event;
	}
	
	@Override
	public void setHeight(final int height) {
		if (height != this.height) {
			fireEvent(WinchEvent.class, this, this.width, this.height, this.width, height);
		}
		super.setHeight(height);
	}
	
	@Override
	public void setWidth(final int width) {
		if (width != this.width) {
			fireEvent(WinchEvent.class, this, this.width, this.height, width, this.height);
		}
		super.setWidth(width);
	}
	
	/**
	 * Activates this component. Equivalent to clicking on the component or pressing Enter while it has the focus.
	 */
	public void activate() {
		fireEvent(ComponentActivateEvent.class, this);
	}
	
	@Getter(NONE) @Setter(NONE) private int hoverTime = 0;
	
	public final void tick() {
		fireEvent(ComponentTickEvent.class, this);
		if (this instanceof PaneContainer) {
			for (final PaneComponent c : ((PaneContainer) this).components) {
				c.tick();
			}
		}
		if (Display.isActive() && ((this instanceof PaneButton) ? ((PaneButton) this).isEnabled() : true)
				&& withinBounds(mouseX, mouseY)) {
			hoverTime++;
		} else {
			hoverTime = 0;
		}
		doTick();
	}
	
	/**
	 * Shortcut for components to be able to get certain events without needing to create objects.
	 */
	protected void mouseDown(final int mouseX, final int mouseY, final int button) {}
	
	/**
	 * Shortcut for components to be able to get certain events without needing to create objects.
	 */
	protected void mouseUp(final int mouseX, final int mouseY, final int button) {}
	
	/**
	 * Shortcut for components to be able to get certain events without needing to create objects.
	 */
	protected void mouseWheel(final int mouseX, final int mouseY, final int distance) {}
	
	/**
	 * Shortcut for components to be able to get certain events without needing to create objects.
	 */
	protected void keyPressed(final char keyChar, final int keyCode) {}
	
	/**
	 * Shortcut for components to be able to get certain events without needing to create objects.
	 */
	protected void winch(final int oldWidth, final int oldHeight, final int newWidth, final int newHeight) {}
	
	/**
	 * Called every tick. Good for doing animation.
	 */
	protected void doTick() {}
}
