package org.beetl.core.antlr;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.beetl.MyDocument;
import org.beetl.core.antlr.BeetlParser.ProgContext;
import org.eclipse.jface.text.IDocument;

public class AntlrUtil {
	
	BeetlAntlrErrorStrategy antlrErrorStrategy = new BeetlAntlrErrorStrategy();
	SyntaxErrorListener syntaxError = new SyntaxErrorListener();

	/** 找出文档的解析错误
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public  Map<Integer,String> getErrorInfo(IDocument doc) throws Exception{
		try{
			String[] delimter = ((MyDocument)doc).delimter;
			Reader reader = new StringReader(doc.get());
			Transformator sf = new Transformator(delimter[2], delimter[3], delimter[0], delimter[1]);
			//先假定
			sf.enableHtmlTagSupport("<#", "</#");
			
			Reader scriptReader = sf.transform(reader);
			ANTLRInputStream input;
			try
			{
				input = new ANTLRInputStream(scriptReader);
			}
			catch (IOException e)
			{
				// 不可能发生
				throw new RuntimeException(e);
			}
			BeetlLexer lexer = new BeetlLexer(input);
			lexer.removeErrorListeners();
			lexer.addErrorListener(syntaxError);

			CommonTokenStream tokens = new CommonTokenStream(lexer);

			BeetlParser parser = new BeetlParser(tokens);
			//测试代码
			parser.setErrorHandler(antlrErrorStrategy);

			ProgContext tree = parser.prog();
			return Collections.EMPTY_MAP;
		}catch(BeetlException ex){
			ErrorInfo info = new ErrorInfo(ex);
		
			Map<Integer,String> error = new HashMap<Integer,String> ();
			String showMsg = "错误符号:"+info.getErrorTokenText()+",错误类型:"+info.getType();
			if(info.getMsg()!=null){
				showMsg = showMsg+" 信息:"+info.getMsg();
			}
			error.put(ex.token.line, showMsg);
			return error;
		}
		
	}
	
}
