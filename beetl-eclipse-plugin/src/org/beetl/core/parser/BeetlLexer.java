package org.beetl.core.parser;

/**
 * 生成token流
 * 
 * @author lijiazhi
 * 
 */
public class BeetlLexer {

	// 错误token
	public final static int ERROR_TT = -1;

	// 静态文本
	public final static int TEXT_TT = 0;;
	// 站位符开始
	public final static int PH_SS_TT = 1;;
	// 占位符结束
	public final static int PH_SE_TT = 2;
	// ID
	public final static int ID_TT = 3;
	// .
	public final static int PERIOD_TT = 4;

	public final static int INTEGER_TT = 5;
	public final static int FLOAT_TT = 6;

	public final static int INCREASE_TT = 7;
	public final static int DECREASE_TT = 8;

	public final static int ADD_TT = 9;
	public final static int MIN_TT = 10;

	public final static int LEFT_PAR_TT = 11;
	public final static int RIGHT_PAR_TT = 12;

	public final static int STRING_TT = 13;

	// 控制语句定界符
	public final static int ST_SS_TT = 14;
	public final static int ST_SE_TT = 15;

	public final static int WS_TT = 16;
	public final static int VAR_TT = 17;
	public final static int IF_TT = 18;

	public final static int LEFT_BRACE_TT = 19;
	public final static int RIGHT_BRACE_TT = 20;

	public final static int EQUALS_TT = 21;
	public final static int ASSIN_TT = 22;
	public final static int SEMI_TT = 23;
	public final static int CR_TT = 24;
	

	public static String[] tokens = new String[] { "TEXT", "PS", "PE", "ID",
			".", "INTERGER", "FLOAT", "++", "--", "+", "-", "(", ")", "STRING",
			"SS", "SE", "WS", "var", "if", "{", "}", "==", "=", ";" ,"CR"};

	LexerState state = null;
	Source source = null;
	LexerDelimiter ld;

	public BeetlLexer(Source source, LexerDelimiter ld) {
		state = new LexerState();
		this.source = source;
		this.ld = ld;
		this.source.setState(state);
		parseFirst();
	}

	public BeetlLexer(Source source, LexerDelimiter ld, int model) {
		state = new LexerState();
		this.source = source;
		this.ld = ld;
		this.source.setState(state);
		state.model = model;

	}

	private void parseFirst() {
		int c = source.get();
		if (c != ld.ps[0] && c != ld.ss[0]) {
			state.model = LexerState.STATIC_MODEL;
		} else if (source.isMatch(ld.ps) && !source.hasEscape()) {

			state.model = LexerState.PH_START;
		} else if (source.isMatch(ld.ss) && !source.hasEscape()) {

			state.model = LexerState.ST_START;
		} else {
			state.model = LexerState.STATIC_MODEL;
		}
	}

	public BeetlToken nextToken() {
		switch (state.model) {
		case LexerState.STATIC_MODEL:
			return textModel();
		case LexerState.PH_MODEL:
			return statementModel();
		case LexerState.ST_MODEL:
			return statementModel();
		case LexerState.PH_START:
			return placeHolderStartToken();
		case LexerState.ST_START:
			return statmentStartToken();
		}

		return null;
	}

	private BeetlToken statementModel() {
		int c;

		// 判断是否是结束符号
		while ((c = source.get()) != Source.EOF) {
			if (c == this.ld.se[0] && state.model == LexerState.ST_MODEL
					&& source.isMatch(this.ld.se)) {

				BeetlToken t = this.getToken(ld.se.length, ST_SE_TT,
						this.ld.strSe);
				state.model = LexerState.STATIC_MODEL;
				return t;

			} else if (c == this.ld.pe[0] && state.model == LexerState.PH_MODEL
					&& source.isMatch(this.ld.pe)) {
				// todo暂时不考虑escape
				BeetlToken t = this.getToken(ld.pe.length, this.PH_SE_TT,
						this.ld.strPe);
				state.model = LexerState.STATIC_MODEL;
				return t;

			} else if (c == 'v') {
				if (this.forwardMatchRange('a', 'r')) {
					return this.getCharToken(3, VAR_TT);

				} else {
					return idToken();

				}
			} else if (c == 'i') {
				if (this.forwardMatch('f')) {

					return this.getCharToken(2, IF_TT);
				} else {

					return idToken();

				}
			} else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {

				return idToken();

			} else if (c == '{') {
				return this.getCharToken(1, this.LEFT_BRACE_TT);
			} else if (c == '}') {
				return this.getCharToken(1, this.RIGHT_BRACE_TT);
			} else if (c == '(') {
				return this.getCharToken(1, this.LEFT_PAR_TT);
			} else if (c == ')') {
				return this.getCharToken(1, RIGHT_PAR_TT);
			} else if (c == ' ') {
				return this.consumeWS();
			} else if (c == '\r' || c == '\n') {
				return getCRToken(c);
			} else if (c == ';') {
				return this.getCharToken(1, SEMI_TT);

			} else if (c == '=') {
				if (this.forwardMatch('=')) {
					return this.getCharToken(2, this.EQUALS_TT);
				} else {
					return this.getCharToken(1, this.ASSIN_TT);
				}

			}
			if (c > '0' && c < '9') {

				return numberToken();

			} else if (c == '\'' || c == '\"') {

				return stringToken();

			} else if (c == '.') {
				if (this.forwardMatchRange('0', '9')) {

					return numberToken();

				} else {
					return this.getCharToken(1, this.PERIOD_TT);

				}
			} else if (c == '+') {
				if (this.forwardMatch('+')) {
					return this.getCharToken(2, this.INCREASE_TT);

				} else {
					return this.getCharToken(1, this.ADD_TT);

				}

			} else if (c == '(') {
				return this.getCharToken(1, this.LEFT_PAR_TT);

			} else if (c == ')') {
				return this.getCharToken(1, this.RIGHT_PAR_TT);

			} else if (c == '=') {
				if (this.forwardMatch('=')) {
					return this.getCharToken(2, this.ASSIN_TT);
				} else {
					return this.getCharToken(1, this.EQUALS_TT);
				}
			} else {
				return this.getSingleErrorToken("Error:" + c);
			}
		}

		return null;

	}

	private BeetlToken textModel() {
		int c;
		int start = source.pos();
		int col = state.col;
		int line = state.line;
		while ((c = source.get()) != source.EOF) {
			if (c != ld.ps[0] && c != ld.ss[0]) {

				if (c == '\r' || c == '\n') {
					consumeMoreCR(c);
				} else {
					source.consume();
				}
			} else {
				if (c == ld.ps[0] && source.isMatch(ld.ps)) {

					if (source.hasEscape()) {
						source.consume();
						continue;
					} else {
						state.model = LexerState.PH_START;
						break ;
					}

				} else if (c == ld.ss[0] && source.isMatch(ld.ss)) {
					if (source.hasEscape()) {
						source.consume();
						continue;
					} else {
						state.model = LexerState.ST_START;
						break;
					}

				} else {
					source.consume();
				}
			}
		}
		if(start==source.pos()){
			if(state.model == LexerState.PH_START){
				return this.placeHolderStartToken();
			}else if(state.model==LexerState.ST_START){
				return this.statmentStartToken();
			}else{
				return null;
			}
		}
		
		return getStaticTextToken(start, source.pos(), col, line);

	}

	private BeetlToken emptyToken() {
		BeetlToken t = new BeetlToken();
		t.start = source.pos();
		t.col = state.col;
		t.line = state.line;
		return t;
	}

	private BeetlToken idToken() {
		BeetlToken t = emptyToken();
		int c;
		while ((c = source.get()) != source.EOF) {
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
				source.consume();
			} else {
				break;
			}
		}
		int end = source.pos();
		t.end = end;
		String text = source.getRange(t.start, t.end);
		t.text = text;
		t.type = this.ID_TT;
		return t;
	}

	private BeetlToken stringToken() {
		BeetlToken t = emptyToken();
		int c = source.get();
		source.consume();
		int find = c;
		while ((c = source.get()) != source.EOF) {
			if (c == find) {
				if (!source.hasEscape()) {
					// 结束
					source.consume();
					int end = source.pos();
					t.end = end;
					String text = source.getRange(t.start, t.end);
					t.text = text;
					t.type = this.STRING_TT;
					return t;
				}
			} else if (c == '\r' || c == '\n') {
				BeetlToken error = this.getErrorToken(t.start, source.pos());
				this.consumeMoreCR(c);
				return error;

			} else {
				source.consume();
			}
		}

		BeetlToken error = this.getErrorToken(t.start, source.pos());
		return error;

	}

	private BeetlToken numberToken() {
		BeetlToken t = emptyToken();
		t.type = this.INTEGER_TT;
		int c;
		boolean period = false;
		while ((c = source.get()) != source.EOF) {
			if (c >= '0' && c <= '9') {
				source.consume();
			} else if (c == 'h' || c == 'H') {
				source.consume();
				break;
			} else if (c == '.') {
				if (period) {

				}
				t.type = this.FLOAT_TT;
				source.consume();
				continue;
			} else {
				break;

			}

		}

		t.end = source.pos();
		t.text = source.getRange(t.start, t.end);
		return t;
	}

	private BeetlToken placeHolderStartToken() {
		state.model = LexerState.PH_MODEL;
		return getToken(this.ld.ps.length, this.PH_SS_TT, this.ld.strPs);
	}

	private BeetlToken statmentStartToken() {
		state.model = LexerState.ST_MODEL;
		return getToken(this.ld.ss.length, this.ST_SS_TT, this.ld.strSs);
	}

	private BeetlToken getCharToken(int len, int type) {
		BeetlToken token = emptyToken();
		source.consume(len);
		int end = source.pos();
		token.end = end;
		token.type = type;
		token.text = tokens[type];

		return token;
	}

	private BeetlToken getToken(int len, int type) {
		BeetlToken token = emptyToken();
		source.consume(len);
		int end = source.pos();
		token.end = end;
		token.type = type;
		token.text = source.getRange(token.start, end);

		return token;
	}

	private BeetlToken getToken(int len, int type, String text) {
		BeetlToken token = emptyToken();
		source.consume(len);
		int end = source.pos();
		token.end = end;
		token.type = type;
		token.text = text;
		return token;
	}

	private BeetlToken getStaticTextToken(int start, int end, int col, int line) {

		String text = source.getRange(start, end);
		BeetlToken token = new BeetlToken(TEXT_TT, text);
		token.col = col;
		token.line = line;
		token.start = start;
		token.end = end;

		return token;

	}

	private BeetlToken getSingleErrorToken(String msg) {
		BeetlToken token = emptyToken();
		source.consume();
		int end = source.pos();
		token.end = end;
		token.type = -1;
		token.text = msg;
		return token;
	}

	private BeetlToken getErrorToken(int start, int end) {
		BeetlToken token = emptyToken();
		token.start = start;
		token.end = end;
		token.type = -1;
		token.text = source.getRange(start, end);
		return token;
	}

	private boolean forwardMatchRange(int start, int end) {
		int c = source.get(1);
		return c >= start && c <= end;
	}

	private boolean forwardMatch(int m) {
		int c = source.get(1);
		return c == m;
	}

	private boolean forwardMatch(int[] cs) {
		int c = source.get(1);
		for (int i : cs) {
			if (c == i) {
				return true;
			}
		}
		return false;
	}

	private BeetlToken getCRToken(int crFirst){
		if(state.cr_len!=0){
			return this.getCharToken(state.cr_len, CR_TT);
		}else{
			int c = source.get(1);
			if (c == source.EOF) {
				state.cr_len = 1;
				
			}else if (crFirst == '\n' && c == '\r') {
				state.cr_len = 2;
				
			} else if (crFirst == '\r' && c == '\n') {
				state.cr_len = 2;
			
			} else {
				state.cr_len = 1;
				
			}
			return this.getCharToken(state.cr_len, CR_TT);
		}
		
		
	}
	private void consumeMoreCR(int crFirst) {

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

	private BeetlToken consumeWS() {
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

	public static void main(String[] args) {
		String template = "<%\nvar a=1;%>\n${1}";
		Source source = new Source(template);
		LexerDelimiter ld = new LexerDelimiter("${", "}", "<%", "%>");

		BeetlLexer lexer = new BeetlLexer(source, ld);
		BeetlToken token = null;// lexer.nextToken();
		while ((token = lexer.nextToken()) != null) {
			System.out.println(token);
		}

	}
}
