package org.beetl.editors;

import java.awt.Toolkit;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.*;

public class BeetlDoubleClickStrategy extends DefaultTextDoubleClickStrategy {
	protected ITextViewer fText;
	BeetlEclipseEditor editor = null;
	public BeetlDoubleClickStrategy(BeetlEclipseEditor editor){
		this.editor = editor;
	}
	
	public void doubleClicked(ITextViewer part) {
		int offset= part.getSelectedRange().x;

		if (offset < 0)
			return;
		final Document document= (Document)part.getDocument();
		
		BeetlTokenSource source = ProjectUtil.getBeetlTokenSource(document);	
		//看前面一个字符
		offset--;
		Object[] info = source.find(offset);
		if(info==null) super.doubleClicked(part);
		BeetlToken t = (BeetlToken)info[0];
		int index = (Integer)info[1];
		if(t.getType()==BeetlLexer.LEFT_BRACE_TT||t.getType()==BeetlLexer.RIGHT_BRACE_TT){
			
			BeetlToken token = source.findPair(offset, BeetlLexer.LEFT_BRACE_TT,
					BeetlLexer.RIGHT_BRACE_TT);
			if (token == null) {
				Toolkit.getDefaultToolkit().beep();
//				return null;
			}
		
			editor.selectAndReveal(offset, token.end-offset);
			return ;
			
			
			
		}else if(t.getType()==BeetlLexer.LEFT_PAR_TT||t.getType()==BeetlLexer.RIGHT_PAR_TT){
			BeetlToken token = source.findPair(offset, BeetlLexer.LEFT_PAR_TT,
					BeetlLexer.RIGHT_PAR_TT);
			if (token == null) {
				Toolkit.getDefaultToolkit().beep();
//				return null;
			}
			
			editor.selectAndReveal(offset, token.end-offset);
			return ;
			
		}
		super.doubleClicked(part);
		
//		int pos = part.getSelectedRange().x;
//
//		if (pos < 0)
//			return;
//		part.getTextWidget().setSelection(5);
//		fText = part;
//
//		if (!selectComment(pos)) {
//			selectWord(pos);
//		}
	}
//	protected boolean selectComment(int caretPos) {
//		IDocument doc = fText.getDocument();
//		int startPos, endPos;
//
//		try {
//			int pos = caretPos;
//			char c = ' ';
//
//			while (pos >= 0) {
//				c = doc.getChar(pos);
//				if (c == '\\') {
//					pos -= 2;
//					continue;
//				}
//				if (c == Character.LINE_SEPARATOR || c == '\"')
//					break;
//				--pos;
//			}
//
//			if (c != '\"')
//				return false;
//
//			startPos = pos;
//
//			pos = caretPos;
//			int length = doc.getLength();
//			c = ' ';
//
//			while (pos < length) {
//				c = doc.getChar(pos);
//				if (c == Character.LINE_SEPARATOR || c == '\"')
//					break;
//				++pos;
//			}
//			if (c != '\"')
//				return false;
//
//			endPos = pos;
//
//			int offset = startPos + 1;
//			int len = endPos - offset;
//			fText.setSelectedRange(offset, len);
//			return true;
//		} catch (BadLocationException x) {
//		}
//
//		return false;
//	}
//	protected boolean selectWord(int caretPos) {
//
//		IDocument doc = fText.getDocument();
//		int startPos, endPos;
//
//		try {
//
//			int pos = caretPos;
//			char c;
//
//			while (pos >= 0) {
//				c = doc.getChar(pos);
//				if (!Character.isJavaIdentifierPart(c))
//					break;
//				--pos;
//			}
//
//			startPos = pos;
//
//			pos = caretPos;
//			int length = doc.getLength();
//
//			while (pos < length) {
//				c = doc.getChar(pos);
//				if (!Character.isJavaIdentifierPart(c))
//					break;
//				++pos;
//			}
//
//			endPos = pos;
//			selectRange(startPos, endPos);
//			return true;
//
//		} catch (BadLocationException x) {
//		}
//
//		return false;
//	}
//
//	private void selectRange(int startPos, int stopPos) {
//		int offset = startPos + 1;
//		int length = stopPos - offset;
//		fText.setSelectedRange(offset, length);
//	}
}