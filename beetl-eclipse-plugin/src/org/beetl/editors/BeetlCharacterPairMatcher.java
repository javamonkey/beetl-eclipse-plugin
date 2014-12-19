package org.beetl.editors;

import java.util.List;
import java.util.Stack;

import org.beetl.core.parser.BeetlLexer;
import org.beetl.core.parser.BeetlToken;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
/**
 * 
 * @author joelli
 *
 */

public class BeetlCharacterPairMatcher implements ICharacterPairMatcher {

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAnchor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IRegion match(IDocument doc, int offset) {
		String text = doc.get();
		BeetlTokenSource source = ProjectUtil.getBeetlTokenSource(text, null,doc);	
		Object[] info = source.find(offset);
		if(info==null) return null;
		BeetlToken t = (BeetlToken)info[0];
		int index = (Integer)info[1];
		if(t.getType()==BeetlLexer.LEFT_BRACE_TT){
			BeetlToken pair = findRight(source,BeetlLexer.LEFT_BRACE_TT,BeetlLexer.RIGHT_BRACE_TT,index);
			if(pair==null) return null;
			Region r = new Region(pair.start,pair.end-pair.start);
			return r ;
			
		}else if(t.getType()==BeetlLexer.LEFT_PAR_TT){
			BeetlToken pair = findRight(source,BeetlLexer.LEFT_PAR_TT,BeetlLexer.RIGHT_PAR_TT,index);
			if(pair==null) return null;
			Region r = new Region(pair.start,pair.end-pair.start);
			return r ;
			
		}
		return null;
	}
	
	private BeetlToken findRight(BeetlTokenSource source,int lt,int rt,int index){
		List<BeetlToken> tokens = source.tokens;
		int k = 0;
		for(int i=index+1;i<tokens.size();i++){
			BeetlToken target = tokens.get(i);
			if(target.getType()==lt){
				k++;
				continue;
			}
			if(target.getType()==rt){
				if(k==0){
					return target ;
				}else{
					k--;
				}
				
			}
		}
		
		return null;
		
	}

}
