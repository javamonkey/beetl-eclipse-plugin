package org.beetl.editors;

import org.beetl.MyFastPartitioner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class BeetlDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new MyFastPartitioner(
					new BeetlPartitionScanner(), new String[] {
							BeetlPartitionScanner.PLACE_HOLDER_PART,
							BeetlPartitionScanner.STATIC_TEXT_PART,
							BeetlPartitionScanner.STATEMENT_PART });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}

		return document;
	}
}