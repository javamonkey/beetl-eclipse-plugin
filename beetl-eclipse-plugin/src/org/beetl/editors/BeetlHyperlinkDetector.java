package org.beetl.editors;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.core.resources.IFile;
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
		BeetlTokenSource s = new BeetlTokenSource(null);
		s.parse(content);
		Object[] result = s.find(region.getOffset());
		if(result==null) return null;
		BeetlToken token = (BeetlToken)result[0];
		if(token.getType()==BeetlLexer.STRING_TT){
			String path = token.text.substring(1,token.text.length()-1);
			IFile targetFile = file.getProject().getFile(path);
			Region newRegion = new Region(token.start+1,token.end-token.start-1);
			return new IHyperlink[]{ new BeetlTemplateHyperlink(newRegion,targetFile)};
		}else{
			return null;
		}
//		String tempaltePath = ProjectUtil.getProjectTemplateRoot(file);
		
	}

}
