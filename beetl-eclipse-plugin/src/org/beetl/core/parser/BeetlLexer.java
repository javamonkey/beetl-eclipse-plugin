package org.beetl.core.parser;

import java.util.HashSet;
import java.util.Set;

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
	
	public final static int ELSE_TT = 25;
	public final static int FOR_TT = 26;
	public final static int IN_TT = 27;
	
	public final static int CONTINUE_TT = 28;
	public final static int BREAK_TT = 29;
	public final static int RETURN_TT = 30;
	
	public final static int ELSEFOR_TT = 31;
	public final static int WHILE_TT = 32;
	public final static int SWITCH_TT = 33;
	public final static int SELECT_TT = 34;
	public final static int DIRECTIVE_L_TT = 35;
	public final static int DIRECTIVE_G_TT = 36;
	public final static int CASE_TT = 37;
	public final static int DEFAULT_TT = 38;
	public final static int TRY_TT = 39;
	public final static int CATCH_TT = 40;
	public final static int LEFT_SQBR_TT = 41;
	public final static int RIGHT_SQBR_TT = 42;
	public final static int VIRTUAL_TT = 43;
	public final static int MUlTIP_TT = 44;
	public final static int DIV_TT = 45;
	public final static int MOD_TT = 46;
	public final static int NOT_EQUAL_TT = 47;
	public final static int LARGE_EQUAL_TT = 48;
	public final static int LARGE_TT = 49;
	public final static int LESS_EQUAL_TT = 50;
	public final static int LESS_TT = 51;
	public final static int NOT_TT = 52;
	public final static int AND_TT = 53;
	public final static int OR_TT = 54;
	public final static int QUESTOIN_TT = 55;
	public final static int AT_TT = 56;
	public final static int NULL_TT = 57;
	public final static int TRUE_TT = 58;
	public final static int FALSE_TT = 59;
	public final static int COMMA_TT = 60;
	public final static int COLON_TT = 61;
	public final static int LEFT_TOKEN_TT = 62;
	public final static int RIGHT_TOKEN_TT = 63;
	public final static int LEFT_TEXT_TOKEN_TT = 64;
	public final static int SINGLE_LINE_COMMENT_TOKEN_TT = 65;
	public final static int MUTIPLE_LINE_COMMENT_TOKEN_TT = 66;

	public static String[] tokens = new String[] { "TEXT", "PS", "PE", "ID",
			".", "INTERGER", "FLOAT", "++", "--", "+", "-", "(", ")", "STRING",
			"SS", "SE", "WS", "var", "if", "{", "}", "==", "=", ";", "CR", 
			"else", "for" , "in", "continue", "break" , "return" ,
			"elsefor","while","switch","select","directive","DIRECTIVE","case","default","try","catch",
			"[","]",".~","*","/","%","!=",">=",">","<=","<","!","&&","||","?","@","null","true","false",",",":","<<",">>","<$","//","/**/"};
	
	public static Set<String> tokenSet = new HashSet<String>(tokens.length);
	
	static{
		for (String string : tokens) {
			tokenSet.add(string);
		}
	}
	
	/**
	 * issues6
	 * @param text
	 * @return
	 */
	public static boolean contains(String text){
		if("".equals(text.trim())){
			return true;
		}
		return tokenSet.contains(text);
	}
	
	public LexerState state = null;
	public Source source = null;
	LexerDelimiter ld;

	public BeetlLexer(Source source, LexerDelimiter ld) {
		 this(source, ld, -1);
	}
	public BeetlLexer(Source source, LexerDelimiter ld, int model) {
		state = new LexerState();
		this.source = source;
		this.ld = ld;
		this.source.setState(state);
		if(model==-1){
			parseFirst();
		}else{
			state.model = model;
		}
		if(ld.strSe==null){
			parseCR();
		}
	
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
	
	private void parseCR(){
		int c =  0 ;
		for(int i=0;i<source.size();i++){
			c = source.get(i);
			if(c=='\r'){
				if(source.get(i+1)=='\n'){
					ld.strSe = "\r\n";
				}else{
					ld.strSe = "\r";
				}
				break;
			}else if(c=='\n'){
				if(source.get(i+1)=='\r'){
					ld.strSe = "\n\r";
				}else{
					ld.strSe = "\n";
				}
				break;
			}
			continue;
		}
		if(ld.strSe!=null){
			ld.se = ld.strSe.toCharArray();
		}else{
			ld.strSe = "\n";
			ld.se = ld.strSe.toCharArray();
		}
	}

	public BeetlToken nextToken() {
		switch (state.model) {
		case LexerState.STATIC_MODEL:
			return textModel();
		case LexerState.PH_MODEL:{
			BeetlToken  t =  statementModel();			
			return t ;
		}
			
//		case LexerState.COMMENT_MODEL:{
//			return commentModel();
//		}
		case LexerState.ST_MODEL:{
			BeetlToken  t =  statementModel();
			
			return t ;
		}
			
		case LexerState.PH_START:
			return placeHolderStartToken();
		case LexerState.ST_START:
			return statmentStartToken();
		}

		return null;
	}
	
//	private BeetlToken commentModel(){
//		String template = source.template;
//		BeetlTextToken token = new BeetlTextToken();
//		token.col = 0;
//		token.start =0;		
//		token.end = template.length();
//		token.line = 1;
//		token.endLine = 1;
//		token.text = template;
//		token.type = MUTIPLE_LINE_COMMENT_TOKEN_TT;
//		token.channel = 1;
//		state.model = -1;
//		return token;
//		
//	}

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
				if (this.forwardMatchs((int)'a', (int)'r')) {
					return this.getCharToken(3, VAR_TT);

				} else {
					return idToken();

				}
			} else if (c == 'i') {
				if (this.forwardMatch('f')) {

					return this.getCharToken(2, IF_TT);
				}else if (this.forwardMatchs((int)'n', (int)' ')) {//${userLP.index}  不加空格index的in也变色了
					return this.getCharToken(3, IN_TT);
				}else {

					return idToken();

				}
			} else if (c == 'e') {
				if (this.forwardMatchsFour((int)'l',(int)'s',(int)'e')) {

					return this.getCharToken(4, ELSE_TT);
				} else if (this.forwardMatchsFour((int)'f',(int)'o',(int)'r')) {
					return this.getCharToken(7, ELSEFOR_TT);
				}else {

					return idToken();

				}
			}else if (c == 'r') {
				if (this.forwardMatchsMore((int)'e',(int)'t',(int)'u',(int)'r',(int)'n',(int)';')) {

					return this.getCharToken(7, RETURN_TT);
				}else {

					return idToken();

				}
			}else if (c == 'w') {
				if (this.forwardMatchsMore((int)'h',(int)'i',(int)'l',(int)'e')) {

					return this.getCharToken(5, WHILE_TT);
				} else {

					return idToken();

				}
			}else if (c == 's') {
				if (this.forwardMatchsMore((int)'w',(int)'i',(int)'t',(int)'c',(int)'h')) {

					return this.getCharToken(6, SWITCH_TT);
				}else if (this.forwardMatchsMore((int)'e',(int)'l',(int)'e',(int)'c',(int)'t')) {
					return this.getCharToken(6, SELECT_TT);
				} else {

					return idToken();

				}
			}else if (c == 'c') {
				if (this.forwardMatchsMore((int)'o',(int)'n',(int)'t',(int)'i',(int)'n',(int)'u',(int)'e')) {

					return this.getCharToken(8,CONTINUE_TT);
				}else if(this.forwardMatchsMore((int)'a',(int)'t',(int)'c',(int)'h')){
					
					return this.getCharToken(5,CATCH_TT);
				}else if(this.forwardMatchsMore((int)'a',(int)'s',(int)'e')){
					
					return this.getCharToken(4,CASE_TT);
				} else {

					return idToken();

				}
			}else if (c == 'b') {
				if (this.forwardMatchsMore((int)'r',(int)'e',(int)'a',(int)'k')) {

					return this.getCharToken(5,BREAK_TT);
				} else {

					return idToken();

				}
			}else if (c == 'd') {
				if (this.forwardMatchsMore((int)'e',(int)'f',(int)'a',(int)'u',(int)'l',(int)'t')) {

					return this.getCharToken(7,DEFAULT_TT);
				}else if (this.forwardMatchsMore((int)'i',(int)'r',(int)'e',(int)'c',(int)'t',(int)'i',(int)'v',(int)'e')) {
					
					return this.getCharToken(9,DIRECTIVE_L_TT);
				} else {

					return idToken();

				}
			}else if (c == 'D') {
				if (this.forwardMatchsMore((int)'I',(int)'R',(int)'E',(int)'C',(int)'T',(int)'I',(int)'V',(int)'E')) {
					
					return this.getCharToken(9,DIRECTIVE_G_TT);
				}else {

					return idToken();

				}
			}else if (c == 't') {
				if (this.forwardMatchsMore((int)'r',(int)'u',(int)'e')) {

					return this.getCharToken(4,TRUE_TT);
				}else if (this.forwardMatchsMore((int)'r',(int)'y')) {

					return this.getCharToken(3,TRY_TT);
				} else {

					return idToken();

				}
			}else if (c == 'n') {
				if (this.forwardMatchsMore((int)'u',(int)'l',(int)'l')) {

					return this.getCharToken(4,NULL_TT);
				} else {

					return idToken();

				}
			}else if (c == 'f') {
				if (this.forwardMatchs((int)'o',(int)'r')) {

					return this.getCharToken(3, FOR_TT);
				}if (this.forwardMatchsMore((int)'a',(int)'l',(int)'s',(int)'e')) {

					return this.getCharToken(5, FALSE_TT);
				} else {

					return idToken();

				}
			}else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {

				return idToken();

			} else if (c == '{') {
				return this.getCharToken(1, LEFT_BRACE_TT);
			} else if (c == '}') {
				return this.getCharToken(1, RIGHT_BRACE_TT);
			} else if (c == '(') {
				return this.getCharToken(1, LEFT_PAR_TT);
			} else if (c == ')') {
				return this.getCharToken(1, RIGHT_PAR_TT);
			}else if (c == '[') {
				return this.getCharToken(1, LEFT_SQBR_TT);
			} else if (c == ']') {
				return this.getCharToken(1, RIGHT_SQBR_TT);
			} else if (c == ' ') {
				return this.consumeWS();
			} else if (c == '\r' || c == '\n') {
				return getCRToken(c);
			} else if (c == ';') {
				return this.getCharToken(1, SEMI_TT);

			} else if (c == '?') {
				return this.getCharToken(1, QUESTOIN_TT);

			}else if (c == '@') {
				return this.getCharToken(1, AT_TT);

			}else if (c == ',') {
				return this.getCharToken(1, COMMA_TT);

			}else if (c == ':') {
				return this.getCharToken(1, COLON_TT);

			}
			else if (c == '=') {
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

				} else if (this.forwardMatch((int)'~')) {
					return this.getCharToken(2, VIRTUAL_TT);

				}else {
					return this.getCharToken(1, this.PERIOD_TT);

				}
			} else if (c == '+') {
				if (this.forwardMatch('+')) {
					return this.getCharToken(2, this.INCREASE_TT);

				} else {
					return this.getCharToken(1, this.ADD_TT);

				}

			} else if (c == '!') {
				if (this.forwardMatch('=')) {
					return this.getCharToken(2, NOT_EQUAL_TT);

				} else {
					return this.getCharToken(1, NOT_TT);

				}

			} else if (c == '>') {
				if (this.forwardMatch('=')) {
					return this.getCharToken(2, LARGE_EQUAL_TT);

				}else if (this.forwardMatch('>')) {
					return this.getCharToken(2, RIGHT_TOKEN_TT);

				} else {
					return this.getCharToken(1, LARGE_TT);

				}

			} else if (c == '<') {
				if (this.forwardMatch('=')) {
					return this.getCharToken(2, LESS_EQUAL_TT);

				}else if (this.forwardMatch('<')) {
					return this.getCharToken(2, LEFT_TOKEN_TT);

				}else if (this.forwardMatch('$')) {
					return this.getCharToken(2, LEFT_TEXT_TOKEN_TT);

				} else {
					return this.getCharToken(1, LESS_TT);

				}

			} else if (c == '*') {
				return this.getCharToken(1, MUlTIP_TT);

			} else if (c == '/') {
				if(this.forwardMatch('/')){
					source.consume(2);
					return this.comsumeSingleLineComment();
				}else if(this.forwardMatch('*')){
					source.consume(2);
					return this.comsumeMutipleLineComment();
				}
				else{
					return this.getCharToken(1, DIV_TT);
				}
				

			} else if (c == '%') {
				
				return this.getCharToken(1, MOD_TT);

			} else if (c == '=') {
				if (this.forwardMatch('=')) {
					return this.getCharToken(2, this.ASSIN_TT);
				} else {
					return this.getCharToken(1, this.EQUALS_TT);
				}
			}else if (c == '&') {
				if (this.forwardMatch('&')) {
					return this.getCharToken(2, AND_TT);
				} else {
					return idToken();
				}
			}else if (c == '|') {
				if (this.forwardMatch('|')) {
					return this.getCharToken(2, OR_TT);
				} else {
					return idToken();
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
		int endLine = line ;
		while ((c = source.get()) != source.EOF) {
			if (c != ld.ps[0] && c != ld.ss[0]) {

				if (c == '\r' || c == '\n') {
					consumeMoreCR(c);
					endLine++;
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
						break;
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
		if (start == source.pos()) {
			if (state.model == LexerState.PH_START) {
				return this.placeHolderStartToken();
			} else if (state.model == LexerState.ST_START) {
				return this.statmentStartToken();
			} else {
				return null;
			}
		}
		
		if(endLine>line){
			if(source.state.col!=1){
				//结束的时候，col不在第一列，说明静态文本已经到下一行了
				return getStaticTextToken(start, source.pos(), col, line,endLine);
			}else{
				//结束的时候，col在第一列，说明静态文本到本行结束
				return getStaticTextToken(start, source.pos(), col, line,endLine-1);
			}
		}else{
			return getStaticTextToken(start, source.pos(), col, line,endLine);
		}
		

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

	private BeetlToken getStaticTextToken(int start, int end, int col, int line,int endLine) {

		String text = source.getRange(start, end);
		BeetlTextToken token = new BeetlTextToken(TEXT_TT, text);
		token.col = col;
		token.line = line;
		token.start = start;
		token.end = end;
		token.endLine = endLine;

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

	private boolean forwardMatchs(int one,int two) {
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
	private boolean forwardMatchsMore(int... arr) {
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
	
	private boolean forwardMatchsFour(int one,int two,int three) {
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

	private BeetlToken getCRToken(int crFirst) {
		state.addLine();
		if (state.cr_len != 0) {
			return this.getCharToken(state.cr_len, CR_TT);
		} else {
			int c = source.get(1);
			if (c == source.EOF) {
				state.cr_len = 1;

			} else if (crFirst == '\n' && c == '\r') {
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
	
	
	
	private BeetlToken  comsumeMutipleLineComment(){
		int c = source.get();
		int line = state.line;
		int col = state.col;
		int start = source.pos();
		
		boolean inStat = true;
		while ((c = source.get()) != Source.EOF) {
			if(c=='*'&&this.forwardMatch('/')&&inStat){
				source.consume(2);
				break ;
			}
			if(c==ld.ss[0]&&source.isMatch(ld.ss) && !source.hasEscape()){
				inStat = true;
				source.consume(ld.ss.length);
				continue  ;
			}
			
			if(c==ld.se[0]&&source.isMatch(ld.se) && !source.hasEscape()){
				//在定界符号外面了
				inStat = false;
				source.consume(ld.se.length);
				continue  ;
			}else if(c=='\r'||c=='\n'){
				this.consumeMoreCR(c);
				continue ;
				
			}
			source.consume();
			//其他字符就继续了
		}
		
		
			BeetlTextToken token = new BeetlTextToken();
			token.col = col;
			token.start = start-2;
			token.end = source.pos();
			token.line = line;
			token.endLine = state.line;
			token.text = source.getRange(token.start, source.pos());
			token.type = MUTIPLE_LINE_COMMENT_TOKEN_TT;
			token.channel = 1;
			return token;
		

		
	}
	private BeetlToken  comsumeSingleLineComment(){
		int c = source.get();
		int line = state.line;
		int start = source.pos();
		while ((c = source.get()) != Source.EOF) {
			if (c == '\r' || c == '\n') {				
				consumeMoreCR(c);
				break;
			} else {
				source.consume();
			}
		}

		BeetlToken token = new BeetlToken();
		token.col = state.col;
		token.start = start-2;
		token.end = source.pos();
		token.line = line;
		//保留了可能的回车，需要嘛？
		if(c==Source.EOF){
			token.text = source.getRange(token.start, source.pos());
		}else{
			//去掉回车
			token.text = source.getRange(token.start, source.pos()-state.cr_len);
		}
		
		token.type = SINGLE_LINE_COMMENT_TOKEN_TT;
		token.channel = 1;
		return token;
	}


	public static void main(String[] args) {
		System.err.println(tokens.length);
		String template = "@var a=1 \n tttt"
				+ "%>";
		Source source = new Source(template);
		LexerDelimiter ld = new LexerDelimiter("${", "}", "@", "\n");

		BeetlLexer lexer = new BeetlLexer(source, ld);
		BeetlToken token = null;// lexer.nextToken();
		while ((token = lexer.nextToken()) != null) {
			System.out.println(token);
		}

	}
}
