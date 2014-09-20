package org.beetl.editors;

import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.rules.Token;

public class ViewToken extends Token {
	BeetlToken token = null;
	public ViewToken(BeetlToken token){
		super(token==null?null:token.getText());
		this.token = token;
	}
	
	@Override
	public Object getData() {
		return token.getText();
	}

	@Override
	public boolean isEOF() {
		return token==null;
	}

	@Override
	public boolean isOther() {
		
		return true;
	}

	@Override
	public boolean isUndefined() {
	
		return false;
	}

	@Override
	public boolean isWhitespace() {
		
		return false;
	}

}
