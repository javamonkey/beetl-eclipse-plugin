package org.beetl.editors;
import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.core.parser.LexerState;
import org.beetl.core.parser.Source;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class BeetlPartitionScanner implements IPartitionTokenScanner {

	public static String PLACE_HOLDER_PART = "PLACE_HOLDER_PART";
	public static String STATEMENT_PART = "STATEMENT_PART";
	public static String STATIC_TEXT_PART = "STATIC_TEXT_PART";
	public static String COMMENT_PART = "COMMENT_TEXT_PART";

	static Token HolderToken = new Token(PLACE_HOLDER_PART);
	static Token TextToken = new Token(STATIC_TEXT_PART);
	static Token StatementToken = new Token(STATEMENT_PART);
	static Token CommentToken = new Token(COMMENT_PART);

	Source source = null;
	LexerDelimiter ld = null;
	BeetlLexer lexer = null;
	int i = 0;
	String contentType = null;
	int offset;
	int partionOffset;
	String content = null;
	int length = 0;
	BeetlToken lastToken = new BeetlToken();;
	BeetlToken commToken = null;;
	int index = 0;
	
	
	BeetlEclipseEditor editor = null;
	public BeetlPartitionScanner(BeetlEclipseEditor editor  ){
		this.editor = editor;
	}
	@Override
	public int getTokenLength() {
		return length;
	}

	@Override
	public int getTokenOffset() {
		return partionOffset + offset;
	}

	@Override
	public IToken nextToken() {
		if(commToken!=null){
			offset = commToken.start;
			length = commToken.end - commToken.start;
			this.lastToken = commToken;
			commToken  = null;
			debug(CommentToken);
			return  CommentToken;
		}
		BeetlToken token = lexer.nextToken();
		if(token == null) return Token.EOF;
		
		if (lexer.state.model==LexerState.ST_MODEL) {
			offset = token.start;
			length = this.ld.strSs.length();
			while ((token = lexer.nextToken()) != null) {
				
				if (token.getType() == BeetlLexer.ST_SE_TT) {
					length = token.end - lastToken.end;
					break;
				}else if(token.getType()==BeetlLexer.MUTIPLE_LINE_COMMENT_TOKEN_TT){
					commToken = token;
					break ;
				}else{
					length = token.end - lastToken.end;
					
				}
			}
			this.lastToken = token;		
			debug(StatementToken);
			return StatementToken;
		} else if (lexer.state.model==LexerState.PH_MODEL) {
			offset = token.start;
			length = this.ld.strPs.length();
			while ((token = lexer.nextToken()) != null) {
				length = token.end - lastToken.end;
				if (token.getType() == BeetlLexer.PH_SE_TT) {
					break;
				}
			}
			
			
			this.lastToken = token;
			debug(HolderToken);
			return this.HolderToken;

		} else if (lexer.state.model==LexerState.STATIC_MODEL) {
			offset = token.start;
			length = token.end - token.start;
			this.lastToken = token;	
			debug(TextToken);
			return TextToken;
		} else {
			offset = token.start;
			length = token.end - token.start;
			this.lastToken = token;	
			debug(TextToken);
			return TextToken;
		}
		
		
		
		

	}


	private void debug(Token t) {
		/*
		if(index==0){
			System.out.println("==================");
		}
		
		System.out.println(++index+":offset=" + (this.offset + this.partionOffset)
				+ " len=" + this.length + " type=" + t.getData()+" contentType="+contentType);		
		System.out.println();
		*/
	}

	@Override
	public void setRange(IDocument document, int arg1, int arg2) {
		String text = document.get();
		content = text.substring(arg1, arg1 + arg2);
		source = new Source(content);
		if(ld==null){
			String[] de = ProjectUtil.getProjectDelimter(this.editor);
			ld =  new LexerDelimiter(de[2], de[3], de[0], de[1]);
		}
		lexer = new BeetlLexer(source, ld);
		lastToken = new BeetlToken();
		this.length =0 ;
		this.offset = 0;
//		System.out.println("part:" + content);

	}

	@Override
	public void setPartialRange(IDocument document, int offset, int length,
			String contentType, int partitionOffset) {
		this.contentType = contentType;
		if (partitionOffset == -1)
			partitionOffset = 0;
		else {
			this.partionOffset = partitionOffset;
		}

		setRange(document, partitionOffset, length+(offset-partitionOffset));
	}

	public void testInit(String str) {
		this.content = str;
		source = new Source(content);
		lexer = new BeetlLexer(source, ld);
		lastToken = new BeetlToken();
	}

	public static void main(String[] args) {
//		BeetlPartitionScanner s = new BeetlPartitionScanner(n);
//		String test = "abc${";
//		s.testInit(test);
//		Token token = null;
//		while ((token = (Token) s.nextToken()) != Token.EOF) {
//			System.out.println(s.offset + ":" + s.length);
//		}

	}

}
