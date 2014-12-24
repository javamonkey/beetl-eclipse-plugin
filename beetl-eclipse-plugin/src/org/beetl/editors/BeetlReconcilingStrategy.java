package org.beetl.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

public class BeetlReconcilingStrategy implements IReconcilingStrategy {

	IDocument document = null;
	@Override
	public void setDocument(IDocument document) {
		this.document = document;

	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		printError();

	}

	@Override
	public void reconcile(IRegion partition) {
		printError();
	}
	
	public void printError(){
		
	}

}
