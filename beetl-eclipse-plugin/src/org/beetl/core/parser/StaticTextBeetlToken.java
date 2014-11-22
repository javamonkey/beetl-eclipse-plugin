package org.beetl.core.parser;

import java.util.LinkedList;
import java.util.List;

public class StaticTextBeetlToken extends BeetlToken {
	StaticTextBeetlToken last = null;
	List<BeetlToken> list = new LinkedList<BeetlToken>();
		public StaticTextBeetlToken(int type, String text,StaticTextBeetlToken lastTextToken) {
		super(type,text);
		this.last =lastTextToken;
	}
	
	public void parseTextAsHtml(){
		
	}
	
	
}
