package br.gov.lexml.swing.componentes.models;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class GenListToListCellRendererAdapter<T> implements ListCellRenderer {
	private final GenListCellRender<T> renderer;

	public GenListToListCellRendererAdapter(GenListCellRender<T> renderer) {
		this.renderer = renderer;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		return renderer.getListCellRendererComponent(list, renderer
				.getElementClass().cast(value), index, isSelected, cellHasFocus);
	}
}
