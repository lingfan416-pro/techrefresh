package org.lfan142.jdbc.connection;

import java.sql.*;

public class StudentOperation {

    public static void insertStusent(String name, int age, String email){
        String sql = "INSERT INTO students(name, age, email) VALUES(?, ?, ?)";

        try(Connection conn = StudentCRUD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " student inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void getAllStudents(){
        String sql = "SELECT * FROM students";

        try(Connection conn = StudentCRUD.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getInt("age") + " | " +
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateStudent(int id, String newEmail){
        String sql = "UPDATE students SET email=? WHERE id=?";

        try(Connection conn = StudentCRUD.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, newEmail);
            stmt.setInt(2, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id){
        String deleteSql = "DELETE From students WHERE id =?";
        try(Connection conn = StudentCRUD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(deleteSql);){
            stmt.setInt(1, id);
            int row = stmt.executeUpdate();
            System.out.println(row + " student deleted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
