package org.beetl.editor.handler;

import org.beetl.editors.ProjectUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.texteditor.ITextEditor;


public class EditorDocumentListener implements IDocumentListener {
	
	IFile file;
	
	public EditorDocumentListener(IFile file){
		this.file = file;
		
	}
	
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
//		System.out.println("....about "+event.toString());
		
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		
		Document eventDoc = (Document)event.getDocument();
		
//		System.out.println("....change  "+editor.getClass()+"  "+event.toString());
		ITextEditor[] all = ProjectUtil.getEditors(file);
		for(ITextEditor ed:all){
		
			Document doc = ProjectUtil.getDocument(ed);
			doc.removeDocumentListener(this);
		}
		
		for(ITextEditor ed:all){
//			if(ed==this.editor) continue ;
			Document doc = ProjectUtil.getDocument(ed);		
			if(doc==eventDoc) continue;
			try {				
				doc.replace(event.getOffset(), event.getLength(), event.getText());

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue ;
			}
		}
		
		
		for(ITextEditor ed:all){
			Document doc = ProjectUtil.getDocument(ed);
			doc.addDocumentListener(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditorDocumentListener other = (EditorDocumentListener) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
	
	

}
