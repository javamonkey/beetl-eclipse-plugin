package org.beetl.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class BeetlConfiguration extends SourceViewerConfiguration {

	

	public BeetlConfiguration() {
		super();
	}
	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		
		BeetlDamagerRepairer dr = new BeetlDamagerRepairer(new BeetlTokenScanner(BeetlPartitionScanner.PLACE_HOLDER_PART));
		reconciler.setDamager(dr, BeetlPartitionScanner.PLACE_HOLDER_PART);
		reconciler.setRepairer(dr, BeetlPartitionScanner.PLACE_HOLDER_PART);
		
		
		dr = new BeetlDamagerRepairer(new BeetlTokenScanner(BeetlPartitionScanner.STATIC_TEXT_PART));
		reconciler.setDamager(dr, BeetlPartitionScanner.STATIC_TEXT_PART);
		reconciler.setRepairer(dr, BeetlPartitionScanner.STATIC_TEXT_PART);
		
		
		
		return reconciler;
	}

}