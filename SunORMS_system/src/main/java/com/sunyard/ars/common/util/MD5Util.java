package com.sunyard.ars.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class MD5Util {


//	public static void main(String[] args) throws NoSuchAlgorithmException,
//			IOException {
//
//		long start = System.currentTimeMillis();
//		System.out.println("开始计算文件MD5值,请稍后...");
//		String fileName = "H:\\20160613145307125552\\001-00001-9056B397-9D57-4edc-A2DF-CA4CB0FD1333-1.jpg";
//		// // String fileName = "E:\\SoTowerStudio-3.1.0.exe";
//		String hashType = "MD5";
//		String hash = getHash(fileName, hashType);
//		System.out.println("MD5:" + hash);
//		long end = System.currentTimeMillis();
//		System.out.println("一共耗时:" + (end - start) + "毫秒");
//	}

	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取文件MD5
	 * @param fileName
	 * @param hashType
	 * @return
	 */
	public static String getHash(String fileName, String hashType) {
		boolean flag = true;
		File f = new File(fileName);
		MessageDigest md5 = null;
		InputStream ins = null;

//		System.out
//				.println(" -------------------------------------------------------------------------------");
//		System.out.println("|当前文件名称:" + f.getName());
//		System.out.println("|当前文件大小:" + (f.length() / 1024 / 1024) + "MB");
//		System.out.println("|当前文件路径[绝对]:" + f.getAbsolutePath());
		try {
//			System.out.println("|当前文件路径[---]:" + f.getCanonicalPath());

//			System.out
//					.println(" -------------------------------------------------------------------------------");

			ins = new FileInputStream(f);

			byte[] buffer = new byte[8192];

			md5 = MessageDigest.getInstance(hashType);
			int len;
			while ((len = ins.read(buffer)) != -1) {
				md5.update(buffer, 0, len);
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;

		} finally {
			try {
				if(ins!=null){
					ins.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				flag = false;

			}

		}
		if(flag && md5!=null){
			return toHexString(md5.digest());
		}else{
			return "";
		}


	}

	public static String getHash2(String fileName) {
		File f = new File(fileName);
		return String.valueOf(f.lastModified());
	}

	protected static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}


}
