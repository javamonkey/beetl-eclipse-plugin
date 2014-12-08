package org.beetl.editor.handler;

import org.beetl.editors.BeetlEclipseEditor;
import org.beetl.editors.ProjectUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class EditorToggleHandler extends AbstractHandler {
	
	static String editorId = "org.beetl.editors.BeetlEclipseEditor";
	/**
	 * The constructor.
	 */
	public EditorToggleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		
		
		IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                 .getActiveWorkbenchWindow().getActivePage();  
	
		
		 ITextEditor oldEditor =  ProjectUtil.getActiveEditor(event) ;
		 int caretOffset = ProjectUtil.getCaretOffset(oldEditor);		
		 IEditorPart newEditor = null;
		 try {
			 
			 if(oldEditor instanceof BeetlEclipseEditor){
				 BeetlEclipseEditor editor = (BeetlEclipseEditor)oldEditor;
				 String oldEditorId = editor.getOldEditorId();
				 if(oldEditorId!=null){
					 newEditor =  wbPage.openEditor((IEditorInput)oldEditor.getEditorInput(), oldEditorId, true, IWorkbenchPage.MATCH_ID);
						
				 }else{
					 return null;
				 }
				 
			
				 
			 }else{
					String oldEditorId = wbPage.getActiveEditor().getEditorSite().getId();
					BeetlEclipseEditor beetlEditor = (BeetlEclipseEditor) wbPage.openEditor((IEditorInput)oldEditor.getEditorInput(), editorId, true, IWorkbenchPage.MATCH_ID);
					beetlEditor.setOldEditorId(oldEditorId);
					newEditor = beetlEditor;
					
			 }
			 
			 ISourceViewer viewer = (ISourceViewer)newEditor.getAdapter(ITextOperationTarget.class);
			 viewer.getTextWidget().setSelection(caretOffset);
			 
			 IFile file = ProjectUtil.getInputFile(oldEditor.getEditorInput());
			 //同样文件
			
			 
			 ITextEditor[] all = ProjectUtil.getEditors(file);
			 EditorDocumentListener listener = new EditorDocumentListener(file);
			 //检测是否已经有同步监听了
			 for(ITextEditor ed:all){
				
				 Document doc = ProjectUtil.getDocument(ed);
				 
				 //删除没有的，添加新的
				 doc.removeDocumentListener(listener);
				 doc.addDocumentListener(listener);
			 }
			 
		 }catch(Exception ex){
			 ex.printStackTrace();
			 return null;
		 }
			
		
		
		return null;
	}
}
