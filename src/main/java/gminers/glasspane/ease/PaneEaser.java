package gminers.glasspane.ease;

import gminers.kitchensink.Strings;

import java.awt.Color;
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
import lombok.NonNull;
import lombok.Setter;
import lombok.val;

import com.gameminers.glasspane.internal.PaneEaserManager;
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
 * Implements Closeable because it adds itself to an external map for ticking.
 * @author Aesen Vismea
 *
 */
public class PaneEaser implements Closeable {
	protected final Object toEase;
	protected Map<String, Byte> byteTargets = Maps.newHashMap();
	protected Map<String, Short> shortTargets = Maps.newHashMap();
	protected Map<String, Integer> integerTargets = Maps.newHashMap();
	protected Map<String, Long> longTargets = Maps.newHashMap();
	
	
	protected Map<String, Integer> colorTargets = Maps.newHashMap();
	
	
	protected Map<String, Float> floatTargets = Maps.newHashMap();
	protected Map<String, Double> doubleTargets = Maps.newHashMap();
	
	
	protected Map<String, FieldAccessor> accessors = Maps.newHashMap();
	
	@Getter
	@Setter
	protected double speed = 4D;
	@Getter
	@Setter
	protected boolean autoClose;
	
	protected boolean closed = false;
	
	protected List<Runnable> closeListeners = Lists.newArrayList();
	
	
	public PaneEaser(@NonNull Object toEase) {
		this.toEase = toEase;
		PaneEaserManager.easers.put(toEase, this);;
	}
	
	private <T extends Number> void ease(Iterator<Entry<String, T>> iter, Class<?> primitive) {
		while (iter.hasNext()) {
			Entry<String, T> en = iter.next();
			FieldAccessor<T> accessor = getAccessor(en.getKey(), primitive);
			T current = accessor.get();
			T val = (T) numerfy(adjust(current.doubleValue(), en.getValue().doubleValue()), current.getClass());
			if (current.equals(val)) {
				iter.remove();
			}
			accessor.set(val);
		}
	}
	
	private Number numerfy(double val, Class<? extends Number> clazz) {
		if (clazz == Byte.class) {
			return Byte.valueOf((byte)val);
		} else if (clazz == Short.class) {
			return Short.valueOf((short)val);
		} else if (clazz == Integer.class) {
			return Integer.valueOf((int)val);
		} else if (clazz == Long.class) {
			return Long.valueOf((long)val);
		} else if (clazz == Float.class) {
			return Float.valueOf((float)val);
		} else if (clazz == Double.class) {
			return Double.valueOf(val);
		}
		return null;
	}

	public void onTick(Phase phase) {
		if (closed) return;
		if (phase == Phase.START) {
			ease(byteTargets.entrySet().iterator(), byte.class);
			ease(shortTargets.entrySet().iterator(), short.class);
			ease(integerTargets.entrySet().iterator(), int.class);
			ease(longTargets.entrySet().iterator(), long.class);
			ease(floatTargets.entrySet().iterator(), float.class);
			ease(doubleTargets.entrySet().iterator(), double.class);
			{
				Iterator<Entry<String, Integer>> iter = colorTargets.entrySet().iterator();
				while (iter.hasNext()) {
					val en = iter.next();
					FieldAccessor<Integer> accessor = getAccessor(en.getKey(), int.class);
					val current = accessor.get();
					if (current.equals(en.getValue())) {
						iter.remove();
					} else {
						Color targetCol = new Color(en.getValue(), true);
						Color col = new Color(current, true);
						float r = (float) Math.min(255, adjust(col.getRed(), targetCol.getRed()));
						float g = (float) Math.min(255, adjust(col.getGreen(), targetCol.getGreen()));
						float b = (float) Math.min(255, adjust(col.getBlue(), targetCol.getBlue()));
						float a = (float) Math.min(255, adjust(col.getAlpha(), targetCol.getAlpha()));
						accessor.set(new Color(r/255f, g/255f, b/255f, a/255f).getRGB());
					}
				}
			}
		} else {
			if (autoClose &&
					byteTargets.isEmpty() &&
					shortTargets.isEmpty() &&
					integerTargets.isEmpty() &&
					longTargets.isEmpty() &&
					floatTargets.isEmpty() &&
					doubleTargets.isEmpty() &&
					colorTargets.isEmpty()) {
				close();
			}
		}
	}

	protected double adjust(double current, double target) {
		double adjustment = target-current;
		if (adjustment > 0.05) {
			adjustment = Math.max(0.05, adjustment/speed);
		} else if (adjustment < -0.05) {
			adjustment = Math.min(-0.05, adjustment/speed);
		}
		return current+adjustment;
	}

	protected FieldAccessor getAccessor(String key, Class<?> setterClass) {
		if (!accessors.containsKey(key)) {
			accessors.put(key, new FriendlyFieldAccessor(toEase, toEase.getClass(), setterClass, key));
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
	
	public void easeColorInt(String value, int target) {
		colorTargets.put(value, target);
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
		colorTargets.remove(value);
	}
	
	public void registerCloseListener(Runnable r) {
		closeListeners.add(r);
	}
	
	public void unregisterCloseListener(Runnable r) {
		closeListeners.remove(r);
	}
	
	@Override
	public void close() {
		if (closed) return;
		PaneEaserManager.easers.remove(toEase);
		for (Runnable r : closeListeners) {
			r.run();
		}
		closeListeners = null;
		byteTargets = null;
		shortTargets = null;
		integerTargets = null;
		longTargets = null;
		
		floatTargets = null;
		doubleTargets = null;
		closed = true;
	}
}

