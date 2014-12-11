package org.beetl.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class BeetlTemplateHyperlink implements IHyperlink {
	IRegion region;
	IFile targetFile;
	
	public BeetlTemplateHyperlink(IRegion region,IFile targetFile){
		this.region = region;
		this.targetFile = targetFile;
		
	}
	
	@Override
	public IRegion getHyperlinkRegion() {
		// TODO Auto-generated method stub
		return region;
	}

	@Override
	public String getTypeLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHyperlinkText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
//		System.out.println(templatePath);
		IWorkbenchPage wbPage = PlatformUI.getWorkbench()  
                .getActiveWorkbenchWindow().getActivePage();  
	
		try {
			wbPage.openEditor(new FileEditorInput(targetFile), ProjectUtil.editorId, true, IWorkbenchPage.MATCH_ID|IWorkbenchPage.MATCH_INPUT);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}

	}

}
