package com.craftstone.buildtool;

//Constants needed for the buildscript
public class Constants {
	public static final String TOOL_VERSION = "1.0";
	public static final String STONE_URL = "https://github.com/CraftStone/Stone/archive/master.zip";
	public static final String MCP_URL = "https://dl.dropboxusercontent.com/s/2r564hikh3hzrfd/mcp910-pre1.zip"; //Removed mcp url add it yourself
	private static final String SERVER_VERSION = "1.8";
	private static final String SERVER_URL = "https://s3.amazonaws.com/Minecraft.Download/versions/@/minecraft_server.@.jar";
	public static final String GRADLE_PROJECT_NAME = "CraftStone";

	public static String getServerUrl() {
		return SERVER_URL.replaceAll("@", SERVER_VERSION);
	}
}