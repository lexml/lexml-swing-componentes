package br.gov.lexml.swing.componentes

import br.gov.lexml.swing.componentes.models.SetModelListener
import java.awt.event.ActionListener
import java.awt.event.ActionEvent

class ActionToSetModelListener[T](l : ActionListener) extends SetModelListener[T] {
    private def act(action : String) =
      l.actionPerformed(new ActionEvent(null,ActionEvent.ACTION_PERFORMED,action))
	override def elementAdded(x : T) = act("elementAdded")
	override def elementRemoved(x : T) = act("elementRemoved")
}