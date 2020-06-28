package com.sunyard.ars.common.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WaterMarkUtil {

	private static Logger logger = LoggerFactory.getLogger(WaterMarkUtil.class);

	/**
	 * 把image 转成 BufferedImage，直接ImageIO.read 图像可能会变色
	 *
	 * @param image
	 * @return BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		image = new ImageIcon(image).getImage();
//		boolean hasAlpha = false;
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;
//			if (hasAlpha) {
//				transparency = Transparency.BITMASK;
//			}
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
		}
		if (bimage == null) {
			int type = BufferedImage.TYPE_INT_RGB;
//			if (hasAlpha) {
//				type = BufferedImage.TYPE_INT_ARGB;
//			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		Graphics g = bimage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}

	/**
	 * 给imgBuf加上文字水印
	 *
	 * @param pattern 打印日期的格式 imgBuf 系统参数表配置 typefaceOfImg 字体 fontStyleOfImg 字体风格 {
	 *                Font.PLAIN(普通 0) Font.BOLD(加粗 1) Font.ITALIC(斜体 2)
	 *                Font.BOLD+Font.ITALIC(粗斜体 3) } fontSizePerOfImg 字体大小 于宽的百分比
	 *                XYLocationOfImg 字体位置 eg：45,45 字体颜色黑色
	 */
	public static void addMark(BufferedImage imgBuf, String addText, String pattern) {
		try {
			Graphics2D g = (Graphics2D) imgBuf.getGraphics();
			// 设置字体
			String typeface = "宋体";
			int fontStyle = Font.BOLD + Font.ITALIC; // Font.PLAIN(普通) Font.BOLD(加粗) Font.ITALIC(斜体)
			// Font.BOLD+Font.ITALIC(粗斜体)
			int fontSize = imgBuf.getWidth() / 12;
			String shuiYinChar = addText;
			int charX = 0; // 文字位置
			int charY = 0; // 文字位置
			float alpha = 0.2f; // 文字的透明度
			// 字体
			SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get("typefaceOfImg");
			if (sp != null) {
				typeface = sp.getParamValue();
			}
			// 风格
			sp = ARSConstants.SYSTEM_PARAMETER.get("fontStyleOfImg");
			if (sp != null) {
				fontStyle = Integer.parseInt(sp.getParamValue());
			}
			// 字体大小
			sp = ARSConstants.SYSTEM_PARAMETER.get("fontSizePerOfImg");
			if (sp != null) {
				fontSize = Integer.parseInt(sp.getParamValue());
			}

			Font temFont = new Font(typeface, fontStyle, fontSize);

			g.setFont(temFont);

			// 计算文字大小
			JLabel label = new JLabel(addText);
			FontMetrics metrics = label.getFontMetrics(temFont);
			int stringWidth = metrics.stringWidth(label.getText());// 文字水印的宽
			int stringHeight = metrics.getHeight();
			/*
			 * FontDesignMetrics metrics = FontDesignMetrics.getMetrics(temFont); int
			 * stringHeight = metrics.getHeight(); int stringWidth =
			 * metrics.stringWidth(shuiYinChar);
			 */
			// 默认字体的位置，文字大致放在中间位置
			int ascent = metrics.getAscent();// 水印文字的基线到顶部最大的高
			charX = (imgBuf.getWidth() - stringWidth) / 2;
			charY = (imgBuf.getHeight() + stringHeight) / 2 - (stringHeight - ascent);

			// 设置的位置
			sp = ARSConstants.SYSTEM_PARAMETER.get("CharXYLocationOfImg");
			if (sp != null) {
				String[] strXY = sp.getParamValue().split(",");
				charX = imgBuf.getWidth() * Integer.parseInt(strXY[0]) / 100;
				charY = imgBuf.getHeight() * Integer.parseInt(strXY[1]) / 100;
			}

			// 得到透明度
			sp = ARSConstants.SYSTEM_PARAMETER.get("alphaOfImg");
			if (sp != null) {
				alpha = Float.parseFloat(sp.getParamValue());
			}
			// 去除曲线锯齿状
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 文字的透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			// 文字颜色
			g.setColor(Color.BLACK);
			g.drawString(shuiYinChar, charX, charY);

			// 正文字体不要设置太大，会超出图片和重叠
			if (pattern != null) {
				// 打印日期,在文字下面
				String nowDate = new SimpleDateFormat(pattern).format(new Date());
				// 计算水印日期的相关信息
				Font font = new Font("宋体", fontStyle, Math.max(fontSize / 5, 1)); // 日期的字体
				//FontDesignMetrics metricsDate = FontDesignMetrics.getMetrics(font);

				JLabel label2 = new JLabel(nowDate);
				FontMetrics metrics2 = label.getFontMetrics(font);
				int dateWidth = metrics2.stringWidth(label2.getText());// 文字水印的宽
				int dateStartX = Math.max(0, charX + stringWidth - dateWidth);
				// charY + (stringHeight - ascent) + 10 代表 正文内容向下 10
				// int dateStartY = Math.min(imgBuf.getHeight() - dateHeight, charY +
				// (stringHeight - ascent) + 10);
				int dateStartY = charY + (stringHeight - ascent) + 10;
				g.setFont(font);
				g.drawString(nowDate, dateStartX, dateStartY);
			}
			g.dispose();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("绘制文字水印失败");
		}
	}

	/**
	 * 给imgBuf加上图像水印
	 *
	 * @param imgBuf
	 * @param pattern
	 */

	public static void addMarkPic(BufferedImage imgBuf, String pattern) {
		try {
			Graphics2D g = (Graphics2D) imgBuf.getGraphics();
			// 设置字体
			int startX = 0; // 图像水印的位置x 和 y
			int startY = 0;
			int loggerWith = 0;
			int loggerHeight = 0;
			float alpha = 0.2f; // 文字的透明度

			SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get("pictLoggerPath");
			if (sp == null) {
				throw new Exception("系统没有配置水印的图像的路径");
			}
			String logFileName = sp.getParamValue();
			File loggerFile = new File(FileUtil.pathManipulation(logFileName));
			if (!loggerFile.exists() || loggerFile.isDirectory()) {
				throw new Exception("水印图片不存在");
			}

			// 得到透明度
			sp = ARSConstants.SYSTEM_PARAMETER.get("alphaOfImg");
			if (sp != null) {
				alpha = Float.parseFloat(sp.getParamValue());
			}

			// 去除曲线锯齿状
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

			Image image = Toolkit.getDefaultToolkit().getImage(logFileName);
			BufferedImage img = toBufferedImage(image);
			g.setColor(Color.DARK_GRAY);
			sp = ARSConstants.SYSTEM_PARAMETER.get("loggerSizeOfImg"); // 图像大小
			if (sp != null) {
				String[] xyRate = sp.getParamValue().split(","); // 图像的比例 x轴比例
				loggerWith = Math.min((int) (img.getWidth() * Double.parseDouble(xyRate[0])), imgBuf.getWidth());
				if (xyRate.length == 1) {
					loggerHeight = loggerWith / img.getWidth() * img.getHeight();
				} else { // y轴比例
					loggerHeight = (int) Math.min(img.getWidth() * Double.parseDouble(xyRate[1]), imgBuf.getHeight());
				}
			} else {
				loggerWith = Math.min(imgBuf.getHeight(), imgBuf.getWidth()) * 1 / 3;
				loggerHeight = loggerWith * img.getHeight() / img.getWidth();
			}
			sp = ARSConstants.SYSTEM_PARAMETER.get("PictXYLocationOfImg"); // 图像位置
			if (sp != null) {
				String[] strXY = sp.getParamValue().split(",");
				startX = imgBuf.getWidth() * Integer.parseInt(strXY[0]) / 100;
				startY = imgBuf.getHeight() * Integer.parseInt(strXY[1]) / 100;
			} else {
				startX = (imgBuf.getWidth() - loggerWith) / 2;
				startY = (imgBuf.getHeight() - loggerHeight) / 2;
			}
			// 画图
			g.drawImage(img, startX, startY, loggerWith, loggerHeight, null);

			// 如果正文的字体再大，可能会超出图片和重叠起来
			if (pattern != null) {
				// 打印日期,在文字下面
				String nowDate = new SimpleDateFormat(pattern).format(new Date());
				sp = ARSConstants.SYSTEM_PARAMETER.get("contentOfImg");
				if (sp != null) {
					nowDate = sp.getParamValue() + " " + nowDate;
				}
				int fontStyle = Font.BOLD + Font.ITALIC;
				int fontSize = Math.max(img.getWidth() / 3, 10);
				// 计算水印日期的相关信息
				Font font = new Font("宋体", fontStyle, Math.max(fontSize / 5, 1));

				JLabel label = new JLabel(nowDate);
				FontMetrics metrics = label.getFontMetrics(font);

				// FontDesignMetrics metricsDate = FontDesignMetrics.getMetrics(font);
				// int dateWidth = metricsDate.stringWidth(nowDate);
				int dateWidth = metrics.stringWidth(label.getText());
				int dateStartX = Math.max(0, startX + loggerWith - dateWidth);

				// 让日期小字在图片的正下方10处
				int dateStartY = (imgBuf.getHeight() + loggerHeight) / 2 + 10 + metrics.getAscent();
				g.setFont(font);
				g.drawString(nowDate, dateStartX, dateStartY);
			}
			g.dispose();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("绘制图片水印失败");
		}
	}

	// 斜向下45的水印
	public static void addWatermark(BufferedImage imgBuf, String waterMarkContent) {
		defaultAddWatermark(imgBuf,waterMarkContent,45);
	}


	/**
	 *
	 * @param imgBuf  图片
	 * @param waterMarkContent 水印文字
	 * @param degree 角度 （与水平或垂直要相差超过15度就水平或垂直）
	 */
	private static void defaultAddWatermark(BufferedImage imgBuf, String waterMarkContent,float degree) {
		try {
			// 设置字体
			String typeface = "宋体";
			int fontStyle = Font.BOLD + Font.ITALIC; // Font.PLAIN(普通) Font.BOLD(加粗) Font.ITALIC(斜体)
			int fontSize = 15;

			// 字体
			SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get("typefaceOfImg");
			if (sp != null) {
				typeface = sp.getParamValue();
			}
			// 风格
			sp = ARSConstants.SYSTEM_PARAMETER.get("fontStyleOfImg");
			if (sp != null) {
				fontStyle = Integer.parseInt(sp.getParamValue());
			}
			// 字体大小
			sp = ARSConstants.SYSTEM_PARAMETER.get("fontSizePerOfImg");
			if (sp != null) {
				fontSize = Integer.parseInt(sp.getParamValue());
			}
			Font font = new Font(typeface, fontStyle, fontSize);


			float alpha = 0.4f;// 设置水印透明度 默认为1.0 值越小颜色越浅
			int srcImgWidth = imgBuf.getWidth(null);// 获取图片的宽
			int srcImgHeight = imgBuf.getHeight(null);// 获取图片的高
			Graphics2D g = imgBuf.createGraphics();// 得到画笔
			g.setColor(Color.BLACK); // 设置水印颜色
			g.setFont(font); // 设置字体
			// 得到透明度
			sp = ARSConstants.SYSTEM_PARAMETER.get("alphaOfImg");
			if (sp != null) {
				alpha = Float.parseFloat(sp.getParamValue());
			}
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));// 设置水印文字透明度

			JLabel label = new JLabel(waterMarkContent);
			FontMetrics metrics = label.getFontMetrics(font);
			int textWidth = metrics.stringWidth(label.getText());// 文字水印的宽

			float de = computeDegree(degree);
			if( de >= -15 && de <= 15 || de >= 165 && de <= 195){
			    de = 0;
				if(de >= 165 && de <= 195){
                    de = 180;
					g.rotate(Math.toRadians(180), 0, 0);// 180
				}
				horizontalOrVerticalWater(g,waterMarkContent,srcImgWidth,srcImgHeight,textWidth*2,metrics.getHeight() * 5,de);
			}else if(de >= 75 && de <= 105 || de >= 255 && de <= 285){
			    de = 90;
				if(de >= 75 && de <= 105){
					g.rotate(Math.toRadians(90), 0, 0);// 90
                    de = 90;
				}else {
					g.rotate(Math.toRadians(-90), 0, 0);// 270
                    de = 270;
				}
				horizontalOrVerticalWater(g,waterMarkContent,srcImgWidth,srcImgHeight,metrics.getHeight() * 5,textWidth*2,de);
			}else {
				float radians = (float) Math.toRadians(degree); //旋转的弧度
				g.rotate(radians, 0, 0);// 设置水印旋转
				int col = (int ) (srcImgWidth / ( Math.abs(Math.cos(radians)) * textWidth * 2)) + 1; //有空白的加一好看点
				int row = (int ) (srcImgHeight / ( Math.abs(Math.sin(radians))* textWidth * 2)) + 1;
				int a = 0; //上次的x坐标
				int b = 0; //上次的y坐标
				for(int rowNo = 0;rowNo < row;rowNo ++){ //每一行
					for(int colNo = 0;colNo < col;colNo ++){
						g.rotate(-radians, a, b); //旋转回去
						a = (int)(0 + colNo * textWidth* Math.abs(Math.cos(radians)) * 2);
						b = (int)(0 + rowNo * textWidth* Math.abs(Math.sin(radians))* 2);
						g.rotate(radians, a, b);// 以（a,b）设置水印旋转
						g.drawString(waterMarkContent, a,b);
					}
				}
				g.dispose();// 释放资源
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("给图片加上水印失败!");
		}
	}


	/**
	 * 图片水平竖直的水印
	 * @param g
	 * @param waterMarkContent
	 * @param imgWidth
	 * @param imgHeight
	 * @param xDiffer
	 * @param yDiffer
	 */
	private static void horizontalOrVerticalWater(Graphics2D g,String waterMarkContent,int imgWidth,int imgHeight,int xDiffer,int yDiffer,float degre){

		int col = imgWidth / xDiffer + 1;
		int row = imgHeight / yDiffer + 1;

		int a = 0;
		int b = 0;
		for(int rowNo = 0;rowNo < row;rowNo ++){ //每一行
			for(int colNo = 0;colNo < col;colNo ++){
			    g.rotate(Math.toRadians(-1 * degre), a, b);
			    a = 0 + colNo * xDiffer;
			    b = 0 + rowNo * yDiffer;
                g.rotate(Math.toRadians( degre), a, b);
				g.drawString(waterMarkContent, a,b);

			}
		}
		g.dispose();// 释放资源

	}


	/**
	 * pdf 打印水印
	 * @param doc
	 * @param paragraph
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param xDiffer  x轴间距
	 * @param yDiffer  y
	 * @param degre
	 * @param i        pdf的第几页
	 * @param flag    每行是否错开，斜的时候好看点
	 */
	private void pdfWaterPringt(Document doc,Paragraph paragraph,
								 float left,float right,float bottom,float top, float xDiffer,float yDiffer,float degre,int i,boolean flag){

		int col = (int ) ((right - left) /  xDiffer )  + 1;  //行的数量  +1 好看点
		int row = (int ) ((top - bottom) / yDiffer  ) + 1; // 列的数量  +1 好看点

		if(col > (right - left) || row > (top - bottom)){
			//本次有问题，防止很多次循环，。。。
			return;
		}
		float radAngle = (float)Math.toRadians(degre);
		float x =0,y = 0;
		if(degre > 0 && degre < 90){
			x = xDiffer;
			y = -yDiffer;
		}else if(degre > 90 && degre < 180){
			x = -xDiffer;
			y = -yDiffer;
		}else  if(degre > 180 && degre < 270){
			x = -xDiffer;
			y = yDiffer;
		}else{
			x = xDiffer;
			y = yDiffer;
		}
		for(int rowNo = 0;rowNo < row;rowNo ++){ //每一行
			for(int colNo = 0;colNo < col;colNo ++){
				if( flag && rowNo % 2 == 0){
					doc.showTextAligned(paragraph,
							(float)(left+(colNo + 0)*xDiffer+ 0.5*x),
							(float)(bottom + (rowNo + 0) * yDiffer + 0.5*yDiffer),
							i,
							TextAlignment.CENTER,VerticalAlignment.TOP,
							radAngle);
				}else {
					doc.showTextAligned(paragraph,
							left+(colNo + 0) * xDiffer  ,
							(float) (bottom + (rowNo + 0) *  yDiffer  + 0.5*yDiffer) ,
							i,
							TextAlignment.CENTER,VerticalAlignment.TOP, radAngle);
				}
			}
		}
	}

	/**
	 *
	 * @param src
	 * @param outS
	 * @param text
	 * @param FONTSIZE
	 * @param degree
	 * @throws IOException
	 * java的三角函数有精度问题
	 */
	public void pdfDefaultWater(InputStream src, OutputStream outS, String text, int FONTSIZE, float degree) throws IOException {

		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(outS));
		Document doc = new Document(pdfDoc);

		//String path = getClass().getClassLoader().getResource("font/STCAIYUN.TTF").getPath();
		String path = ARSConstants.CONFIGPATH + File.separator + "";
		String fontType = null;
		SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get("fontTypeOfWaterPDF");
		if (sp != null) {
			fontType = sp.getParamValue();
		}else {
			fontType = path;
		}
		//创建字体
		PdfFont font = null;
		try {
			font = PdfFontFactory.createFont(fontType, PdfEncodings.IDENTITY_H,true);
		}catch (Exception e){
			//e.printStackTrace();
			try{
				font = PdfFontFactory.createFont(path, PdfEncodings.IDENTITY_H,true);
			}catch (IOException e1){
				throw e1;
			}
		}

		// 得到透明度
		float alpha = 0.5f;
		sp = ARSConstants.SYSTEM_PARAMETER.get("alphaOfImg");
		if (sp != null) {
			alpha = Float.parseFloat(sp.getParamValue());
		}
		PdfExtGState gs1 = new PdfExtGState().setFillOpacity(alpha);
		Paragraph paragraph = new Paragraph(text).setFont(font).setFontSize(FONTSIZE);


		for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
			PdfPage pdfPage = pdfDoc.getPage(i);
			Rectangle pageSize = pdfPage.getPageSize();

			PdfCanvas over = new PdfCanvas(pdfPage);
			over.saveState();
			over.setExtGState(gs1);

			float textLenth = getTextLenth(font, text, FONTSIZE);
			float textHeight = getTextHeight(font, text, FONTSIZE);

			float left = pageSize.getLeft();
			float right = pageSize.getRight();
			float bottom = pageSize.getBottom();
			float top = pageSize.getTop();

			float de = computeDegree(degree);
			if( de == 0 || de == 180){
				//水平
				pdfWaterPringt(doc,paragraph,left,right,bottom,top,
						textLenth * 2,textHeight *5,de,i,false);
			}else if(de == 90 || de == 270){
				pdfWaterPringt(doc,paragraph,left,right,bottom,top,
						textHeight *5,textLenth * 2,de,i,false);
			}else {
				pdfWaterPringt(doc,paragraph,left,right,bottom,top,
						(float)(textLenth * 2 * Math.abs(Math.cos(Math.toRadians(de)))) ,(float)(textLenth * 2 * Math.abs(Math.sin(Math.toRadians(de)))),de,i,true);

			}
			over.restoreState();
		}

		doc.close();
	}

	//得到PdfFont字体的高度
	private float getTextHeight(PdfFont f, String str, int fontSize){
		float ascent = f.getAscent(str, fontSize);
		float descent = f.getDescent(str, fontSize);
		return  ascent - descent;
	}

	//PdfFont字符串的长度
	private float getTextLenth(PdfFont f, String str, int fontSize){
		return f.getWidth(str, fontSize);
	}


	private static float computeDegree(float degree){
		int de = ((int)degree) % 360;
		if(de < 0){
			de = 360 + de; //转成正数 0到359之间
		}
		if( (de <= 15 && de > 0) || (de >= 345 && de < 360) ){
			return 0; // 水平 0度
		}else if(de >= 75 && de <= 105){
			return 90; //垂直向上
		}else if(de >= 165 && de <= 195){
			return 180;
		}else if(de >= 255 && de <= 185){
			return 270; //垂直向下
		}
		return de;
	}


}
