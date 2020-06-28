package com.sunyard.aos.common.util.net.ftp;


/**
 * 自定义ftp异常类
 * 
 * @author:	lewe
 * @date:	2017年12月6日 下午4:01:03
 */
public class FtpException extends Exception {
	private static final long serialVersionUID = -179711247969397582L;

	public FtpException(String message){
		super(message);
	}
	
	public FtpException(String message, Throwable cause) {
		super(message, cause);
	}
}
