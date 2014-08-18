package gminers.glasspane.component.button;


import java.util.List;

import lombok.Getter;

import com.google.common.collect.Lists;


/**
 * Used to group together radio buttons to make them mutually exclusive.
 * 
 * @author Aesen Vismea
 * 
 */
public class RadioButtonGroup {
	protected List<PaneRadioButton> buttons = Lists.newCopyOnWriteArrayList();
	/**
	 * The RadioButton that is currently selected in this group.
	 */
	@Getter protected PaneRadioButton selected = null;
	
	/**
	 * Removes a button from this group.
	 * 
	 * @param button
	 *            The button to remove.
	 */
	public void remove(final PaneRadioButton button) {
		if (buttons.contains(button)) {
			if (button.group == this) {
				button.group = null;
			}
			if (selected == button) {
				selected = null;
			}
			buttons.remove(button);
		}
	}
	
	/**
	 * Adds a button to this group.
	 * 
	 * @param button
	 *            The button to add.
	 */
	public void add(final PaneRadioButton button) {
		if (button.group != null && button.group != this) {
			button.group.remove(button);
		}
		if (button.selected) {
			if (selected == null) {
				selected = button;
			} else {
				button.selected = false;
			}
		}
		button.group = this;
		buttons.add(button);
	}
	
	/**
	 * Deselects the currently selected radio button.
	 */
	public void deselect() {
		selected = null;
	}
	
	/**
	 * Selects the passed radio button, if it's part of this group.
	 * 
	 * @param button
	 *            The button to select
	 */
	public void select(final PaneRadioButton button) {
		if (buttons.contains(button)) {
			selected = button;
		}
	}
	
	public int size() {
		return buttons.size();
	}
	
	public boolean isEmpty() {
		return buttons.isEmpty();
	}
	
	public boolean contains(final PaneRadioButton o) {
		return buttons.contains(o);
	}
	
	public void clear() {
		for (final PaneRadioButton b : buttons) {
			remove(b);
		}
	}
	
	public int indexOf(final PaneRadioButton o) {
		return buttons.indexOf(o);
	}
	
	public int lastIndexOf(final PaneRadioButton o) {
		return buttons.lastIndexOf(o);
	}
}
