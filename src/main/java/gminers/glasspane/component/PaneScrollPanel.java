package gminers.glasspane.component;


import gminers.glasspane.PaneBB;
import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.event.MouseWheelEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Rendering;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


/**
 * A panel that can be scrolled by the user.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneScrollPanel
		extends PaneContainer {
	private float momentum = 0.0f;
	private float incomingMomentum = 0.0f;
	private float offset = 0.0f;
	/**
	 * Whether or not to draw a ShadowPanel-style shadow.
	 */
	@Getter @Setter private boolean shadowed = true;
	/**
	 * The depth of the shadow, if enabled.
	 */
	@Getter @Setter private int shadowDepth = 3;
	
	@SuppressWarnings("deprecation")
	public PaneScrollPanel() {
		// Workaround: Scroll Panel causes horrible white flickering when it's not clipped to size. I don't know why.
		setClipToSize(true);
	}
	
	@PaneEventHandler
	public void onKeyPress(final KeyTypedEvent e) {
		if (e.getKeyCode() == Keyboard.KEY_NEXT) {
			incomingMomentum -= height / 2f;
		} else if (e.getKeyCode() == Keyboard.KEY_PRIOR) {
			incomingMomentum += height / 2f;
		} else if (e.getKeyCode() == Keyboard.KEY_DOWN) {
			incomingMomentum -= 2;
		} else if (e.getKeyCode() == Keyboard.KEY_UP) {
			incomingMomentum += 2;
		} else if (e.getKeyCode() == Keyboard.KEY_HOME) {
			incomingMomentum = ((getMinimumChildY() - offset) / 2f) + (-momentum);
		} else if (e.getKeyCode() == Keyboard.KEY_END) {
			incomingMomentum = ((-(getMaximumChildEdgeY() - height) - offset) / 2f) + (-momentum);
		}
	}
	
	@PaneEventHandler
	public void onMouseWheel(final MouseWheelEvent e) {
		incomingMomentum += (e.getDistance() / 4f);
	}
	
	public int getMinimumChildY() {
		if (components.isEmpty()) return 0;
		int rtrn = Integer.MAX_VALUE;
		for (final PaneComponent pc : components) {
			rtrn = Math.min(rtrn, pc.getY());
		}
		return rtrn;
	}
	
	public int getMaximumChildEdgeY() {
		if (components.isEmpty()) return 0;
		int rtrn = Integer.MIN_VALUE;
		for (final PaneComponent pc : components) {
			rtrn = Math.max(rtrn, pc.getEdgeY());
		}
		return rtrn;
	}
	
	@Override
	protected void doTick() {
		if (getMaximumChildEdgeY() <= getHeight()) {
			momentum = 0;
			offset = 0;
		} else {
			float in = incomingMomentum / 2f;
			in = Math.min(Math.max(24, Math.abs(momentum)), Math.abs(in)) * Math.signum(in);
			incomingMomentum -= in;
			if (incomingMomentum == Float.NaN || Math.abs(incomingMomentum) < 0.001) {
				incomingMomentum = 0;
			}
			momentum += in;
			momentum = momentum / 1.5f;
			if (momentum == Float.NaN || Math.abs(momentum) < 0.001) {
				momentum = 0;
			}
			offset += momentum;
			if (offset > 0) {
				offset = momentum;
			} else if (offset < -((getMaximumChildEdgeY() + 8) - height)) {
				offset = -((getMaximumChildEdgeY() + 8) - height) + momentum;
			}
		}
	}
	
	private final PaneBB goat = new PaneBB();
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		final int col = 0x88000000;
		if (shadowed) {
			Rendering.drawRect(0, 0, width, height, col);
		}
		final float ofs = (float) (Math.floor(offset * 2f) / 2f);
		final int iOfs = (int) offset;
		final int pX = getPX();
		final int pY = getPY();
		GL11.glPushMatrix();
		GL11.glTranslatef(0, ofs, 0);
		for (final PaneComponent pc : components) {
			if (intersects(goat.mimic(pc).translate(x, iOfs + y))) {
				pc.render(mouseX - pX, (mouseY - pY), partialTicks);
			}
		}
		GL11.glPopMatrix();
		if (shadowed) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			Rendering.drawGradientRect(0, 0, width, shadowDepth, 0xFF000000, 0x00000000, 0);
			Rendering.drawGradientRect(0, height - shadowDepth, width, height, 0x00000000, 0xFF000000, 0);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		}
		GL11.glScissor(getAbsoluteX(getGlassPane().getWidth()), getAbsoluteY(getGlassPane().getHeight()),
				getAbsoluteWidth(getGlassPane().getWidth()), getAbsoluteHeight(getGlassPane().getHeight()));
		Rendering.drawRect(width - 4, 0, width, height, col);
		final int diff = getMaximumChildEdgeY() - getMinimumChildY();
		float percentage = -offset / diff;
		if (Float.isInfinite(percentage) || Float.isNaN(percentage)) {
			percentage = 0.0f;
		}
		final float viewportPercentage = Math.min(1.0f, diff > 0 ? (float) height / (float) diff : 1);
		final int barHeight = height - 2;
		int segHeight = (int) (barHeight * viewportPercentage);
		if (segHeight <= 0) {
			segHeight = 1;
		}
		final int segY = (int) (Math.floor((barHeight * percentage) * 2f) / 2f);
		Rendering.drawRect(width - 3, segY + 1, width - 1, segY + 1 + segHeight, 0x88FFFFFF);
	}
	
	@Override
	protected int getPY() {
		return (int) (super.getPY() + Math.floor(offset));
	}
	
	public boolean isChildVisible(final PaneComponent pc) {
		Validate.isTrue(components.contains(pc), "Attempt to call isChildVisible with a non-child");
		return intersects(goat.mimic(pc).translate(x, ((int) offset) + y));
	}
	
}
