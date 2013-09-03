package br.gov.lexml.swing.componentes.models;

import java.util.Collection;
import java.util.List;

public interface GenListModel<T> {			
	GenListModel<T> insert(T element,int pos);
	boolean remove(T element);
	T remove(int pos);
	int size();
	
	GenListModel<T> add(T element);
	int indexOf(T element);
	GenListModel<T> clear();
	GenListModel<T> addAll(Collection<T> elements);
	GenListModel<T> replaceWith(Collection<T> elements);
	GenListModel<T> move(int srcPos, int dstPos);
	
	List<T> elements();
	T get(int pos);
	
	GenListModel<T> addListener(GenListModelListener<T> listener);
	GenListModel<T> removeListener(GenListModelListener<T> listener);
}
