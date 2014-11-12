package org.beetl.editors;

import java.util.Iterator;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;

public class BeetlTokenScanner implements ITokenScanner {

	BeetlTokenSource source = null;
	Iterator<BeetlToken> it = null;
	BeetlToken current = null;
	String type = null;
	int offset = 0;

	public BeetlTokenScanner(String type) {
		this.type = type;
	}

	@Override
	public int getTokenLength() {
		// TODO Auto-generated method stub
		return current.end - current.start;
	}

	@Override
	public int getTokenOffset() {
		// TODO Auto-generated method stub
		return offset + current.start;
	}

	@Override
	public IToken nextToken() {
		if (it.hasNext()) {
			current = it.next();
			return getTokenByBeetlToken(current);
		} else {
			current = null;
			return Token.EOF;
		}

		// return getTokenByBeetlToken(current);

	}

	@Override
	public void setRange(IDocument arg0, int arg1, int arg2) {
		String text = arg0.get();
		String content = text.substring(arg1, arg1 + arg2);
		offset = arg1;
		source = new BeetlTokenSource(this.type);
		source.parse(content);
		System.out.println("scan:" + content);
		it = source.tokens.iterator();
		System.out.println("tokens:" + source.tokens);
	}

	private IToken getTokenByBeetlToken(BeetlToken t) {
		if (t.type == BeetlLexer.TEXT_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATIC_TEXT)));

		} else if (t.type == BeetlLexer.STRING_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STRING)));

		} else if (t.type == BeetlLexer.VAR_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}else if (t.type == BeetlLexer.IF_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}else if (t.type == BeetlLexer.ELSE_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		} else if (t.type == BeetlLexer.FOR_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}else if (t.type == BeetlLexer.IN_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}else if (t.type == BeetlLexer.CONTINUE_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}else if (t.type == BeetlLexer.BREAK_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}else if (t.type == BeetlLexer.RETURN_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

		}
		else if (t.type == BeetlLexer.ST_SS_TT
				|| t.type == BeetlLexer.ST_SE_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.ST)));

		} else if (t.type == BeetlLexer.PH_SE_TT
				|| t.type == BeetlLexer.PH_SS_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.HOLDER)));

		} else if (t.type == BeetlLexer.ERROR_TT) {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.ERROR)));
		} else {
			return new Token(new TextAttribute(ColorManager.instance()
					.getColor(SyntaxColorConstants.DEFAULT)));

		}
		// return new ViewToken(t);

	}

}
