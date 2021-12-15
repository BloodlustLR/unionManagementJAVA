package org.eu.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 文件操作工具类
 * 
 * @author: dylan
 */

@Slf4j
public class FileUtil {
	
	/**
	 * MultipartFile 转 File
	 *
	 * @param file
	 * @throws Exception
	 */
	public static File multipartFileToFile(MultipartFile file) throws Exception {

		File toFile = null;
		if (file.equals("") || file.getSize() <= 0) {
			file = null;
		} else {
			InputStream ins = null;
			ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			inputStreamToFile(ins, toFile);
			ins.close();
		}
		return toFile;
	}

	//获取流文件
	public static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//MultipartFile图片转base64
	public static String multipartFileToBase64(MultipartFile file){
		if (file == null || file.isEmpty()) {
			throw new RuntimeException("图片不能为空！");
		}
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		String contentType = file.getContentType();
		byte[] imageBytes = null;
		String base64EncoderImg="";
		try {
			imageBytes = file.getBytes();
			BASE64Encoder base64Encoder =new BASE64Encoder();
			/**
			 * 1.Java使用BASE64Encoder 需要添加图片头（"data:" + contentType + ";base64,"），
			 *   其中contentType是文件的内容格式。
			 * 2.Java中在使用BASE64Enconder().encode()会出现字符串换行问题，这是因为RFC 822中规定，
			 *   每72个字符中加一个换行符号，这样会造成在使用base64字符串时出现问题，
			 *   所以我们在使用时要先用replaceAll("[\\s*\t\n\r]", "")解决换行的问题。
			 */
			base64EncoderImg = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
			base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64EncoderImg;
	}


	//MultipartFile图片转base64(去掉图片头)
	public static String multipartFileToBase64WithoutHeader(MultipartFile file){
		if (file == null || file.isEmpty()) {
			throw new RuntimeException("图片不能为空！");
		}
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		String contentType = file.getContentType();
		byte[] imageBytes = null;
		String base64EncoderImg="";
		try {
			imageBytes = file.getBytes();
			BASE64Encoder base64Encoder =new BASE64Encoder();
			/**
			 * 1.Java使用BASE64Encoder 需要添加图片头（"data:" + contentType + ";base64,"），
			 *   其中contentType是文件的内容格式。
			 * 2.Java中在使用BASE64Enconder().encode()会出现字符串换行问题，这是因为RFC 822中规定，
			 *   每72个字符中加一个换行符号，这样会造成在使用base64字符串时出现问题，
			 *   所以我们在使用时要先用replaceAll("[\\s*\t\n\r]", "")解决换行的问题。
			 */
			base64EncoderImg = base64Encoder.encode(imageBytes);
			base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64EncoderImg;
	}

	//MultipartFile图片转base64(去掉图片头,Jpg格式)
	public static String multipartFileToJPGBase64(MultipartFile file){
		if (file == null || file.isEmpty()) {
			throw new RuntimeException("图片不能为空！");
		}
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		String contentType = file.getContentType();
		byte[] imageBytes = null;
		String base64EncoderImg="";
		try {
			imageBytes = file.getBytes();
			InputStream is = new ByteArrayInputStream(imageBytes);
			BufferedImage bufferedImage = ImageIO.read(is);
			BufferedImage newBufferedImage = new BufferedImage(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			// TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,Color.WHITE, null);
			// write to jpeg file
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();



			BASE64Encoder base64Encoder =new BASE64Encoder();
			/**
			 * 1.Java使用BASE64Encoder 需要添加图片头（"data:" + contentType + ";base64,"），
			 *   其中contentType是文件的内容格式。
			 * 2.Java中在使用BASE64Enconder().encode()会出现字符串换行问题，这是因为RFC 822中规定，
			 *   每72个字符中加一个换行符号，这样会造成在使用base64字符串时出现问题，
			 *   所以我们在使用时要先用replaceAll("[\\s*\t\n\r]", "")解决换行的问题。
			 */
			base64EncoderImg = base64Encoder.encode(outputStream.toByteArray());
			base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64EncoderImg;
	}


	/**
	 * 把一个文件转化为byte字节数组。
	 *
	 * @return
	 */
	public static byte[] fileConvertToByteArray(File file) {
		byte[] data = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int len;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}

			data = baos.toByteArray();

			fis.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 删除本地临时文件
	 * @param filePath
	 */
	public static void deleteTempFile(String filePath) {
		if (filePath != null) {
			File del = new File(filePath);
			del.delete();
		}
	}

}
