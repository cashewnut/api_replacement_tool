package pers.xyy.api_replacement_tool.utils;

import java.sql.*;

public class DBUtil {
    private final static String DRIVER = LoadProperties.get("DRIVER");
    private final static String URL = LoadProperties.get("MYSQLURL");
    private final static String USERNAME = LoadProperties.get("MYSQLUSERNAME");
    private final static String PASSWORD = LoadProperties.get("MYSQLPASSWORD");
    public static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        }catch(Exception e ){
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null)
                preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeStatement(Statement statement){
        try{
            if(statement != null){
                statement.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void closeResultset(ResultSet resultSet){
        try{
            if(resultSet != null)
                resultSet.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection connection = DBUtil.getConnection();
    }

}
