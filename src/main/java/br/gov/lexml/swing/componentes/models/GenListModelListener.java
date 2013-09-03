package br.gov.lexml.swing.componentes.models;

public interface GenListModelListener<T> {
	void itemInserted(int pos, T element);
	void itemRemoved(int pos,T element);
	void itemMoved(int from,int to, T element);
}
