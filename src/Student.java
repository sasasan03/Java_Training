import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 一斉リネーム: com + opt + R
// 最後尾へ移動： com 矢印
// 行を複製： com + opt ＋ 下

public class Student {

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/training";
		String user = "student";
		String password = "password";
	
		String selectSql = "SELECT * FROM student";
		
		try(Connection conn = DriverManager.getConnection(url,user,password)) {
		    PreparedStatement ps = conn.prepareStatement(selectSql);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt("id"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
