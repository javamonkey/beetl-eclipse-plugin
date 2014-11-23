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
	public final static int HTML_TAG_UNKONW = 1005;
	
	
	
	
	
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
				nextStartTagToken();				
				continue;
			}
			case S_TAG_ATTR:{
				nextAttributeToken();
			
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
				if(this.forwardMatch('/')){
					this.status = S_TAG_END;
				}else{
					this.status = S_TAG_START;
				}
				
				break ;
			}else{
				source.consume();
			}
		}
		
		int end = source.pos();
		
		BeetlToken t = createToken(start,end,HTML_TAG_TEXT);
		list.add(t);
		
		
	}
	
	
	
	public void nextStartTagToken(){
		
		int c ;
		int start = source.pos();
		while((c=source.get())!=source.EOF){
			if(c==' '||c=='\t'){				
				this.status = S_TAG_ATTR;
				break ;
			}else if(c=='>'){
				this.status = S_TEXT;
				source.consume();
				break;
			}else if(c=='/'&&this.forwardMatch('>')){
				this.status = S_TEXT;
				source.consume(2);
				break;
			}
			else {
				source.consume();
			}
		}
		
		int end = source.pos();
		BeetlToken t = createToken(start, end, HTML_TAG_START);
		list.add(t);
		return ;
		
	}
	
public void nextEndTagToken(){
		
		int c ;
		int start = 0;
		while((c=source.get())!=source.EOF){
			if(c==' '||c=='\t'){				
				this.status = S_TAG_ATTR;
				break ;
			}else if(c=='>'){
				this.status = S_TEXT;
				source.consume();
				break;
			}else if(c=='/'&&this.forwardMatch('>')){
				this.status = S_TEXT;
				source.consume(2);
				break;
			}
			else {
				source.consume();
			}
		}
		
		int end = source.pos();
		BeetlToken t = createToken(start, end, HTML_TAG_START);
		list.add(t);
		return ;
		
	}
	
	
	
	public void nextAttributeToken(){
		this.consumeWS();
		int c ;
		int start = 0;
		while((c=source.get())!=source.EOF){
			if(c==' '||c=='\t'){				
				this.status = S_TAG_ATTR;
				break ;
			}else if(c=='>'){
				this.status = S_TEXT;
				source.consume();
				break;
			}else if(c=='/'&&this.forwardMatch('>')){
				this.status = S_TEXT;
				source.consume(2);
				break;
			}
			else {
				source.consume();
			}
		}
		
		int end = source.pos();
		BeetlToken t = createToken(start, end, HTML_TAG_START);
		list.add(t);
		return ;
	}
	
	public BeetlToken idToken(){
		int c ;		
		int start = source.pos();
		while((c=source.get())!=source.EOF){
			if(c==' '||c=='\t'){
				break ;
			}else if((c>'a'&&c<='z')||c>='A'||c<='Z'){
				source.consume();
				continue;
				
			}else if(c>='0'&&c<='9'){
				source.consume();
				continue;
			}else if(c=='_'||c=='$'){
				source.consume();
				continue;
			}
			else {
				break;
			}
		}
		
		int end = source.pos();
		BeetlToken t = createToken(start, end, HTML_TAG_UNKONW);	
		return t;
		
	}
	
	private BeetlToken stringToken() {
		//第一个引号
		int c = source.get();
		source.consume();
		int find = c;
		
		while ((c = source.get()) != source.EOF) {
			if (c == find) {
				source.consume();
				if (!source.hasEscape()) {
					// 结束
					createToken
					
				}
			}  else {
				source.consume();
			}
		}

		BeetlToken error = this.getErrorToken(t.start, source.pos());
		return error;

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

}
