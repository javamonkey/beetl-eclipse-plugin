package org.beetl.editors;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;

public class BeetlTemplateHyperlink implements IHyperlink {
	IRegion region;
	String templatePath;
	
	public BeetlTemplateHyperlink(IRegion region,String templatePath){
		this.region = new Region(region.getOffset(),templatePath.length());
		this.templatePath = templatePath;
		
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
		return templatePath;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		return ;

	}

}
