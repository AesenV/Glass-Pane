package gminers.glasspane.component.button;


import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.event.ComponentActivateEvent;
import gminers.glasspane.event.StateChangedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Implements a clickable button that toggles states between selected and deselected.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
public class PaneToggleButton
		extends PaneButton {
	/**
	 * Whether or not this button is selected.
	 */
	boolean selected = false;
	
	public PaneToggleButton() {
		this("Toggle Button");
	}
	
	public PaneToggleButton(final String text) {
		this.text = text;
		alignmentX = HorzAlignment.MIDDLE;
		alignmentY = VertAlignment.MIDDLE;
		width = 200;
		height = 20;
	}
	
	public void setSelected(final boolean selected) {
		final boolean oldSelected = this.selected;
		this.selected = selected;
		if (oldSelected != this.selected) {
			fireEvent(StateChangedEvent.class, this);
		}
	}
	
	@PaneEventHandler
	public void onActivateForToggle(final ComponentActivateEvent e) {
		if (enabled) {
			selected = !selected;
			fireEvent(StateChangedEvent.class, this);
		}
	}
}
