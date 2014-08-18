package gminers.glasspane.component.button;


import gminers.glasspane.HorzAlignment;
import gminers.glasspane.event.ComponentActivateEvent;
import gminers.glasspane.listener.PaneEventHandler;
import lombok.Getter;


/**
 * Implements a radio button, a mutually-exclusive version of a checkbox. To actually work as mutually exclusive, all radio buttons in the
 * same group need to be added to the same RadioButtonGroup object. A radio button with no group acts as a checkbox with a different
 * appearance.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneRadioButton
		extends PaneCheckBox {
	@Getter protected RadioButtonGroup group = null;
	
	public PaneRadioButton() {
		this("Radio Button");
	}
	
	public PaneRadioButton(final RadioButtonGroup group) {
		this("Radio Button", group);
	}
	
	public PaneRadioButton(final String text) {
		this(text, false);
	}
	
	public PaneRadioButton(final String text, final RadioButtonGroup group) {
		this(text, false, group);
	}
	
	public PaneRadioButton(final String text, final boolean selected) {
		this(text, selected, null);
	}
	
	public PaneRadioButton(final String text, final boolean selected, final RadioButtonGroup group) {
		alignmentX = HorzAlignment.LEFT;
		this.text = text;
		this.group = group;
		this.selected = selected;
		if (group != null) {
			group.add(this);
		}
		u = 230;
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		if (group != null) {
			selected = (group.getSelected() == this);
		}
		super.doRender(mouseX, mouseY, partialTicks);
	}
	
	@Override
	@PaneEventHandler
	public void onActivateForToggle(final ComponentActivateEvent e) {
		if (group == null) {
			super.onActivateForToggle(e);
		} else {
			group.select(this);
		}
	}
}
