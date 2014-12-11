package org.beetl.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
public class ProjectUtil {
	
	public static String editorId = "org.beetl.editors.BeetlEclipseEditor";
	
	// 每个工程模板的跟目录
	static Map<IProject,String> webRoot = new HashMap<IProject,String>();
	
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
	
	public static String getProjectBeetlConfig(IFile file){
		JavaProject project = (JavaProject)JavaModelManager.getJavaModelManager().getJavaModel().getJavaProject(file);
		
		try {
			IPath outputPath = project.getOutputLocation();
			IFolder outputFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(outputPath);
			IFile f = outputFolder.getFile("beetl.properties");
			String tt = f.getLocation().toOSString();
			return tt;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String getProjectTemplateRoot(IFile file){
		IProject project = file.getProject();
		String path = webRoot.get(project);
		if(path==null){
			//代开窗口
			InputDialog dialog = new InputDialog(Display.getCurrent().getActiveShell(),//shell窗口
		               "Beetl","输入模板跟路径", file.getFullPath().toOSString(),null);
		        
		        int i = dialog.open();//返回值为按钮
		
				path = dialog.getValue();
				
			
//				path = root.getLocationURI().toString();
				webRoot.put(project, path);
				IFile root = project.getFile(path);
				return path;
			
		}else{
			return path;
		}
		
	
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
