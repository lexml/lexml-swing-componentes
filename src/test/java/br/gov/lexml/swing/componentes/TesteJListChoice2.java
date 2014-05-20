package br.gov.lexml.swing.componentes;
import javax.swing.JFrame;

import br.gov.lexml.swing.componentes.models.SetModel;

public class TesteJListChoice2 {

	private static JListChoice2<String> list;
	
	//private static void
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TesteJOpenList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        list = JListChoice2.apply(); 
        
        list.getDomainModel().add("Elemento 1");
        list.getDomainModel().add("Elemento 2");
        list.getDomainModel().add("Elemento 3 dafd fadf asdfasdf as");
        
        //Add the ubiquitous "Hello World" label.
        frame.getContentPane().add(list);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	//public static void
	public static void main(String[] args) {  		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {       
			public void run() {
				 createAndShowGUI();				
			}
		});    
    }

}


