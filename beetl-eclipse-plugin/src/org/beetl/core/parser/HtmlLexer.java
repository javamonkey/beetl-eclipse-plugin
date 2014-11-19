package org.beetl.core.parser;

import java.util.LinkedList;
import java.util.List;

public class HtmlLexer {
	String text ;
	int lastType;
	int status = 0;
	List<BeetlToken> list = new LinkedList<BeetlToken>();
	
	int i = 0;
	
	public final static int S_TAG_START = 1;
	public final static int S_TAG_ATTR = 2;	
	public final static int S_TAG_END = 2;
	
	
	public final static int TAG_START = 1000;
	public final static int TAG_END = 1001;
	public final static int TAG_ATTR_NAME = 1002;
	public final static int TAG_ATTR_VALUE = 1003;
	public final static int TAG_TEXT = 1004;
	public final static int ERROR = 1005;
	
	
	
	
	
	public HtmlLexer(String str,int lastType){
		this.text = str;
		this.lastType = lastType;
		
	}
	
	public void parse(){
		if(lastType==0){
			status = S_TAG_START;
		}else{
			status = lastType ;
		}
		
		while(true){
			switch(status){
			case S_TAG_START:{
				nextTagToken();
				status = S_TAG_ATTR; 
				continue;
			}
			case S_TAG_ATTR:{
				nextAttributeToken();
				status = S_TAG_END; 
				continue;
				
			}
			}
		}
	}
	
	public void nextTagToken(){
		int c = 0;
		
		while(i<text.length()){
			c = text.charAt(i);
			i++;
			if(c==' '|| c=='\t' || c=='\r'||c=='\n'){
				continue;
			}else if(c=='<'){
				String tagName = getTagName();
				BeetlToken token = new BeetlToken();
				
			}
		}
	}
	
	
	
	public void nextAttributeToken(){
		
	}
	
	private String getTagName(){
		int pos = i;
		int c = 0;
		while(pos<text.length()){
			 c = text.charAt(pos);
			 pos++;
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
				continue;
			}else if(c==':'){
				continue ;
			}else{
				//结束
				break;
			}
		}
		
		String str =  text.substring(i,pos);
		i = pos;
		return str;
	}
}
