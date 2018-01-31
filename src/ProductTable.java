import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class ProductTable extends JFrame {
	private DefaultTableModel model;
	private JPanel contentPane;
	private JTable table;
	private Database db;
	private JLabel lblName;
	private JTextField txtName;
	private JLabel lblPrice;
	private JTextField txtPrice;
	private JButton btnAdd;
	private JButton btnRefreshTable;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ProductTable frame = new ProductTable();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public ProductTable() {
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		db = Database.getInstance();
		try {
			db.connect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setTitle("Product Table");
		setIconImage(new ImageIcon("icon.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 44, 414, 206);
		contentPane.add(scrollPane);
		
		table = new JTable();
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"ID", "Name", "Price", "Delete", "Save"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, true, true, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		table.setModel(model);
		Action delete = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        DefaultTableModel m = ((DefaultTableModel)table.getModel());
		        int val = Integer.parseInt((String) m.getValueAt(modelRow, 0));
		        String query = "DELETE FROM `item` WHERE id="+ val;
		        try {
					db.st = db.cn.createStatement();
					db.st.execute(query);
					m.removeRow(modelRow);
		        } catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		    }
		};
		
		Action save = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        DefaultTableModel m = ((DefaultTableModel)table.getModel());
		        int val = Integer.parseInt((String) m.getValueAt(modelRow, 0));
		        
		        String name  = (String) m.getValueAt(modelRow, 1);
		        String price = (String) m.getValueAt(modelRow, 2);
		               
		        String query = "UPDATE `item` SET `name`='"+name+"',`price`='"+price+"' WHERE id="+ val;
		        
		        try {
					db.st = db.cn.createStatement();
					db.st.execute(query);
					JOptionPane.showMessageDialog(contentPane, "Updated!");
		        } catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		    }
		};
		 
		new ButtonColumn(table, delete, 3).setMnemonic(KeyEvent.VK_D);
		new ButtonColumn(table, save, 4).setMnemonic(KeyEvent.VK_D);
		
		updateTable();
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		lblName = new JLabel("Name:");
		lblName.setBounds(10, 11, 46, 14);
		contentPane.add(lblName);
		
		txtName = new JTextField();
		txtName.setBounds(47, 8, 86, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		lblPrice = new JLabel("Price:");
		lblPrice.setBounds(143, 11, 54, 14);
		contentPane.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(177, 8, 60, 20);
		contentPane.add(txtPrice);
		txtPrice.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String price = txtPrice.getText();
				if (name.isEmpty() || price.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Error: Check name and price.");
					return;
				}
				String query = "INSERT INTO item(`name`,`price`) VALUES('"+name+"','"+price+"')";
				try {
					db.st = db.cn.createStatement();
					db.st.execute(query);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateTable();
			}
		});
		btnAdd.setBounds(250, 7, 54, 23);
		contentPane.add(btnAdd);
		
		btnRefreshTable = new JButton("Refresh Table");
		btnRefreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTable();
			}
		});
		btnRefreshTable.setBounds(314, 7, 110, 23);
		contentPane.add(btnRefreshTable);
	}
	
	private void updateTable() {

		model.getDataVector().removeAllElements();
		model.fireTableDataChanged();
		try {
			db.st = db.cn.createStatement();
			ResultSet rs = db.st.executeQuery("SELECT id, name, price FROM item;");
			while (rs.next()) {
				String value[] = {rs.getString("id"), rs.getString("name"), rs.getString("price"), "Delete", "Save"};
				model.addRow(value);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.fireTableDataChanged();
	}

}
