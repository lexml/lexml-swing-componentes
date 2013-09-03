package br.gov.lexml.swing.componentes.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DefaultSetModel<T> implements SetModel<T> {

	private final Comparator<T> comparator;
	
	private final SortedSet<T> elements;

	private final Set<SetModelListener<T>> listeners = new HashSet<SetModelListener<T>>();
	
	public DefaultSetModel(Comparator<T> comparator) {
		this.comparator = comparator;
		this.elements = new TreeSet<T>(comparator);
	}
	private void notify(Notification n) {
		for (SetModelListener<T> listener : listeners) {
			n.notify(listener);
		}
	}

	private abstract class Notification {
		public abstract void notify(SetModelListener<T> listener);
	}

	public synchronized SetModel<T> add(final T element) {
		if (elements.add(element)) {
			notify(new Notification() {
				@Override
				public void notify(SetModelListener<T> listener) {
					listener.elementAdded(element);
				}
			});
		}
		return this;
	}

	public SetModel<T> remove(final T element) {
		if (elements.remove(element)) {
			notify(new Notification() {
				@Override
				public void notify(SetModelListener<T> listener) {
					listener.elementRemoved(element);
				}
			});
		}
		return this;
	}

	public SetModel<T> addAll(Collection<T> elements) {
		for (T element : elements) {
			add(element);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public SetModel<T> clear() {
		for (Object o : elements.toArray()) {
			remove((T) o);
		}
		return this;
	}

	public SortedSet<T> _elements() {
		return elements;
	}
	public SortedSet<T> elements() {		
		return Collections.unmodifiableSortedSet(elements);
	}
	
	@Override
	public int size() {
		return elements.size();
	}

	public SetModel<T> addListener(SetModelListener<T> listener) {
		listeners.add(listener);
		return this;
	}

	public SetModel<T> removeListener(SetModelListener<T> listener) {
		listeners.remove(listener);
		return this;
	}
	
	@Override
	public SetModel<T> cloneModel() {
		SetModel<T> newModel = new DefaultSetModel<T>(comparator);
		newModel.addAll(elements);
		return newModel;
	}

	public static <T extends Comparable<T>> SetModel<T> newNaturalOrderModel() {
		return new DefaultSetModel<T>(new NaturalComparator<T>());
	}
}
