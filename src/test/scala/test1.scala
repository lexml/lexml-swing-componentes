//:paste
object Test1 {
  import javax.swing.event._
  import javax.swing._
  import javax.swing.event._
  import java.awt.event._
  import java.awt.{ List ⇒ AWTList, _ }
  import scala.collection.JavaConversions._
  import br.gov.lexml.swing.componentes._
  import br.gov.lexml.swing.componentes.models._
  import java.util.Comparator
  import scala.collection.JavaConversions._

  val frame = new JFrame("teste")
  val domList = List("banana", "maçã", "abacate") ++ (1 until 20).map("fruta" + _)
  val domModel = new DefaultGenListModel(domList)

  val jlist = JListChoice2(domModel)
  frame.getContentPane().setLayout(new BorderLayout())
  frame.getContentPane.add(jlist, BorderLayout.CENTER)
  frame.setVisible(true)

  val srcModel = jlist.getSourceModel
  val dstModel = jlist.getDestModel
  val unmovable = (1 until 20).map("fruta" + _).toSet
  val unmovable2 = (1 until 20 by 2).map("fruta" + _).toSet
  unmovable.toList.take(10).foreach(srcModel.remove(_))
  val approver = new DefaultListChoiceActionApprover[String] {
    def approved(s: String) = !unmovable.contains(s)
    override def additionApproved(s: String) = approved(s)
    override def removalApproved(s: String, n: Int) = approved(s)
    override def moveApproved(s: String, idx: Int) = !unmovable2.contains(s)
  }
  jlist.setApprover(approver)

  

  
}