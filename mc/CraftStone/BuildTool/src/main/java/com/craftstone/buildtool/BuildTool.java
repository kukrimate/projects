package com.craftstone.buildtool;

import java.io.File;

public class BuildTool {
	private File workingDirectory;
	private File temp;
	private ToolManager tools;

	public BuildTool(String[] in) {
		workingDirectory = new File("").getAbsoluteFile();
		System.out.println("Working directory: " + workingDirectory);

		tools = new ToolManager(workingDirectory);

		makeDirectories();
		getStone();
		getMcp();
		downloadServer();
		tools.callSpecialSource(temp.toString() + File.separator + "mc.jar", workingDirectory + File.separator + "mappings" + File.separator, temp.toString() + File.separator + "mc_remapped.jar");
		tools.callMcInjector(temp.toString() + File.separator + "mc_remapped.jar", temp.toString() + File.separator + "mc_exp.jar", workingDirectory.toString() + File.separator + "mappings" + File.separator + "joined.srg", workingDirectory.toString() + File.separator + "mappings" + File.separator + "exceptor.json");
		repackServer(temp.toString() + File.separator + "mc_exp.jar", temp.toString() + File.separator + "mc_ff_in.jar");
		tools.callFernflower(temp.toString() + File.separator + "mc_ff_in.jar", temp.toString() + File.separator + "ff_out" + File.separator);
		
		tools.decompressFile(temp.toString() + File.separator + "ff_out" + File.separator + "mc_ff_in.jar", temp.toString() + File.separator + "server" + File.separator);
		
		if (Util.isWindows()) { //Windows
			Util.dirToCrLf(tools, workingDirectory.toString() + File.separator + "patches" + File.separator);
			Util.dirToCrLf(tools, temp.toString() + File.separator + "server" + File.separator);
		} else { //Linux or OS X
			Util.dirToLf(tools, workingDirectory.toString() + File.separator + "patches" + File.separator);
			Util.dirToLf(tools, temp.toString() + File.separator + "server" + File.separator);
		}
		
		//tools.callPatch(temp.toString() + File.separator + "server" + File.separator, workingDirectory.toString() + File.separator + "patches" + File.separator + "server.patch");
		Util.patchDirectory(tools, temp.toString() + File.separator + "server" + File.separator, workingDirectory.toString() + File.separator + "patches" + File.separator);
		
		Util.copyDirectory(temp.toString() + File.separator + "server" + File.separator + "net" + File.separator, workingDirectory.toString() + File.separator + Constants.GRADLE_PROJECT_NAME + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "net" + File.separator);
		cleanUp();
	}

	private void makeDirectories() {
		temp = new File(workingDirectory + File.separator + "temp" + File.separator);
		temp.mkdirs();
	}
	
	private void getMcp() {
		System.out.println("Downloading MCP!");
		Util.downloadFile(Constants.MCP_URL, temp.toString() + File.separator + "mcp.zip");
		tools.decompressFile(temp.toString() + File.separator + "mcp.zip", temp.toString() + File.separator + "mcp" + File.separator);
		new File(temp.toString() + File.separator + "mcp.zip").delete();
		Util.copyDirectory(temp.toString() + File.separator + "mcp" + File.separator + "conf" + File.separator, workingDirectory.toString() + File.separator + "mappings" + File.separator);
		Util.deleteDirectory(temp.toString() + File.separator + "mcp" + File.separator);
	}
	
	private void repackServer(String inJar, String jarOut) {
		File repackTemp = new File(temp.toString() + File.separator + "repackTemp" + File.separator);
		File newJarTemp = new File(temp.toString() + File.separator + "newJarTemp" + File.separator);
		repackTemp.mkdirs();
		newJarTemp.mkdirs();
		
		tools.decompressFile(inJar, repackTemp.toString() + File.separator);
		
		Util.copyDirectory(repackTemp.toString() + File.separator + "net" + File.separator, newJarTemp.toString() + File.separator + "net" + File.separator);
		
		tools.compressFile(jarOut, newJarTemp.toString());
		
		Util.deleteDirectory(newJarTemp.getAbsolutePath());
		Util.deleteDirectory(repackTemp.getAbsolutePath());
	}

	private void cleanUp() {
		Util.deleteDirectory(temp.getAbsolutePath());
	}

	private void getStone() {
		System.out.println("Downloading StoneAPI!");
		
		Util.downloadFile(Constants.STONE_URL, temp.toString() + File.separator + "Stone-master.zip");
		
		tools.decompressFile(temp.toString() + File.separator + "Stone-master.zip", temp.toString());
		
		new File(temp.toString() + File.separator + "Stone-master.zip").delete();
		Util.copyDirectory(temp.toString() + File.separator + "Stone-master" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator, workingDirectory.toString() + File.separator + Constants.GRADLE_PROJECT_NAME + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator);
		Util.deleteDirectory(new File(temp.toString() + File.separator + "Stone-master" + File.separator).getAbsolutePath());
	}
	
	private void downloadServer() {
		System.out.println("Downloading Minecraft Server!");
		Util.downloadFile(Constants.getServerUrl(), temp.toString() + File.separator + "mc.jar");
	}
	
	public static void main(String[] args) {
		new BuildTool(args);
	}
}
