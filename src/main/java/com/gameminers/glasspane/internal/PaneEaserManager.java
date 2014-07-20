package com.gameminers.glasspane.internal;

import gminers.glasspane.ease.PaneEaser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;


public class PaneEaserManager {
	public static Map<Object, PaneEaser> easers = Collections.synchronizedMap(new HashMap<Object, PaneEaser>());
	PaneEaserManager() {}
	@SubscribeEvent
	public void onTick(TickEvent e) {
		if (e.type != Type.CLIENT) return;
		Minecraft.getMinecraft().mcProfiler.startSection("paneEaser");
		for (PaneEaser pe : easers.values().toArray(new PaneEaser[easers.size()])) {
			Minecraft.getMinecraft().mcProfiler.startSection(Integer.toHexString(pe.hashCode()));
			try {
				pe.onTick(e.phase);
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println("Exception while ticking easer "+Integer.toHexString(pe.hashCode()));
			}
			Minecraft.getMinecraft().mcProfiler.endSection();
		}
		Minecraft.getMinecraft().mcProfiler.endSection();
	}
}
