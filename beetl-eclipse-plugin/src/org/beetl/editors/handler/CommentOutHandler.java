package org.beetl.editors.handler;

import java.util.List;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.beetl.editors.BeetlEclipseEditor;
import org.beetl.editors.BeetlTokenSource;
import org.beetl.editors.ProjectUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelection;

public class CommentOutHandler  extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		BeetlEclipseEditor editor = (BeetlEclipseEditor) ProjectUtil
				.getActiveEditor(event);
		Document document = ProjectUtil.getDocument(editor);

		BeetlTokenSource s = ProjectUtil.getBeetlTokenSource(document);
		
		ProjectionViewer viewer = (ProjectionViewer) editor
				.getAdapter(ITextOperationTarget.class);
		ISelection selection = viewer.getSelectionProvider().getSelection();

		/*
		 * just comment out a single line, without the NPT check cannot work with a selected block yet.
		 */		
		if(selection instanceof TextSelection){
			try {
				
				TextSelection ts = (TextSelection)selection;
				int start = ts.getStartLine();
				int end = ts.getEndLine();
				List tokens = s.getTokens();
				for(int i=start;i<=end;i++){
					int[] status = this.canCmment(tokens, i);
					if(status[0]==0) continue;
					else if(status[0]==1){
						int col = status[1];
						int len = document.getLineLength(i);
						int lineOffset = document.getLineOffset(i);
						int pos = lineOffset+col;
						int length = len-col;
						String content=document.get(lineOffset+col, len-col);
						content = "//"+content;
						document.replace(pos, length, content);						
						
					}else if(status[0]==2){
						int col = status[1];
						int len = document.getLineLength(i);
						int lineOffset = document.getLineOffset(i);
						int pos = lineOffset+col;
						int length = len-col;
						String content=document.get(lineOffset+col, len-col);
						content=content.replaceFirst("//", "");
						document.replace(pos, length, content);	
					}
				}
				
//				document.getLineInformationOfOffset( ((TextSelection)selection).getOffset());
//				int pos = document.getLineInformationOfOffset( ((TextSelection)selection).getOffset()).getOffset();
//				int length = document.getLineInformationOfOffset( ((TextSelection)selection).getOffset()).getLength();
//				String content=document.get(pos, length);
//				if(content.contains("//")){//uncomment the line
//					content=content.replaceFirst("//", "");
//				}
//				else{//comment out the line
//					content = "//"+content;
//				}
//				document.replace(pos, length, content);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}


		return null;
	}
	
	/** 
	 * @param tokens
	 * @param line
	 * @return  返回一个数组，第一个数表示解析结果，0 不能， 1 可以注释， 2取消注释，第二个表示操作位置
	 */
	private int[] canCmment(List<BeetlToken> tokens,int line){
		//beetl 从1行开始
		line = line+1;
		int i=0;
		int offset = 0;
		
		for(;i<tokens.size();i++){
			BeetlToken t = tokens.get(i);
			if(t.getLine()==line){
				if(t.getType()==BeetlLexer.WS_TT){
					offset = t.col ;
					
				}else 	if(t.getType()==BeetlLexer.ST_SS_TT){
					offset = t.col+(t.end-t.start-1);				
					
				}else if(t.getType()==BeetlLexer.TEXT_TT){
					return  new int[]{0,0};
				}
			
				//检查本行都得是beetl脚本,否则，不能操作
				for(int j=i;j<tokens.size();j++){
					BeetlToken token = tokens.get(j);
					//到下一行退出
					if(token.getLine()!=line) break;
					
					if(token.getType() == BeetlLexer.SINGLE_LINE_COMMENT_TOKEN_TT){
						return new int[]{2,offset};
					}
					if(token.getType()==BeetlLexer.TEXT_TT){
						return  new int[]{0,0};
					}
				}
				
				return new int[]{1,offset};
				
			}else{
				continue ;
			}
		}
		
		return  new int[]{0,0};
	}
	
	
	


}
