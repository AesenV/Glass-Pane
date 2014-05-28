package gminers.glasspane.ease;

import gminers.kitchensink.Strings;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.math.NumberUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.math.BigIntegerMath;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;

/**
 * Implements a class that can ease values in any object.<br/>
 * <br/>
 * "But Aesen, why not use Trident?"<br/>
 * Trident is too damn complicated, so I implemented my own, super easy to use, reflection-based easer built for use in Forge.<br/>
 * <br/>
 * Implements Closeable because it registers itself as a Forge EventBus listener.
 * @author Aesen Vismea
 *
 */
public class PaneEaser implements Closeable {
	private final Object toEase;
	private final Map<String, Byte> byteTargets = Maps.newHashMap();
	private final Map<String, Short> shortTargets = Maps.newHashMap();
	
	private final Map<String, Integer> integerTargets = Maps.newHashMap();
	private final Map<String, Long> longTargets = Maps.newHashMap();
	
	
	
	private final Map<String, Float> floatTargets = Maps.newHashMap();
	private final Map<String, Double> doubleTargets = Maps.newHashMap();
	
	
	private final Map<String, FieldAccessor> accessors = Maps.newHashMap();
	
	
	public PaneEaser(Object toEase) {
		this.toEase = toEase;
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onTick(TickEvent e) {
		if (e.type == Type.CLIENT && e.phase == Phase.START) {
			{
				Iterator<Entry<String, Byte>> iter = byteTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Byte> accessor = getAccessor(en.getKey());
					val current = accessor.get();
					val val = (byte)adjust(current, en.getValue());
					if (current.equals(val)) {
						iter.remove();
					}
					accessor.set(val);
				}
			}
			{
				Iterator<Entry<String, Short>> iter = shortTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Short> accessor = getAccessor(en.getKey());
					val current = accessor.get();
					val val = (short)adjust(current, en.getValue());
					if (current.equals(val)) {
						iter.remove();
					}
					accessor.set(val);
				}
			}
			{
				Iterator<Entry<String, Integer>> iter = integerTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Integer> accessor = getAccessor(en.getKey());
					val current = accessor.get();
					val val = (int)adjust(current, en.getValue());
					if (current.equals(val)) {
						iter.remove();
					}
					accessor.set(val);
				}
			}
			{
				Iterator<Entry<String, Long>> iter = longTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Long> accessor = getAccessor(en.getKey());
					val current = accessor.get();
					val val = (long)adjust(current, en.getValue());
					if (current.equals(val)) {
						iter.remove();
					}
					accessor.set(val);
				}
			}
			
			
			
			
			
			{
				Iterator<Entry<String, Float>> iter = floatTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Float> accessor = getAccessor(en.getKey());
					val current = accessor.get();
					val val = (float)adjust(current, en.getValue());
					if (current.equals(val)) {
						iter.remove();
					}
					accessor.set(val);
				}
			}
			{
				Iterator<Entry<String, Double>> iter = doubleTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Double> accessor = getAccessor(en.getKey());
					val current = accessor.get();
					val val = (double)adjust(current, en.getValue());
					if (current.equals(val)) {
						iter.remove();
					}
					accessor.set(val);
				}
			}
		}
	}

	private double adjust(double current, double target) {
		double adjustment = (target > current ? target-current : current-target);
		if (adjustment > 1 || adjustment < -1) {
			adjustment = Math.max(1, (adjustment/4D));
		}
		return current+adjustment;
	}

	private FieldAccessor getAccessor(String key) {
		if (!accessors.containsKey(key)) {
			accessors.put(key, new FriendlyFieldAccessor(toEase, toEase.getClass(), key));
		}
		return accessors.get(key);
	}

	public void setAccessor(String value, FieldAccessor<?> accessor) {
		accessors.put(value, accessor);
	}

	public void easeByte(String value, byte target) {
		byteTargets.put(value, target);
	}
	public void easeShort(String value, short target) {
		shortTargets.put(value, target);
	}
	public void easeInteger(String value, int target) {
		integerTargets.put(value, target);
	}
	public void easeLong(String value, long target) {
		longTargets.put(value, target);
	}
	
	public void easeFloat(String value, float target) {
		floatTargets.put(value, target);
	}
	public void easeDouble(String value, double target) {
		doubleTargets.put(value, target);
	}
	
	public void cancelEase(String value) {
		byteTargets.remove(value);
		shortTargets.remove(value);
		integerTargets.remove(value);
		longTargets.remove(value);
		
		floatTargets.remove(value);
		doubleTargets.remove(value);
	}
	
	@Override
	public void close() {
		FMLCommonHandler.instance().bus().unregister(this);
	}
}

