package org.beetl.editors;

import org.beetl.MyDocument;
import org.beetl.MyFastPartitioner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;

public class BeetlDocumentProvider extends FileDocumentProvider {
	
	BeetlEclipseEditor editor = null;
	public BeetlDocumentProvider(BeetlEclipseEditor editor){
		this.editor = editor;
	}

	protected IDocument createDocument(Object element) throws CoreException {
		FileEditorInput file = (FileEditorInput)element;		
		String[] str = ProjectUtil.getProjectDelimter(editor);
		MyDocument document = new MyDocument();
		document.delimter = str;
		
		if (setDocumentContent(document, (IEditorInput) element, getEncoding(element))) {
			setupDocument(element, document);
			
		}
	
		
		if (document != null) {
			IDocumentPartitioner partitioner = new MyFastPartitioner(
					editor,new BeetlPartitionScanner(editor), new String[] {
							BeetlPartitionScanner.PLACE_HOLDER_PART,
							BeetlPartitionScanner.STATIC_TEXT_PART,						
							BeetlPartitionScanner.STATEMENT_PART /*,BeetlPartitionScanner.COMMENT_PART*/});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}

		return document;
	}
}