package com.craftstone.diffmaker;

import java.io.File;
import java.util.ArrayList;

public class DiffManager {
	private String workDir;
	
	public static final String TOOLS_FOLDER = "tools";
	public static final String DIFF_FILENAME = "diff";
	
	public DiffManager(String workDir) {
		this.workDir = workDir;
	}
	
	public void callDiff(String origFile, String modFile, String patchFile) {
		ArrayList<String> params = new ArrayList<String>();
		params.add(getDiffPath(DIFF_FILENAME));
		params.add("-u");
		params.add(origFile);
		params.add(modFile);
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(params);
		builder.redirectOutput(new File(patchFile));
		System.out.println("String diff: " + Util.arrayToString(params.toArray(new String[params.size()])));
		try {
			Process process = builder.start();
			process.waitFor();
		} catch(Exception e) {
			System.out.println("Error creating patch");
			e.printStackTrace();
		}
	}
	
	private String getDiffPath(String toolName) {
		if (Util.isOnWindows()) {
			return this.workDir + File.separator + TOOLS_FOLDER + File.separator + toolName + ".exe";
		} else {
			return toolName;
		}
	}
}
