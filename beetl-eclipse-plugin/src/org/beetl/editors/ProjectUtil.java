package org.beetl.editors;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.beetl.MyDocument;
import org.beetl.core.antlr.BeetlParser;
import org.beetl.core.antlr.BeetlParser.ProgContext;
import org.beetl.core.antlr.Transformator;
import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlTextToken;
import org.beetl.core.parser.BeetlToken;
import org.beetl.core.parser.LexerDelimiter;
import org.beetl.editors.property.BeetlPropertyPage;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Position;
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
//	public static String[] delimter = null;
	// 每个工程模板的跟目录
	//static Map<IProject,IPath> webRoot = new HashMap<IProject,IPath>();
	static DocumentCache docCache = new DocumentCache();
	public static String line = System.getProperty("line.separator");
	
	public static BeetlTokenSource getBeetlTokenSource(String str,String type,IDocument doc){
		String[] delimter = ((MyDocument)doc).delimter;
		LexerDelimiter ld = new LexerDelimiter(delimter[2], delimter[3], delimter[0], delimter[1]);
		BeetlTokenSource source = new BeetlTokenSource(type);
		
		source.ld = ld;
		source.parse(str);
		return source;
	}
	
	public static synchronized BeetlTokenSource getBeetlTokenSource(Document doc){
		BeetlTokenSource source = docCache.getTokenSource(doc);
		
		if(source!=null) return source;
		
		String[] delimter = ((MyDocument)doc).delimter;
		LexerDelimiter ld = new LexerDelimiter(delimter[2], delimter[3], delimter[0], delimter[1]);
		source = new BeetlTokenSource(null);
		
		source.ld = ld;
		source.parse(doc.get());
		docCache.setTokenSource((Document)doc, source);
		return source;
	}
	
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
	
//	public static String getProjectBeetlConfig(IFile file){
//		JavaProject project = (JavaProject)JavaModelManager.getJavaModelManager().getJavaModel().getJavaProject(file);
//		
//		try {
//			IPath outputPath = project.getOutputLocation();
//			IFolder outputFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(outputPath);
//			IFile f = outputFolder.getFile("beetl.properties");
//			String tt = f.getLocation().toOSString();
//			return tt;
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	public static String[] getProjectDelimter() {
//		String[] str = new String[4];
//		IWorkbenchPage wbPage = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
//		IEditorInput input = wbPage.getActiveEditor().getEditorInput();
//		IFile file = getInputFile(input);
//		IProject project = file.getProject();
//		QualifiedName qname = new QualifiedName("", BeetlPropertyPage.ST_START);
//		try {
//			str[0] = project.getPersistentProperty(qname);
//			qname = new QualifiedName("", BeetlPropertyPage.ST_END);
//			str[1] = project.getPersistentProperty(qname);
//			qname = new QualifiedName("", BeetlPropertyPage.PL_START);
//			str[2] = project.getPersistentProperty(qname);
//			qname = new QualifiedName("", BeetlPropertyPage.PL_END);
//			str[3] = project.getPersistentProperty(qname);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return new String[] { "<%", "%>", "$", "}" };
//		}
//
//		return str;
//	}
	
	public static String[] getProjectDelimter(BeetlEclipseEditor editor) {
		String[] str = new String[6];
		IEditorInput input = editor.getEditorInput();
		IFile file = getInputFile(input);
		IProject project = file.getProject();
		QualifiedName qname = new QualifiedName("", BeetlPropertyPage.ST_START);
		try {
			str[0] = project.getPersistentProperty(qname);
			if(str[0]==null||str[0].length()==0){
				str[0] = "<%";
			}
			qname = new QualifiedName("", BeetlPropertyPage.ST_END);			
			str[1] = project.getPersistentProperty(qname);
			if(str[1]==null){
				str[1] = "%>";
			}
			if(str[1].length()==0){
				str[1] = line;
			}
		
			qname = new QualifiedName("", BeetlPropertyPage.PL_START);
			str[2] = project.getPersistentProperty(qname);
			if(str[2]==null||str[2].length()==0){
				str[2] = "${";
			}
			qname = new QualifiedName("", BeetlPropertyPage.PL_END);
			str[3] = project.getPersistentProperty(qname);
			if(str[3]==null||str[3].length()==0){
				str[3] = "}";
			}
			
			qname = new QualifiedName("", BeetlPropertyPage.HTML_TAG);
			str[4] = project.getPersistentProperty(qname);
			if(str[4]==null||str[4].length()==0){
				str[4] = "#";
			}
			
			qname = new QualifiedName("", BeetlPropertyPage.HTML_VAR);
			str[5] = project.getPersistentProperty(qname);
			if(str[5]==null||str[5].length()==0){
				str[5] = "var";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new String[] { "<%", "%>", "$", "}" ,"#","var"};
		}

		return str;
	}
	
//	public static void initProject(IFile file){
//		if(delimter!=null) return ;
//		String filePath = getProjectBeetlConfig(file);
//		delimter = getBasicConfig(filePath);
//	}
//	
	
	
	
	public static IPath getProjectTemplateRoot(IFile file){
		IProject project = file.getProject();
		QualifiedName indexName = new QualifiedName("", BeetlPropertyPage.OWNER_PROPERTY);
		String templdatePath;
		try {
			templdatePath = project.getPersistentProperty(indexName);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//		System.out.println("templdatePath: "+templdatePath);
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
					try {
						project.setPersistentProperty(indexName,path.toOSString());
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
					
					return path;
				}
			}
			return null;

		}else{
			path = new Path(templdatePath);
			return path;
		}
		
	
	}
	
	public static  void unfolding(BeetlEclipseEditor editor,Document document){
		editor.getAnnotationModel().expandAll(0, document.getLength());
	}
	public static  void folding(BeetlEclipseEditor editor,Document document){
		editor.getAnnotationModel().collapseAll(0, document.getLength());
	}
	
	
	public static  void foldingDocument(BeetlEclipseEditor editor,Document document){
		
		 
		 BeetlTokenSource s = ProjectUtil.getBeetlTokenSource((Document)document);	
		
	
		 
		 List<BeetlToken> tokens = s.getTokens();
		 List<Position> posList = new ArrayList<Position>();
		 for(int i=0;i<tokens.size();i++){
			 BeetlToken token = tokens.get(i);
			 if(token.type == BeetlLexer.TEXT_TT){
				 BeetlTextToken t = (BeetlTextToken)token;
				 int startLine = t.line;
				 int endLine = t.endLine;
				 if(i!=0){
					 BeetlToken pre = tokens.get(i-1);
					 if(pre.line==t.line){
						 if(t.endLine>t.line){
							 //从下一行开始
							 startLine++;
						 }else{
							 //同一行，不折叠
							 continue ;
						 }
						
					 }
				 }
				 
				 if(i<tokens.size()-1){
					 BeetlToken next = tokens.get(i+1);
					 if(next.getCol()!=1){
						 
						 if(t.endLine>t.line){
							 endLine--;
						 }else{
							 //同一行，不折叠
							 continue;
						 }
						
					 }
				 }
					 
					 
				if(startLine<endLine){
					//如果有多行，可以折叠
					 try {
						 //同eclipse编辑器统一
						int start = document.getLineOffset(startLine-1);
						IRegion r = (IRegion)document.getLineInformation(endLine-1);
						String cr = document.getLineDelimiter(endLine-1);
						
						 int len = r.getOffset()-start+r.getLength()+(cr!=null?cr.length():0);
						 Position  pos = new Position(start,len);
						 posList.add(pos);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					continue;
				}
				
				
			 }
		 }
		 
		 editor.updateFoldingStructure(posList);
		// editor.getAnnotationModel().collapseAll(start,end);
		
		 
	}
	
	
	
	
//	public static String[] getBasicConfig(String configPath){
//		File file = new File(configPath);
//		if(!file.exists()){
//			//目前返回默认
//			return new String[]{"<%","%>","${",""};
//		}
//		Properties ps = new Properties();
//		try {
//			ps.load(new FileInputStream(configPath));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return new String[]{"<%","%>","${",""};
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			return new String[]{"<%","%>","${",""};
//		}
//		
//		String ds = ps.getProperty("DELIMITER_STATEMENT_START","<%");
//		String de = ps.getProperty("DELIMITER_STATEMENT_END","%>");
//		if(de==null||de.length()==0||de.equalsIgnoreCase("null")){
//			de = null;
//		}
//		String pls = ps.getProperty("DELIMITER_PLACEHOLDER_START","${");
//		String ple = ps.getProperty("DELIMITER_PLACEHOLDER_END","}");
//		
//		
//		return new String[]{ds,de,pls,ple};
//		
//	}
	
	
	
	

}
