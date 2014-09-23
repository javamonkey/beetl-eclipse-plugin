package org.beetl.editors;

import java.util.LinkedList;
import java.util.List;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.core.parser.Source;

public class BeetlTokenSource {
	String content ;
	List<BeetlToken> tokens  = new LinkedList<BeetlToken>();
	LexerDelimiter ld = new LexerDelimiter("${", "}", "<%", "%>");
	public BeetlTokenSource(){
		
	}
	
	public void parse(String content ){
		this.content = content;
		Source source = new Source(content);
		BeetlLexer lexer = new BeetlLexer(source, ld);
		BeetlToken token = null;// lexer.nextToken();
		while ((token = lexer.nextToken()) != null) {
			tokens.add(token);
		}
	}
	public BeetlToken find(int p){
		for(BeetlToken token:tokens){
			if(p>=token.start){
				if(p<token.end){
					return token;
				}else{
					return null;
				}
			}
		}
		return null;
	}
}
