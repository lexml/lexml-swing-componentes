package br.gov.lexml.swing.componentes.models;

import java.awt.Component;

import javax.swing.JList;


public interface GenListCellRender<T> {
	Component getListCellRendererComponent(JList list, T value, int index,
			boolean isSelected, boolean hasFocus);
	Class<T> getElementClass();
} 
