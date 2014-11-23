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
	
	protected boolean forwardMatchRange(int start, int end) {
		int c = source.get(1);
		return c >= start && c <= end;
	}
	protected boolean forwardMatch(int m) {
		int c = source.get(1);
		return c == m;
	}

	protected boolean forwardMatchs(int one,int two) {
		int c = source.get(1);
		if(c!=Source.EOF&&c==one){
			c = source.get(2);
			if(c!=Source.EOF&&c==two){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 匹配多个字符
	 * @param arr
	 * @return
	 */
	protected boolean forwardMatchsMore(int... arr) {
		int c = 0;
		for(int i=0; i < arr.length;i++){
			c = source.get(i+1);
			if(c!=Source.EOF&&c==arr[i]&& i==(arr.length-1)){
				return true;
			}else{
				continue;
			}
			
		}
		return false;
	}
	
	protected boolean forwardMatchsFour(int one,int two,int three) {
		int c = source.get(1);
		if(c!=Source.EOF&&c==one){
			c = source.get(2);
			if(c!=Source.EOF&&c==two){
				c = source.get(3);
				if(c!=Source.EOF&&c==three){
					return true;
				}
			}
		}
		
		return false;
	}

}
