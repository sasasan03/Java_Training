import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {
	public static String url = "jdbc:postgresql://localhost:5432/training";
	public static String user = "student";
	public static String password = "password";

	void findAll() throws SQLException {
		String sql = "SELECT * FROM student";
		try(Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);
				) {
			conn.setAutoCommit(false);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					String name = rs.getString("name");
					int age = rs.getInt("age");
					System.out.println("名前：" + name + "、年齢：" + age);
				}
			}
		}
	}
}
