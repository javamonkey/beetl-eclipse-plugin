package org.beetl.core.parser;

public class LexerState {
	/*0:静态文本 1 占位符 2 语句 3 注释*/
	public int model = 0;	
	public int line = 1;
	public int col = 0;
	
	public int cr_len = 0;
	
	public static final int STATIC_MODEL = 0;
	public static final int PH_START = 1;
	public static final int PH_MODEL = 2;
	public static final int PH_END = 3;
	
	public static final int ST_START = 2;
	public static final int ST_MODEL = 3;
	public static final int ST_END = 4;
	
	
	public void addLine(){
		line++;
		col = 1;
	}
	
	
}
