// 一斉リネーム: com + opt + R
// 最後尾へ移動： com 矢印
// 行を複製： com + opt ＋ 下
// インデックスの整形：　com + shif + F
// タブの切り替え：　com ＋　[ or  ]
// 文字のサイス切り替え：　トラックパッドで２本指

public class Student {

	public static void main(String[] args) {

		StudentDAO dao = new StudentDAO();
		
		try {
			dao.findLogByStudentIdAndPage(1, "page_50");
		} catch (AppException e) {
		    System.out.println(e.getMessage());
		    e.printStackTrace();
		}
	}
}


//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.explainQuery(1);
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.findLogByStudentId(1);
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.studentsWithScore();
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.maxScorePerStudent();
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.aboveAverage();
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}


//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.highScoreExcludeSubject("英語",70);
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			dao.highScoreStudents(200);
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static void main(String[] args) {
//
//		StudentDAO dao = new StudentDAO();
//		
//		try {
//			System.out.println(">>>>>>>>>>>>>>>>>>>find all");
//			List<String> list = dao.findAll();
//			list.forEach(name -> System.out.println("名前：" + name));
//			System.out.println("====================insert");
//			int insertC = dao.insert("川高", 88);
//			System.out.println(insertC);
//			System.out.println("====================update");
//			int updateC = dao.update(7, "ももたろう", 66);
//			System.out.println(updateC);
//			System.out.println("====================delete");
//			int delete1 = dao.delete(2);
//			int delete2 = dao.delete(7);
//			System.out.println("d1" + delete1 + "d2" + delete2);
//			System.out.println("====================findById");
//			String name2 = dao.findById(16);
//			System.out.println(name2 != null ? "名前：" + name2 : "該当なし");
//			System.out.println("<<<<<<<<<<<<<<<<<<<findAll");
//			List<String> afterList = dao.findAll();
//			afterList.forEach(name -> System.out.println("名前：" + name));
//		} catch (AppException e) {
//		    System.out.println(e.getMessage());
//		    e.printStackTrace();
//		}
//	}
//}

//public class Student {
//
//	public static String url = "jdbc:postgresql://localhost:5432/training";
//	public static String user = "student";
//	public static String password = "password";
//
//	public static void main(String[] args) {
//
//		try {
//			transferWithError(2, "中町", 24);
//		} catch (SQLException e) {
//			 System.out.println("処理失敗・ロールバックしました");
//			e.printStackTrace();
//		}
//	}
//
//	static void transferWithError(int deleteId, String newName, int newAge) throws SQLException {
//		String deleteSql = "DELETE FROM student WHERE id = ?";
//		String insertSql = "INSERT INTO student (name, age) VALUES (?, ?)";
//
//		try (Connection conn = DriverManager.getConnection(url, user, password);
//				PreparedStatement deletePs = conn.prepareStatement(deleteSql);
//				PreparedStatement insertPs = conn.prepareStatement(insertSql);) {
//			conn.setAutoCommit(false);
//			try {
//				deletePs.setInt(1, deleteId);
//				throw new SQLException("強制エラー");
//				} 
//			 catch(SQLException e) {
//					conn.rollback();
//					throw e;
//			}
//		}
//	}
//}

//===========================================INNERJOIN WHERE + ORDER BYで得点が高い方順で表示する
//public class Student {
//
//	public static String url = "jdbc:postgresql://localhost:5432/training";
//	public static String user = "student";
//	public static String password = "password";
//
//	public static void main(String[] args) {
//
//		try {
//			findBySubjectOrderByPoint("英語");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	static void findBySubjectOrderByPoint(String subject) throws SQLException {
//		String sql = "SELECT name, subject, point "
//				+ "FROM student "
//				+ "INNER JOIN score "
//				+ "ON student.id = score.student_id "
//				+ "WHERE score.subject = ? "
//				+ "ORDER BY point DESC";
//				
//		boolean found = false;
//		try (Connection conn = DriverManager.getConnection(url, user, password);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setString(1, subject);
//			try (ResultSet rs = ps.executeQuery();) {
//				while (rs.next()) {
//					if (!found) found = true;
//					found = true;
//					String name = rs.getString("name");
//					String getSubject = rs.getString("subject");
//					int point = rs.getInt("point");
//					System.out.println("名前=" + name + ",科目=" + getSubject + ",点数=" + point);
//				}
//			}
//			if (!found) {
//				System.out.println("該当なし");
//			}
//		}
//	}
//}

// ===========================================INNER JOIN + WHERE 
//public class Student {
//
//	public static String url = "jdbc:postgresql://localhost:5432/training";
//	public static String user = "student";
//	public static String password = "password";
//
//	public static void main(String[] args) {
//
//		try {
//			findBySubject("英語");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	static void findBySubject(String subject) throws SQLException {
//		String sql = "SELECT name, subject, point FROM student INNER JOIN score ON student.id = score.student_id WHERE score.subject = ?";
//		boolean found = false;
//		try (Connection conn = DriverManager.getConnection(url, user, password);
//				PreparedStatement ps = conn.prepareStatement(sql);) {
//			ps.setString(1, subject);
//			try (ResultSet rs = ps.executeQuery();) {
//				while (rs.next()) {
//					found = true;
//					String name = rs.getString("name");
//					String getSubject = rs.getString("subject");
//					int point = rs.getInt("point");
//					System.out.println("名前=" + name + ",科目=" + getSubject + ",点数=" + point);
//				}
//			}
//			if (!found) {
//				System.out.println("該当なし");
//			}
//		}
//	}
//}

// =====================================テーブル結合　StudentとScoreの結合。カラムの取得
//public class Student {
//
//	public static String url = "jdbc:postgresql://localhost:5432/training";
//	public static String user = "student";
//	public static String password = "password";
//	
//	public static void main(String[] args) {
//		
//		String sql = "SELECT name, subject, point FROM student INNER JOIN score ON student.id = score.student_id";
//		
//		try(Connection conn = DriverManager.getConnection(url, user, password);
//				PreparedStatement ps = conn.prepareStatement(sql);
//				ResultSet rs = ps.executeQuery()) {
//			while (rs.next()) {
//				String name = rs.getString("name");
//				String subject = rs.getString("subject");
//				int point = rs.getInt("point");
//				System.out.println("名前=" + name + ",科目=" + subject + ",点数=" + point);
//			}
//		} catch(SQLException e) {
//			System.out.println(e);
//		}
//	}
//	
//}

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
