package org.beetl.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class BeetlEclipseEditor extends TextEditor {

	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR= "matchingBracketsColor";

	
	
	public BeetlEclipseEditor() {
		super();
		
		setSourceViewerConfiguration(new BeetlConfiguration());
		setDocumentProvider(new BeetlDocumentProvider());
	
		
		
	}
	
	
	@Override
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		/*
		 * Set the block matcher
		 */
		support.setCharacterPairMatcher(new BeetlCharacterPairMatcher());
		support.setMatchingCharacterPainterPreferenceKeys(EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(EDITOR_MATCHING_BRACKETS, true);
		store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, "128,128,128");
		
		/**
		 * 配置编辑器上下文 用来绑定  快捷键
		 */
		IContextService contextService = 
		(IContextService) getSite().getService(IContextService.class);
		contextService.activateContext("beetl-eclipse-plugin.beetlcontext");
		
		super.configureSourceViewerDecorationSupport(support);
	}
	
	
	
	public void dispose() {
		
		super.dispose();
	}

}
