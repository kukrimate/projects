package com.craftstone.diffmaker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
	public static boolean isOnWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("win");
	}
	
	public static void createPatchesFromFolder(DiffManager diff, String origFolder, String newFolder, String patchFolder) {
		List<String> origFiles = getAllFilesInFolder(origFolder);
		List<String> newFiles = getAllFilesInFolder(newFolder);
		
		new File(patchFolder + File.separator).mkdirs();
		
		int index = 0;
		
		for (String str : origFiles) {
			String origFile = str;
			String newFile = newFiles.get(index);
			String patchFile = patchFolder + File.separator + new File(origFile).getName() + ".patch";
			diff.callDiff(origFile, newFile, patchFile);
			if (new File(patchFile).length() == 0) {
				new File(patchFile).delete();
			}
			index++;
		}
	}
	
	private static List<String> getAllFilesInFolder(String directory) {
		List<String> files = new ArrayList<String>();
		File directoryFile = new File(directory);
		for (File file : directoryFile.listFiles()) {
			if (file.isDirectory()) {
				files.addAll(getAllFilesInFolder(file.getAbsolutePath()));
			} else {
				files.add(file.getAbsolutePath());
			}
		}
		return files;
	}
	
	public static String arrayToString(String[] array) {
		String str = "";
		for (String s : array) {
			str += s + " ";
		}
		return str;
	}
}
