import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
	Connection cn;
	Statement st;
	private static Database d;
	private Database() {};
	
    public static Database getInstance(){
        if(d == null){
            d = new Database();
        }
        return d;
    }
	
	public void connect() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "root");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
