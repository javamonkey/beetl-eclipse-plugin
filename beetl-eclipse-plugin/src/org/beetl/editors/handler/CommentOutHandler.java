package org.beetl.editors.handler;

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
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;

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
				document.getLineInformationOfOffset( ((TextSelection)selection).getOffset());
				int pos = document.getLineInformationOfOffset( ((TextSelection)selection).getOffset()).getOffset();
				int length = document.getLineInformationOfOffset( ((TextSelection)selection).getOffset()).getLength();
				String content=document.get(pos, length);
				if(content.contains("//")){//uncomment the line
					content=content.replaceFirst("//", "");
				}
				else{//comment out the line
					content = "//"+content;
				}
				document.replace(pos, length, content);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}


		return null;
	}
	


}
