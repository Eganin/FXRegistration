package sample.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.DB;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.User;

public class RegController {

    @FXML
    private TextField login_reg;

    @FXML
    private TextField email_reg;

    @FXML
    private PasswordField password_reg;

    @FXML
    private CheckBox checkbox;

    @FXML
    private Hyperlink conf;

    @FXML
    private Button reg_button;

    @FXML
    private TextField login_auth;

    @FXML
    private PasswordField password_auth;

    @FXML
    private Button auth_button;

    @FXML
    private Button user_profile;

    private DB db = new DB();

    @FXML
    void initialize() {
        /*
        меняем цвет оконтовок
         */
        reg_button.setOnAction(actionEvent -> {
            /*
            Регистрация пользователя
             */
            /*
            Длинны вводимых значений
             */
            int loginLength = login_reg.getCharacters().length();
            int emailLength = email_reg.getCharacters().length();
            int passwordLength = password_reg.getCharacters().length();
            /*
            Значения вводимые пользователем
             */
            String login = login_reg.getCharacters().toString();
            String email = email_reg.getCharacters().toString();
            String password = password_reg.getCharacters().toString();

            login_reg.setStyle("-fx-border-color: #fafafa");
            email_reg.setStyle("-fx-border-color: #fafafa");
            password_reg.setStyle("-fx-border-color: #fafafa");
            reg_button.setText("Зарегистрироваться");

            if (loginLength <= 3) {// при ошибке будет появлятся красная обводка
                login_reg.setStyle("-fx-border-color: red");
                return;
            } else if (emailLength <= 5) {
                email_reg.setStyle("-fx-border-color: red");
                return;
            } else if (passwordLength <= 3) {
                password_reg.setStyle("-fx-border-color: red");
                return;
            } else if (!checkbox.isSelected()) {// проверяем нажата ли галочка
                reg_button.setText("Поставьте галочку");
                return;
            }

            String pass = md5String(password);// вызов метода хэширования пароля

            try {
                boolean isReg = db.regUser(login, email, pass);// регистрируем пользователя
                if (isReg) {
                /*
                при успешном добавлении в БД
                очищяем поля
                 */
                    login_reg.setText("");
                    email_reg.setText("");
                    password_reg.setText("");

                    reg_button.setText("Готово");
                } else {
                    reg_button.setText("Введите другой логин");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        auth_button.setOnAction(actionEvent -> {
            /*
            Длинны вводимых значений
             */
            int loginAuthLength = login_auth.getCharacters().length();
            int passwordAuthLength = password_auth.getCharacters().length();

            /*
            Значения вводимые пользователем
             */
            String loginAuth = login_auth.getCharacters().toString();
            String passwordAuth = password_auth.getCharacters().toString();

            /*
            Авторизация пользователя
             */
            password_auth.setStyle("-fx-border-color: #fafafa");
            login_auth.setStyle("-fx-border-color: #fafafa");


            if (loginAuthLength <= 3) {// при ошибке будет появлятся красная обводка
                login_auth.setStyle("-fx-border-color: red");
                return;
            } else if (passwordAuthLength <= 3) {
                password_auth.setStyle("-fx-border-color: red");
                return;
            }

            String pass = md5String(passwordAuth);
            try {
                boolean isAuth = db.authUser(loginAuth, pass);

                if (isAuth) {

                    try {
                        FileOutputStream fos = new FileOutputStream("user.settings");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);

                        oos.writeObject(new User(loginAuth));

                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    login_auth.setText("");
                    password_auth.setText("");

                    auth_button.setText("Готово");

                    /*
                    Открытие нового окна по нажатию кнопки
                    */
                    Parent rootUser = null;
                    try {
                        rootUser = FXMLLoader.load(getClass().getResource("/sample/scenes/main.fxml"));
                        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        primaryStage.setTitle("Новости");
                        primaryStage.setScene(new Scene(rootUser, 600, 400));
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    auth_button.setText("Не найдено");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        user_profile.setOnAction(actionEvent -> {
            try {
                /*
                Открытие нового окна по нажатию кнопки
                 */
                Parent rootUser = FXMLLoader.load(getClass().getResource("/sample/scenes/user_profile.fxml"));
                Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                primaryStage.setTitle("Личный кабинет");
                primaryStage.setScene(new Scene(rootUser, 600, 400));
                primaryStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public static String md5String(String pass) {
        /*
        Функция хэширования паролей
         */
        MessageDigest messageDigest = null; //
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pass.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInteger = new BigInteger(1, digest);
        String md5hex = bigInteger.toString(16);

        while (md5hex.length() < 32) {
            md5hex = "0" + md5hex;
        }

        return md5hex;
    }


}
