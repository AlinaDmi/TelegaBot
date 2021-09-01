import org.sqlite.JDBC;

import java.sql.*;
import java.util.*;

public class DbHandler {

    private static final String CON_STR = "jdbc:sqlite:users.db";

    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private final Connection connection;

    DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public List<User> getAllUsers() {

        try (Statement statement = this.connection.createStatement()) {

            List<User> users = new ArrayList<User>();

            ResultSet resultSet = statement.executeQuery("SELECT id, chat_id, sub, city FROM users");
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getInt("chat_id"),
                        resultSet.getBoolean("sub"),
                        resultSet.getString("city")
                        ));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<User> getFollowed() {

        try (Statement statement = this.connection.createStatement()) {
            List<User> users = new ArrayList<User>();
            ResultSet resultSet = statement.executeQuery("SELECT id,chat_id, sub, city FROM users WHERE sub = 1");
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getInt("chat_id"),
                        resultSet.getBoolean("sub"),
                        resultSet.getString("city")
                ));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



    public void addUser(Integer id,Integer chat_id, boolean sub, String city) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO Users(`id`, `chat_id`, `sub`, `city`) " +
                        "VALUES(?, ?, ?, ?)")) {
            statement.setObject(1, id);
            statement.setObject(2, chat_id);
            statement.setObject(3, sub);
            statement.setObject(4, city);

            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM Users WHERE id = ?")) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Subscribe(int id, String city,List<User> users) {
        for (User user : users){
            if (user.id==id){
                user.sub=true;
                user.city=city;
            }
        }
        try (PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE Users SET city=? ,"+"sub=?"+" WHERE id = ?")) {
            statement.setObject(1, city);
            statement.setObject(2, true);
            statement.setObject(3, id);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void UnSubscribe(int id,List<User> users) {
        for (User user : users){
            if (user.id==id){
                user.sub=false;
                user.city="none";
            }
        }
        try (PreparedStatement statement = this.connection.prepareStatement(

                "UPDATE Users SET city=? ,"+"sub=?"+" WHERE id = ?")) {
            statement.setObject(1, "none");
            statement.setObject(2, false);
            statement.setObject(3, id);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}