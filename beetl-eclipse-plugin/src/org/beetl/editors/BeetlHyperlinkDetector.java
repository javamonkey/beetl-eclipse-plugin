package org.beetl.editors;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class BeetlHyperlinkDetector extends AbstractHyperlinkDetector {

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		// TODO Auto-generated method stub
		
		SourceViewer source = (SourceViewer)textViewer;
		 IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                 .getActiveWorkbenchWindow().getActivePage(); 
		
		IFile file = ProjectUtil.getInputFile(wbPage.getActiveEditor().getEditorInput());
		
	
		String content = source.getDocument().get();
		
		BeetlTokenSource s = ProjectUtil.getBeetlTokenSource(content, null,source.getDocument());	
		
		Object[] result = s.find(region.getOffset());
		if(result==null) return null;
		BeetlToken token = (BeetlToken)result[0];
		if(token.getType()==BeetlLexer.STRING_TT){
			
			IPath root = ProjectUtil.getProjectTemplateRoot(file);
//			System.out.println("root:"+root.toOSString());
			if(root==null) return null;
			
			String path = token.text.substring(1,token.text.length()-1);
			
			IFile targetFile = null;
			if(path.startsWith("/")){
				IPath newPath = root.append(path);
				targetFile =ResourcesPlugin.getWorkspace().getRoot().getFile(newPath);
				
			}else {
				//计算相对位置
				targetFile = getPath(file,path);
			
			}
			//正则判断是否以.xxx结尾
//			System.out.println("targetFile:"+targetFile.getLocationURI());
			
		
			Region newRegion = new Region(token.start+1,path.length());
			return new IHyperlink[]{ new BeetlTemplateHyperlink(newRegion,targetFile)};
		}else{
			return null;
		}
//		String tempaltePath = ProjectUtil.getProjectTemplateRoot(file);
		
	}
	
	public IFile getPath(IFile base,String p){
		
			return (IFile)base.getParent().findMember(p);
	
	}

}
