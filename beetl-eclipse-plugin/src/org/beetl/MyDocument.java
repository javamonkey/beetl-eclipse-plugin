package org.beetl;

import org.eclipse.jface.text.CopyOnWriteTextStore;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.GapTextStore;

public class MyDocument extends Document {
	public MyDocument() {
		super();
		
	}

	/**
	 * Creates a new document with the given initial content.
	 *
	 * @param initialContent the document's initial content
	 */
	public MyDocument(String initialContent) {
		super(initialContent);
	}
}
