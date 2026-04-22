import java.sql.*;

public class student {
    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASS = "Chakri@7799";

    // Helper method to get a connection
    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // CREATE: Insert a new student
    public static void insertStudent(int id, String name, String email, String course) throws Exception {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, course);
            pstmt.executeUpdate();
            System.out.println("Inserted successfully!");
        }
    }

    // READ: Show all students
    public static void showStudents() throws Exception {
        String sql = "SELECT * FROM students";
        try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3) + " | " + rs.getString(4));
            }
        }
    }

    // UPDATE: Change details for a specific ID
    public static void updateStudent(int id, String newName) throws Exception {
        String sql = "UPDATE students SET name = ? WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated successfully!");
        }
    }

    // DELETE: Remove a student
    public static void deleteStudent(int id) throws Exception {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Deleted successfully!");
        }
    }

    public static void main(String[] args) throws Exception {
        insertStudent(2, "Alice", "alice@example.com", "ECE");
        updateStudent(1, "Chakri Updated");
        showStudents();
    }
}
