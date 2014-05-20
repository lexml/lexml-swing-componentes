package br.gov.lexml.swing.componentes;
import javax.swing.*;   

public class TesteJOpenList {

	private static JOpenList<String> openList;
	
	//private static void
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TesteJOpenList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        openList = JOpenList.createStringOpenList();   

        //Add the ubiquitous "Hello World" label.
        frame.getContentPane().add(openList);

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


