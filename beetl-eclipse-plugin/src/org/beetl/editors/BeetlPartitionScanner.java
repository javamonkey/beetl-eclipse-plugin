package org.beetl.editors;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.core.parser.Source;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class BeetlPartitionScanner implements IPartitionTokenScanner {

	public static String PLACE_HOLDER_PART =  "PLACE_HOLDER_PART";
	public static String STATEMENT_PART =  "STATEMENT_PART";	
	public static String STATIC_TEXT_PART =  "STATIC_TEXT_PART";
	static Token holderToken = new Token(PLACE_HOLDER_PART);
	static Token textToken = new Token(STATIC_TEXT_PART);
	static Token statementToken = new Token(STATEMENT_PART);
	
	Source source = null;
	LexerDelimiter ld = new LexerDelimiter("${", "}", "<%", "%>");
	BeetlLexer lexer = null;
	int i = 0;
	String contentType = null;
	int offset ;
	int partionOffset ;
	String content = null;
	int length = 0;
	BeetlToken lastToken = new BeetlToken();;
	public BeetlPartitionScanner(){
		lastToken.type = -2 ;
	}
	
	@Override
	public int getTokenLength() {
		return length;
	}

	@Override
	public int getTokenOffset() {
		return offset;
	}

	@Override
	public IToken nextToken() {
		
		BeetlToken token = null;
		while((token=lexer.nextToken())!=null){
			if(token.getType()==BeetlLexer.TEXT_TT){
				offset = token.start;
				length = token.end-token.start;
				this.lastToken = token;
				return textToken;
			}else if(token.getType()==BeetlLexer.ST_SS_TT){
				offset = token.start;
				while((token=lexer.nextToken())!=null){
					if(token.getType()==BeetlLexer.ST_SE_TT){
						length = token.end-lastToken.end;
						this.lastToken = token;
						return statementToken;
					}
				}
				return Token.EOF;
			}else if(token.getType()==BeetlLexer.PH_SS_TT){
				offset = token.start;
				while((token=lexer.nextToken())!=null){
					if(token.getType()==BeetlLexer.PH_SE_TT){
						length = token.end-lastToken.end;
						this.lastToken = token;
						return this.holderToken;
					}
				}
				return Token.EOF;
				
			}else{
				return Token.EOF;
			}
			
			
		}
		return Token.EOF;
		
	}
	
	

	@Override
	public void setRange(IDocument document, int arg1, int arg2) {
		String text = document.get();
		content = text.substring(arg1,arg1+arg2);		
		source = new Source(content);		
		lexer = new BeetlLexer(source,ld);

	}

	@Override
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
		this.contentType= contentType;
		this.partionOffset= partitionOffset;
		if (partitionOffset > -1) {
			int delta= offset - partitionOffset;
			if (delta > 0) {
				setRange(document, partitionOffset, length + delta);
				this.offset = offset;
				return;
			}
		}
		setRange(document, offset, length);
	}

}
