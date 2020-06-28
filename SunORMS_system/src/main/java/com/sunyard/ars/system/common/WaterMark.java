package com.sunyard.ars.system.common;

import com.sunyard.aos.common.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class WaterMark {
	private String WATER_MARK = "";
	//private String date = DateUtil.getNow();

	/*public boolean addMark(File dir) {
		boolean flag = false;
		File[] imgList = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jpg"))
					return true;
				else
					return false;
			}
		});
		OutputStream output =null;
		BufferedImage imgBuf = null;
		FileInputStream fis = null;
		try{
			if (null != imgList) {
				for (int i = 0; i < imgList.length; i++) {
					try {
						File tmpFile = (File) imgList[i];
						fis = new FileInputStream(tmpFile);
						imgBuf = ImageIO.read(fis);
						Graphics2D g = (Graphics2D) imgBuf.getGraphics();

						g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
						g.setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_OVER, (float) 0.15));
						int fontSize = imgBuf.getWidth() / 12;
						int fontSizeDate = imgBuf.getWidth() / 24;
						int width = (imgBuf.getWidth() / 12) * 3;
						Font temFont = new Font("宋体", Font.PLAIN, fontSize);
						//Font dateFont = new Font("����", Font.BOLD, fontSizeDate);
						g.setFont(temFont);
						g.setColor(Color.BLACK);
						g.drawString("fjkdfjkd", width, imgBuf.getHeight() / 3);
						//g.setFont(dateFont);
					*//*g.drawString(DateUtil.getNowTime_CH(), width, 2 * imgBuf
							.getHeight() / 3);*//*
						String outputFilePath = dir.getPath() + "/"
								+ tmpFile.getName();
						File outputFile = new File(outputFilePath);
						output = new FileOutputStream(outputFilePath);
						ImageIO.write(imgBuf, "JPG", output);
						output.flush();
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		finally{
			if(output!=null){
				FileUtil.safeClose(output);
			}


			if(fis !=null){
				FileUtil.safeClose(fis);
			}
		}
		return flag;
	}
	
	public boolean addMark(File[] imgList, File mark) {
		boolean flag = false;
		OutputStream output =null;
		FileInputStream fis = null;
		for (int i = 0; i < imgList.length; i++) {
			try {
				File tmpFile = (File) imgList[i];
				System.out.println(tmpFile.getName());
				fis = new FileInputStream(tmpFile);
				BufferedImage imgBuf = ImageIO
						.read(fis);
				Graphics2D g = (Graphics2D) imgBuf.getGraphics();

				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.15));
				//String str = WaterMark.class.getResource("/hkfm.PNG").getPath();
				fis = new FileInputStream(mark);
				BufferedImage img = ImageIO.read(fis);
				//g.setColor(Color.DARK_GRAY);
				g.drawImage(img, null, imgBuf.getWidth() / 4, imgBuf
						.getHeight() / 2);
				
				String outputFilePath = "D:\\img" + File.separator
						+ tmpFile.getName();
				File outputFile = new File(FileUtil.pathManipulation(outputFilePath));
				output = new FileOutputStream(outputFilePath);
				ImageIO.write(imgBuf, "JPG", output);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(output!=null){
					FileUtil.safeClose(output);
				}

				if(fis !=null){
					FileUtil.safeClose(fis);
				}

			}
		}
		return flag;
	}*/



}
