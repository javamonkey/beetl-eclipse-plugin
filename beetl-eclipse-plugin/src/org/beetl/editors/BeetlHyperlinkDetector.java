package org.beetl.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class BeetlHyperlinkDetector extends AbstractHyperlinkDetector {

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		// TODO Auto-generated method stub
		
		SourceViewer source = (SourceViewer)textViewer;
		 IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                 .getActiveWorkbenchWindow().getActivePage(); 
		
		IFile file = ProjectUtil.getInputFile(wbPage.getActiveEditor().getEditorInput());
		String tempaltePath = ProjectUtil.getProjectTemplateRoot(file);
		return new IHyperlink[]{ new BeetlTemplateHyperlink(region,tempaltePath)};
	}

}
