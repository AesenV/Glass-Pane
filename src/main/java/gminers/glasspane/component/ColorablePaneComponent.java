package gminers.glasspane.component;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


/**
 * Base classes for most components that can be colored.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class ColorablePaneComponent
		extends PaneComponent {
	/**
	 * The color of this component.
	 */
	int color = 0xFFFFFF;
	
	/**
	 * Inverts the red, green, and blue components of this color, but preserves the alpha.
	 */
	public void invertColor() {
		color = (~color & 0xFFFFFF) | (color & 0xFF000000);
	}
	
}
