package gminers.glasspane.component.numeric;


import gminers.glasspane.Direction;
import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.Focusable;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import gminers.glasspane.event.StateChangedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


/**
 * A PaneSlider implements a Slider, which has a knob that can be moved left&right/up&down to increase/decrease a value.
 * 
 * @author aesen
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PaneSlider
		extends PaneLabel
		implements Focusable {
	public PaneSlider() {
		setText("Slider");
		setWidth(427);
		setHeight(20);
		setAlignmentX(HorzAlignment.MIDDLE);
		setAlignmentY(VertAlignment.MIDDLE);
	}
	
	/**
	 * Whether or not this slider should render as enabled, and should be allowed to be moved and have the focus.
	 */
	boolean enabled = true;
	/**
	 * The color of this slider's knob. If set to 0xFFFFFF, renders the same as a default Minecraft slider.
	 */
	int knobColor = 0xFFFFFF;
	/**
	 * The color of the text of this slider when it is disabled.
	 */
	int disabledColor = 0xA0A0A0;
	/**
	 * The color of the text of this slider when hovering.
	 */
	int hoveredColor = 0xFFFFA0;
	/**
	 * The current value of this slider.
	 */
	int value = 0;
	/**
	 * The maximum value of this slider.
	 */
	int maximum = 16;
	/**
	 * Whether or not to render a solid background on this slider. If set to false, this can be used to make a progress slider
	 * by putting a slider in front of a progress bar.
	 */
	boolean renderBackground = true;
	/**
	 * The direction of this slider.
	 */
	Direction direction = Direction.HORIZONTAL;
	/**
	 * The length to use for the knob. If this slider is vertical, this is height. If it is horizontal, it is width.
	 */
	int knobLength = 8;
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private float stretch = 0f;
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private float stretchTarget = 0f;
	
	@Override
	protected void doRender(int mouseX, int mouseY, float partialTicks) {
		// determine the u and v offsets we want
		final int u = 0;
		int v = 40;
		final boolean hover = Mouse.isInsideWindow() && withinBounds(mouseX, mouseY);
		// bind the widgets file
		Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE);
		boolean horz = (direction == Direction.HORIZONTAL);
		if (renderBackground) {
			GL11.glPushMatrix();
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			boolean focus = getParent() != null && getParent().getFocusedComponent() == this;
			if (horz) {
				PaneButton.renderStretchyTexturedRect(0, 0, u, 0, width, height, 220, 40);
				if (focus) {
					PaneButton.renderStretchyTexturedRect(0, 0, u, 200, width, height, 220, 40);
				}
			} else {
				GL11.glTranslatef(width / 2f, height / 2f, 0);
				GL11.glRotatef(90f, 0, 0, 1.0f);
				PaneButton.renderStretchyTexturedRect(-(height / 2), -(width / 2), u, 0, height, width, 220, 40);
				if (focus) {
					PaneButton.renderStretchyTexturedRect(-(height / 2), -(width / 2), u, 200, height, width, 220, 40);
				}
			}
			GL11.glPopMatrix();
		}
		
		// unpack the knob color
		int r = knobColor >> 16 & 255;
		int g = knobColor >> 8 & 255;
		final int b = knobColor & 255;
		
		float mult = (float) value / (float) maximum;
		int knobX = 0;
		int knobY = 0;
		float transX = 0;
		float transY = 0;
		int knobWidth = 0;
		int knobHeight = 0;
		int length = horz ? width : height;
		int knob = (int) (mult * (length - knobLength));
		if (knob > knobLength && knob < (length - knobLength)) {
			knob = knob + (knobLength / 2);
		}
		float trans = stretch * ((length / maximum) / 2f);
		if (horz) {
			knobX = knob;
			transX = trans;
			knobWidth = knobLength;
			knobHeight = height;
		} else {
			knobY = knob;
			transY = trans;
			knobWidth = width;
			knobHeight = knobLength;
		}
		GL11.glPushMatrix();
		GL11.glTranslatef(transX, transY, 0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(r, g, b, 0.15f);
		PaneButton.renderStretchyTexturedRect(knobX, knobY, 0, v, knobWidth, knobHeight, 220, 40);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		
		// apply the knob color
		GL11.glColor3f(r / 255f, g / 255f, b / 255f);
		PaneButton.renderStretchyTexturedRect(knobX, knobY, 0, v, knobWidth, knobHeight, 220, 40);
		
		// change the label's color, if needed
		final int trueColor = color;
		if (!enabled) {
			color = disabledColor;
		} else if (hover) {
			color = hoveredColor;
		}
		// render the label
		GL11.glTranslatef(0, 0, 0.001f);
		super.doRender(mouseX, mouseY, partialTicks);
		color = trueColor;
	}
	
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private boolean down = false;
	
	@Override
	protected void doTick() {
		if (down) {
			if (!enabled || !Mouse.isButtonDown(0)) {
				down = false;
				return;
			}
			if (enabled) {
				int mouseOfs = (direction == Direction.HORIZONTAL) ? mouseX - getX() : mouseY - getY();
				int length = (direction == Direction.HORIZONTAL) ? width : height;
				float knobMult = (float) (value) / (float) maximum;
				int knobPos = (int) ((knobMult * (length - knobLength)) + (knobLength / 2));
				int ofs = mouseOfs - knobPos;
				float mult = ((float) mouseOfs / (float) length);
				float work = Math.max(0, Math.min((float) mult * (float) maximum, maximum));
				int oldValue = value;
				if (work < value) {
					value = (int) Math.ceil(work);
				} else if (work > value) {
					value = (int) Math.floor(work);
				}
				if (oldValue != value) {
					fireEvent(StateChangedEvent.class, this);
				}
				stretchTarget = ofs / ((float) length / (float) maximum);
				if (value >= maximum && stretchTarget > 0) {
					stretchTarget = 0;
				}
				if (value <= 0 && stretchTarget < 0) {
					stretchTarget = 0;
				}
			}
			stretch = stretch + ((stretchTarget - stretch) / 4f);
		} else {
			stretch /= 2;
		}
	}
	
	@Override
	protected void mouseWheel(int mouseX, int mouseY, int distance) {
		super.mouseWheel(mouseX, mouseY, distance);
		if (distance > 0 && value < maximum) {
			value++;
			fireEvent(StateChangedEvent.class, this);
		} else if (distance < 0 && value > 0) {
			value--;
			fireEvent(StateChangedEvent.class, this);
		}
	}
	
	@Override
	protected void mouseDown(int mouseX, int mouseY, int button) {
		super.mouseDown(mouseX, mouseY, button);
		if (button == 0) {
			down = true;
		}
	}
}
