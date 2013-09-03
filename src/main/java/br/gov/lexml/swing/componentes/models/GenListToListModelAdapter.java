package br.gov.lexml.swing.componentes.models;

import javax.swing.AbstractListModel;

public class GenListToListModelAdapter<T> extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private final GenListModel<T> listModel;

	public GenListToListModelAdapter(GenListModel<T> listModel) {
		this.listModel = listModel;
		listModel.addListener(new GenListModelListener<T>() {
			public void itemInserted(int pos, T element) {
				fireIntervalAdded(GenListToListModelAdapter.this, pos, pos);
			};

			public void itemRemoved(int pos, T element) {
				fireIntervalRemoved(GenListToListModelAdapter.this, pos, pos);
			};
			public void itemMoved(int from, int to, T element) {
				fireContentsChanged(GenListToListModelAdapter.this, from, to);
			};
		});
	}

	public int getSize() {
		return listModel.size();
	}

	public Object getElementAt(int index) {
		return listModel.get(index);
	}

}
