package kepler.jmeter.scriptJar.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class StringTool {

	public static boolean inArray(String value,String[] array){
		boolean in = false;
		for (String s:
			 array) {
			if(in = s.equals(value))
				break;
		}
		return in;
	}

	/**从文件中读取整个字符串
	 * @param path	path
	 * @return	data
	 */
	public static String getStringFromFile(String path) {
		String data = "";
		try {
			InputStream is = new FileInputStream(path);
			byte[] b = new byte[is.available()];
			int i;
			int index = 0;
			while ((i = is.read()) != -1) {
				b[index] = (byte) i;
				index++;
			}
			is.close();
			data = new String(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**length长度的随机INT，作为String返回
	 * @param length	length
	 * @return	String
	 */
	public static String randomInt(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(Integer.toString(new Random().nextInt(10)));
		}
		return sb.toString();
	}

	/**length长度的随机字符a-z，A-Z，0-9三种可能
	 * @param length	length
	 * @return	String
	 */
	public static String randomString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			// 产生0-2个随机数，既与a-z，A-Z，0-9三种可能
			int number = random.nextInt(3);
			long result;
			switch (number) {
			case 0:
				result = Math.round(Math.random() * 25 + 65);
				sb.append(String.valueOf((char) result));
				break;
			case 1:
				result = Math.round(Math.random() * 25 + 97);
				sb.append(String.valueOf((char) result));
				break;
			case 2:
				sb.append(Integer.toString(new Random().nextInt(10)));
				break;
			}
		}
		return sb.toString();
	}
	
	/**随机汉字
	 * @param length	length
	 * @return	String
	 */
	public static String randomChineseString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(randomChineseChar());
		}
		return sb.toString();
	}
	
	private static char randomChineseChar() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }
}
