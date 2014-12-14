package org.beetl.core.parser;

public class BeetlTextToken  extends BeetlToken{
	
	public int endLine ;
	public BeetlTextToken() {
		super();
	}

	public BeetlTextToken(int type, String text) {
		super(type,text);
	}

	

	@Override
	public String toString() {
		String typeStr = type != -1 ? BeetlLexer.tokens[type] : "ERROR";
		return "Token [text=" + text + ", line=" + line + ",endLine=" + endLine + ", col=" + col
				+ ", start=" + start + ", end=" + end + ", type=" + typeStr
				+ "]";
	}

}
