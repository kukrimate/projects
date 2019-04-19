package com.craftstone;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import com.google.common.collect.Lists;

public class Util {
	public static List<Method> filterMethods(Class<? extends Annotation> ann, List<Method> methods) {
		//System.out.println(ann.getName());
		List<Method> newList = Lists.newArrayList();
		for (Method m : methods) {
			if (m.isAnnotationPresent(ann)) {
				newList.add(m);
			}
		}
		return newList;
	}
	
	public static List<Method> filterMethodsForParam(Class<?> paramType, List<Method> methods) {
		List<Method> newList = Lists.newArrayList();
		for (Method m : methods) {
			if (m.getParameterTypes()[0].equals(paramType)) {
				newList.add(m);
			}
		}
		return newList;
	}
	
	public static List<String> getMethodNames(List<Method> methods) {
		List<String> newList = Lists.newArrayList();
		for (Method m : methods) {
			newList.add(m.getName());
		}
		return newList;
	}
	
	public static void invokeMethod(Method method, Object instanceToInvokeOn, Object param) {
		method.setAccessible(true);
		try {
			method.invoke(instanceToInvokeOn, param);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		method.setAccessible(false);
	}
	
	public static WorldServer getWorldFromName(String name) {
		MinecraftServer mc = MinecraftServer.getServer();
		for (WorldServer w : mc.worldServers) {
			if (w.getWorldInfo().getWorldName().equalsIgnoreCase(name)) {
				return w;
			} else continue;
		}
		return null;
	}
}	
