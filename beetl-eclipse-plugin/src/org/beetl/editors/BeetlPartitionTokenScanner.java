package org.beetl.editors;

import java.util.Iterator;
import java.util.List;

import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class BeetlPartitionTokenScanner implements IPartitionTokenScanner {

	public static String PLACE_HOLDER_PART =  "PLACE_HOLDER_PART";
	public static String STATIC_TEXT_PART =  "STATIC_TEXT_PART";
	EclipseTokenSource  source = null;
	Iterator<BeetlToken> it = null;
	BeetlToken current = null;
	int i = 0;
	String contentType = null;
	int offset ;
	int partionOffset ;
	@Override
	public int getTokenLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTokenOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IToken nextToken() {
		if(it.hasNext()){
			BeetlToken t = it.next();
			i
			
		}else{
			return Token.EOF;
		}
		
	}

	@Override
	public void setRange(IDocument arg0, int arg1, int arg2) {
		String text = arg0.get();
		String content = text.substring(arg1,arg1+arg2);
		source = new EclipseTokenSource();
		source.parse(content);
		it = source.tokens.iterator();

	}

	@Override
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
		this.contentType= contentType;
		this.partionOffset= partitionOffset;
		if (partitionOffset > -1) {
			int delta= offset - partitionOffset;
			if (delta > 0) {
				setRange(document, partitionOffset, length + delta);
				this.offset = offset;
				return;
			}
		}
		setRange(document, offset, length);
	}

}
