package br.gov.lexml.swing.componentes.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixedListComparator<T> implements Comparator<T> {

	private final Map<T,Integer> reference = new HashMap<T,Integer>();
		
	private final Comparator<T> defaultComparator;

	private final List<T> referenceList;
	
	public FixedListComparator(Collection<T> reference,Comparator<T> defaultComparator ) {
		super();							
		this.defaultComparator = defaultComparator;
		this.referenceList = new ArrayList<T>(reference);
		updateOrder();
	}
			
	public FixedListComparator(Collection<T> reference) {
		this(reference,new IdentityComparator<T>());
	}

	@Override
	public int compare(T o1, T o2) {		
		Integer p1 = reference.get(o1);
		Integer p2 = reference.get(o2);
		int res;
		if(p1 != null && p2 != null) {	
			res = p1.compareTo(p2);
		} else if (p1 != null) {
			res = -1;
		} else  if( p2 != null) {
			res = 1;
		} else {
			res = defaultComparator.compare(o1,o2);
		}		
		return res;
	}
	
	public List<T> getReference() {
		return Collections.unmodifiableList(referenceList);		
	}
	
	public FixedListComparator<T> setReference(List<T> reference) {
		this.referenceList.clear();
		this.referenceList.addAll(reference);
		updateOrder();
		return this;
	}
		
	private void updateOrder() {
		int pos = 0;
		for(T e : referenceList) {
			if(!this.reference.containsKey(e)) {
				this.reference.put(e,pos);
				pos++;				
			}
		}
	}
}
