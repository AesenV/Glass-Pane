package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PaneImage;
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
		enter.setTooltip("Show the Glass Pane Test Harness");
		enter.registerListeners(new Object() {
			private int	hue	= 0;
			
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
	}
}
