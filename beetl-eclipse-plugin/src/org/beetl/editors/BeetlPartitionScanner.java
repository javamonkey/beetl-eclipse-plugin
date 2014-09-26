package org.beetl.editors;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.core.parser.LexerState;
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
		int c = source.get();
		if(c==Source.EOF){
			return Token.EOF;
		}
		
		if (c != ld.ps[0] ) {
		  
		   return  consumeText();
			
		} else  if(source.isMatch(ld.ps)){

			if (source.hasEscape()) {
				 return  consumeText();
			} else {
				return holderText();
			}

		}else{
			throw new RuntimeException(c+"");
		}
		
	}
	
	private Token consumeText(){
		  this.offset = source.pos();
		  int c ;
		   while((c=source.get())!=Source.EOF){
			   if(c!=ld.ps[0]&&c!=ld.ss[0]){
				   source.consume();
				   continue;
			   }else{
				     if(c==ld.ps[0]&&source.isMatch(ld.ps)&&!source.hasEscape()){
				    	 break ;
					}else if(c==ld.ss[0]&&source.isMatch(ld.ss)&&!source.hasEscape()){
				    	 break ;
					}
				     else{
						 source.consume();
						 continue;
					}
			   }
		   }
			this.length = source.pos()-offset;
			return textToken;
	}
	
	private Token holderText(){
		  this.offset = source.pos();
		  int c ;
		   while((c=source.get())!=Source.EOF){
			   if(c!=ld.pe[0]){
				   source.consume();
				   continue;
			   }else{
				     if(source.isMatch(ld.pe)&&!source.hasEscape()){
				    	 source.consume(ld.pe.length);
				    	 break ;
					}else{
						 source.consume();
						 continue;
					}
			   }
		   }
			this.length = source.pos()-offset;
			return holderToken;
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
