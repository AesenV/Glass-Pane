package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import net.minecraft.client.Minecraft;


public class PaneTestLabels
		extends GlassPane {
	public PaneTestLabels() {
		setName("Labels");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestLabels.java"), PaneTestHarness.createFlipButton());
		
		PaneLabel basic = new PaneLabel("Basic Label");
		basic.setX(10);
		basic.setY(10);
		add(basic);
		
		PaneLabel colored = new PaneLabel("Colored Label");
		colored.setColor(0xFF0000);
		colored.setX(10);
		colored.setY(22);
		add(colored);
		
		PaneLabel chat = new PaneLabel("\u00A76Colored \u00A7bLabel \u00A7c(Using \u00A7aChat \u00A7eColors)");
		chat.setX(10);
		chat.setY(22);
		add(chat);
		
		PaneLabel multiline = new PaneLabel("Multi-Li\nne Label");
		multiline.setX(10);
		multiline.setY(34);
		add(multiline);
		
		PaneLabel outlined = new PaneLabel("Outlined Label");
		outlined.setX(10);
		outlined.setY(58);
		outlined.setOutlined(true);
		outlined.setShadow(false);
		outlined.setColor(0xFFFF55);
		add(outlined);
		
		PaneLabel invertedOutline = new PaneLabel("Outlined Label (Inverse)");
		invertedOutline.setX(10);
		invertedOutline.setY(70);
		invertedOutline.setOutlined(true);
		invertedOutline.setInvertedOutline(true);
		invertedOutline.setShadow(false);
		invertedOutline.setColor(0xFFFF55);
		add(invertedOutline);
		
		PaneLabel sga = new PaneLabel("SGA Label (like the enchantment table)");
		sga.setX(10);
		sga.setY(82);
		sga.setRenderer(Minecraft.getMinecraft().standardGalacticFontRenderer);
		add(sga);
		
		PaneLabel small = new PaneLabel("Small Text Label");
		small.setX(10);
		small.setY(94);
		small.setSmall(true);
		add(small);
	}
}
