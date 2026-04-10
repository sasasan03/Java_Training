import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//一斉リネーム: com + opt + R
//最後尾へ移動： com 矢印
//行を複製： com + opt ＋ 下
//インデックスの整形：　com + shif + F
//タブの切り替え：　com ＋　[ or  ]
//文字のサイス切り替え：　トラックパッドで２本指

public class StudentDAO {
	private static final String url = "jdbc:postgresql://localhost:5432/training";
	private static final String user = "student";
	private static final String password = "password";

	void findAll() throws SQLException {
		String sql = "SELECT * FROM student";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					String name = rs.getString("name");
					int age = rs.getInt("age");
					System.out.println("名前：" + name + "、年齢：" + age);
				}
			}
		}
	}
	
	void insert(String name, int age) throws SQLException {
		String sql = "INSERT "
				+ "INTO student (name, age) "
				+ "VALUES (?, ?)";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			conn.setAutoCommit(true);
			ps.setString(1,name);
			ps.setInt(2,age);
			int insertCount = ps.executeUpdate();
			System.out.println("保存数" + insertCount);
		}
	}
	
	void update(int id, String newName, int newAge) throws SQLException {
		String sql = "UPDATE student "
				+ "SET name = ?, age = ?"
				+ " WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			conn.setAutoCommit(true);
			ps.setString(1, newName);
			ps.setInt(2, newAge);
			ps.setInt(3, id);
			int updateCount = ps.executeUpdate();
			System.out.println("保存数" + updateCount);
		}
	}
	
	void delete(int id) throws SQLException {
		String sql = "DELETE FROM student WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			conn.setAutoCommit(true);
			ps.setInt(1, id);
			int deleteCount = ps.executeUpdate();
			System.out.println("保存数" + deleteCount);
		}
	}
	
	void findById(int id) throws SQLException {
		String sql = "SELECT name, age"
				+ " FROM student"
				+ " WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			conn.setAutoCommit(true);
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next())  {
					String name = rs.getString("name");
					int age = rs.getInt("age");
					System.out.println("名前：" + name + "、年齢：" + age);
				}
			} 
		}
	}
}
