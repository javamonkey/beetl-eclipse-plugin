package org.beetl.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;



public class AutoPairEditStrategy implements IAutoEditStrategy {

	
	public AutoPairEditStrategy() {
	}


	
	private void autoPair(IDocument d, DocumentCommand c,char pair) {

		if (c.offset == -1 || d.getLength() == 0)
			return;

	

			StringBuffer buf= new StringBuffer(c.text);
			buf.append(pair);

			c.text= buf.toString();
			c.offset = c.offset;
			//c.doit = true;
			c.caretOffset = c.offset+1;
			c.shiftsCaret = false;
			c.doit = false;
			
		
		
	}

	/*
	 * @see org.eclipse.jface.text.IAutoEditStrategy#customizeDocumentCommand(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.DocumentCommand)
	 */
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		if (c.length == 0 && c.text != null){
			if( c.text.equals("{")){
				autoPair(d, c,'}');
			}else if(c.text.equals("(")){
				autoPair(d, c,')');
			}
		}
			
			
	}
}
