package br.gov.lexml.swing.componentes

import br.gov.lexml.swing.componentes.models.GenListModelListener
import java.awt.event.ActionListener
import java.awt.event.ActionEvent

class ActionToGenListModelListener[T](l : ActionListener) extends GenListModelListener[T] {
	private def act(action : String) =
      l.actionPerformed(new ActionEvent(null,ActionEvent.ACTION_PERFORMED,action))
    
    override def itemInserted(p : Int, e : T) = act("itemInserted")
    override def itemRemoved(pos : Int,e : T) = act("itemRemoved")
    override def itemMoved(from : Int, to : Int, e : T) = act("itemMoved")
}