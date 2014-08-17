package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import net.minecraft.client.Minecraft;


public class PaneTestTooltips
		extends GlassPane {
	public PaneTestTooltips() {
		setName("Tooltips");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestTooltips.java"), PaneTestHarness.createFlipButton());
		
		PaneLabel basic = new PaneLabel("Basic Tooltip");
		basic.setTooltip("Basic tooltip with\nnewlines");
		basic.setX(10);
		basic.setY(20);
		add(basic);
		
		PaneLabel colored = new PaneLabel("Colored Tooltip");
		colored.setTooltip("Tooltip with \u00A7echat colors \u00A7band\nnewlines");
		colored.setX(10);
		colored.setY(36);
		add(colored);
		
		PaneLabel sga = new PaneLabel("Tooltip with SGA font renderer");
		sga.setTooltip("Tooltip with the SGA font renderer");
		sga.setTooltipFontRenderer(Minecraft.getMinecraft().standardGalacticFontRenderer);
		sga.setX(10);
		sga.setY(52);
		add(sga);
	}
}
