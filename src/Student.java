import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 一斉リネーム: com + opt + R
// 最後尾へ移動： com 矢印
// 行を複製： com + opt ＋ 下
// インデックスの整形：　com + shif + F

public class Student {

	public static String url = "jdbc:postgresql://localhost:5432/training";
	public static String user = "student";
	public static String password = "password";
	
	public static void main(String[] args) {
		
		String sql = "SELECT name, subject, point FROM student INNER JOIN score ON student.id = score.student_id";
		
		try(Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				String name = rs.getString("name");
				String subject = rs.getString("subject");
				int point = rs.getInt("point");
				System.out.println("名前=" + name + ",科目=" + subject + ",点数=" + point);
			}
		} catch(SQLException e) {
			System.out.println(e);
		}
	}
	
}





// ========================================30歳以上の学生数を求める
//public class Student {
//
//	public static String url = "jdbc:postgresql://localhost:5432/training";
//	public static String user = "student";
//	public static String password = "password";
//	
//	public static void main(String[] args) {
//		try {
//			countByAge(30);
//		} catch(SQLException e) {
//			System.out.println(e);
//		}
//	}
//	
//	static int countByAge(int minAge) throws SQLException {
//		String sql = "SELECT COUNT(*) AS cnt FROM student WHERE age >= ?";
//		
//		try (Connection conn = DriverManager.getConnection(url, user, password);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setInt(1,minAge);
//			try (ResultSet rs = ps.executeQuery()){
//				if (rs.next()) {
//					int count = rs.getInt(1);
//					System.out.println("取得件数" + count);
//					return count;
//				}
//			}
//		}
//		return 0;
//	}
//}

