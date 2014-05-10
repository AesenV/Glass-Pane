package gminers.glasspane.event;


import gminers.glasspane.component.button.PaneToggleButton;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a {@link PaneToggleButton} changes state. For the new state, call PaneToggleButton.isSelected(). For the old state, invert
 * the return value of isSelected.
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString
public class ToggleButtonStateChangedEvent
		extends PaneEvent {
	/**
	 * The ToggleButton being toggled. The same as 'source', but already cast to PaneToggleButton for your convenience.
	 */
	PaneToggleButton	toggleButton;
	
	public ToggleButtonStateChangedEvent(final PaneToggleButton toggleButton) {
		super(toggleButton);
		this.toggleButton = toggleButton;
	}
}
