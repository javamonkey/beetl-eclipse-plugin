package org.beetl.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.antlr.AntlrUtil;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.swt.widgets.Display;

public class BeetlReconcilingStrategy implements IReconcilingStrategy {

	IDocument document = null;
	boolean hasError = false ;
	
	BeetlEclipseEditor editor ;
	public BeetlReconcilingStrategy(
			BeetlEclipseEditor editor){
		this.editor = editor;
		
	}
	@Override
	public void setDocument(IDocument document) {
		this.document = document;

	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		printError();

	}

	@Override
	public void reconcile(IRegion partition) {
		printError();
	}
	
	public void printError(){
		

	
		
		AntlrUtil util = new AntlrUtil();
		try {
			Map<Integer,String> error = util.getErrorInfo(document);
			if(error.isEmpty()){
				if(hasError){
					editor.removeErrorNode();
					hasError = false;
				}
				return ;
			}
			List<Position> list = new ArrayList<Position>();
			List<String> msgs = new ArrayList<String>();
			for(Entry<Integer,String> entry:error.entrySet()){
				int line = entry.getKey()-1;
				int offset = document.getLineInformation(line).getOffset();
				String msg = entry.getValue();
				Position pos = new Position(offset);
				list.add(pos);
				msgs.add(msg);
			}
			
			editor.updateErrorNode(list,msgs);
			hasError = true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}

}
