package org.lfan142.jdbc.connection;

import java.sql.*;

public class JdbcConnection {

    public static void main(String[] args) throws ClassNotFoundException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/mydb";
        String userName = "root";
        String password = "123456";
        try {
           Connection connection = DriverManager.getConnection(url, userName, password);
           PreparedStatement statement = connection.prepareStatement("select * from designation");
           ResultSet resultSet = statement.executeQuery();
           while(resultSet.next()){
               int code = resultSet.getInt("code");
               String title = resultSet.getString("title");
               System.out.println("code: " + code + ", title" + title);
           }
           resultSet.close();
           statement.close();
           connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
