package sample;

import java.sql.*;

public class DB {

    /*
    info DB
     */
    private final String HOST = "localhost";
    private final String PORT = "3307";
    private final String DB_NAME = "registration";
    private final String LOGIN = "root";
    private final String PASS = "root";

    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        /*
        Соединение с БД
         */
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDbConnection();
        System.out.println(dbConn.isValid(1000));
    }

    public boolean regUser(String login, String email, String password) throws SQLException, ClassNotFoundException {
        /*
        Метод добавляеи пользователя в базу данных если он там отсутсвует
         */
        /*
        Запрос на введение данных
         */
        String sql = "INSERT INTO users (login , email, password) VALUES (? , ? , ?)";

        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM users WHERE login = '" + login + "' LIMIT 1");
        if (res.next()) {// если нашелся пользователь с таким же логином отправляем false
            return false;
        }

        /*
        Иначе
        Полставляем значения в запрос и выполням его
         */
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setString(1, login);
        prSt.setString(2, email);
        prSt.setString(3, password);
        prSt.executeUpdate();
        return true;
    }

    public boolean authUser(String login, String password) throws SQLException, ClassNotFoundException {
        /*
        Поиск пользователя в БД
         */
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM users WHERE login = '" + login + "' " +
                "AND password = '" + password + "' LIMIT 1");// execute sql command
        if (res.next()) {// если нашелся пользователь с таким же логином отправляем true
            return true;
        }
        return false;
    }

    public boolean userProfile(String login, String email, String password) throws ClassNotFoundException {
        String sql = "UPDATE users SET login = ? , email = ? " +
                ", password = ? WHERE login = ?";// execute sql command
        try {
            /*
            используем PreparedStatement для подставления значения в команду
             */
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(sql);
            preparedStatement.setString(1, login);// подставление значений
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, login);

            preparedStatement.executeUpdate();//execute

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ResultSet getArticles() throws SQLException, ClassNotFoundException {
        String sql = "SELECT title , intro FROM articles";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        return res;
    }

    public boolean insertStat(String title , String anons , String text) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO articles (title,intro,text) VALUES (?,?,?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(sql);
        preparedStatement.setString(1, title);// подставление значений
        preparedStatement.setString(2, text);
        preparedStatement.setString(3, anons);
        preparedStatement.executeUpdate();//execute

        return true;
    }


}
