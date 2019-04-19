package com.craftstone;

import java.lang.reflect.Method;
import java.util.List;

import com.craftstone.stone.event.Listener;
import com.google.common.collect.Lists;

public class ArrayTools {
	public static List<String> arrayToList(String[] array) {
		List<String> list = Lists.newArrayList();
		for (String str : array) {
			list.add(str);
		}
		return list;
	}
	
	public static List<Listener> arrayToList(Listener[] array) {
		List<Listener> list = Lists.newArrayList();
		for (Listener str : array) {
			list.add(str);
		}
		return list;
	}
	
	public static List<Method> arrayToList(Method[] array) {
		List<Method> list = Lists.newArrayList();
		for (Method str : array) {
			list.add(str);
		}
		return list;
	}
}
