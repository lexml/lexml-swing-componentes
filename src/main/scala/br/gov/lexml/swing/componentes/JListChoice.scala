package br.gov.lexml.swing.componentes
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import scala.annotation.implicitNotFound
import br.gov.lexml.swing.componentes.models.DefaultSetModelListener
import br.gov.lexml.swing.componentes.models.SetModelListener
import br.gov.lexml.swing.componentes.models.DefaultGenListModelListener
import br.gov.lexml.swing.componentes.models.GenListCellRender
import br.gov.lexml.swing.componentes.models.GenListModel
import br.gov.lexml.swing.componentes.models.GenListToListCellRendererAdapter
import br.gov.lexml.swing.componentes.models.GenListToListModelAdapter
import br.gov.lexml.swing.componentes.models.SetModel
import br.gov.lexml.swing.componentes.models.SetToListModelAdapter
import br.gov.lexml.swing.componentes.models.ListChoiceActionApprover
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.SwingUtilities
import javax.swing.JButton
import java.awt.Dimension
import scala.collection.JavaConversions._
import javax.swing.BorderFactory
import java.awt.Component
import javax.swing.JScrollPane
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import scala.Function2
import scala.collection.SortedSet
import br.gov.lexml.swing.componentes.models.ListChoiceActionApprover
import br.gov.lexml.swing.componentes.models.DefaultSetModel
import br.gov.lexml.swing.componentes.models.DefaultGenListModel
import br.gov.lexml.swing.componentes.models.NaturalComparator

class JListChoice[T](domainModel: SetModel[T], destModel: GenListModel[T],
  renderer: GenListCellRender[T],
  _approver: ListChoiceActionApprover[T])
  extends Box(BoxLayout.X_AXIS) {
    
  setApprover(_approver)

  var statusChecker = List[() ⇒ Unit]()

  val sourceModel = domainModel.cloneModel

  val sourceListener = new DefaultSetModelListener[T] {
    override def elementAdded(e: T) = invokeLater(destModel.remove(e))
    override def elementRemoved(e: T) = invokeLater { if (destModel.indexOf(e) < 0) destModel.add(e) }
  }

  val destListener = new DefaultGenListModelListener[T] {
    override def itemInserted(p: Int, e: T) = invokeLater(sourceModel.remove(e))
    override def itemRemoved(p: Int, e: T) = invokeLater(sourceModel.add(e))
  }

  val domainListener = new SetModelListener[T] {
    override def elementAdded(e: T) = invokeLater(sourceModel.add(e))

    override def elementRemoved(e: T) = invokeLater {
      unplugListeners()
      sourceModel.remove(e)
      destModel.remove(e)
      plugListeners()
    }

  }

  domainModel.addListener(domainListener)

  plugListeners()

  val sourceModelAdapter = new SetToListModelAdapter(sourceModel)
  val sourceList = new JList(sourceModelAdapter)

  val sourceMouseListener = doubleClickListener { e ⇒
    val idx = sourceList.locationToIndex(e.getPoint)
    val element = sourceModelAdapter.get(idx)
    if (approver.additionApproved(element)) {
      invokeLater {
        sourceModel.remove(element)
      }
    }
  }

  sourceList.addMouseListener(sourceMouseListener)

  val destList = new JList(new GenListToListModelAdapter(destModel))

  val destMouseListener = doubleClickListener { e ⇒
    invokeLater {
      val idx = destList.locationToIndex(e.getPoint)
      val element = destModel.get(idx)
      if (approver.removalApproved(element, idx)) {
        destModel.remove(element)
      }
    }
  }
  destList.addMouseListener(destMouseListener)

  if (renderer != null) {
    sourceList.setCellRenderer(new GenListToListCellRendererAdapter(
      renderer))
    destList.setCellRenderer(new GenListToListCellRendererAdapter(
      renderer))
  }

  val buttonPanel = box(
    BoxLayout.Y_AXIS,
    Some((4, 4, 4, 4)),
    vertGlue(),
    button(" >", "Adiciona item(s) selecionado(s).", invokeLater { addSelected() },
      atLeastOneEnabledForAddition),
    rigidColumn(16),
    button(">>", "Adiciona todos os itens.", invokeLater {
      sourceModel.elements.filter(approver.additionApproved).foreach(sourceModel.remove)
    }, atLeastOneEnabledInAllForAddition),
    rigidColumn(32),
    button(" <", "Remove item(s) selecionado(s).", invokeLater { removeSelected() },
      atLeastOneEnabledForRemoval),
    rigidColumn(16),
    button("<<", "Remove todos os itens.", invokeLater {
      destModel.elements().toList.zipWithIndex.reverse.filter({
        case (el, idx) ⇒ approver.removalApproved(el, idx)
      }).map(_._2).foreach(destModel.remove)
    }, atLeastOneInAllEnabledForRemoval),
    vertGlue())

  val sideButtonPanel = box(
    BoxLayout.Y_AXIS,
    Some(4, 4, 4, 4),
    vertGlue(),
    button("+", "Move item(s) selecionado(s) para cima.", invokeLater { move(false) }, {
      () ⇒
        hasAtLeastOneIn(1, destModel.size(),
          destList.getSelectedIndices()) && atLeastOneMoveEnabled(false)
    }),
    rigidColumn(16),
    button("-", "Move item(s) selecionado(s) para baixo.", invokeLater { move(true) }, {
      () ⇒
        hasAtLeastOneIn(0, destModel.size() - 2,
          destList.getSelectedIndices()) && atLeastOneMoveEnabled(true)
    }),
    vertGlue())

  add(new JScrollPane(sourceList))
  add(buttonPanel)
  add(new JScrollPane(destList))
  add(sideButtonPanel)
  updateButtonStatus()
  val l = new ListSelectionListener {
    override def valueChanged(e: ListSelectionEvent) = invokeLater { updateButtonStatus() }
  }

  sourceList.getSelectionModel.addListSelectionListener(l)
  destList.getSelectionModel.addListSelectionListener(l)

  sourceModel.addListener(new DefaultSetModelListener[T] {
    override def elementAdded(e: T) { invokeLater { updateButtonStatus() } }
    override def elementRemoved(e: T) { invokeLater { updateButtonStatus() } }
  })

  destModel.addListener(new DefaultGenListModelListener[T] {
    override def itemInserted(pos: Int, e: T) { invokeLater { updateButtonStatus() } }
    override def itemRemoved(pos: Int, e: T) { invokeLater { updateButtonStatus() } }
  })

  //Constructor ends

  def getDomainModel() = domainModel

  def getSourceModel() = sourceModel

  def getDestModel() = destModel
  
  def setApprover(_approver : ListChoiceActionApprover[T]) {
    approver = Option(_approver).getOrElse(new DefaultListChoiceActionApprover[T])
  }

  //Private methods

  private var approver : ListChoiceActionApprover[T] = _
  
  private def invokeLater(f: ⇒ Unit) =
    SwingUtilities.invokeLater(new Runnable() {
      override def run() {
        f
      }
    })

  private def actionListener(f: ActionEvent ⇒ Unit) = new ActionListener() {
    override def actionPerformed(e: ActionEvent) = f(e)
  }

  private def clickListener(f: MouseEvent ⇒ Unit) = new MouseAdapter() {
    override def mouseClicked(e: MouseEvent) = f(e)
  }

  private def doubleClickListener(f: MouseEvent ⇒ Unit) = clickListener {
    case e if e.getClickCount() > 1 ⇒ f(e)
    case _ ⇒
  }

  private def button(label: String, hint: String, action: ⇒ Unit,
    conditions: (() ⇒ Boolean)*) = {
    val b = new JButton(label)
    b.setToolTipText(hint)
    b.addActionListener(actionListener { _ ⇒ action })
    statusChecker = conditions.foldLeft(statusChecker) {
      case (checkers, c) ⇒ (() ⇒ b.setEnabled(c())) :: checkers
    }
    b
  }

  private def box(axis: Int, border: Option[(Int, Int, Int, Int)], comps: Component*) = {
    val bx = new Box(axis)
    for { (a, b, c, d) ← border } { bx.setBorder(BorderFactory.createEmptyBorder(a, b, c, d)) }
    comps.foreach(bx.add)
    bx
  }

  private def plugListeners() {
    sourceModel.addListener(sourceListener)
    destModel.addListener(destListener)
  }

  private def unplugListeners() {
    sourceModel.removeListener(sourceListener);
    destModel.removeListener(destListener);
  }

  private def vertGlue() = Box.createVerticalGlue()

  private def rigidLine(w: Int) = Box.createRigidArea(new Dimension(w, 1))

  private def rigidColumn(h: Int) = Box.createRigidArea(new Dimension(1, h))

  private def addSelected() {
    def el(i: Int) = sourceModelAdapter.get(i)
    sourceList.
      getSelectedIndices.filter(x ⇒ approver.additionApproved(el(x))).
      sorted.zipWithIndex.foreach {
        case (shift, x) ⇒ sourceModel.remove(el(x - shift))
      }
  }

  private def removeSelected() =
    destList.getSelectedIndices.sorted.reverse.toSeq
      .map(idx ⇒ (idx, destModel.get(idx)))
      .filter(ei ⇒ approver.removalApproved(ei._2, ei._1))
      .map(_._2)
      .foreach(destModel.remove)

  private def move(down: Boolean) = {
    println("move(" + down + "):")
    val (moves, newSelected) = filterMoves(down)
    println("  moves = " + moves)
    println("  newSelected = " + newSelected.toList)
    moves.foreach({ case (from, to) ⇒ destModel.move(from, to) })
    destList.setSelectedIndices(newSelected)
  }

  private def updateButtonStatus() = statusChecker.foreach(_())

  private def filterMoves(down: Boolean): (List[(Int, Int)], Array[Int]) = {
    println("filterMoves(" + down + "):")
    val selected1 = destList.getSelectedIndices.toList
    println("  selected1: " + selected1.toList)
    val available1 = SortedSet((0 until destModel.size()).filter(i ⇒ approver.moveApproved(destModel.get(i), i)): _*)
    println("  available1: " + available1)
    val len = destModel.size()
    println("  len: " + len)
    def revPos(x: Int) = len - x - 1
    val selected = if (down) { selected1.map(revPos) } else { selected1 }
    println("  selected: " + selected)
    val available = if(down) { available1.map(revPos) } else { available1 }
    println("  available: " + available)
    val sel = SortedSet(selected.toSeq.filter(available.contains(_)): _*)
    println("  sel: " + sel)
    val beforeSel = sel.headOption.map(available.until(_)).getOrElse(SortedSet[Int]())
    println("  beforeSel: " + beforeSel)
    val searchPositions = beforeSel.lastOption.map(available.from(_)).getOrElse(SortedSet[Int]())
    println("  searchPositions: " + searchPositions)
    val swaps = sel.toList.zip(searchPositions)
    println("  swaps: " + swaps)
    val moves = swaps.flatMap {
      case (x, y) if Math.abs(x - y) > 1 ⇒ Seq((x, y), (y + 1, x))
      case p ⇒ Seq(p)
    }
    println("  moves: " + moves)
    val resultMoves = if(down) { moves.map { case (x, y) ⇒ (revPos(x), revPos(y)) } } else { moves }
    println("  resultMoves: " + resultMoves)
    val newSelected = if(down) { swaps.map(x ⇒ revPos(x._2)) } else { swaps.map(_._2) }
    println("  newSelected: " + newSelected.toList)
    (resultMoves, newSelected.toArray)
  }

  private def atLeastOneMoveEnabled(down: Boolean) =
    !filterMoves(down)._1.isEmpty

  private def hasAtLeastOneIn(min: Int, max: Int, l: Iterable[Int]) =
    l.exists(v ⇒ v >= min && v <= max)

  private def atLeastOneEnabledForAddition() =
    sourceList.getSelectedIndices.toSeq.map(sourceModelAdapter.get)
      .exists(approver.additionApproved)

  private def atLeastOneEnabledInAllForAddition() =
    sourceModel.elements.exists(approver.additionApproved)

  private def atLeastOneEnabledForRemoval() =
    destList.getSelectedIndices().map(idx ⇒ (destModel.get(idx), idx))
      .exists(x ⇒ approver.removalApproved(x._1, x._2))

  private def atLeastOneInAllEnabledForRemoval() =
    destModel.elements().zipWithIndex
      .exists(x ⇒ approver.removalApproved(x._1, x._2))
}

object JListChoice {
  def create1[T <: Comparable[T]](domainModel: Option[SetModel[T]] = None, destModel: Option[GenListModel[T]] = None,
    renderer: Option[GenListCellRender[T]] = None,
    approver: Option[ListChoiceActionApprover[T]] = None) : JListChoice[T] = 
	  new JListChoice(
	      domainModel.getOrElse(new DefaultSetModel[T](new NaturalComparator)),
	      destModel.getOrElse(new DefaultGenListModel[T]),
	      renderer.orNull,
	      approver.getOrElse(new DefaultListChoiceActionApprover)
	      )
  def create2[T](domainModel: SetModel[T], destModel: Option[GenListModel[T]] = None,
    renderer: Option[GenListCellRender[T]] = None,
    approver: Option[ListChoiceActionApprover[T]] = None) : JListChoice[T] =
      new JListChoice(
	      domainModel,
	      destModel.getOrElse(new DefaultGenListModel[T]),
	      renderer.orNull,
	      approver.getOrElse(new DefaultListChoiceActionApprover)
	      )
  def apply[T <: Comparable[T]]() = create1[T]()	  
  def apply[T](domainModel : SetModel[T]) = create2[T](domainModel)
  def apply[T](domainModel : SetModel[T],renderer : GenListCellRender[T]) =
	  	create2[T](domainModel = domainModel, renderer = Some(renderer))
  def apply[T](domainModel : SetModel[T], destModel : GenListModel[T]) =
	  	create2[T](domainModel = domainModel, destModel = Some(destModel))
}
