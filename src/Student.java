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
	
		String selectIdSql = "SELECT * FROM student WHERE id = ?";
		
		try(
				Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(selectIdSql);
			) {
			ps.setInt(1, 100);
			try(ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
						System.out.println(rs.getString("name"));
				} else {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
