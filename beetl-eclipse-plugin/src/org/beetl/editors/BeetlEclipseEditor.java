package org.beetl.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class BeetlEclipseEditor extends TextEditor {

	

	public BeetlEclipseEditor() {
		super();
		
		setSourceViewerConfiguration(new BeetlConfiguration());
		setDocumentProvider(new BeetlDocumentProvider());
	}
	public void dispose() {
		
		super.dispose();
	}

}
