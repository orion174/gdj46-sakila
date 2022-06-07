package dao;

import java.util.*;
import java.sql.*;

public class StaffDao {
	
	public List<Map<String,Object>> selectStaffList() {				
		List<Map<String,Object>> list = new ArrayList<>();
		
		// Maria 드라이버 로딩
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// Maria RDBMS에 접속(IP주소, 접속계정 아이디, 패스워드)
		try {
		Class.forName("org.mariadb.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sakila","root","java1234");

		String sql = "SELECT"
				+ "		s1.staff_id staffId,"
				+ "		CONCAT(s1.first_name,' ',s1.last_name) staffName,"
				+ "		s1.address_id addressId,"
				+ "		CONCAT(a.address, IFNULL(a.address2,' '), district) staffAddress,"
				+ "		s1.picture picture,"
				+ "		s1.email email,"
				+ "		s1.store_id storeId,"
				+ "		s1.active active,"
				+ "		s1.username userName,"
				+ "		s1.password password,"
				+ "		s1.last_update lastUpdate"
				+ "		FROM staff s1 "
				+ "		INNER JOIN store s2"
				+ "		INNER JOIN address a"
				+ "		ON s1.store_id = s2.store_id"
				+ "		AND s1.address_id = a.address_id;";
		
		// staff 쿼리 저장
		stmt = conn.prepareStatement(sql);
		rs = stmt.executeQuery();
		
		// 데이터 변환
		while(rs.next()) {
			Map<String,Object> map = new HashMap<>(); // 다형성 
			map.put("staffId", rs.getInt("staffId"));
			map.put("staffName", rs.getString("staffName"));
			map.put("addressId", rs.getInt("addressId"));
			map.put("staffAddress", rs.getString("staffAddress"));
			map.put("picture", rs.getString("picture"));
			map.put("email", rs.getString("email"));
			map.put("storeId", rs.getInt("storeId"));
			map.put("active", rs.getInt("active"));
			map.put("userName", rs.getString("userName"));
			map.put("password", rs.getString("password"));
			map.put("lastUpdate", rs.getString("lastUpdate"));
			list.add(map);
		}
	} catch (Exception e) {	// ClassNotFoundException, SQLException두개의 예외를 부모타입 Exception으로 처리 -> 다형성
		e.printStackTrace();
		System.out.println("Exception 발생");
	} finally { // DB 연결종료 코드
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}
	
	// selectStaffList() 테스트 코드 <- 단위테스트
	public static void main(String[] args) {
		StaffDao sd = new StaffDao();
		List<Map<String,Object>> list = sd.selectStaffList();
		for(Map m : list) {
			System.out.print(m.get("staffId")+", ");
			System.out.print(m.get("staffName")+", ");
			System.out.print(m.get("addressId")+", ");				
			System.out.print(m.get("staffAddress")+", ");
			System.out.print(m.get("picture")+", ");
			System.out.print(m.get("email")+", ");
			System.out.print(m.get("storeId")+", ");
			System.out.print(m.get("active")+", ");
			System.out.print(m.get("userName")+", ");
			System.out.print(m.get("password")+", ");
			System.out.print(m.get("lastUpdate")+", ");

		}
	}
}