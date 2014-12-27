package org.beetl.editors;

import java.util.ArrayList;
import java.util.List;

import org.beetl.MyDocument;
import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class StatementContentAssistProcessor implements IContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer arg0,
			int offset) {

		BeetlTokenSource source = ProjectUtil
				.getBeetlTokenSource((Document) arg0.getDocument());

		MyDocument document = (MyDocument)arg0.getDocument();
		
		Object[] info = source.find(offset);
		if (info != null) {
			BeetlToken token = (BeetlToken) info[0];
			System.out.println("token.getType(): " + token.getType()
					+ "  offset: " + offset);
			if (token.getType() == BeetlLexer.ID_TT) {
				String id = token.getText();
				List<String> list = getString(source, id, offset);
				if (list.size() == 0)
					return null;
				ICompletionProposal[] result = new ICompletionProposal[list
						.size()];
				int i = 0;
				for (String key : list) {

					result[i++] = new CompletionProposal(key, token.start,
							token.end - token.start, key.length());

				}
				return result;

			} else{
				return getDelimit(token,offset,document);
			}

		} else{
			return getDelimit(null,offset,document);
		}
	}

	private ICompletionProposal[] getDelimit(BeetlToken token,int offset,MyDocument document) {
		List<String> list = new ArrayList<String>();
		// 定界符号和占位符
		list.add(document.delimter[0]+document.delimter[1]);
		list.add(document.delimter[2]+document.delimter[3]);

		ICompletionProposal[] result = new ICompletionProposal[list.size()];
		int i = 0;
		for (String key : list) {

			if (token != null) {
				int positionOffset = offset;
				int cursor = offset+2;
				System.out.println("positionOffset: "+positionOffset);
				System.out.println("cursor: "+cursor);
				result[i++] = new CompletionProposal(key, positionOffset,
						0, cursor);
			} else {
				result[i++] = new CompletionProposal(key, 0, 0, 2);
			}

		}
		return result;
	}

	private List<String> getString(BeetlTokenSource source, String id,
			int offset) {
		List<String> list = new ArrayList<String>();

		// 检测之前的变量
		List<BeetlToken> tokens = source.tokens;
		for (BeetlToken t : tokens) {
			if (t.start > offset)
				break;
			if (t.getType() == BeetlLexer.ID_TT) {
				if (t.getText().indexOf(id) != -1) {
					list.add(t.getText());
				}
			}
		}

		// 检测关键字
		for (String keyWord : BeetlLexer.tokens) {
			if (keyWord.indexOf(id) != -1) {
				list.add(keyWord);
			}
		}
		return list;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer arg0,
			int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
