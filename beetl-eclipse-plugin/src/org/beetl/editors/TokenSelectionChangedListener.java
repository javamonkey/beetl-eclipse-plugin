package org.beetl.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

public class TokenSelectionChangedListener implements ISelectionChangedListener {

	StyleRange[] old = null;
	int[] pos = null;
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		TextSelection selction = (TextSelection)event.getSelection() ;
		
//		System.out.println(selction.getText());
		
		if (BeetlLexer.contains(selction.getText())) {
			return;
		}
		
		if(selction.getText().contains("\r")||selction.getText().contains("\n")){
			return ;
		}
	
		int offset = selction.getOffset();
		int len = selction.getLength();
		SourceViewer view =(SourceViewer)event.getSource();
		StyledText st = view.getTextWidget();
		if(len==0){
			//System.out.println("redraw:"+Arrays.asList(old));
			reset(st);
			return ;
		}
		reset(st);
		String text = view.getDocument().get();
		BeetlTokenSource source = ProjectUtil.getBeetlTokenSource(text,null, view.getDocument());
	
		Object[] info = source.find(offset+1);
		if(info==null)return ;
		BeetlToken t = (BeetlToken)info[0];
		int index = (Integer)info[1];
		if(t.getStart()==offset&&t.getEnd()==(offset+len)){
			
			List<BeetlToken> tokens = this.findSameTokens(index, source, t);
			List<StyleRange> oldList = new ArrayList<StyleRange>();
			List<StyleRange> newList = new ArrayList<StyleRange>();
			
			for(BeetlToken token:tokens){
				StyleRange[] old1 = st.getStyleRanges(token.start , token.end-token.start);
				if(old1!=null&&old1.length!=0){
					for(StyleRange old2:old1){
						oldList.add(old2);
					}
				}
				
				StyleRange newStyle =  new StyleRange();
				newStyle.background = ColorManager.instance().getColor(SyntaxColorConstants.ERROR);
				newStyle.start = token.start;
				newStyle.length = token.end-token.start;
				st.setStyleRange(newStyle);
				
				
			}
			
			if(oldList.size()==0){
				this.old=null;
			}else{
				this.old=oldList.toArray(new StyleRange[0]);
			}
//			StyleRange sr = new StyleRange();
//			sr.background = ColorManager.instance().getColor(SyntaxColorConstants.ERROR);
//			sr.start = offset-3;
//			sr.length = 3;
//			old = st.getStyleRanges(sr.start , 3);
//			lastStart = sr.start;
//			lastLen = 3;
//			st.setStyleRange(sr);
//			System.out.println(Arrays.asList(old));
		}
		
		
		
	}
	
	private List<BeetlToken> findSameTokens(int index,BeetlTokenSource source,BeetlToken t ){
		 List<BeetlToken> list = new ArrayList<BeetlToken> ();
		for(int i=1;i<source.tokens.size();i++){
			BeetlToken token = source.tokens.get(i);
			if(token.getType()==t.getType()&&token.getText().equals(t.getText())){
				list.add(token);
			}
		}
		return list;
	}
	
	private void reset(StyledText st ){
		if(old!=null){
			
			for(StyleRange sr:old){
				st.replaceStyleRanges(sr.start, sr.length, new StyleRange[]{sr});
			}
			//st.setStyleRanges(old);
			
			old=null;
		
		}
	}

}
