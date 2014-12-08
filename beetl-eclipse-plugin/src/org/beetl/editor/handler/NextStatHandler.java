package org.beetl.editor.handler;

import org.beetl.editors.ProjectUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class NextStatHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public NextStatHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		
		System.out.println("okok");
		 /*
		 if(part instanceof BeetlEclipseEditor){
			 BeetlEclipseEditor editor = (BeetlEclipseEditor)part;
			 ISourceViewer view = editor.getSourceView();
			// caretOffset = view.getTextWidget().getCaretOffset();
//			 view.getTextWidget().setSelection(10);
//			 //view.setSelectedRange(10, 2);
//			 //FileEditorInput input = (FileEditorInput)editor.getEditorInput();
////			 input.getAdapter(adapter)
			
			 
		 }else if(part instanceof AbstractTextEditor){
			 
		 }
		 */
		
		return null;
	}
}
