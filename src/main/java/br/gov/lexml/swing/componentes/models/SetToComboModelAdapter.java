package br.gov.lexml.swing.componentes.models;

import javax.swing.ComboBoxModel;


public class SetToComboModelAdapter<T> extends SetToListModelAdapter<T>
		implements ComboBoxModel {
	
	private Object selectedItem;
	
	private static final long serialVersionUID = 1L;

	public SetToComboModelAdapter(SetModel<T> setModel) {
		super(setModel);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		this.selectedItem = anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@SuppressWarnings("unchecked")
	public T getSelectedItemG() {
		return (T) selectedItem;
	}	
}
