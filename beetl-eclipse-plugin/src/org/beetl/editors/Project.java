package org.beetl.editors;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
public class Project {
	static String editorId = "org.beetl.editors.BeetlEclipseEditor";
	public static void open() {
		 IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                 .getActiveWorkbenchWindow().getActivePage();  
		
	
		 IEditorPart part =  wbPage.getActiveEditor();
		 if(part instanceof BeetlEclipseEditor){
			 BeetlEclipseEditor editor = (BeetlEclipseEditor)part;
//			 ISourceViewer view = editor.getSourceView();
//			 view.getTextWidget().setSelection(10);
//			 //view.setSelectedRange(10, 2);
//			 //FileEditorInput input = (FileEditorInput)editor.getEditorInput();
////			 input.getAdapter(adapter)
			
			 
		 }
		 
		 try {
			 wbPage.closeAllEditors(true);
			
			wbPage.openEditor((IEditorInput)part.getEditorInput(), editorId, false, IWorkbenchPage.MATCH_ID);
			
		 
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
