import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 一斉リネーム: com + opt + R
// 最後尾へ移動： com 矢印
// 行を複製： com + opt ＋ 下
// インデックスの整形：　com + shif + F

public class Student {

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/training";
		String user = "student";
		String password = "password";

		String deleteSql = "DELETE FROM student WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(deleteSql);) {
			ps.setInt(1, 3);
			int count = ps.executeUpdate();
			if (count != 0) {
				System.out.println("削除件数：" + count);
			} else {
				System.out.println("該当なし");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
