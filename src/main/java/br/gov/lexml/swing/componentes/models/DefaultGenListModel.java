package br.gov.lexml.swing.componentes.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultGenListModel<T> implements GenListModel<T> {
		
	private final List<T> elements = new ArrayList<T>();

	private final Set<GenListModelListener<T>> listeners = new HashSet<GenListModelListener<T>>();

	private abstract class Notification {
		public abstract void notify(GenListModelListener<T> listener);
	}
	
	public DefaultGenListModel() { }

	public DefaultGenListModel(Collection<T> elements) {		
		this.elements.addAll(elements);
	}



	private void notify(Notification n) {
		for (GenListModelListener<T> listener : listeners) {
			n.notify(listener);
		}
	}

	public GenListModel<T> insert(final T element, final int pos) {
		if (pos >= 0 && pos <= elements.size()) {
			elements.add(pos, element);
			notify(new Notification() {
				@Override
				public void notify(GenListModelListener<T> listener) {
					listener.itemInserted(pos, element);
				}
			});
		}
		return this;
	}
	public GenListModel<T> insertAll(final int pos, final Collection<T> elements) {
		if (pos >= 0 && pos <= elements.size()) {
			this.elements.addAll(pos,elements);			
			notify(new Notification() {
				@Override
				public void notify(GenListModelListener<T> listener) {
					for(int i=0;i<elements.size();i++) {
						int p = pos + i;					
						listener.itemInserted(p, DefaultGenListModel.this.elements.get(p));
					}
				}
			});
		}
		return this;
	}

	public int indexOf(T element) {
		return elements.indexOf(element);
	}

	public boolean remove(T element) {
		int idx = elements.indexOf(element);
		if (idx >= 0) {
			remove(idx);
			return true;
		} else {
			return false;
		}
	}

	public T remove(final int pos) {
		if (pos >= 0 && pos < elements.size()) {
			final T element = elements.remove(pos);
			notify(new Notification() {
				@Override
				public void notify(GenListModelListener<T> listener) {
					listener.itemRemoved(pos, element);
				}
			});
			return element;
		}
		return null;
	}

	public int size() {
		return elements.size();
	}

	public GenListModel<T> add(T element) {
		return insert(element, elements.size());
	}

	public GenListModel<T> clear() {
		while (!elements.isEmpty()) {
			remove(0);
		}
		return this;
	}

	public GenListModel<T> addAll(Collection<T> elements) {
		insertAll(this.elements.size(),elements);		
		return this;
	}

	public GenListModel<T> move(final int srcPos, final int dstPos) {
		if (srcPos != dstPos && srcPos >= 0 && srcPos < elements.size()
				&& dstPos >= 0 && dstPos < elements.size()) {
			final T element = elements.remove(srcPos);
			elements.add(dstPos, element);
			notify(new Notification() {
				@Override
				public void notify(GenListModelListener<T> listener) {
					listener.itemMoved(srcPos, dstPos, element);

				}
			});
		}
		return this;
	}

	public List<T> elements() {
		return Collections.unmodifiableList(new ArrayList<T>(elements));
	}

	public T get(int pos) {
		return elements.get(pos);
	}

	public GenListModel<T> addListener(GenListModelListener<T> listener) {
		listeners.add(listener);
		return this;
	}

	public GenListModel<T> removeListener(GenListModelListener<T> listener) {
		listeners.remove(listener);
		return this;
	}

	public GenListModel<T> replaceWith(Collection<T> elements) {
		clear(); // FIXME: implementar direito
		addAll(elements);
		return this;
	}
	public String toString() {
		return System.identityHashCode(this) + ": " + elements.toString();
	}

}
