package br.gov.lexml.swing.componentes.models;

import java.util.Comparator;


public class IdentityComparator<T> implements Comparator<T> {
	public IdentityComparator() {
		
	}

	@Override
	public int compare(T o1, T o2) {
		return System.identityHashCode(o1) - System.identityHashCode(o2);
	}	
}
