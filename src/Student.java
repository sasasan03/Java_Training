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

		String updateSql = "UPDATE student SET name = ?, age = ? WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(updateSql);) {
			ps.setString(1, "谷本");
			ps.setInt(2, 29);
			ps.setInt(3, 100);
			int count = ps.executeUpdate();
			if (count != 0) {
				System.out.println("作成カウント：" + count);
			} else {
				System.out.println("該当なし");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
