package org.beetl.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
public class ProjectUtil {
	
	/** 切换编辑器
	 * @param event
	 */
	
	public static int getCaretOffset(ITextEditor te){
		
			 ISourceViewer viewer = (ISourceViewer)
		            te.getAdapter(ITextOperationTarget.class);
		        return    viewer.getTextWidget().getCaretOffset();
		   
		
	}
	
	public static void syncText(ITextEditor[] texts){
		
	}
	
	public static ITextEditor getActiveEditor(ExecutionEvent event){
		try {
			return ((ITextEditor) HandlerUtil.getActivePartChecked(event));
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static ITextEditor[] getEditors(IFile file){
		 IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                 .getActiveWorkbenchWindow().getActivePage();  
		 IEditorPart[] parts = wbPage.getEditors();
		 List<ITextEditor> list = new ArrayList<ITextEditor>(2);
		 for(IEditorPart part:parts){
			 if(part instanceof ITextEditor ){
				 ITextEditor editor = (ITextEditor)part;
				 IEditorInput input =  editor.getEditorInput();
				 IFile ifile = getInputFile(input);
				 if(file.equals(ifile)){
					 list.add(editor);
				 }
			 }
		 }
		 
		 return list.toArray(new ITextEditor[0]) ;
	}
	
	public static IFile getInputFile(IEditorInput input){
		if(input instanceof FileEditorInput){
			FileEditorInput file = (FileEditorInput)input;
			return file.getFile();
		}else{
			return  null;
		}
	}
	
	public static Document getDocument(ITextEditor editor){
		 ISourceViewer viewer = (ISourceViewer)editor.getAdapter(ITextOperationTarget.class);
		 return (Document)viewer.getDocument();

	}
	
	public static ISourceViewer getViewer(ITextEditor editor){
		 ISourceViewer viewer = (ISourceViewer)editor.getAdapter(ITextOperationTarget.class);
		return viewer;

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
