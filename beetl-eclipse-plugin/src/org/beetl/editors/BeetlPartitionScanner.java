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

	public static String PLACE_HOLDER_PART = "PLACE_HOLDER_PART";
	public static String STATEMENT_PART = "STATEMENT_PART";
	public static String STATIC_TEXT_PART = "STATIC_TEXT_PART";
	static Token holderToken = new Token(PLACE_HOLDER_PART);
	static Token textToken = new Token(STATIC_TEXT_PART);
	static Token statementToken = new Token(STATEMENT_PART);

	Source source = null;
	LexerDelimiter ld = new LexerDelimiter("${", "}", "<%", "%>");
	BeetlLexer lexer = null;
	int i = 0;
	String contentType = null;
	int offset;
	int partionOffset;
	String content = null;
	int length = 0;
	BeetlToken lastToken = new BeetlToken();;

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

		BeetlToken token = null;
		while ((token = lexer.nextToken()) != null) {
			if (token.getType() == BeetlLexer.TEXT_TT) {
				offset = token.start;
				length = token.end - token.start;
				this.lastToken = token;
				debug(textToken);
				return textToken;
			} else if (token.getType() == BeetlLexer.ST_SS_TT) {
				offset = token.start;
				while ((token = lexer.nextToken()) != null) {
					length = token.end - lastToken.end;
					if (token.getType() == BeetlLexer.ST_SE_TT) {
						break;
					}
				}

				this.lastToken = token;
				debug(statementToken);
				return statementToken;
			} else if (token.getType() == BeetlLexer.PH_SS_TT) {
				offset = token.start;
				while ((token = lexer.nextToken()) != null) {
					length = token.end - lastToken.end;
					if (token.getType() == BeetlLexer.PH_SE_TT) {
						break;
					}
				}

				this.lastToken = token;
				debug(holderToken);
				return this.holderToken;

			} else {
				return textToken;
			}

		}
		return Token.EOF;

	}

	private void debug(Token t) {
		System.out.println("offset=" + (this.offset + this.partionOffset)
				+ " len=" + this.length + " type=" + t.getData());
	}

	@Override
	public void setRange(IDocument document, int arg1, int arg2) {
		String text = document.get();
		content = text.substring(arg1, arg1 + arg2);
		source = new Source(content);
		lexer = new BeetlLexer(source, ld);
		lastToken = new BeetlToken();

		System.out.println("part:" + content);

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

		setRange(document, partitionOffset, length);
	}

	public void testInit(String str) {
		this.content = str;
		source = new Source(content);
		lexer = new BeetlLexer(source, ld);
		lastToken = new BeetlToken();
	}

	public static void main(String[] args) {
		BeetlPartitionScanner s = new BeetlPartitionScanner();
		String test = "123";
		s.testInit(test);
		Token token = null;
		while ((token = (Token) s.nextToken()) != Token.EOF) {
			System.out.println(s.offset + ":" + s.length);
		}

	}

}
