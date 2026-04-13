import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//一斉リネーム: com + opt + R
//最後尾へ移動： com 矢印
//行を複製： com + opt ＋ 下
//インデックスの整形：　com + shif + F
//タブの切り替え：　com ＋　[ or  ]
//文字のサイス切り替え：　トラックパッドで２本指

public class StudentDAO {
	private static final String URL = "jdbc:postgresql://localhost:5432/training";
	private static final String USER = "student";
	private static final String PASSWORD = "password";

	List<String> findAll() {
		String sql = "SELECT * FROM student";
		List<String> list = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					list.add(rs.getString("name"));
				}
				return list;
			}
		} catch (SQLException e) {
		    throw new AppException("全件取得に失敗しました", e);
		}
	}

	int insert(String name, int age) {
		String sql = "INSERT "
				+ "INTO student (name, age) "
				+ "VALUES (?, ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, name);
			ps.setInt(2, age);
			return ps.executeUpdate();
		} catch (SQLException e) {
		   throw new AppException("新規作成に失敗しました", e);
		}
	}

	int update(int id, String newName, int newAge) {
		String sql = "UPDATE student "
				+ "SET name = ?, age = ?"
				+ " WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, newName);
			ps.setInt(2, newAge);
			ps.setInt(3, id);
			int updateCount = ps.executeUpdate();
			return updateCount;
		} catch (SQLException e) {
		    throw new AppException("記録更新に失敗しました", e);
		}
	}

	int delete(int id) {
		String sql = "DELETE FROM student WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, id);
			return ps.executeUpdate();
		}  catch (SQLException e) {
		    throw new AppException("記録削除に失敗しました", e);
		}
	}

	String findById(int id) {
		String sql = "SELECT name, age"
				+ " FROM student"
				+ " WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getString("name");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
		    throw new AppException("指定idの取得に失敗しました", e);
		}
	}
	
	void avgBySubject() {
		String sql = "SELECT subject, AVG(point) AS avg_point "
				+ "FROM score "
				+ "GROUP BY subject";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				while(rs.next()) {
					String subject = rs.getString("subject");
					double avg = rs.getInt("avg_point");
					double rounded = Math.round(avg * 10.0) / 10.0;
					System.out.println("科目＝" + subject + "平均点＝" + rounded);
				}
			}
		} catch (SQLException e) {
		    throw new AppException("教科の平均点取得に失敗しました", e);
		}
	}
}



// ===================================================== CRUDメソッドの実装
//public class StudentDAO {
//	private static final String URL = "jdbc:postgresql://localhost:5432/training";
//	private static final String USER = "student";
//	private static final String PASSWORD = "password";
//
//	void findAll() throws SQLException {
//		String sql = "SELECT * FROM student";
//		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			try (ResultSet rs = ps.executeQuery();) {
//				while (rs.next()) {
//					String name = rs.getString("name");
//					int age = rs.getInt("age");
//					System.out.println("名前：" + name + "、年齢：" + age);
//				}
//			}
//		}
//	}
//
//	void insert(String name, int age) throws SQLException {
//		String sql = "INSERT "
//				+ "INTO student (name, age) "
//				+ "VALUES (?, ?)";
//		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setString(1, name);
//			ps.setInt(2, age);
//			int insertCount = ps.executeUpdate();
//			System.out.println("追加件数" + insertCount);
//		}
//	}
//
//	void update(int id, String newName, int newAge) throws SQLException {
//		String sql = "UPDATE student "
//				+ "SET name = ?, age = ?"
//				+ " WHERE id = ?";
//		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setString(1, newName);
//			ps.setInt(2, newAge);
//			ps.setInt(3, id);
//			int updateCount = ps.executeUpdate();
//			if (updateCount == 0) {
//				System.out.println("対象なし");
//			} else {
//				System.out.println("変更件数" + updateCount);
//			}
//		}
//	}
//
//	void delete(int id) throws SQLException {
//		String sql = "DELETE FROM student WHERE id = ?";
//		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setInt(1, id);
//			int deleteCount = ps.executeUpdate();
//			System.out.println("削除件数" + deleteCount);
//		}
//	}
//
//	void findById(int id) throws SQLException {
//		String sql = "SELECT name, age"
//				+ " FROM student"
//				+ " WHERE id = ?";
//		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setInt(1, id);
//			try (ResultSet rs = ps.executeQuery();) {
//				if (rs.next()) {
//					String name = rs.getString("name");
//					int age = rs.getInt("age");
//					System.out.println("名前：" + name + "、年齢：" + age);
//				} else {
//					System.out.println("該当なし");
//				}
//			}
//		}
//	}
//}
