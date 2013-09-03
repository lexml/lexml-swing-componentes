package br.gov.lexml.swing.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.gov.lexml.swing.componentes.models.DefaultGenListModel;
import br.gov.lexml.swing.componentes.models.DefaultGenListModelListener;
import br.gov.lexml.swing.componentes.models.GenListCellRender;
import br.gov.lexml.swing.componentes.models.GenListModel;
import br.gov.lexml.swing.componentes.models.GenListToListCellRendererAdapter;
import br.gov.lexml.swing.componentes.models.GenListToListModelAdapter;
import br.gov.lexml.swing.componentes.models.ValueGetter;

public class JOpenList<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	private final GenListModel<T> listModel;
	private final JList list;
	private final ValueGetter<T> getter;
	
	private final JButton addButton;
	private final JButton upButton;
	private final JButton downButton;
	private final JButton removeButton;
	
	public JOpenList(JComponent editorComponent, ValueGetter<T> getter) {
		this(new DefaultGenListModel<T>(),null,editorComponent,getter);
	}
	public JOpenList(GenListModel<T> model, GenListCellRender<T> renderer,
			JComponent editorComponent,ValueGetter<T> getter) {		
		setLayout(new GridBagLayout());
		this.listModel = model;
		list = new JList(new GenListToListModelAdapter<T>(model));
		if(renderer != null) {
			list.setCellRenderer(new GenListToListCellRendererAdapter<T>(renderer));
		}
		this.getter = getter;
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.insets  = new Insets(0,0,8,0);
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		add(editorComponent,gbc1);
				
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 3;
		gbc2.weightx = 1;
		gbc2.weighty = 1;
		gbc2.fill = GridBagConstraints.BOTH;
		add(list,gbc2);
		
		addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addEditorValue();
			}
		
		});
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 1;
		gbc3.gridy = 0;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 0;
		gbc3.weighty = 0;
		gbc3.insets = new Insets(2,4,2,4);
		gbc3.fill = GridBagConstraints.NONE;
		gbc3.anchor = GridBagConstraints.WEST;
		add(addButton,gbc3);
		
		upButton = new JButton("^");
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				upSelected();
			}
		});
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 1;
		gbc4.gridy = 1;
		gbc4.gridwidth = 1;
		gbc4.gridheight = 1;
		gbc4.weightx = 0;
		gbc4.weighty = 0;
		gbc4.insets = new Insets(2,4,2,4);
		gbc4.fill = GridBagConstraints.NONE;
		gbc4.anchor = GridBagConstraints.NORTH;
		add(upButton,gbc4);
		
		downButton = new JButton("v");
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				downSelected();
			}
		});
		GridBagConstraints gbc5 = new GridBagConstraints();
		gbc5.gridx = 1;
		gbc5.gridy = 2;
		gbc5.gridwidth = 1;
		gbc5.gridheight = 1;
		gbc5.weightx = 0;
		gbc5.weighty = 0;
		gbc5.insets = new Insets(2,4,2,4);
		gbc5.fill = GridBagConstraints.NONE;
		gbc5.anchor = GridBagConstraints.NORTH;
		add(downButton,gbc5);
		
		removeButton = new JButton("-");		
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelected();
			}
		});
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.gridx = 1;
		gbc6.gridy = 3;
		gbc6.gridwidth = 1;
		gbc6.gridheight = 1;
		gbc6.weightx = 0;
		gbc6.weighty = 0;
		gbc6.insets = new Insets(2,4,2,4);
		gbc6.fill = GridBagConstraints.NONE;
		gbc6.anchor = GridBagConstraints.SOUTH;
		add(removeButton,gbc6);
		
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateButtonStatus();
			}
		});
		
		listModel.addListener(new DefaultGenListModelListener<T>() {
			public void itemInserted(int pos, T element) { 
				updateButtonStatus(); 
			};
			public void itemRemoved(int pos, T element) {
				updateButtonStatus();
			};
		});
	}
	private void removeSelected() {
		int[] selectedPos = list.getSelectedIndices();
		Arrays.sort(selectedPos);
		for (int i = selectedPos.length - 1; i >= 0; i--) {
			listModel.remove(selectedPos[i]);
		}
	}

	private void upSelected() {	
		int[] idxs = list.getSelectedIndices();
		int[] newIdxs = new int[idxs.length];
		Arrays.sort(idxs);
		int nextPos = -1;
		for(int i=0;i<idxs.length;i++) {
			int idx = idxs[i];
			if(nextPos < 0) { nextPos = Math.max(idx-1,0); }
			newIdxs[i] = nextPos;
			if(nextPos != idx) {
				listModel.move(idx, nextPos);			
			}
			nextPos++;
		}
		list.setSelectedIndices(newIdxs);
	}
	
	private void downSelected() {
		int[] idxs = list.getSelectedIndices();
		int[] newIdxs = new int[idxs.length];
		Arrays.sort(idxs);
		int nextPos = -1;
		for(int i=idxs.length-1;i>=0;i--) {
			int idx = idxs[i];
			if(nextPos < 0) { nextPos = Math.min(idx+1,listModel.size()-1); }
			newIdxs[i] = nextPos;
			if(nextPos != idx) {
				listModel.move(idx, nextPos);
			} 
			nextPos--;
		}
		list.setSelectedIndices(newIdxs);
	}
	
	private static boolean hasAtLeastOneIn(int min,int max,int[] v) {
		for(int i=0;i<v.length;i++) {
			if(v[i] >= min && v[i] <= max) return true;
		}
		return false;
	}
	
	public void setAddEnabled(boolean enabled) {
		addButton.setEnabled(enabled);
	}
	
	private void updateButtonStatus() {		
		removeButton.setEnabled(!list.getSelectionModel().isSelectionEmpty());		
		upButton.setEnabled(hasAtLeastOneIn(1,listModel.size(),list.getSelectedIndices()));
		downButton.setEnabled(hasAtLeastOneIn(0,listModel.size()-2,list.getSelectedIndices()));
	}
	
	public void addEditorValue() {
		T value = getter.get();
		if(value != null) {
			listModel.add(value);
		}
		
	}
	public GenListModel<T> getModel() {
		return listModel;
	}
	
	public static JOpenList<String> createStringOpenList() {
		return createStringOpenList(new DefaultGenListModel<String>());
	}
	public static JOpenList<String> createStringOpenList(GenListModel<String> model) {
		final JTextField tf = new JTextField();
		final JOpenList<String> olist = new JOpenList<String>(model, null, tf,
				new ValueGetter<String>() {
			@Override
			public String get() {
				String res = tf.getText();
				if(res.isEmpty()) {
					return null;
				} else {
					tf.setText("");
					return res;
				}
			}
		});
		tf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				olist.addEditorValue();
			}
		});
		tf.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) { }
			@Override
			public void insertUpdate(DocumentEvent e) {
				olist.setAddEnabled(!tf.getText().isEmpty());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				olist.setAddEnabled(!tf.getText().isEmpty());				
			}
		});
		return olist;
	}
		
}
