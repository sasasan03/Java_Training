import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//一斉リネーム: com + opt + R
//最後尾へ移動： com 矢印
//行を複製： com + opt ＋ 下
//インデックスの整形：　com + shif + F
//タブの切り替え：　com ＋　[ or  ]
//文字のサイス切り替え：　トラックパッドで２本指

// OVER・・・行を集約せずに集計結果を各行に付加できる
// PARTITOIN BY ・・・OVERの中で使用するここで指定したもので分割して計算できる
// RANK・・・同じ順位・次の順位を飛ばす（1・1・3）
// ROW_NUMBER()・・・必ず連番・同点でも別の番号（1・2・3）
// [CASE] WHEN THEN ELSE [END]
// LAG・・・

public class StudentDAO {
	private static final String URL = "jdbc:postgresql://localhost:5432/training";
	private static final String USER = "student";
	private static final String PASSWORD = "password";
	
	void scoreWithDiff() {
		String sql = "SELECT st.name, sc.subject, sc.point, "
				+ "LAG(sc.point) OVER (PARTITION BY sc.student_id ORDER BY sc.subject) AS prev_point, "
				+ "CASE "
				+ " WHEN LAG(sc.point) OVER (PARTITION BY sc.student_id ORDER BY sc.subject) IS NULL THEN '-' "
				+ " WHEN sc.point > LAG(sc.point) OVER (PARTITION BY sc.student_id ORDER BY sc.subject) THEN '上昇' "
				+ " WHEN sc.point < LAG(sc.point) OVER (PARTITION BY sc.student_id ORDER BY sc.subject) THEN '下降' "
				+ " ELSE '同じ' "
				+ "END AS trend "
				+ "FROM score AS sc "
				+ "INNER JOIN student AS st ON sc.student_id = st.id "
				+ "ORDER BY st.id, sc.subject";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					String trend = rs.getString("trend") ;
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point + "前回比=" + trend);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("各生徒の前回比のデータを取得できませんでした", e);
		}
	}

	void countByGradeAndSubject() {
		String sql = "SELECT sc.subject, "
				+ "SUM(CASE WHEN sc.point >= 80 THEN 1 ELSE 0 END) AS excellent, "
				+ "SUM(CASE WHEN sc.point >= 70 AND sc.point < 80 THEN 1 ELSE 0 END) AS good, "
				+ "SUM(CASE WHEN sc.point >= 60 AND sc.point < 70 THEN 1 ELSE 0 END) AS average, "
				+ "SUM(CASE WHEN sc.point < 60 THEN 1 ELSE 0 END) AS poor "
				+ "FROM score AS sc "
				+ "GROUP BY sc.subject "
				+ "ORDER BY sc.subject";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String subject = rs.getString("subject");
					int excellent = rs.getInt("excellent");
					int good = rs.getInt("good");
					int average = rs.getInt("average");
					int poor = rs.getInt("poor");
					System.out.println("科目=" + subject + " 「優」" + excellent + "、「良」" + good + "、「可」" + average + "、「不可」" + poor);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("教科毎の得点評価を取得できませんでした", e);
		}
	}

	void orderByGrade() {
		String sql = "SELECT st.name, sc.subject, sc.point "
				+ "FROM score AS sc "
				+ "INNER JOIN student AS st "
				+ "ON sc.student_id = st.id "
				+ "ORDER BY "
				+ "CASE "
				+ " WHEN sc.point >= 80 THEN 1 "
				+ " WHEN sc.point >= 70 THEN 2 "
				+ " WHEN sc.point >= 60 THEN 3 "
				+ " ELSE 4 "
				+ "END ";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("80点以上の生徒を取得できませんでした", e);
		}
	}

	void findByGrade(String grade) {
		String sql = "SELECT st.name, sc.subject, sc.point "
				+ "FROM score AS sc "
				+ "INNER JOIN student AS st "
				+ "ON sc.student_id = st.id WHERE( "
				+ " CASE "
				+ "   WHEN sc.point >= 80 THEN '優' "
				+ "   WHEN sc.point >= 70 THEN '良' "
				+ "   WHEN sc.point >= 60 THEN '可' "
				+ "   ELSE '不可' "
				+ "  END "
				+ ") = ? "
				+ "ORDER BY st.id";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, grade);
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("80点以上の生徒を取得できませんでした", e);
		}
	}

	void scoreWithGrade() {
		String sql = "SELECT st.name, sc.subject, sc.point, "
				+ "CASE "
				+ " WHEN sc.point >= 80 THEN '優' "
				+ "	WHEN sc.point >= 70 THEN '良' "
				+ "	WHEN sc.point >= 60 THEN '可' "
				+ "	ELSE '不可' "
				+ "END AS evaluation "
				+ "FROM student AS st "
				+ "INNER JOIN score AS sc "
				+ "ON st.id = sc.student_id "
				+ "ORDER BY st.id";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					String evaluation = rs.getString("evaluation");
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point + ",評価=" + evaluation);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("評価を取得ができませんでした", e);
		}
	}

	void rowNumberBySubject() {
		String sql = "SELECT st.name, sc.subject, sc.point, "
				+ "ROW_NUMBER() OVER (PARTITION BY sc.subject ORDER BY sc.point DESC) AS row_num "
				+ "FROM student AS st "
				+ "INNER JOIN score AS sc "
				+ "ON st.id = sc.student_id "
				+ "ORDER BY sc.subject, row_num";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					int rowNumber = rs.getInt("row_num");
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point + ",連番=" + rowNumber);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("各科目の連番取得ができませんでした", e);
		}
	}

	void rankBySubject() {
		String sql = "SELECT st.name, sc.subject, sc.point, "
				+ "RANK() OVER (PARTITION BY sc.subject ORDER BY sc.point DESC) AS rank_num "
				+ "FROM student AS st "
				+ "INNER JOIN score AS sc "
				+ "ON st.id = sc.student_id "
				+ "ORDER BY sc.subject, rank_num";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					int rankNum = rs.getInt("rank_num");
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point + ",順位=" + rankNum);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("各科目の順位データ取得ができませんでした", e);
		}
	}

	void pointWithStudentAvg() {
		String sql = "SELECT st.name, sc.subject, sc.point, "
				+ "AVG(sc.point) "
				+ "OVER (PARTITION BY st.id) AS student_avg "
				+ "FROM student AS st "
				+ "INNER JOIN score AS sc "
				+ "ON st.id = sc.student_id "
				+ "ORDER BY st.id";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					double avg = Math.round(rs.getDouble("student_avg") * 10.0) / 10.0;
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point + ",生徒平均=" + avg);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("学生毎の平均点のデータ取得ができませんでした", e);
		}
	}

	void pointWithAvg() {
		String sql = "SELECT st.name, sc.subject, sc.point, AVG(sc.point) OVER () AS avg_point "
				+ "FROM student AS st "
				+ "INNER JOIN score AS sc "
				+ "ON st.id = sc.student_id "
				+ "ORDER BY st.id";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					String subject = rs.getString("subject");
					int point = rs.getInt("point");
					double avg = Math.round(rs.getDouble("avg_point") * 10) / 10;
					System.out.println("名前=" + name + ", 科目=" + subject + ", 点数=" + point + ",全体平均=" + avg);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("生徒情報と生徒の科目・点数と平均データ取得ができませんでした", e);
		}
	}

	void findLogByStudentIdAndPage(int studentId, String page) {
		// 1回目：EXPLAINで実行計画を表示
		String explainSql = "EXPLAIN SELECT * FROM access_log WHERE student_id = ? AND page = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(explainSql)) {
			ps.setInt(1, studentId);
			ps.setString(2, page);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					System.out.println(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			throw new AppException("実行計画の取得に失敗しました", e);
		}

		// 2回目：実際のデータ取得と時間計測
		String dataSql = "SELECT * FROM access_log WHERE student_id = ? AND page = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(dataSql)) {
			long start = System.currentTimeMillis();
			ps.setInt(1, studentId);
			ps.setString(2, page);
			int count = 0;
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					count++;
				}
			}
			long end = System.currentTimeMillis();
			if (count == 0) {
				System.out.println("該当なし");
			} else {
				System.out.println("取得件数: " + count + "件");
			}
			System.out.println("実行時間: " + (end - start) + "ms");
		} catch (SQLException e) {
			throw new AppException("データ取得に失敗しました", e);
		}
	}

	void explainQuery(int studentId) {
		String sql = "EXPLAIN SELECT * FROM access_log WHERE student_id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, studentId);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					System.out.println(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			throw new AppException("access_logテーブルのデータ取得ができませんでした", e);
		}

	}

	void findLogByStudentId(int studentId) {
		String sql = "SELECT * FROM access_log WHERE student_id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			long start = System.currentTimeMillis();
			ps.setInt(1, studentId);
			try (ResultSet rs = ps.executeQuery();) {
				int count = 0;
				while (rs.next()) {
					count++;
					int studentID = rs.getInt("student_id");
					LocalDateTime time = rs.getTimestamp("accessed_at").toLocalDateTime();
					String page = rs.getString("page");
					System.out.println("========================");
					System.out.println("生徒ID=" + studentID + "、アクセス時間=" + time + "、ページ=" + page);
				}
				System.out.println("取得件数: " + count + "件");
				if (count == 0) {
					System.out.println("該当なし");
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("実行時間: " + (end - start) + "ms");
			// Indexなし： 取得件数: 4938件 実行時間: 70ms
			// Indexあり： 取得件数: 4938件 実行時間: 83ms
		} catch (SQLException e) {
			throw new AppException("指定した生徒IDでデータ取得ができませんでした", e);
		}
	}

	void studentsWithScore() {
		String sql = "SELECT st.name, "
				+ "st.age FROM student AS st "
				+ "WHERE EXISTS("
				+ "		SELECT 1 "
				+ "		FROM score AS sc "
				+ "		WHERE sc.student_id = st.id"
				+ ")";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					int age = rs.getInt("age");
					System.out.println("名前=" + name + "、年齢=" + age);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("得点が存在する生徒の名前と年齢のデータ取得に失敗しました", e);
		}
	}

	void maxScorePerStudent() {
		String sql = "SELECT st.name, tmp.max_p "
				+ "FROM (SELECT student_id, MAX(point) AS max_p FROM score GROUP BY student_id) AS tmp "
				+ "INNER JOIN student AS st "
				+ "ON tmp.student_id = st.id";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					int maxPoint = rs.getInt("max_p");
					System.out.println("名前=" + name + "、最高得点=" + maxPoint);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}

		} catch (SQLException e) {
			throw new AppException("生徒名とその生徒の最高点数のデータ取得に失敗しました", e);
		}
	}

	void aboveAverage() {
		String sql = "SELECT sc.student_id AS id, st.name AS name, sc.point AS point"
				+ " FROM score AS sc"
				+ " LEFT JOIN student AS st"
				+ " ON sc.student_id = st.id"
				+ " WHERE point >= (SELECT AVG(point) FROM score)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					int id = rs.getInt("id");
					String name = rs.getString("name");
					int point = rs.getInt("point");
					System.out.println("名前=" + name + "点数=" + point);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("平均点以上をとった生徒ID・名前・得点のデータ取得に失敗しました", e);
		}
	}

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
		} catch (SQLException e) {
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
				while (rs.next()) {
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

	void totalByStudent() {
		String sql = "SELECT student.name AS name, SUM(score.point) as total_point"
				+ " FROM student"
				+ " INNER JOIN score"
				+ " ON student.id = score.student_id"
				+ " GROUP BY student.id, student.name"
				+ " ORDER BY total_point DESC";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					String name = rs.getString("name");
					int total_score = rs.getInt("total_point");
					System.out.println("名前＝" + name + "、総得点＝" + total_score);
				}
			}
		} catch (SQLException e) {
			throw new AppException("教科の平均点取得に失敗しました", e);
		}
	}

	void highScoreStudents(int minTotal) {
		String sql = "SELECT st.name, SUM(point) AS total_point"
				+ " FROM Student AS st"
				+ " INNER JOIN score AS sc"
				+ " ON st.id = sc.student_id"
				+ " GROUP BY st.id, st.name"
				+ " HAVING SUM(sc.point) >= ?"
				+ " ORDER BY total_point DESC";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setInt(1, minTotal);
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					int totalScore = rs.getInt("total_point");
					System.out.println("名前＝" + name + "、総得点＝" + totalScore);
				}
				if (!found) {
					System.out.println("該当生徒がいません");
				}
			}
		} catch (SQLException e) {
			throw new AppException("教科の平均点取得に失敗しました", e);
		}
	}

	void highScoreExcludeSubject(String excludeSubject, int minTotal) {
		String sql = "SELECT st.name, SUM(point) AS total_point"
				+ " FROM score AS sc"
				+ " INNER JOIN student AS st"
				+ " ON sc.student_id = st.id"
				+ " WHERE sc.subject != ?"
				+ " GROUP BY st.id, st.name"
				+ " HAVING SUM(point) >= ?"
				+ " ORDER BY total_point DESC";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, excludeSubject);
			ps.setInt(2, minTotal);
			try (ResultSet rs = ps.executeQuery();) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					String name = rs.getString("name");
					int totalPoint = rs.getInt("total_point");
					System.out.println("名前＝" + name + "、合計得点＝" + totalPoint);
				}
				if (!found) {
					System.out.println("該当なし");
				}
			}
		} catch (SQLException e) {
			throw new AppException("特定の科目を除外した合計点が高い学生の絞り込みデータ取得に失敗しました", e);
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
