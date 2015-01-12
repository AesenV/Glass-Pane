package gminers.glasspane.component.numeric;


import gminers.glasspane.component.ColorablePaneComponent;
import gminers.glasspane.component.Focusable;
import gminers.glasspane.component.button.PaneButton;

import java.text.NumberFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
@Deprecated
/**
 * @deprecated Hard to use, doesn't work well.
 */
public class PaneSpinner
		extends ColorablePaneComponent
		implements Focusable {
	public static final NumberFormat DEFAULT_FORMAT = NumberFormat.getInstance();
	
	static {
		DEFAULT_FORMAT.setGroupingUsed(true);
	}
	
	float value = 0;
	float maximum = Float.POSITIVE_INFINITY;
	float minimum = Float.NEGATIVE_INFINITY;
	float increment = 1;
	NumberFormat format = (NumberFormat) DEFAULT_FORMAT.clone();
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) float lagValue = 0;
	FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
	
	public PaneSpinner() {
		setClipToSize(true);
	}
	
	public PaneSpinner(final float value) {
		this.value = value;
	}
	
	public PaneSpinner(final float value, final float minimum, final float maximum) {
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	@Override
	protected void doTick() {
		if (Float.isNaN(lagValue)) {
			lagValue = 0f;
		}
		if (lagValue < value) {
			lagValue += getAdjustment(value, lagValue);
		} else if (lagValue > value) {
			lagValue -= getAdjustment(lagValue, value);
		}
	}
	
	protected float getAdjustment(final float big, final float small) {
		final float diff = (big - small);
		return (float) (diff > 0.01 ? (diff * 0.45) : diff);
	}
	
	@Override
	protected void keyPressed(final char keyChar, final int keyCode) {
		int direction;
		switch (keyCode) {
			case Keyboard.KEY_UP: {
				direction = 1;
				break;
			}
			case Keyboard.KEY_DOWN: {
				direction = -1;
				break;
			}
			default:
				return;
		}
		int dist = 1;
		final boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		final boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		if (shift) {
			dist *= 10;
		}
		if (ctrl) {
			dist *= 10;
		}
		value = Math.max(minimum, Math.min(value + ((dist * direction) * increment), maximum));
	}
	
	@Override
	protected void mouseWheel(final int mouseX, final int mouseY, final int distance) {
		if (withinBounds(mouseX, mouseY)) {
			int dist = 1;
			final boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
			final boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
			if (shift) {
				dist *= 10;
			}
			if (ctrl) {
				dist *= 10;
			}
			value = Math.max(minimum, Math.min(value + ((dist * Math.signum(distance)) * increment), maximum));
		}
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		// bind the widgets
		Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE);
		
		// u and v, for convenient changing
		final int u = 0;
		final int v = 0;
		
		// set color
		GL11.glColor3f(0.6f, 0.6f, 0.6f);
		PaneButton.renderStretchyTexturedRect(0, 0, u, v, width, height, 220, 40);
		
		// if we're focused, draw a blue border over the normal black one
		GL11.glTranslatef(0, 0, 0.001f);
		if (getParent() != null) {
			if (getParent().getFocusedComponent() == this) {
				final int fv = 200;
				PaneButton.renderStretchyTexturedRect(0, 0, u, fv, width, height, 220, 40);
			}
		}
		
		final float dist = (float) (Math.abs(lagValue / increment) - Math.floor(Math.abs(lagValue / increment)));
		final float next = (int) Math.ceil(lagValue / increment) * increment;
		final float cur = (int) Math.floor(lagValue / increment) * increment;
		
		
		final String textCur = format.format(isClipToSize() ? cur : value);
		final String textNex = format.format(next);
		
		// if we're caught up, do a simple render
		// also use clipToSize as an 'animation' flag
		if (!isClipToSize() || next == cur) {
			renderer.drawStringWithShadow(textCur, (width - 3) - renderer.getStringWidth(textCur), 2, color);
		} else {
			GL11.glPushMatrix();
			if (next <= 0) {
				GL11.glTranslatef(0, (height - 2) * (1.0f - dist), 0);
			} else {
				GL11.glTranslatef(0, (height - 2) * dist, 0);
			}
			renderer.drawStringWithShadow(textNex, (width - 3) - renderer.getStringWidth(textNex), -8, color);
			renderer.drawStringWithShadow(textCur, (width - 3) - renderer.getStringWidth(textCur), 2, color);
			GL11.glPopMatrix();
		}
	}
}
