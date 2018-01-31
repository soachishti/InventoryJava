
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Button;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inventory extends JFrame {
	private Database db;
	private JPanel contentPane;
	private JTextField txtCustomer;
	private JTextField txtInvoiceID;
	private JTextField txtDate;
	private JTextField txtTotal;
	private JTable table;
	private JLabel lblProduct;
	private JLabel lblQuantity;
	private JSpinner spnrQuantity;
	private JButton btnAddProduct;
	private JComboBox cmbProduct;
	private JLabel lblDate;
	private DefaultTableModel model;
	private HashMap products;
	private Integer productTotal;
	private Button button;
	private Button button_1;
	
	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {
		Inventory frame = new Inventory();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public Inventory() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {
		setIconImage(new ImageIcon("icon.png").getImage());
		setAutoRequestFocus(false);
		setResizable(false);
		setTitle("Simple Inventory");
		products = new HashMap<String, String[]>();
		productTotal = 0;
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 582, 356);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
		        System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnAbout = new JMenu("Help");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About a;
				try {
					a = new About();
					a.setVisible(true);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		mnAbout.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		db = Database.getInstance();
		try {
			db.connect();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(contentPane, "Unable to connect to the database"
					+ "						Detail: "+e);
		}
		
		JLabel lblInvoiceId = new JLabel("Customer Name:");
		lblInvoiceId.setBounds(133, 11, 97, 14);
		contentPane.add(lblInvoiceId);
				
		txtCustomer = new JTextField();
		txtCustomer.setBounds(240, 8, 98, 20);
		contentPane.add(txtCustomer);
		txtCustomer.setColumns(10);
		
		JLabel label = new JLabel("Invoice ID:");
		label.setBounds(10, 11, 63, 14);
		contentPane.add(label);
		
		txtInvoiceID = new JTextField();
		txtInvoiceID.setEnabled(false);
		txtInvoiceID.setColumns(10);
		txtInvoiceID.setBounds(75, 8, 48, 20);
		updateInvoiceID();
		contentPane.add(txtInvoiceID);
				
		lblDate = new JLabel("Date:");
		lblDate.setBounds(408, 11, 44, 14);
		contentPane.add(lblDate);
		
		txtDate = new JTextField();
		txtDate.setBounds(443, 8, 117, 20);
		
		new Thread() {
			public void run(){
				while (true) {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					txtDate.setText(dateFormat.format(date));
					try {
						Thread.sleep(1000); 
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}.start();
		
		txtDate.setEnabled(false);
		contentPane.add(txtDate);
		txtDate.setColumns(10);
				
		cmbProduct = new JComboBox();
		DefaultComboBoxModel cmbModel = new DefaultComboBoxModel(new String[] {});
		db.st = db.cn.createStatement();
		ResultSet rs = db.st.executeQuery("SELECT * FROM item");
		
		while (rs.next()) {
			String value[] = {rs.getString("id"), rs.getString("price")};
			products.put(rs.getString("name"), value);
			cmbModel.addElement(rs.getString("name"));
		}
		
		cmbProduct.setModel(cmbModel);
		cmbProduct.setBounds(75, 42, 148, 20);
		contentPane.add(cmbProduct);
		
		lblProduct = new JLabel("Product:");
		lblProduct.setBounds(10, 45, 65, 14);
		contentPane.add(lblProduct);
		
		lblQuantity = new JLabel("Quantity:");
		lblQuantity.setBounds(240, 45, 63, 14);
		contentPane.add(lblQuantity);
		
		spnrQuantity = new JSpinner();
		spnrQuantity.setBounds(313, 42, 57, 20);
		contentPane.add(spnrQuantity);
		
		btnAddProduct = new JButton("Add Product");
		btnAddProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String product = cmbProduct.getSelectedItem().toString();
				
				String[] data = (String[])products.get(product);
				String id = data[0];
				Integer price = Integer.parseInt(data[1]);
				
				Integer quantity = Integer.parseInt(spnrQuantity.getValue().toString());
				if (quantity <= 0) {
					JOptionPane.showMessageDialog(contentPane, "Zero quantitiy.");
					return;
				}
				
				Integer total = quantity * price;
				
				productTotal += total;
				txtTotal.setText("Rs. " + productTotal.toString() + "/-");
				
				Object[] row = {model.getRowCount()+1, product, quantity, price, total.toString()};
				model.addRow(row);
			}
		});
		btnAddProduct.setBounds(456, 41, 104, 23);
		contentPane.add(btnAddProduct);
		
		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(421, 272, 46, 14);
		contentPane.add(lblTotal);
		
		txtTotal = new JTextField();
		txtTotal.setEnabled(false);
		txtTotal.setText("0.00");
		txtTotal.setBounds(474, 269, 86, 20);
		contentPane.add(txtTotal);
		txtTotal.setColumns(10);
		
		Button btnSave = new Button("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String customer = txtCustomer.getText();
				String date = txtDate.getText();
				if (model.getRowCount() == 0 || customer.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Error: Check product and customer name.");
				}
				else {
					String query = "INSERT INTO `invoice`(`customer`, `date`, `total_price`) VALUES ('"+customer+"','"+date+"', "+productTotal+")";
					try {
						db.st = db.cn.createStatement();
						db.st.executeUpdate(query);
						
						ResultSet rs = db.st.executeQuery("select last_insert_id() as last_id from invoice");
						rs.next();
						Integer invoiceID  = Integer.parseInt(rs.getString("last_id"));
						
						for (int i = 0; i < model.getRowCount(); i++) {
							String name = (String) model.getValueAt(i, 1);
							Integer id = Integer.parseInt(((String [])products.get(name))[0]);
							
							String price = (String) model.getValueAt(i, 4);
							Integer quantity = (Integer) model.getValueAt(i, 2);
							
							query = "INSERT INTO `invoice_detail`(`item_id`, `quantity`, `price`, `invoice_id`) VALUES ('"+id.toString()+"','"+quantity.toString()+"','"+price+"',"+invoiceID+")";
							db.st.execute(query);
						}
						model.getDataVector().removeAllElements();
						model.fireTableDataChanged();
						spnrQuantity.setValue(0);
						txtCustomer.setText("");
						
						JOptionPane.showMessageDialog(contentPane, "Records added!");
						updateInvoiceID();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		btnSave.setBounds(10, 264, 117, 22);
		contentPane.add(btnSave);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 75, 552, 171);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Sr.", "Name", "Quantity", "Unit Price", "Total"
				}
			);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		button = new Button("Product Table");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProductTable pt = new ProductTable();
				pt.setVisible(true);
			}
		});
		button.setBounds(143, 264, 117, 22);
		contentPane.add(button);
		
		button_1 = new Button("Search");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Search s = new Search();
				s.setVisible(true);
			}
		});
		button_1.setBounds(268, 264, 117, 22);
		contentPane.add(button_1);
		
		
	}
	
	private void updateInvoiceID() {
		String query = "SELECT MAX(id) FROM invoice;";
		try {
			db.st = db.cn.createStatement();
			ResultSet rs = db.st.executeQuery(query);
			rs.next();
			Integer invoiceID = rs.getInt(1)+1;
			txtInvoiceID.setText(invoiceID.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
