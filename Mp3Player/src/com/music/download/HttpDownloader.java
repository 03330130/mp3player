package com.music.download;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.music.utils.FileUtils;


public class HttpDownloader {

	public static String readURL(String url) {
		StringBuilder sb;
		try {
		HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
		conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
		conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String temp = null;
		sb = new StringBuilder();
		while ((temp = br.readLine()) != null)
			sb.append(temp).append("\n");
		br.close();
		conn.disconnect();
		} catch (IOException e) {
			return null;
		}		
		return sb.toString();
	}
	
	/**ר�����ظ�ʵķ���*/
	public void downloadLyricFile(String urlStr, String path, String fileName) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path + fileName), "GBK"));
			bw.write(readURL(urlStr));
			bw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * �ú����������� -1�����������ļ����� 0�����������ļ��ɹ� 1�������ļ��Ѿ�����
	 */
	public int downFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			if (fileUtils.isFileExist(fileName,path)) {
				return 1;
			} else {			
				inputStream = getInputStreamFromUrl(urlStr);				
				File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);				
				if (resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {			
			return -1;
		} finally {
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * ����URL�õ�������
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr){
		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
			urlConn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			inputStream = urlConn.getInputStream();			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}				
		return inputStream;
	}
	
	/**
     * ��һ��������õ���������ַ���
     * ������ʽ
     * @param is ��
     * @return �ַ���
     */
    public String getStringContent(InputStream is) {
        InputStreamReader r = null;
        try {
            StringBuilder sb = new StringBuilder();
            //TODO �����ǹ̶�����ҳ���ݵı���д��GBK,Ӧ���ǿ����õ�
            r = new InputStreamReader(is, "utf-8");
            char[] buffer = new char[1024];
            int length = -1;
            while ((length = r.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, length));
            }
            return sb.toString();
        } catch (Exception ex) {         
            return "";
        } finally {
            try {
                r.close();
            } catch (Exception ex) {                
            }
        }
    }
}

