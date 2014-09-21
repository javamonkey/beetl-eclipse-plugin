package org.beetl.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class BeetlDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		  if (document != null)
		    {
		        IDocumentPartitioner partitioner = new FastPartitioner(
		         new BeetlPartitionTokenScanner(), new String[]
		        {
		        	 BeetlPartitionTokenScanner.PLACE_HOLDER_PART,
		        	 BeetlPartitionTokenScanner.STATIC_TEXT_PART
		        });
		        partitioner.connect(document);
		        document.setDocumentPartitioner(partitioner);
		    }

		return document;
	}
}