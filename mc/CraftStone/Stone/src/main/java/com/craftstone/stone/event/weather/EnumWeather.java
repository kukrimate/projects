package com.craftstone.stone.event.weather;

public enum EnumWeather {
	CLEAR,
	DOWNFALLING;
	
	public static EnumWeather getFromBoolean(boolean flag) {
		if (flag) {
			return DOWNFALLING;
		} else {
			return CLEAR;
		}
	}
}
