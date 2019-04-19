package com.craftstone.diffmaker;

import java.io.File;

public class DiffMaker {
	public DiffMaker() {
		File workingDirectory = new File("");
		String workDir = workingDirectory.getAbsolutePath();
		System.out.println("Working directory: " + workDir);
		
		DiffManager diff = new DiffManager(workDir);
		
		Util.createPatchesFromFolder(diff, workDir + File.separator + Constants.ORIG_FOLDER + File.separator, workDir + File.separator + Constants.CHANGED_FOLDER + File.separator, workDir + File.separator + Constants.OUTPUT_FOLDER + File.separator);
	}
	
	public static void main(String[] args) {
		new DiffMaker();
	}
}
