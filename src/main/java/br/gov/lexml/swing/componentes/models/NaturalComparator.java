package br.gov.lexml.swing.componentes.models;

import java.util.Comparator;

public class NaturalComparator<T extends Comparable<T>> implements Comparator<T> {	
	public NaturalComparator() { 

	}
	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	};

}
