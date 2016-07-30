package com.webmailhostopia.common.utils;


import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HttpFileDownload {
	final static int size = 1024;
	
	
	public static boolean fileUrl(String fAddress, String localFileName, String destinationDir) throws Exception {
		return fileUrl(fAddress, localFileName, destinationDir, null);
	}

	public static boolean fileUrl(String fAddress, String localFileName, String destinationDir, Map<String, String> httpHeaders) throws Exception {
		OutputStream outStream = null;
		URLConnection uCon = null;

		InputStream is = null;
		try {
			URL Url;
			byte[] buf;
			@SuppressWarnings("unused")
			int ByteRead, ByteWritten = 0;
			Url = new URL(fAddress);
			outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + "\\" + localFileName));
		
			String cookie = "";
			if (httpHeaders!= null) {
				for (String header : httpHeaders.keySet()){
					cookie = cookie + ";" + header + "=" + httpHeaders.get(header);
				}	
			}
			uCon = Url.openConnection();			
			uCon.setRequestProperty("Cookie", cookie);
					
			is = uCon.getInputStream();
			buf = new byte[size];
			while ((ByteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, ByteRead);
				ByteWritten += ByteRead;
			}
			return true;
			// System.out.println("Downloaded Successfully.");
			// System.out.println("File name:\"" + localFileName + "\"\nNo of bytes :" + ByteWritten);
		} catch (FileNotFoundException ex) {
			throw new Exception("Failed to download file " + localFileName + ". Reason: file not found.");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	public static boolean fileDownload(String fAddress, String destinationDir) throws Exception {

		int slashIndex = fAddress.lastIndexOf('/');
		int periodIndex = fAddress.lastIndexOf('.');

		String fileName = fAddress.substring(slashIndex + 1);

		if (periodIndex >= 1 && slashIndex >= 0 && slashIndex < fAddress.length() - 1) {
			return fileUrl(fAddress, fileName, destinationDir);
		} else {
			throw new Exception("invalid path or file name.");
		}
	}

	// public static void main(String[] args) {

	// fileDownload("http://istadv20:8082/artifactory/repo/com/eurekify/automation/LinkAttributes/12.6.0005/LinkAttributes-12.6.0005.dump", "c:\\temp");

	// }
}