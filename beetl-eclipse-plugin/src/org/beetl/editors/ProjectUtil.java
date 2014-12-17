package org.beetl.editors;

import java.util.ArrayList;
import java.util.List;
import org.beetl.activator.BeetlActivator;
import org.beetl.preferences.PreferenceConstants;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
public class ProjectUtil {
	
	public static String editorId = "org.beetl.editors.BeetlEclipseEditor";
	
	// 每个工程模板的跟目录
	//static Map<IProject,IPath> webRoot = new HashMap<IProject,IPath>();
	
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
	
	
	public static IPath getProjectTemplateRoot(IFile file){
		IProject project = file.getProject();
		
		IPreferenceStore store = BeetlActivator.getDefault().getPreferenceStore();
		String templdatePath = store.getString(PreferenceConstants.P_STRING);
		System.out.println("templdatePath: "+templdatePath);
		IPath path = null;
		//IPath path = webRoot.get(project);
		if(templdatePath==null || "".equals(templdatePath)){

			
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(
					Display.getCurrent().getActiveShell(), ResourcesPlugin
							.getWorkspace().getRoot(), true, "请选择模板根目录");
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1) {
					path = (IPath) result[0];
					store.setValue(PreferenceConstants.P_STRING, path.toOSString());
					//webRoot.put(project, path);
					
					
					return path;
				}
			}
			return null;

		}else{
			path = new Path(templdatePath);
			return path;
		}
		
	
	}
	
	
	
	

}
