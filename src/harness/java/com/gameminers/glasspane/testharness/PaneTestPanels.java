package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PanePanel;
import gminers.glasspane.component.PaneShadowPanel;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;


public class PaneTestPanels
		extends GlassPane {
	public PaneTestPanels() {
		setName("Panels");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestPanels.java"), PaneTestHarness.createFlipButton());
		
		PanePanel panel = new PanePanel();
		panel.setBorderText("Basic");
		panel.setX(10);
		panel.setY(10);
		panel.setWidth(100);
		panel.setHeight(40);
		add(panel);
		
		PanePanel thick = new PanePanel();
		thick.setBorderText("Thick");
		thick.setX(114);
		thick.setY(10);
		thick.setBorderThickness(3);
		thick.setWidth(100);
		thick.setHeight(40);
		add(thick);
		
		PanePanel borderless = new PanePanel();
		borderless.setX(218);
		borderless.setY(10);
		borderless.setShowBorder(false);
		borderless.setWidth(100);
		borderless.setHeight(40);
		borderless.add(new PaneLabel("Borderless"));
		add(borderless);
		
		PanePanel colored = new PanePanel();
		colored.setX(322);
		colored.setY(10);
		colored.setBorderText("Colored");
		colored.setBorderColor(0xFFFF55);
		colored.setWidth(100);
		colored.setHeight(40);
		add(colored);
		
		PaneShadowPanel shadow = new PaneShadowPanel();
		shadow.setX(10);
		shadow.setY(54);
		shadow.setShowBorder(true);
		shadow.setBorderText("Shadow");
		shadow.setWidth(100);
		shadow.setHeight(40);
		add(shadow);
		
		PaneShadowPanel borderlessShadow = new PaneShadowPanel();
		borderlessShadow.setX(114);
		borderlessShadow.setY(54);
		borderlessShadow.setWidth(100);
		borderlessShadow.setHeight(40);
		borderlessShadow.add(new PaneLabel("Borderless Shadow"));
		add(borderlessShadow);
	}
}
