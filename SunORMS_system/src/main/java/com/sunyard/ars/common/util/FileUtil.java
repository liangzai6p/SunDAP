package com.sunyard.ars.common.util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ClassName: FileUtil
 * @Description: 文件操作工具类
 * @Author：柏兵
 * @Date： 2014年8月25日 上午10:08:24 (创建文件的精确时间)
 */
public class FileUtil {


	/* @Fields M : M的的计量级 */
	public static final long M = 1024*1024;
	/* @Fields G : G的的计量级 */
	public static final long G = 1024*1024*1024;

	/**
	 * 创建目录
	 * @param dir
	 * @return
	 */
	public static boolean create(File dir){
		return mkdirsWithExistsCheck(dir);
	}


	/**
	 * 目录创建
	 *
	 * @param dir
	 * @return
	 */
	public static boolean mkdirsWithExistsCheck(File dir) {
		if (dir.mkdir() || dir.exists()) {
			return true;
		}
		File canonDir = null;
		try {
			canonDir = dir.getCanonicalFile();
		} catch (IOException e) {
			return false;
		}
		String parent = canonDir.getParent();
		return (parent != null)
				&& (mkdirsWithExistsCheck(new File(parent)) && (canonDir
				.mkdir() || canonDir.exists()));
	}


	/**
	 * 	目录删除
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static boolean fullyDelete(File dir) throws IOException {
		if(null !=dir){
			File contents[] = dir.listFiles();
			if (contents != null) {
				for (int i = 0; i < contents.length; i++) {
					if (contents[i].isFile()) {
						if (!contents[i].delete()) {
							return false;
						}
					} else {
						boolean b = false;
						b = contents[i].delete();
						if (b){
							continue;
						}
						if (!fullyDelete(contents[i])) {
							return false;
						}
					}
				}
			}
			return dir.delete();
		}else{
			return true;
		}
	}
	/**
	 * 	清空指定目录
	 * @param dir 目录
	 *
	 */
	public void emptyDir(File dir){
		File[] files = dir.listFiles();
		if(null != files){
			for(int i=0; i<files.length; i++){
				if(files[i].isFile())
					files[i].delete();
				else{
					emptyDir(files[i]);
					files[i].delete();
				}
			}
		}
	}

	/**
	 * 	判断文件是否存在
	 * @param dir 目录
	 * @param fileName 文件名
	 * @return 0 不存在；1 存在
	 */
	public int existFile(File dir,String fileName){
		int flag = 0;
		String[] fileList = dir.list();
		if(null != fileList){
			for(String tmpFileName:fileList){
				if(tmpFileName.equals(fileName))
					flag = 1;
			}
		}
		return flag;
	}



	/**
	 * 	业务相应目录或文件
	 * @param paramete
	 * @return
	 */
	public String getCurrentDir(String paramete){
		return "";
	}

	/**
	 * 	目录文件大小(单位byte)
	 * @param file
	 * @return
	 */
	public static long size(File file){
		long size = 0l;
		if(file.isFile()){
			size =  file.length();
		}else if(file.isDirectory()){
			File[] tmpFiles = file.listFiles();
			for(int i=0;i<tmpFiles.length;i++){
				size = size+size(tmpFiles[i]);
			}
		}
		return size;
	}

	/**
	 * 	目录文件大小(单位M)
	 * @param file
	 * @return
	 */
	public static double sizeM(File file){
		return ((double)size(file)/(double)M);
	}


	public static double sizeG(File file){
		System.out.println(((double)size(file)/(double)G));
		return ((double)size(file)/(double)G);
	}

	public static boolean checkAndMakeDir(String path){
		boolean flag = true;
		File file = new File(path);
		if(!file.exists()){
			flag = file.mkdirs();
		}
		return flag;
	}


	
	/**
	 * 根据文件路径判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean fileIsComplete(String fileName) {
		File flagFile = new File(fileName);
		return flagFile.exists();
	}
	

	
	
	/**
	 * 
	 * @Title deleteXml
	 * @Description 清理XML文件
	 * @author Zhang
	 * 2017年7月14日
	 * @param imagefile
	 */
	 public static void deleteXml(File imagefile) {
		 File[] files = imagefile.listFiles();
		 for (File file : files) {
			 if(file.getName().indexOf("xml") > -1)
				 file.delete();
		}
	}
	
	
	 /**
	  * 
	  * @Title existFileType
	  * @Description 判断目录下是否存在某个类型的文件
	  * @author Zhang
	  * 2017年7月17日
	  * @param imagefile
	  */
	 public static boolean existFileType(File imagefile, String type) {
		 File[] files = imagefile.listFiles();
		 for (File file : files) {
			 if(file.getName().indexOf(type) > -1)
				 return true;
		}
		 return false;
	}
	
}
