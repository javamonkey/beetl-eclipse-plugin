package org.beetl.editors.handler;

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
		//if(1==1)return null;
		BeetlEclipseEditor editor =  (BeetlEclipseEditor)ProjectUtil.getActiveEditor(event) ;
		Document document = ProjectUtil.getDocument(editor);
		if(editor.isFoldingAll()){
			ProjectUtil.unfolding(editor, document);
			editor.setFoldingAll(false);
		}else{
			ProjectUtil.folding(editor, document);
			editor.setFoldingAll(true);
		}
		
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
