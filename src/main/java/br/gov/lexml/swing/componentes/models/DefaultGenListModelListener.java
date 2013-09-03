package br.gov.lexml.swing.componentes.models;

public class DefaultGenListModelListener<T> implements GenListModelListener<T> {

	@Override
	public void itemInserted(int pos, T element) {}

	@Override
	public void itemRemoved(int pos, T element) {}

	@Override
	public void itemMoved(int from, int to, T element) {}

}
