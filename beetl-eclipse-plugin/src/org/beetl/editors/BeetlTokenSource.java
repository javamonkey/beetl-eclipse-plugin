package org.beetl.editors;

import java.util.LinkedList;
import java.util.List;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.core.parser.LexerState;
import org.beetl.core.parser.Source;

public class BeetlTokenSource {
	String content;
	List<BeetlToken> tokens = new LinkedList<BeetlToken>();
	LexerDelimiter ld = new LexerDelimiter("${", "}", "<%", "%>");
	int type = 0;

	public BeetlTokenSource(String type) {
		if (type == BeetlPartitionScanner.PLACE_HOLDER_PART) {
			this.type = LexerState.PH_MODEL;
		} else if (type == BeetlPartitionScanner.STATEMENT_PART) {
			this.type = LexerState.ST_MODEL;
		} else {
			this.type = LexerState.STATIC_MODEL;
		}
	}

	public void parse(String content) {
		this.content = content;
		Source source = new Source(content);
		if (type == LexerState.PH_MODEL) {
			if (content.startsWith(ld.strPs)) {
				type = LexerState.PH_START;
			}
		} else if (type == LexerState.ST_MODEL) {
			if (content.startsWith(ld.strSs)) {
				type = LexerState.ST_START;
			}
		}

		BeetlLexer lexer = new BeetlLexer(source, ld, type);
		BeetlToken token = null;// lexer.nextToken();
		while ((token = lexer.nextToken()) != null) {
			tokens.add(token);
		}
	}

	public Object[] find(int p) {
		int i=0;
		for (BeetlToken token : tokens) {
			
			if (p >= token.start) {
				if (p < token.end) {
					return new Object[]{token,i};
				} 
			}
			i++;
		}
		return null;
	}
}
