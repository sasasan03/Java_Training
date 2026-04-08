import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Student {

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/training";
		String user = "student";
		String password = "password";
		Connection conn;
		Statement statement;
		
		try {
			conn =  DriverManager.getConnection(url,user,password);
			System.out.println("===========データベース接続成功");
			String sql = "INSERT INTO student (name, age) VALUES ('田中', 30)";
		    PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			System.out.println("===========保存完了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
