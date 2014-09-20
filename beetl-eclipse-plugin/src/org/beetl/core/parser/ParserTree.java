package org.beetl.core.parser;

import java.util.List;

/** 抽象语法树
 * @author lijiazhi
 *
 */
class ParserTree {
	int type;
	List<ParserTree> child;
	BeetlToken token;
}
