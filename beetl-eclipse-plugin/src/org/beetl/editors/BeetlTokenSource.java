package org.beetl.editors;

import java.util.LinkedList;
import java.util.List;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.core.parser.LexerState;
import org.beetl.core.parser.Source;

public class BeetlTokenSource {
	String content;
	List<BeetlToken> tokens = new LinkedList<BeetlToken>();
	LexerDelimiter ld = new LexerDelimiter("${", "}", "<%", "%>");
	int type = 0;

	public BeetlTokenSource(String type) {
		if (type == BeetlPartitionScanner.PLACE_HOLDER_PART) {
			this.type = LexerState.PH_MODEL;
		} else if (type == BeetlPartitionScanner.STATEMENT_PART) {
			this.type = LexerState.ST_MODEL;
		} else {
			this.type = LexerState.STATIC_MODEL;
		}
	}

	public void parse(String content) {
		this.content = content;
		Source source = new Source(content);
		if (type == LexerState.PH_MODEL) {
			if (content.startsWith(ld.strPs)) {
				type = LexerState.PH_START;
			}
		} else if (type == LexerState.ST_MODEL) {
			if (content.startsWith(ld.strSs)) {
				type = LexerState.ST_START;
			}
		}

		BeetlLexer lexer = new BeetlLexer(source, ld, type);
		BeetlToken token = null;// lexer.nextToken();
		while ((token = lexer.nextToken()) != null) {
			tokens.add(token);
		}
	}

	public Object[] find(int p) {
		int i=0;
		for (BeetlToken token : tokens) {
			
			if (p <= token.end&&p >= token.start) {
				
				return new Object[]{token,i};				
			}
			i++;
		}
		return null;
	}
	
	
	public BeetlToken findNextStOrHolder(int p){
		
		int step = 0;
		for (int i=0;i<tokens.size();i++) {
			BeetlToken token = tokens.get(i);
			if (step==0 &&p <= token.end&&p >= token.start) {
				step=1;
				continue ;
							
			}
			if(step==1){
				if(token.getType()==BeetlLexer.PH_SS_TT){
					return token;
				}else if(token.getType()==BeetlLexer.ST_SS_TT){
					//如果后面有回车，则定位到下一个token那
					if(i+1<tokens.size()){
						BeetlToken nextToken = tokens.get(i+1);
						return nextToken;
					}else{
						return token;
					}
				}
				
			}
			;
		}
		return null;
		
	}
	
public BeetlToken findPreStOrHolder(int p){
		
		Object[] obj = this.find(p);
		if(obj==null) return null ;
		int max = (Integer)obj[1];
		int status = 0;
		
		for (int i=max-1;i>=0;i--) {
			BeetlToken token = tokens.get(i);
			if(status==0&&token.getType()==BeetlLexer.PH_SE_TT){
				status = 1;
				continue ;
			}else if(status==0&&token.getType()==BeetlLexer.ST_SE_TT){
				status = 2;
				continue ;
			
			}else if(status==1&&token.getType()==BeetlLexer.PH_SS_TT){
				//找到
				return token;
			}else if(status==2&&token.getType()==BeetlLexer.ST_SS_TT){
				//如果后面有回车，则定位到下一个token那
				BeetlToken nextToken = tokens.get(i+1);
				return nextToken;
				
			}
			;
		}
		return null;
		
	}
}
