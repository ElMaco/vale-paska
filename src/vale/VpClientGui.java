package vale;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class VpClientGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JScrollPane scrollPane = new JScrollPane();
	private DefaultListModel<String> l1 = new DefaultListModel<String>();
	private JList<String> list1 = new JList<String>(l1);
	private DefaultListModel<String> l2 = new DefaultListModel<String>();
	private final JTextArea textArea = new JTextArea();
	private final JScrollPane scrollPane_2 = new JScrollPane(textArea);
	private DefaultListModel<String> l3 = new DefaultListModel<String>();
	private final JLabel lblInfo = new JLabel("Paina connect niin pääset peliin");
	private final JPanel panelNapit = new JPanel();
	private final JButton btnPelaa1 = new JButton("Pelaa");
	private final JButton btnEpailen1 = new JButton("Ep\u00E4ilen!");
	private final JPanel panel = new JPanel();
	private final JTextField textField = new JTextField();
	private final JLabel lblVaite1 = new JLabel("V\u00E4ite:");
	private final JLabel lblVuoro2 = new JLabel("0");
	private final JLabel lblVaite2 = new JLabel("2 2");
	private final JLabel lblVuoro1 = new JLabel("Vuoro: Pelaaja");
	private final JLabel lblPakka = new JLabel("42");
	private final JButton btnKaatukoon1 = new JButton("Kaatukoon!");
	private final JButton btnConnect1 = new JButton("Connect");
	
	
	public int[] getValitutKortit()
	{
		return list1.getSelectedIndices();
	}
	
	
	
	public String getVaite()
	{
		return textField.getText();
	}

	
	public void setLblInfoText(String s)
	{
		lblInfo.setText(s);
	}
	
	
	public void setVuoroText(String s)
	{
		lblVuoro2.setText(s);
	}
	
	
	public void setVaiteText(String s)
	{
		lblVaite2.setText(s);
	}
	
	
	public void setPakkaText(String s)
	{
		lblPakka.setText(s);
	}
	
	
	public void setConnectText(String s)
	{
		btnConnect1.setText(s);
	}
	
	
	public void addInfoText(String s)
	{
		textArea.append(">> "+s+"\n");
	}
	
	public void setList1(String[] s)
	{
		l1.clear();
		for (int i=0; i<s.length; i++)
			l1.addElement(s[i]);
	}
	
	
	

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VpClientGui frame = new VpClientGui();
					VpClient vpClient = new VpClient(frame);
					frame.setVisible(true);
					frame.pack();
				} catch (Exception e) {
					e.printStackTrace();					
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VpClientGui() {
		textField.setText("2");
		textField.setColumns(10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(scrollPane, BorderLayout.WEST);
		
		
		scrollPane.setViewportView(list1);
		
		contentPane.add(scrollPane_2, BorderLayout.CENTER);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setColumns(10);
		
		
		contentPane.add(panelNapit, BorderLayout.SOUTH);
		
		panelNapit.add(btnPelaa1);
		
		panelNapit.add(btnEpailen1);
		
		panelNapit.add(btnKaatukoon1);
		
		panelNapit.add(btnConnect1);
		
		
		contentPane.add(panel, BorderLayout.NORTH);
		
		panel.add(lblPakka);
		
		panel.add(textField);
		
		panel.add(lblVuoro1);
		
		panel.add(lblVuoro2);
		
		panel.add(lblVaite1);
		
		panel.add(lblVaite2);
		panel.add(lblInfo);
	}
	
	
	public void addPelaaListener(ActionListener l)
	{
		btnPelaa1.addActionListener(l);
	}
	
	public void addEpailenListener(ActionListener l)
	{
		btnEpailen1.addActionListener(l);
	}
	
	public void addKaatuuListener(ActionListener l)
	{
		btnKaatukoon1.addActionListener(l);
	}
	
	public void addConnectListener(ActionListener l)
	{
		btnConnect1.addActionListener(l);
	}
	

}
