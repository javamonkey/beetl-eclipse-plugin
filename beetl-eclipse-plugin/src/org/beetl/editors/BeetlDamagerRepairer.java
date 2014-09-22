package org.beetl.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.swt.graphics.Color;

public class BeetlDamagerRepairer extends DefaultDamagerRepairer {

	public BeetlDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
		// TODO Auto-generated constructor stub
	}
	
	protected   TextAttribute getTokenTextAttribute(IToken token) {
		Color color = ColorManager.instance().getColor(SyntaxColorConstants.STATIC_TEXT);
		TextAttribute t = new TextAttribute(color);		
		return t;
		
	}

}
