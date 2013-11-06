package com.music.factory.model.xml;

import java.io.InputStream;
import java.util.ArrayList;

import com.music.mp3player.Music;


/**�����ĳ�����
 * @param <E>*/
public abstract class AbstractParse{

	public void parseXml(InputStream xmlContent, Music mp3Info) {
		
	}
	
	public ArrayList<Music> parseXML(InputStream xmlContent) {
		return null;
	};
	
	/**��������а����������ַ�*/	 
	public static String handerName(String name) {
		while(name.indexOf("<em>") != -1) {
			name = name.replace("<em>", "").replace("</em>", "");
		}		
		if(name.indexOf("��") != -1){
			name = name.substring(0, name.indexOf("��"));
		}
		while(name.indexOf(" - ") != -1){
			name = name.substring(0, name.indexOf(" - "));
		}
		if(name.indexOf(":") != -1){
			name = name.substring(0, name.indexOf(":"));
		}
		if(name.indexOf("(") != -1){
			name = name.substring(0, name.indexOf("("));
		}		
		return name;		
	}
}
