package com.nonobank.testcase.utils.dll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  

public class Base64Media {

	public static String GetImageStr(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;
		String imgFilePath="";
		File file11 = new File(path);
		imgFilePath = file11.getAbsolutePath();

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}


	public static String readFile(String path){

		File file = new File(path);
		FileReader reader;
		try {
			reader = new FileReader(file);
			int fileLen = (int)file.length();
			char[] chars = new char[fileLen];
			reader.read(chars);
			String txt = String.valueOf(chars);
			System.out.println(txt);
			return txt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String readFile(){
		File file = new File("/Users/user/nono-dataprovider/dataProvider/src/main/java/com/nonobank/dataProvider/config/idCard.txt");
        FileReader reader;
		try {
			reader = new FileReader(file);
			int fileLen = (int)file.length();
	        char[] chars = new char[fileLen];
	        reader.read(chars);
	        String txt = String.valueOf(chars);
	        System.out.println(txt);
	        return txt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

}
