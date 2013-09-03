package br.gov.lexml.swing.componentes;

import javax.swing.JComboBox;

import br.gov.lexml.swing.componentes.models.DefaultSetModel;
import br.gov.lexml.swing.componentes.models.NaturalComparator;
import br.gov.lexml.swing.componentes.models.SetModel;
import br.gov.lexml.swing.componentes.models.SetToComboModelAdapter;

public class JSetComboBox<T> extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	private final SetModel<T> setModel;
	private final SetToComboModelAdapter<T> adapter;
	public JSetComboBox(SetModel<T> model) {
		super();
		this.setModel = model;
		adapter = new SetToComboModelAdapter<T>(model);
		setModel(adapter);				
	}
	public SetModel<T> getSetModel() {
		return setModel;
	}
	
	public static <T extends Comparable<T>> JSetComboBox<T> newNaturalJSetComboBox() {
		return new JSetComboBox<T>(new DefaultSetModel<T>(new NaturalComparator<T>()));
	}
}
