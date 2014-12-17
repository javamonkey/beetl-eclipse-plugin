package org.beetl.editors;

import org.beetl.core.parser.BeetlLexer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;

public class ColorToken {
	public static Token statement =  new Token(new TextAttribute(ColorManager.instance()
				.getColor(SyntaxColorConstants.STATEMENT),null,SWT.BOLD,null));

	public static Token delimeter = new Token(new TextAttribute(ColorManager.instance()
			.getColor(SyntaxColorConstants.ST),null,SWT.BOLD));
	
	public static Token ph =  new Token(new TextAttribute(ColorManager.instance()
			.getColor(SyntaxColorConstants.HOLDER),null,SWT.BOLD));
	
	public static Token error =  new Token(new TextAttribute(ColorManager.instance()
			.getColor(SyntaxColorConstants.ERROR)));
	
	public static Token defaultToken = new Token(new TextAttribute(ColorManager.instance()
			.getColor(SyntaxColorConstants.DEFAULT)));
	
	public static Token string = new Token(new TextAttribute(ColorManager.instance()
			.getColor(SyntaxColorConstants.STRING)));
	
	public static Token comment = new Token(new TextAttribute(ColorManager.instance()
			.getColor(SyntaxColorConstants.COMMENT)));
	
	public static Token getColorTokenByType(int type){
		
		switch(type){
			case BeetlLexer.TEXT_TT:
				return defaultToken;
			case BeetlLexer.VAR_TT:
			case BeetlLexer.IF_TT:
			case BeetlLexer.ELSE_TT:
			case BeetlLexer.FOR_TT:
			case BeetlLexer.IN_TT:
			case BeetlLexer.CONTINUE_TT:
			case BeetlLexer.BREAK_TT:
			case BeetlLexer.RETURN_TT:
			case BeetlLexer.WHILE_TT:
			case BeetlLexer.SELECT_TT:
			case BeetlLexer.DIRECTIVE_G_TT:
			case BeetlLexer.DIRECTIVE_L_TT:
			case BeetlLexer.CASE_TT:
			case BeetlLexer.DEFAULT_TT:
			case BeetlLexer.TRY_TT:
			case BeetlLexer.CATCH_TT:
			case BeetlLexer.TRUE_TT:
			case BeetlLexer.FALSE_TT:
			case BeetlLexer.NULL_TT: return statement;
			case BeetlLexer.STRING_TT: return string;
			case BeetlLexer.ST_SS_TT: 
			case BeetlLexer.ST_SE_TT: return delimeter;
			case BeetlLexer.PH_SE_TT: 
			case BeetlLexer.PH_SS_TT: return ph;
			case BeetlLexer.ERROR_TT:return error;
			case BeetlLexer.SINGLE_LINE_COMMENT_TOKEN_TT:
			case BeetlLexer.MUTIPLE_LINE_COMMENT_TOKEN_TT: return  comment;
					
			
			default:return defaultToken;
		}
					
		
	}
	
}
