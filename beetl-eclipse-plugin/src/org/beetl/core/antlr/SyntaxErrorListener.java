package org.beetl.core.antlr;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;


public class SyntaxErrorListener extends BaseErrorListener
{
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e)
	{

		BeetlException be = new BeetlException(BeetlException.TOKEN_ERROR);
		be.token = new GrammarToken(msg, line, charPositionInLine);
		throw be;

	}
}
