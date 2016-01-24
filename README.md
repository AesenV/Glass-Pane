Glass Pane
==========

#### *WARNING*: Glass Pane is deprecated and no longer maintained. It's 1.8 port is a sloppy mess that screws up GL state regularly. *Do not use it.* Use [Laminate](https://github.com/unascribed/Laminate) instead.

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

If you're using ForgeGradle, or something else that supports Maven repositories, you can instead add the Maven repository `http://mvn.gameminers.com/artifactory/repo` and add `com.gameminers:glasspane:1.0:dev` as a dependency.
You may also need to add `com.gameminers:kitchensink:1.0:dev` if you wish to run the game from your development environment and/or use the Kitchen Sink utilities.

You will also see PaneHarness.jar - this is a mod that implements a test harness for every component and feature in Glass Pane so you can play around with it. Just drop it in your mods folder, and then click the glass pane in the Options menu to go to it.

Support (IRC)
====
You can catch Aesen in the #augment channel on [EsperNet](http://esper.net). [Click here to go to the webchat](http://webchat.esper.net/?nick=&channels=#augment).
