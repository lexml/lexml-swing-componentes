package br.gov.lexml.swing.componentes.models;

public abstract class AbstractGenListCellRenderer<T> implements GenListCellRender<T> {
	private final Class<T> clazz;
	protected AbstractGenListCellRenderer(Class<T> clazz) {
		this.clazz = clazz;
	}
	@Override
	public final Class<T> getElementClass() {
		return clazz;
	}
	
}
