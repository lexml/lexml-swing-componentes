package br.gov.lexml.swing.componentes.models;


public interface ListChoiceActionApprover<T> {
	boolean additionApproved(T element);
	boolean removalApproved(T element, int idx);
	boolean moveApproved(T element, int idx);
}
