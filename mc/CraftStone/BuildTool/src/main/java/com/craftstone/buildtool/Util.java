package com.craftstone.buildtool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Util {
	public static String arrayToString(String[] array) {
		String str = "";
		for (String s : array) {
			str += s + " ";
		}
		return str;
	}

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("win") ? true : false;
	}
	
	public static void patchDirectory(ToolManager tm, String directory, String patchFolder) {
		List<String> files = getAllFilesInFolder(directory);
		
		for (String str : files) {
			File patchFile = new File(patchFolder + File.separator + new File(str).getName() + ".patch");
			if (!patchFile.exists()) {
				continue;
			}
			
			tm.callPatch(str, patchFile.getAbsolutePath());
		}
	}
	
	public static void downloadFile(String url, String path) {
		File folder = new File(new File(path).getParentFile() + File.separator);
		if(!folder.isDirectory()) {
			folder.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(path);
			BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
		    byte data[] = new byte[1024];
		    int count;
		    while((count = in.read(data,0,1024)) != -1)
		    {
		        fos.write(data, 0, count);
		    }
		    
		    fos.close();
		} catch(Exception e) {
			System.out.println("Exception downloading file: " + e.toString());
		}
	}
	
	public static void deleteDirectory(String dir) {
		File directory = new File(dir);
		   if(directory.exists()){
		        File[] files = directory.listFiles();
		        if(null!=files){
		            for(int i=0; i<files.length; i++) {
		                if(files[i].isDirectory()) {
		                    deleteDirectory(files[i].getAbsolutePath());
		                }
		                else {
		                    files[i].delete();
		                }
		            }
		        }
		    }
		   directory.delete();
	}
	
	public static void copyDirectory(String from, String to) {
		File src = new File(from);
		File dest = new File(to);
		
		if(src.isDirectory()) {
    		if(!dest.isDirectory()){
    		   dest.mkdirs();
    		}
 
    		String files[] = src.list();
 
    		for (String file : files) {
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   copyDirectory(srcFile.toString(),destFile.toString());
    		}
 
    	}else{
    		try {
        		InputStream in = new FileInputStream(src);
        	        OutputStream out = new FileOutputStream(dest); 
     
        	        byte[] buffer = new byte[1024];
     
        	        int length;
        	        while ((length = in.read(buffer)) > 0){
        	    	   out.write(buffer, 0, length);
        	        }
     
        	        in.close();
        	        out.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
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
	
	public static void dirToLf(ToolManager tm, String directory) {
		List<String> files = getAllFilesInFolder(directory);
		for (String str : files) {
			tm.toLf(str);
		}
	}
	
	public static void dirToCrLf(ToolManager tm, String directory) {
		List<String> files = getAllFilesInFolder(directory);
		for (String str : files) {
			tm.toCrlf(str);
		}
	}
}
