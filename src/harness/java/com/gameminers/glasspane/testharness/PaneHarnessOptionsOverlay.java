package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.PaneImage;
import gminers.glasspane.component.text.PaneLabel;
import gminers.glasspane.ease.PaneEaser;
import gminers.glasspane.event.ComponentTickEvent;
import gminers.glasspane.event.MouseDownEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Hues;
import net.minecraft.util.ResourceLocation;


public class PaneHarnessOptionsOverlay
		extends GlassPane {
	
	public PaneHarnessOptionsOverlay() {
		final PaneImage enter = new PaneImage(new ResourceLocation("textures/blocks/glass.png"));
		enter.setWidth(16);
		enter.setHeight(16);
		enter.setAutoPosition(true);
		enter.setRelativeX(1.0);
		enter.setRelativeY(1.0);
		enter.setRelativeXOffset(-24);
		enter.setRelativeYOffset(-24);
		enter.registerListeners(new Object() {
			private int hue = 0;
			
			@PaneEventHandler
			public void onClick(MouseDownEvent e) {
				if (e.getMouseButton() == 0) {
					// We don't cache the object, since if we did, it would be annoying to make changes
					// to the test harness using hot code replace.
					new PaneTestHarness().show();
				}
			}
			
			@PaneEventHandler
			public void onTick(ComponentTickEvent e) {
				hue = (hue + 1) % 360;
				enter.setColor(Hues.hueToRGB(hue));
			}
		});
		add(enter);
		final PaneLabel label = new PaneLabel("Glass Pane Test Harness ");
		label.setAutoPosition(true);
		label.setRelativeX(1.0);
		label.setRelativeY(1.0);
		label.setHeight(16);
		label.setAlignmentY(VertAlignment.MIDDLE);
		label.setRelativeXOffset(-(24 + label.getWidth()));
		label.setRelativeYOffset(-24);
		label.setTranslateX(label.getWidth() + 4);
		label.setClipToSize(true);
		final PaneEaser labelEaser = new PaneEaser(label);
		label.registerListeners(new Object() {
			@PaneEventHandler
			public void onTick(ComponentTickEvent e) {
				if (enter.withinBounds(mouseX, mouseY)) {
					labelEaser.easeFloat("translateX", 0);
				} else {
					labelEaser.easeFloat("translateX", label.getWidth() + 4);
				}
			}
		});
		add(label);
	}
}
