package com.auction.util;

import java.io.File;
import org.apache.commons.io.FileUtils;

public class CopyImgThread implements Runnable{

	private String inputUrl;
	private String outUrl;
	
	/**
	 * 图片copy
	 */
	public CopyImgThread(String inputUrl,String outUrl) {
		this.inputUrl = inputUrl;
		this.outUrl = outUrl;
	}
	
	@Override
	public void run() {
		try {
			File inputFile = new File(inputUrl);//源文件
			File outFile = new File(outUrl);
			if(inputFile!=null && inputFile.isFile()){
				FileUtils.copyFile(inputFile,outFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
