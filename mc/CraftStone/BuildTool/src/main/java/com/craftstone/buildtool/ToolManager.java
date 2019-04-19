package com.craftstone.buildtool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ToolManager {
	private static final String TOOL_DIR = "tools";
	private static final String FERNFLOWER = "fernflower.jar";
	private static final String MC_INJECTOR = "mcinjector.jar";
	private static final String SPECIALSOURCE = "specialsource.jar";
	private static final String PATCH = "patch";
	private static final String ZIP = "7za";
	private static final String UNIX2DOS = "unix2dos";
	private static final String DOS2UNIX = "dos2unix";
	
	private File workDir;
	
	public ToolManager(File workingDir) {
		this.workDir = workingDir;
	}
	
	public void toCrlf(String file) {
		if (!Util.isWindows()) {
			return;
		}
		System.out.println("Converting file to CRLF!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		ArrayList<String> params = new ArrayList<String>();
		params.add(getToolPath(UNIX2DOS));
		params.add(file);
		builder.command(params);
		System.out.println("Starting unix2dos: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			waitForProcess(process);
		} catch (Exception e) {
			System.out.println("Error converting");
			e.printStackTrace();
		}
	}
	
	public void toLf(String file) {
		if (Util.isWindows()) {
			return;
		}
		System.out.println("Converting file to LF!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		ArrayList<String> params = new ArrayList<String>();
		params.add(getToolPath(DOS2UNIX));
		params.add(file);
		builder.command(params);
		System.out.println("Starting dos2unix: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			waitForProcess(process);
		} catch (Exception e) {
			System.out.println("Error converting");
			e.printStackTrace();
		}
	}
	
	public void decompressFile(String zipFilePath, String outPathDir) {
		if (!new File(outPathDir).isDirectory()) {
			new File(outPathDir).mkdirs();
		}
		System.out.println("Decompressing with 7-ZIP!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		builder.directory(new File(outPathDir));
		ArrayList<String> params = new ArrayList<String>();
		params.add(getToolPath(ZIP));
		params.add("x");
		params.add(zipFilePath);
		builder.command(params);
		System.out.println("Starting 7zip: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			waitForProcess(process);
		} catch (Exception e) {
			System.out.println("Error Decompressing");
			e.printStackTrace();
		}
	}
	
	public void compressFile(String zipFilePath, String filesPath) {
		if (!new File(filesPath).isDirectory()) {
			return;
		}
		System.out.println("Compressing with 7-ZIP!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		builder.directory(new File(filesPath));
		ArrayList<String> params = new ArrayList<String>();
		params.add(getToolPath(ZIP));
		params.add("a");
		params.add(zipFilePath);
		params.add("*");
		builder.command(params);
		System.out.println("Starting 7zip: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			waitForProcess(process);
		} catch (Exception e) {
			System.out.println("Error Compressing");
			e.printStackTrace();
		}
	}
	
	public void callFernflower(String jarToDecompile, String pathForJavaFiles) {
		System.out.println("Decompiling with FERNFLOWER!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		ArrayList<String> params = new ArrayList<String>();
		params.add("java");
		params.add("-jar");
		params.add(getToolPath(FERNFLOWER));
		params.add("-din=1");
		params.add("-rbr=0");
		params.add("-dgs=1");
		params.add("-asc=1");
		params.add("-log=WARN");
		params.add(jarToDecompile);
		params.add(pathForJavaFiles);
		builder.command(params);
		System.out.println("Starting fernflower: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			waitForProcess(process);
		} catch (Exception e) {
			System.out.println("Error decompiling");
			e.printStackTrace();
		}
	}
	
	public void callPatch(String fileToPatch, String patchFile) {
		System.out.println("Patching with PATCH!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		builder.redirectInput(new File(patchFile));
		ArrayList<String> params = new ArrayList<String>();
		params.add(getToolPath(PATCH));
		params.add(fileToPatch);
		params.add("--ignore-whitespace");
		params.add("-F3");
		builder.command(params);
		System.out.println("Starting patch: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			waitForProcess(process);
		} catch (Exception e) {
			System.out.println("Error patching");
			e.printStackTrace();
		}
	}
	
	public void callMcInjector(String jarToPatch, String outFile, String mapFile, String jsonIn) {
		System.out.println("Patching with MCINJECTOR!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		ArrayList<String> params = new ArrayList<String>();
		params.add("java");
		params.add("-jar");
		params.add(getToolPath(MC_INJECTOR));
		params.add("--jarIn");
		params.add(jarToPatch);
		params.add("--mapIn");
		params.add(mapFile);
		params.add("--jarOut");
		params.add(outFile);
		params.add("--jsonIn");
		params.add(jsonIn);
		builder.command(params);
		System.out.println("Starting mcinjector: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (Exception e) {
			System.out.println("Error patching");
			e.printStackTrace();
		}
	}
	
	public void callSpecialSource(String inputJar, String mappingsFile, String outFile) {
		System.out.println("Remapping with SPECIALSOURCE!");
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectOutput();
		ArrayList<String> params = new ArrayList<String>();
		params.add("java");
		params.add("-jar");
		params.add(getToolPath(SPECIALSOURCE));
		params.add("-i");
		params.add(inputJar);
		params.add("-m");
		params.add(mappingsFile);
		params.add("-o");
		params.add(outFile);
		builder.command(params);
		System.out.println("Starting specialsource: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (Exception e) {
			System.out.println("Error patching");
			e.printStackTrace();
		}
	}
	
	private String getToolPath(String toolName) {
		if (Util.isWindows() && !toolName.toLowerCase().endsWith(".jar")) {
			return workDir.toString() + File.separator + TOOL_DIR + File.separator + "windows" + File.separator + toolName + ".exe";
		} else if (toolName.toLowerCase().endsWith(".jar")) {
			return workDir.toString() + File.separator + TOOL_DIR + File.separator + toolName;
		} else {
			return toolName;
		}
	}
	
	private void waitForProcess(Process process) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				line.toString();
				//System.out.println(line);
			}
			process.waitFor();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
