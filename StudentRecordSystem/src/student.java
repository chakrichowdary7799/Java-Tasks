import java.sql.*;

public class student {
    public static void main(String[] args) throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/student_db";
        String username = "root";
        String password = "Chakri@7799";
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, username, password);
        Statement stmt = con.createStatement();
        stmt.executeUpdate("DROP TABLE students");
        con.close();
    }
}