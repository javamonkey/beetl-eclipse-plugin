package org.beetl.editors;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
public class ProjectUtil {
	static String editorId = "org.beetl.editors.BeetlEclipseEditor";
	/** 切换编辑器
	 * @param event
	 */
	public static void toggleEditor(ExecutionEvent event) {
		 IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                 .getActiveWorkbenchWindow().getActivePage();  
		 int caretOffset = 0;
		 
		 try {
			 ISourceViewer viewer = (ISourceViewer)
		            ((ITextEditor) HandlerUtil.getActivePartChecked(event))
		                .getAdapter(ITextOperationTarget.class);
		         caretOffset = viewer.getTextWidget().getCaretOffset();
		    } catch (org.eclipse.core.commands.ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		 IEditorPart part =  wbPage.getActiveEditor();
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
		 IEditorPart ep = null;
		 try {
			 
			 if(part instanceof BeetlEclipseEditor){
				 BeetlEclipseEditor editor = (BeetlEclipseEditor)part;
				 String oldEditorId = editor.getOldEditorId();
				// wbPage.closeAllEditors(true);
				 ep =  wbPage.openEditor((IEditorInput)part.getEditorInput(), oldEditorId, true, IWorkbenchPage.MATCH_ID);
				 
			 }else{
					String oldEditorId = wbPage.getActiveEditor().getEditorSite().getId();
					//wbPage.closeAllEditors(true);	
					BeetlEclipseEditor beetlEditor = (BeetlEclipseEditor) wbPage.openEditor((IEditorInput)part.getEditorInput(), editorId, true, IWorkbenchPage.MATCH_ID);
					beetlEditor.setOldEditorId(oldEditorId);
					ep = beetlEditor;
			 }
			 
			 ISourceViewer viewer = (ISourceViewer)ep.getAdapter(ITextOperationTarget.class);
			 viewer.getTextWidget().setSelection(caretOffset);
				
		
		 
		 } catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
			return;
		
	}
	
//	private IProject getActiveProject() {
//		IEditorPart editor = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
//		if (editor.getEditorInput() instanceof FileEditorInput) {
//			FileEditorInput fei = (FileEditorInput) editor.getEditorInput();
//			IFile f = fei.getFile();
//			return f.getProject();
//		}
//		return null;
//	}
//	
//	public void change(){
//		IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
//                .getActiveWorkbenchWindow().getActivePage();  
//		
//	}
}
