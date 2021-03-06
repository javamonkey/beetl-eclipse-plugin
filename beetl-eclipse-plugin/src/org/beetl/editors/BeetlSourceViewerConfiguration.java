package org.beetl.editors;

import java.util.HashMap;
import java.util.Map;

import org.beetl.MyIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.URLHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class BeetlSourceViewerConfiguration extends TextSourceViewerConfiguration {

	
	BeetlEclipseEditor editor ;
	public BeetlSourceViewerConfiguration(BeetlEclipseEditor editor) {
		super();
		this.editor = editor;
		
		
			
			
	}
	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		
		BeetlDamagerRepairer dr = new BeetlDamagerRepairer(new BeetlTokenScanner(BeetlPartitionScanner.PLACE_HOLDER_PART));
		reconciler.setDamager(dr, BeetlPartitionScanner.PLACE_HOLDER_PART);
		reconciler.setRepairer(dr, BeetlPartitionScanner.PLACE_HOLDER_PART);
		
		
		
		dr = new BeetlDamagerRepairer(new BeetlTokenScanner(BeetlPartitionScanner.STATIC_TEXT_PART));
		reconciler.setDamager(dr, BeetlPartitionScanner.STATIC_TEXT_PART);
		reconciler.setRepairer(dr, BeetlPartitionScanner.STATIC_TEXT_PART);
		
		
		dr = new BeetlDamagerRepairer(new BeetlTokenScanner(BeetlPartitionScanner.STATEMENT_PART));
		reconciler.setDamager(dr, BeetlPartitionScanner.STATEMENT_PART);
		reconciler.setRepairer(dr, BeetlPartitionScanner.STATEMENT_PART);
		/*
		dr = new BeetlDamagerRepairer(new BeetlTokenScanner(BeetlPartitionScanner.COMMENT_PART));
		reconciler.setDamager(dr, BeetlPartitionScanner.COMMENT_PART);
		reconciler.setRepairer(dr, BeetlPartitionScanner.COMMENT_PART);
		*/
		
		final ISourceViewer temp =  sourceViewer;
		sourceViewer.getSelectionProvider().addSelectionChangedListener(new TokenSelectionChangedListener ());
		
		return reconciler;
	}
	
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer,
            String contentType){
		
		return new BeetlDoubleClickStrategy (editor);
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
	{

	    ContentAssistant assistant = new ContentAssistant();

	    IContentAssistProcessor tagContentAssistProcessor 
	        = new StatementContentAssistProcessor();
	    assistant.setContentAssistProcessor(tagContentAssistProcessor,
	    		BeetlPartitionScanner.STATEMENT_PART);
	    assistant.setContentAssistProcessor(tagContentAssistProcessor,
	    		BeetlPartitionScanner.PLACE_HOLDER_PART);
	    assistant.setContentAssistProcessor(tagContentAssistProcessor,
	    		BeetlPartitionScanner.STATIC_TEXT_PART);
	   
	    assistant.enableAutoActivation(true);
	    assistant.setAutoActivationDelay(500);
	    assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    return assistant;

	}
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer,
            String contentType){
		if(contentType.equals(BeetlPartitionScanner.STATEMENT_PART)||contentType.equals(BeetlPartitionScanner.PLACE_HOLDER_PART)||contentType.equals(BeetlPartitionScanner.STATIC_TEXT_PART)){
			return new IAutoEditStrategy[]{new MyIndentLineAutoEditStrategy(),new AutoPairEditStrategy()};
		}else return new IAutoEditStrategy[]{new MyIndentLineAutoEditStrategy()};
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer){
		return new String[]{BeetlPartitionScanner.STATEMENT_PART,BeetlPartitionScanner.STATIC_TEXT_PART,BeetlPartitionScanner.PLACE_HOLDER_PART/*,BeetlPartitionScanner.COMMENT_PART*/};
	}

	
	protected Map getHyperlinkDetectorTargets(ISourceViewer sourceViewer) {
		Map targets= new HashMap();
		targets.put(EditorsUI.DEFAULT_TEXT_EDITOR_ID, null);
		targets.put(ProjectUtil.editorId, null);
		return targets;
	}
	
	
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return new IHyperlinkDetector[] { new URLHyperlinkDetector() ,new BeetlHyperlinkDetector()};
	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {

		
		if (editor != null && editor.isEditable()) {

//			JavaCompositeReconcilingStrategy strategy= new JavaCompositeReconcilingStrategy(sourceViewer, editor, getConfiguredDocumentPartitioning(sourceViewer));
//			JavaReconciler reconciler= new JavaReconciler(editor, strategy, false);
//			reconciler.setIsAllowedToModifyDocument(false);
//			reconciler.setDelay(500);
//
//			return reconciler;
			BeetlReconcilingStrategy stra = new BeetlReconcilingStrategy(editor);
			MonoReconciler reconciler= new MonoReconciler(stra, false);
			reconciler.setDelay(500);
			return reconciler;
		}
		return null;
	}
}