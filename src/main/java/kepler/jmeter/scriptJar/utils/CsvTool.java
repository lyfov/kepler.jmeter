package kepler.jmeter.scriptJar.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.tika.io.ClosedInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class CsvTool {

	public static void writeCsv(String[] titles, JSONArray array, String finalPath) {
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			File finalCSVFile = new File(finalPath);
			out = new FileOutputStream(finalCSVFile);
			osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
			// 手动加上BOM标识
			osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
			bw = new BufferedWriter(osw);

			// 写title
			for (String title:titles) {
				bw.append(title).append(",");
			}
			bw.append("\n");
			// 写数据
			for (int i = 0; i < array.length(); i++) {
				// 行
				JSONArray line = array.getJSONArray(i);
				for(int lineIndex=0;lineIndex<line.length();lineIndex++) {
					Object column = line.get(lineIndex);
					if(column instanceof String)
						bw.append((String)column).append(",");
					else if(column instanceof Integer) 
						bw.append(Integer.toString((int) column)).append(",");
					else if(column instanceof Boolean) 
						bw.append(String.valueOf((boolean)column)).append(",");
					else if(column instanceof JSONArray) 
						bw.append("\"").append(array.getJSONArray(i).toString().replaceAll("\"", "\"\"")).append("\",");
					else if(column instanceof JSONObject)
						bw.append("\"").append(array.getJSONArray(i).toString().replaceAll("\"", "\"\"")).append("\",");
				}
				bw.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(bw);
			closeStream(osw);
			closeStream(out);
//			if (bw != null) {
//				try {
//					bw.close();
//					bw = null;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (osw != null) {
//				try {
//					osw.close();
//					osw = null;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (out != null) {
//				try {
//					out.close();
//					out = null;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}

	private static void closeStream(Object stream){
		if (stream != null) {
			try {
				if(stream instanceof FileOutputStream)
					((FileOutputStream) stream).close();
				else if (stream instanceof OutputStreamWriter)
					((OutputStreamWriter) stream).close();
				else if (stream instanceof BufferedWriter)
					((BufferedWriter) stream).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}