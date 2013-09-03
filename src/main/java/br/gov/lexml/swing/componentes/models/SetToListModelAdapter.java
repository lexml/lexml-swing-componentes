package br.gov.lexml.swing.componentes.models;

import java.util.Iterator;

import javax.swing.AbstractListModel;

public class SetToListModelAdapter<T> extends AbstractListModel {
	private static final long serialVersionUID = 1L;
	
	private final SetModel<T> setModel;
			
	public SetToListModelAdapter(SetModel<T> sModel) {		
		this.setModel = sModel;	
		setModel.addListener(new SetModelListener<T>() {
			public void elementAdded(T element) {
				int pos = setModel.elements().headSet(element).size();
				fireIntervalAdded(SetToListModelAdapter.this, pos, pos);
			};
			public void elementRemoved(T element) {
				int pos = setModel.elements().headSet(element).size();
				fireIntervalRemoved(SetToListModelAdapter.this, pos, pos);
			};
		});
	}

	@Override
	public int getSize() {
		return setModel.size();
	}

	@Override
	public Object getElementAt(int index) {
		return get(index);
	}

	public T get(int index) {
		if(index >= setModel.size()) { return null ; }
		Iterator<T> i = setModel.elements().iterator();
		while(index > 0) { index--; i.next(); }
		return i.next();
	}
}
