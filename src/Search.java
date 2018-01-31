import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Search extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTable table;
	private DefaultTableModel model;
	private Database db;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Search frame = new Search();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public Search() {
		setResizable(false);
		setTitle("Search");
		setIconImage(new ImageIcon("icon.png").getImage());

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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 414, 208);
		contentPane.add(scrollPane);
		
		table = new JTable();
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Sr.", "Name", "Product", "Unit Price", "Total Price", "Date"
				}
			);
		updateTable("");
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		JLabel lblSearch = new JLabel("Search by Product:");
		lblSearch.setBounds(10, 17, 103, 14);
		contentPane.add(lblSearch);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(123, 14, 204, 20);
		contentPane.add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txt = txtSearch.getText();
				updateTable(txt);
			}
		});
		btnSubmit.setBounds(335, 13, 89, 23);
		contentPane.add(btnSubmit);
	}
	
	private void updateTable(String str) {
		
		model.getDataVector().removeAllElements();
		model.fireTableDataChanged();
		
		try {	
			db.st = db.cn.createStatement();
			ResultSet rs = db.st.executeQuery("SELECT * FROM invoice, invoice_detail, item WHERE name LIKE '%"+str+"%' AND invoice.id = invoice_detail.invoice_id AND item_id = item.id;");
			Integer count = 1;
			while (rs.next()) {
				String data[] = {count.toString(), rs.getString("customer"), rs.getString("name"),  rs.getString("price"), rs.getString("total_price"), rs.getString("date")};
				model.addRow(data);
				count = count+1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.fireTableDataChanged();
	}

}
