package org.beetl.editors.handler;

import java.awt.Toolkit;

import org.beetl.core.parser.BeetlToken;
import org.beetl.editors.BeetlTokenSource;
import org.beetl.editors.ProjectUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class PreStatHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public PreStatHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		 ITextEditor editor =  ProjectUtil.getActiveEditor(event) ;
		 Document document = ProjectUtil.getDocument(editor);
		
		 BeetlTokenSource s = ProjectUtil.getBeetlTokenSource(document);
		
		 ISourceViewer viewer = (ISourceViewer)
		            editor.getAdapter(ITextOperationTarget.class);			 
	
		 
			ISelection selection = viewer.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				if (textSelection.getOffset() != 0
						|| textSelection.getLength() != 0) {
					int offset = textSelection.getOffset();
					BeetlToken token = s.findPreStOrHolder(offset);
					if(token==null){
						//Toolkit.getDefaultToolkit().beep();
						Display.getDefault().beep();
						return null;
					}
					int newOffset = token.end;
					//viewer.getTextWidget().setSelection(newOffset);
					editor.selectAndReveal(newOffset, 0);
				}

			}
		 
		 
	
		return null;
		
	}
}
