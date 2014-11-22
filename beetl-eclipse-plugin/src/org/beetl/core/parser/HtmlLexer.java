package org.beetl.core.parser;

import java.util.LinkedList;
import java.util.List;

public class HtmlLexer extends BasicLexer {
	//输入源
	Source source = null;
	//解析中的状态
	LexerState state = null;
	/**
	 * 上一次解析状态，有可能延续到本次，如<html name="${a}" >上一次是属性value
	 * 因此本次也是属性value
	 */
	
	int lastHtmlType;
	int status = 0;
	List<BeetlToken> list = new LinkedList<BeetlToken>();
	
	int i = 0;
	
	int pos = 0;
	
	/*解析中的状态*/
	public final static int S_TEXT = 0;
	public final static int S_TAG_START = 1;
	public final static int S_TAG_ATTR = 2;	
	public final static int S_TAG_END = 3;
	
	
	
	/*html 标签 type*/
	public final static int HTML_TAG_START = 1000;
	public final static int HTML_TAG_END = 1001;
	public final static int HTML_TAG_ATTR_NAME = 1002;
	public final static int HTML_TAG_ATTR_VALUE = 1003;
	public final static int HTML_TAG_TEXT = 1004;
	public final static int HTML_TAG_ERROR = 1005;
	
	
	
	
	
	public HtmlLexer(String str,int pos,int lastLine,int lastCol,int lastHtmlType){
		source = new Source(str);
		state = new LexerState();
		state.col = lastCol;
		state.line = lastLine;		
		source.setState(state);
		this.lastHtmlType = lastHtmlType;
		this.pos = pos;
	}
	
	public void parse(){
		if(lastHtmlType==0){
			status = S_TAG_START;
		}else{
			status = lastHtmlType ;
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
	
	public void nextTextToken(){
		int c ;
		int start = source.pos();
		while((c=source.get())!=source.EOF){
			if(c=='<'){
				this.status = S_TAG_START;
				break ;
			}else{
				source.consume();
			}
		}
		
		int end = source.pos();
		
		
		
	}
	
	public BeetlToken createToken(int start,int end,int type){
		BeetlToken t = new BeetlToken();
		t.type = type;
		t.text = source.getRange(start, end);
		t.col = state.col;
		t.line = state.line;
		t.channel = 2;
		t.start = start+this.pos;
		t.end = end+this.pos;
		return t;
	}
	
	public void nextTagToken(){
		
		this.consumeWS();
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
