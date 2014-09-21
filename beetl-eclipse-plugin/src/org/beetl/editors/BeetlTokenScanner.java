package org.beetl.editors;

import java.util.Iterator;

import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

public class BeetlTokenScanner implements ITokenScanner {

	EclipseTokenSource source = null;
	Iterator<BeetlToken> it = null;
	BeetlToken current = null;
	@Override
	public int getTokenLength() {
		// TODO Auto-generated method stub
		return current.start-current.end;
	}

	@Override
	public int getTokenOffset() {
		// TODO Auto-generated method stub
		return current.start;
	}

	@Override
	public IToken nextToken() {
		if(it.hasNext()){
			current = it.next();
			return getTokenByBeetlToken(current);
		}else{
			current = null;
			return Token.EOF;
		}
	
		//return getTokenByBeetlToken(current);
		
		
	}

	@Override
	public void setRange(IDocument arg0, int arg1, int arg2) {
		String text = arg0.get();
		String content = text.substring(arg1,arg1+arg2);
		source = new EclipseTokenSource();
		source.parse(content);
		it = source.tokens.iterator();
	}
	
	private IToken getTokenByBeetlToken( BeetlToken t){
		
		return new Token(new TextAttribute(ColorManager.instance().getColor(SyntaxColorConstants.STATIC_TEXT)));
		//return new ViewToken(t);
		
	
	}
	


}
