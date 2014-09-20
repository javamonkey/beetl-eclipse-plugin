package org.beetl.core.parser;

/**
 * 读取或者消费单个字符
 * 
 * @author lijiazhi
 * 
 */
public class Source {
	char[] cs = null;
	int p;
	int size = 0;
	static int EOF = -1;
	int mark = 0;
	LexerState state = null;

	public Source(String template) {
		cs = template.toCharArray();
		size = cs.length;
	}

	public int get() {
		if (p <= size - 1) {
			return cs[p];
		} else {
			return EOF;

		}
	}

	public int get(int i) {
		if ((p + i) <= size - 1) {
			return cs[p + i];
		} else {
			return EOF;

		}
	}

	public boolean hasEscape() {
		if (p > 1) {
			if (cs[p - 1] == '\\') {
				if (p > 2) {
					return !(cs[p - 2] == '\\');
				}
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	public boolean isMatch(char[] str) {
		int cur = p;
		for (int i = 0; i < str.length; i++) {
			if (cur < size && cs[cur] == str[i]) {
				cur++;
			} else {
				return false;
			}
		}
		return true;
	}

	public void consume() {
		p++;
		state.col++;
	}

	public void consume(int i) {
		p = p + i;
		state.col = state.col + i;
	}

	public int pos() {
		return this.p;
	}

	public void seek(int index) {
		this.p = index;
	}

	public String getRange(int start, int end) {
		return new String(cs, start, end - start);
	}

	public void setState(LexerState state) {
		this.state = state;
	}

}
