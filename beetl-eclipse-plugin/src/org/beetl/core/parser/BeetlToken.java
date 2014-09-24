package org.beetl.core.parser;

public class BeetlToken {
	public String text;
	public int line, col,start,end, type,channel;
	public BeetlToken(){
		
	}
	public BeetlToken(int type,String text){
		this.type = type;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Token [text=" + text + ", line=" + line + ", col=" + col
				+ ", start=" + start + ", end=" + end + ", type=" + type + "]";
	}
	
	
}
