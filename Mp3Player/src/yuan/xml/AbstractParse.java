package yuan.xml;

import java.io.InputStream;
import java.util.List;

import yuan.model.Mp3Info;

/**�����ĳ�����
 * @param <E>*/
public abstract class AbstractParse implements Parse {

	public List<Mp3Info> parseXML(InputStream xmlContent) {
		return null;
	}

	public void parseXml(InputStream xmlContent, Mp3Info mp3Info) {
		
	}
	
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
