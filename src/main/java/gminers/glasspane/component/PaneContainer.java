package gminers.glasspane.component;


import gminers.glasspane.event.ComponentActivateEvent;
import gminers.glasspane.event.ComponentAddedEvent;
import gminers.glasspane.event.ComponentParentChangeEvent;
import gminers.glasspane.event.ComponentRemovedEvent;
import gminers.glasspane.event.FocusGainedEvent;
import gminers.glasspane.event.FocusLostEvent;
import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.event.MouseDownEvent;
import gminers.glasspane.event.MouseUpEvent;
import gminers.glasspane.event.MouseWheelEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;


/**
 * Base class for all components that can have components added to them.
 * 
 * @author Aesen Vismea
 * 
 */
public abstract class PaneContainer
		extends PaneComponent
		implements Focusable {
	protected List<PaneComponent> components = new CopyOnWriteArrayList<PaneComponent>();
	private boolean focusableComponentPresent = false;
	/**
	 * The component that currently has the focus.
	 */
	@Getter @Setter protected PaneComponent focusedComponent = null;
	/**
	 * Whether or not to allow using Tab and Shift+Tab to cycle the currently focused component.
	 */
	@Getter @Setter protected boolean cycleFocusOnTabPress = true;
	
	/**
	 * Adds multiple PaneComponents to this container, with their positions defined by the current PaneLayoutManager.<br/>
	 * <b>TODO</b>: Actually add layout managers. For now, all containers act as if they have an AbsoluteLayout and obey the X,Y coords of
	 * the component, relative to the container.
	 * 
	 * @param c
	 *            Components to add
	 */
	public void add(final PaneComponent... c) {
		for (final PaneComponent co : c) {
			add(co);
		}
	}
	
	/**
	 * Adds a PaneComponent to this container, with it's position defined by the current PaneLayoutManager.<br/>
	 * <b>TODO</b>: Actually add layout managers. For now, all containers act as if they have an AbsoluteLayout and obey the X,Y coords of
	 * the component, relative to the container.
	 * 
	 * @param c
	 *            Component to add
	 */
	public void add(final PaneComponent c) {
		fireEvent(ComponentAddedEvent.class, this, c);
		final PaneContainer oldParent = c.parent;
		c.parent = this;
		c.fireEvent(ComponentParentChangeEvent.class, c, oldParent);
		if (c instanceof Focusable) {
			focusableComponentPresent = true;
		}
		components.add(c);
	}
	
	/**
	 * Removes multiple PaneComponents from this container.
	 * 
	 * @param c
	 *            Component to remove
	 */
	public void remove(final PaneComponent... c) {
		for (final PaneComponent co : c) {
			remove(co);
		}
	}
	
	/**
	 * Removes a PaneComponent from this container.
	 * 
	 * @param c
	 *            Component to remove
	 */
	public void remove(final PaneComponent c) {
		fireEvent(ComponentRemovedEvent.class, this, c);
		if (components.contains(c)) {
			c.parent = null;
			c.fireEvent(ComponentParentChangeEvent.class, c, this);
			if (focusedComponent == c) {
				focusedComponent = null;
			}
			components.remove(c);
			focusableComponentPresent = false;
			for (final PaneComponent co : components) {
				if (co instanceof Focusable) {
					focusableComponentPresent = true;
					break;
				}
			}
		}
	}
	
	/**
	 * Removes all PaneComponents from this container.
	 */
	public void clear() {
		for (final PaneComponent c : components) {
			fireEvent(ComponentRemovedEvent.class, this, c);
			c.parent = null;
			c.fireEvent(ComponentParentChangeEvent.class, c, this);
		}
		focusableComponentPresent = false;
		focusedComponent = null;
		components.clear();
	}
	
	/**
	 * Gets a list of all PaneComponents in this container.
	 * 
	 * @return A list of components in this container.
	 */
	public List<PaneComponent> getComponents() {
		return Lists.newArrayList(components);
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		final int pX = getPX();
		final int pY = getPY();
		for (final PaneComponent pc : components) {
			pc.render(mouseX - pX, mouseY - pY, partialTicks);
		}
	}
	
	protected int getPY() {
		return getY();
	}
	
	protected int getPX() {
		return getX();
	}
	
	@Override
	protected void mouseDown(final int mouseX, final int mouseY, final int button) {
		if (!isVisible()) return;
		boolean clickedAFocusable = false;
		final int pX = getPX();
		final int pY = getPY();
		for (final PaneComponent c : components) {
			if (!c.isVisible()) {
				continue;
			}
			if (c.withinBounds(mouseX - pX, mouseY - pY)) {
				if (c instanceof Focusable && button == 0) {
					clickedAFocusable = true;
					if (c.isVisible() && focusedComponent != c) {
						final PaneComponent oldFocused = focusedComponent;
						focusedComponent = c;
						final FocusGainedEvent fge = focusedComponent.fireEvent(FocusGainedEvent.class,
								focusedComponent);
						if (fge != null) {
							if (fge.isConsumed()) {
								focusedComponent = oldFocused;
								continue;
							}
						}
						if (oldFocused != null) {
							oldFocused.fireEvent(FocusLostEvent.class, oldFocused);
						}
					}
					if (c.isActivatedOnClick()) {
						c.fireEvent(ComponentActivateEvent.class, c);
					}
				}
				c.fireEvent(MouseDownEvent.class, c, mouseX - pX, mouseY - pY, button);
			}
		}
		if (!clickedAFocusable) {
			focusedComponent = null;
			if (parent != null) {
				parent.focusedComponent = null;
			}
		} else if (parent != null) {
			parent.focusedComponent = this;
		}
	}
	
	@Override
	protected void mouseUp(final int mouseX, final int mouseY, final int button) {
		if (!isVisible()) return;
		final int pX = getPX();
		final int pY = getPY();
		for (final PaneComponent c : components) {
			if (!c.isVisible()) {
				continue;
			}
			if (c.withinBounds(mouseX - pX, mouseY - pY)) {
				c.fireEvent(MouseUpEvent.class, c, mouseX - pX, mouseY - pY, button);
			}
		}
	}
	
	@Override
	protected void mouseWheel(final int mouseX, final int mouseY, final int distance) {
		if (!isVisible()) return;
		final int pX = getPX();
		final int pY = getPY();
		for (final PaneComponent c : components) {
			if (!c.isVisible()) {
				continue;
			}
			if (c.withinBounds(mouseX - pX, mouseY - pY)) {
				c.fireEvent(MouseWheelEvent.class, c, mouseX - pX, mouseY - pY, distance);
			}
		}
	}
	
	@Override
	protected void keyPressed(final char keyChar, final int keyCode) {
		if (keyCode == Keyboard.KEY_TAB && cycleFocusOnTabPress) {
			if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				focusPrev(-1);
			} else {
				focusNext(-1);
			}
		} else if (keyCode == Keyboard.KEY_RETURN) {
			if (focusedComponent != null) {
				focusedComponent.fireEvent(ComponentActivateEvent.class, focusedComponent);
			}
		}
		final int pX = getPX();
		final int pY = getPY();
		for (final PaneComponent c : components) {
			if (!c.isVisible()) {
				continue;
			}
			if (c == focusedComponent || c.withinBounds(mouseX - pX, mouseY - pY)) {
				c.fireEvent(KeyTypedEvent.class, c, keyChar, keyCode);
			}
		}
	}
	
	/**
	 * Gives the focus to the previous component in this container that can be focused.
	 */
	public void focusPrev(final int index) {
		// make sure we actually have a component to find
		if (components.isEmpty() || !focusableComponentPresent) return;
		// store the currently focused component
		final PaneComponent oldFocused = focusedComponent;
		// get the index of the currently focused component, or the passed index
		int idx = index == -1 ? components.indexOf(focusedComponent) : index;
		boolean outOfBounds = false;
		do {
			// if we're out of bounds, wrap around
			if (idx - 1 < 0) {
				idx = components.size();
				if (outOfBounds) {
					break; // if we've already gone out of bounds once, break out of the loop
				}
				outOfBounds = true;
			}
			// set the focused component
			focusedComponent = components.get(idx - 1);
			// decrement the index
			idx--;
		} while (!(focusedComponent instanceof Focusable));
		// did we change?
		if (oldFocused != focusedComponent) {
			// fire a gain event to the new component
			final FocusGainedEvent fge = focusedComponent.fireEvent(FocusGainedEvent.class, focusedComponent);
			// if it consumed it, continue searching for a component
			if (fge != null) {
				if (fge.isConsumed() || !focusedComponent.isVisible()) {
					focusedComponent = oldFocused;
					focusPrev(idx);
					return;
				}
			}
			// and send a focus lost event to the old component
			if (oldFocused != null) {
				oldFocused.fireEvent(FocusLostEvent.class, oldFocused);
			}
		}
	}
	
	/**
	 * Gives the focus to the next component in this container that can be focused.
	 */
	public void focusNext(final int index) {
		// make sure we actually have a component to find
		if (components.isEmpty() || !focusableComponentPresent) return;
		// store the currently focused component
		final PaneComponent oldFocused = focusedComponent;
		// get the index of the currently focused component, or the passed index
		int idx = index == -1 ? components.indexOf(focusedComponent) : index;
		boolean outOfBounds = false;
		do {
			// if we're out of bounds, wrap around
			if (idx + 1 >= components.size()) {
				idx = -1;
				if (outOfBounds) {
					break; // if we've already gone out of bounds once, break out of the loop
				}
				outOfBounds = true;
			}
			// set the focused component
			focusedComponent = components.get(idx + 1);
			// increment the index
			idx++;
		} while (!(focusedComponent instanceof Focusable));
		// did we change?
		if (oldFocused != focusedComponent) {
			// fire a gain event to the new component
			final FocusGainedEvent fge = focusedComponent.fireEvent(FocusGainedEvent.class, focusedComponent);
			// if it consumed it, continue searching for a component
			if (fge != null) {
				if (fge.isConsumed() || !focusedComponent.isVisible()) {
					focusedComponent = oldFocused;
					focusNext(idx);
					return;
				}
			}
			// and send a focus lost event to the old component
			if (oldFocused != null) {
				oldFocused.fireEvent(FocusLostEvent.class, oldFocused);
			}
		}
	}
	
	@Override
	protected void winch(final int oldWidth, final int oldHeight, final int newWidth, final int newHeight) {
		for (final PaneComponent c : components) {
			if (c.isAutoResizeWidth()) {
				c.setWidth((int) (newWidth * c.getRelativeWidth()) + c.getRelativeWidthOffset());
			}
			if (c.isAutoResizeHeight()) {
				c.setHeight((int) (newHeight * c.getRelativeHeight()) + c.getRelativeHeightOffset());
			}
			if (c.isAutoPositionX()) {
				c.setX((int) (newWidth * c.getRelativeX()) + c.getRelativeXOffset());
			}
			if (c.isAutoPositionY()) {
				c.setY((int) (newHeight * c.getRelativeY()) + c.getRelativeYOffset());
			}
		}
	}
	
}
