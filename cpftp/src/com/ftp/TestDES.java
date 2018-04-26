package com.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

public class TestDES {
	Key key;

	public TestDES(String str) {
		getKey(str);// 生成密匙
	}

	/**
	 * 根据参数生成KEY
	 */
	public void getKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			// 防止linux下 随机生成key
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(strKey.getBytes());

			_generator.init(56, secureRandom);
			this.key = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
		}
	}

	/**
	 * 文件file进行加密并保存目标文件destFile中
	 * 
	 * @param file
	 *            要加密的文件 如c:/test/srcFile.txt
	 * @param destFile
	 *            加密后存放的文件名 如c:/加密后文件.txt
	 */
	public void encrypt(String file, String destFile) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		// cipher.init(Cipher.ENCRYPT_MODE, getKey());
		cipher.init(Cipher.ENCRYPT_MODE, this.key);
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(destFile);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
	}

	/**
	 * 文件采用DES算法解密文件
	 * 
	 * @param file
	 *            已加密的文件 如c:/加密后文件.txt * @param destFile 解密后存放的文件名 如c:/
	 *            test/解密后文件.txt
	 */
	public void decrypt(String file, String dest) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, this.key);
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(dest);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			System.out.println();
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}

	public static void main(String[] args) throws Exception {

		// ResourceBundle resource =
		// ResourceBundle.getBundle("/home/ftp.properties");//test为属性文件名，放在包com.mmq下，如果是放在src下，直接用test即可

		Properties prop = new Properties();
		// 读取属性文件a.properties
		InputStream in = new BufferedInputStream(new FileInputStream("ftp.properties"));
		// InputStream in =
		// Object.class.getResourceAsStream("/home/ftp.properties");
		prop.load(in); /// 加载属性列表
		in.close();

		TestDES td = new TestDES(prop.getProperty("key"));
		// td.encrypt("e:/r.txt", "e:/r加密.txt"); // 加密
		// td.decrypt("e:/r加密.txt", "e:/r1.txt"); // 解密
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());

		String sourcePlace = prop.getProperty("sourcePlace");
		String savePlace = prop.getProperty("savePlace");

		// String sourcePlace = "e:/test/";
		// String savePlace = "e:/test1/"+sdf.format(new Date());
		//
		File file = new File(sourcePlace);
		String[] tempList = file.list();
//		
//		File dir = new File(savePlace);
//		if (dir.exists()) {// 判断目录是否存在
//			System.out.println("创建目录失败，目标目录已存在！");
//		}
//		if (!savePlace.endsWith(File.separator)) {// 结尾是否以"/"结束
//			savePlace = savePlace + File.separator;
//		}
//		if (dir.mkdirs()) {// 创建目标目录
//			System.out.println("创建目录成功！" + savePlace);
//		} else {
//			System.out.println("创建目录失败！");
//		}
	
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			String path = sourcePlace + tempList[i];
			temp = new File(path);
			if (temp.getName().contains(date)) {// 如果存在这个文件
				System.out.println(temp.getName());
				td.decrypt(sourcePlace + temp.getName(), savePlace + temp.getName()); // 解密
			}

		}
		// td.encrypt(savePlace+"encrypt.txt",savePlace+"decrypt.txt"); // 加密
//		 @SuppressWarnings("unused")
//		 Thread thread = new Thread();
//		 Thread.sleep(100000);
	}
}