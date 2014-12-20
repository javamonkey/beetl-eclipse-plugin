package org.beetl.editors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.text.Document;

public class DocumentCache  {
	LRULinkedHashMap<Document,BeetlTokenSource> map = new LRULinkedHashMap<Document,BeetlTokenSource>(50);
	Map<Document,Long> times = new HashMap<Document,Long>();
	public synchronized BeetlTokenSource getTokenSource(Document doc){
		Long time = times.get(doc);
		if(time==null) return null;
		if(doc.getModificationStamp()!=time) return null;
		return map.get(doc);
	}
	
	public synchronized void setTokenSource(Document doc,BeetlTokenSource source){
		map.put(doc, source);
		times.put(doc, doc.getModificationStamp());
	}
	
	private static class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V>{
		int max =1000 ;
		public LRULinkedHashMap(int max){
			this.max  = max ;
		}
		
		protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
			if(this.size()>max){
				return true;
			}else{
				return false ;
			}
		}
	}
	
	
}
