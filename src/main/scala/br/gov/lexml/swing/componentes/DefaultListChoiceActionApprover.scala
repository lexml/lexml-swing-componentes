package br.gov.lexml.swing.componentes

import br.gov.lexml.swing.componentes.models.ListChoiceActionApprover

class DefaultListChoiceActionApprover[T] extends
		ListChoiceActionApprover[T] {

	override def additionApproved(e : T) = true
	
	override def removalApproved(e : T, p : Int) = true
	
	override def moveApproved(e : T,p : Int) = true
	
}