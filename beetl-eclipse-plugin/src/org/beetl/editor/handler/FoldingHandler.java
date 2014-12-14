package org.beetl.editor.handler;

import java.util.ArrayList;
import java.util.List;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlTextToken;
import org.beetl.core.parser.BeetlToken;
import org.beetl.editors.BeetlEclipseEditor;
import org.beetl.editors.BeetlTokenSource;
import org.beetl.editors.ProjectUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

/**
 * 折叠
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class FoldingHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public FoldingHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//暂时不做，折叠后会导致光标定位失败，除非定位前取消折叠，但似乎取消后，editor混乱了
		if(1==1)return null;
		BeetlEclipseEditor editor =  (BeetlEclipseEditor)ProjectUtil.getActiveEditor(event) ;
		 Document document = ProjectUtil.getDocument(editor);
		 String content = document.get();
		 BeetlTokenSource s = new BeetlTokenSource(null);
		 s.parse(content);
		
		 ProjectionViewer viewer = (ProjectionViewer)
		            editor.getAdapter(ITextOperationTarget.class);	
		 
		 List<BeetlToken> tokens = s.getTokens();
		 List<Position> posList = new ArrayList<Position>();
		 for(int i=0;i<tokens.size();i++){
			 BeetlToken token = tokens.get(i);
			 if(token.type == BeetlLexer.TEXT_TT){
				 BeetlTextToken t = (BeetlTextToken)token;
				 int startLine = t.line;
				 int endLine = t.endLine;
				 if(i!=0){
					 BeetlToken pre = tokens.get(i-1);
					 if(pre.line==t.line){
						 if(t.endLine>t.line){
							 //从下一行开始
							 startLine++;
						 }else{
							 //同一行，不折叠
							 continue ;
						 }
						
					 }
				 }
				 
				 if(i<tokens.size()-1){
					 BeetlToken next = tokens.get(i+1);
					 if(next.getCol()!=1){
						 
						 if(t.endLine>t.line){
							 endLine--;
						 }else{
							 //同一行，不折叠
							 continue;
						 }
						
					 }
				 }
					 
					 
				if(startLine<endLine){
					//如果有多行，可以折叠
					 try {
						 //同eclipse编辑器统一
						int start = document.getLineOffset(startLine-1);
						IRegion r = (IRegion)document.getLineInformation(endLine-1);
						String cr = document.getLineDelimiter(endLine-1);
						
						 int len = r.getOffset()-start+r.getLength()+(cr!=null?cr.length():0);
						 Position  pos = new Position(start,len);
						 posList.add(pos);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					continue;
				}
				
				
			 }
		 }
		 
		 editor.updateFoldingStructure(posList);
		 editor.getAnnotationModel().collapseAll(0, document.getLength());
		
		 
		 return null;
	
	
		
	}
	private boolean hasNextLine(String str){
		if(str.indexOf('\n')!=-1||str.indexOf('\r')!=-1){
			return true;
		}else{
			return false ;
		}
	}
}
