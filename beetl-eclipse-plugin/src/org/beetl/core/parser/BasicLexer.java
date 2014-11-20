package org.beetl.core.parser;

public class BasicLexer {
	protected Source source;
	protected LexerState state = null;
	public final static int WS_TT = 16;
	
	protected void consumeMoreCR(int crFirst) {

		if (state.cr_len == 1) {
			source.consume();

		} else if (state.cr_len == 2) {
			source.consume(2);
		} else {
			int c = source.get(1);
			if (c == source.EOF) {
				source.consume();
				state.cr_len = 1;
				return;
			} else if (crFirst == '\n' && c == '\r') {
				state.cr_len = 2;
				source.consume(2);
			} else if (crFirst == '\r' && c == '\n') {
				state.cr_len = 2;
				source.consume(2);
			} else {
				state.cr_len = 1;
				source.consume();
			}
		}
		state.addLine();
	}

	protected BeetlToken consumeWS() {
		int c = source.get();
		int start = source.pos();
		while ((c = source.get()) != Source.EOF) {
			if (c == ' ' || c == '\t') {
				source.consume();
				continue;
			} else {
				break;
			}
		}

		BeetlToken token = new BeetlToken();
		token.col = state.col;
		token.start = start;
		token.end = source.pos();
		token.line = state.line;
		token.text = "";
		token.type = WS_TT;
		token.channel = 1;
		return token;

	}
}
