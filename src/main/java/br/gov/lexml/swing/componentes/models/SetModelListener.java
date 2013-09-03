package br.gov.lexml.swing.componentes.models;

public interface SetModelListener<T> {
	void elementAdded(T element);
	void elementRemoved(T element);
}
