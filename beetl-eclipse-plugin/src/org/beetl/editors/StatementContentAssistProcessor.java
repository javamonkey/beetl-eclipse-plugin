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
		if(offset==0){
			//刚开始
			return getDelimit(offset,document);
		}
		Object[] info = source.find(offset-1);
		if (info != null) {
		
			BeetlToken pre = (BeetlToken)info[0];
			if(pre.getType()==BeetlLexer.PH_SE_TT||pre.getType()==BeetlLexer.ST_SE_TT){
				//静态文本
				return getDelimit(offset,document);
			}else{
				return showInStatment((MyDocument)document,source,(BeetlToken)info[0],offset);
			}
		
		
		}
		return null;
		
	}
	
	private ICompletionProposal[] showInStatment(MyDocument document,BeetlTokenSource source,BeetlToken token,int offset){
		
		if(token.getType()!=BeetlLexer.TEXT_TT){
			String id = null;
			if (token.getType() == BeetlLexer.ID_TT) {
				id = token.getText();
			
			}
			
			List<String> list = getString(source, id, offset);
			if(id!=null)list.remove(id);
			if (list.size() == 0)
				return null;
			ICompletionProposal[] result = new ICompletionProposal[list
					.size()];
			int i = 0;
			for (String key : list) {

				result[i++] = new CompletionProposal(key,id!=null?token.start:offset,
						id!=null?id.length():0, key.length());

			}
			return result;
		}else{
			return getDelimit(offset,document);
		}
	}

	private ICompletionProposal[] getDelimit(int offset,MyDocument document) {
		List<String> list = new ArrayList<String>();
		// 定界符号和占位符
		boolean hasCR = document.delimter[1].indexOf("\r")!=-1||document.delimter[1].indexOf("\n")!=-1;
		String key = document.delimter[0]+(hasCR?"":document.delimter[1]);
		String key2= document.delimter[2]+document.delimter[3];

		ICompletionProposal[] result = new ICompletionProposal[2];
		
		result[0] = new CompletionProposal(key, offset,	0, document.delimter[0].length());
		result[1] = new CompletionProposal(key2, offset,0, document.delimter[2].length());
		
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
				if (id==null||t.getText().indexOf(id) != -1) {
					list.add(t.getText());
				}
			}
		}

//		// 检测关键字
//		for (String keyWord : BeetlLexer.tokenSet) {
//			if (id==null||keyWord.indexOf(id) != -1) {
//				list.add(keyWord);
//			}
//		}
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
