package br.gov.lexml.swing.componentes.models;

import java.util.Collection;
import java.util.SortedSet;


public interface SetModel<T> {

	SetModel<T> cloneModel();
	
	SetModel<T> add(T element);
	SetModel<T> remove(T element);
	SetModel<T> addAll(Collection<T> elements);
	SetModel<T> clear();
	
	
	SortedSet<T> elements();
	int size();
	
	SetModel<T> addListener(SetModelListener<T> listener);
	SetModel<T> removeListener(SetModelListener<T> listener);				
}
