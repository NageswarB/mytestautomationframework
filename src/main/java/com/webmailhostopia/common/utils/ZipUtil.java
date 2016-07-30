package com.webmailhostopia.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	
	   private static List<String> fileList;
	
	 
	   
	    /**
	     * Zip it
	     * @param zipFile output ZIP file location
	     */
	    public static void zipIt(String zipFileOutput, String  sourceFolder){
	     
	    	fileList = new ArrayList<String>();
	     generateFileList(new File(sourceFolder), sourceFolder);
	     byte[] buffer = new byte[1024];
	 
	     try{
	 
	    	FileOutputStream fos = new FileOutputStream(zipFileOutput);
	    	ZipOutputStream zos = new ZipOutputStream(fos);
	 
	    	System.out.println("Output to Zip : " + zipFileOutput);
	    	
	    	for(String file : fileList){
	 
	    		System.out.println("File Added : " + file);
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);
	 
	        	FileInputStream in = 
	                       new FileInputStream(sourceFolder + File.separator + file);
	 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	 
	        	in.close();
	    	}
	 
	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();
	 
	    	System.out.println("Done");
	    }catch(IOException ex){
	       ex.printStackTrace();   
	    }
	   }
	 
	    /**
	     * Traverse a directory and get all files,
	     * and add the file into fileList  
	     * @param node file or directory
	     */
	    public static void generateFileList(File node, String sourceFolder){
	 
	    	//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString(), sourceFolder));
		}
	 
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename), sourceFolder);
			}
		}
	 
	    }
	 
	    /**
	     * Format the file path for zip
	     * @param file file path
	     * @return Formatted file path
	     */
	    private static String generateZipEntry(String file, String sourceFolder){
	    	return file.substring(sourceFolder.length()+1, file.length());
	    }
	}
