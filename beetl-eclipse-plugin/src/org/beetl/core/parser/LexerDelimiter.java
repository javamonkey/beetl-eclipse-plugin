package org.beetl.core.parser;

public class LexerDelimiter {
	// 定界起始符号
	public char[] ss;
	// 定界结束符号
	public char[] se;
	// 占位起始符号
	public char[] ps;
	// 站位结束符
	public char[] pe;

	public String strPs;
	public String strPe;
	public String strSs;
	public String strSe;

	public LexerDelimiter(String strPs, String strPe, String strSs, String strSe) {
		super();
		this.strPs = strPs;
		this.strPe = strPe;
		this.strSs = strSs;
		this.strSe = strSe;
		this.ps = strPs.toCharArray();
		this.pe = strPe.toCharArray();
		this.ss = strSs.toCharArray();
		this.se = strSe.toCharArray();
	}

}
