import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class About extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {		
			About frame = new About();
			frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public About() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setIconImage(new ImageIcon("icon.png").getImage());
		setTitle("About");
		setResizable(false);
		setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 240, 171);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSimpleInventory = new JLabel("Simple Inventory");
		lblSimpleInventory.setHorizontalAlignment(SwingConstants.CENTER);
		lblSimpleInventory.setFont(new Font("Swis721 Hv BT", Font.PLAIN, 18));
		lblSimpleInventory.setBounds(10, 13, 214, 39);
		contentPane.add(lblSimpleInventory);
		
		JLabel lblDevelopedByP = new JLabel("Developed by p146011");
		lblDevelopedByP.setHorizontalAlignment(SwingConstants.CENTER);
		lblDevelopedByP.setBounds(10, 99, 214, 14);
		contentPane.add(lblDevelopedByP);
		
		JLabel lblVBeta = new JLabel("v1.0 beta");
		lblVBeta.setHorizontalAlignment(SwingConstants.CENTER);
		lblVBeta.setBounds(10, 63, 214, 14);
		contentPane.add(lblVBeta);
	}

}
