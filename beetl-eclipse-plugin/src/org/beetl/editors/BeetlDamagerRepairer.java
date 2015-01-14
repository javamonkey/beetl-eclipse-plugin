package org.beetl.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

public class BeetlDamagerRepairer extends DefaultDamagerRepairer {

	public BeetlDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
		// TODO Auto-generated constructor stub
	}
	
	
	public void createPresentation(TextPresentation presentation, ITypedRegion region) {

		if (fScanner == null) {
			// will be removed if deprecated constructor will be removed
			addRange(presentation, region.getOffset(), region.getLength(), fDefaultTextAttribute);
			return;
		}

		int lastStart= region.getOffset();
		int length= 0;
		boolean firstToken= true;
		IToken lastToken= Token.UNDEFINED;
		TextAttribute lastAttribute= getTokenTextAttribute(lastToken);

		fScanner.setRange(fDocument, lastStart, region.getLength());

		while (true) {
			IToken token= fScanner.nextToken();
			if (token.isEOF())
				break;

			TextAttribute attribute= getTokenTextAttribute(token);
			if (lastAttribute != null && lastAttribute.equals(attribute)) {
				length += fScanner.getTokenLength();
				firstToken= false;
			} else {
				if (!firstToken)
					addRange(presentation, lastStart, length, lastAttribute);
				firstToken= false;
				lastToken= token;
				lastAttribute= attribute;
				lastStart= fScanner.getTokenOffset();
				length= fScanner.getTokenLength();
			}
		}

		addRange(presentation, lastStart, length, lastAttribute);
	}
	
	protected void addRange(TextPresentation presentation, int offset, int length, TextAttribute attr) {
		if (attr != null) {
			int style= attr.getStyle();
			int fontStyle= style & (SWT.ITALIC | SWT.BOLD | SWT.NORMAL);
			StyleRange styleRange= new StyleRange(offset, length, attr.getForeground(), attr.getBackground(), fontStyle);
			styleRange.strikeout= (style & TextAttribute.STRIKETHROUGH) != 0;
			styleRange.underline= (style & TextAttribute.UNDERLINE) != 0;
			styleRange.font= attr.getFont();
			presentation.addStyleRange(styleRange);
		}
	}
	
	protected   TextAttribute getTokenTextAttribute(IToken token) {
		
	
		TextAttribute attr =  (TextAttribute) token.getData();
//		if(attr==null){
//			return new TextAttribute(ColorManager.instance().getColor(SyntaxColorConstants.STATIC_TEXT));
//		}else{
//			return attr;
//		}
	   return attr;
		
	}
	
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {

//		if (!documentPartitioningChanged) {
//			try {
//
//				IRegion info= fDocument.getLineInformationOfOffset(e.getOffset());
//				int start= Math.max(partition.getOffset(), info.getOffset());
//
//				int end= e.getOffset() + (e.getText() == null ? e.getLength() : e.getText().length());
//
//				if (info.getOffset() <= end && end <= info.getOffset() + info.getLength()) {
//					// optimize the case of the same line
//					end= info.getOffset() + info.getLength();
//				} else
//					end= endOfLineOf(end);
//
//				end= Math.min(partition.getOffset() + partition.getLength(), end);
//				return new Region(start, end - start);
//
//			} catch (BadLocationException x) {
//			}
//		}

		return partition;
	}


}
