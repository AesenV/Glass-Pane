Glass Pane
==========

Glass Pane is a versatile GUI framework for Minecraft Forge.

If you've used Swing, or Minecraft's default GuiScreen, you should feel right at home.

Example code:

```java
public class PaneHello extends GlassPane {
	public PaneHello() {
		PaneLabel hello = new PaneLabel("Hello, Glass Pane!");
		hello.setY(5);
		hello.setX(5);
		add(hello);
	}
}
```

All you would have to do to display that Glass Pane you just created:
```java
new PaneHello().show();
```


Overlays
====
One of the most powerful (and main) features of Glass Pane is overlays.
They're simple to use:
```java
new PaneHello().overlay();
```
That will create your Hello pane, and put it in front of the current screen, be it a Glass Pane or a plain vanilla GuiScreen. This means that the text 'Hello, Glass Pane!' will show up on top!

There are also sticky overlays. These are used by calling stickyOverlay() on a Glass Pane. A sticky overlay won't disappear when screens change, and has to be manually removed by using hide().

In addition, there are modal overlays. These are intended for use with yes/no dialogs; they will put the pane in front of the current screen *and* all of it's overlays, with a dark background.


There isn't a formal tutorial yet, and this readme only scratches the surface of what Glass Pane can do. If you want to play with Glass Pane, download the Pane Harness mod and play around with all of the default components and features!


Downloading
====
Glass Pane comes in two flavors; normal and dev. The dev build must be used when developing against Glass Pane in a deobfuscated enviornment, and the normal version is what you put in your mods folder.
You can get Glass Pane on the [releases page](http://github.com/AesenV/Glass-Pane/releases).

You will also see PaneHarness.jar - this is a mod that implements a test harness for every component and feature in Glass Pane so you can play around with it. Just drop it in your mods folder, and then click the glass pane in the Options menu to go to it.

Support (IRC)
====
You can catch Aesen in the #augment channel on [EsperNet](http://esper.net). [Click here to go to the webchat](http://webchat.esper.net/?nick=&channels=#augment).
