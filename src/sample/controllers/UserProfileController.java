package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DB;

import static sample.controllers.RegController.md5String;

public class UserProfileController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_user;

    @FXML
    private TextField password_user;

    @FXML
    private TextField address_user;

    @FXML
    private Button refactor_button;

    @FXML
    private Button back_button;

    private DB db = new DB();

    @FXML
    void initialize() {
        /*
        при нажатии кнопеи изменении данных
         */
        refactor_button.setOnAction(actionEvent -> {
            login_user.setStyle("-fx-border-color: #fafafa");
            password_user.setStyle("-fx-border-color: #fafafa");
            address_user.setStyle("-fx-border-color: #fafafa");
            refactor_button.setText("Изменить данные");

            if (login_user.getCharacters().length() <= 3) {
                login_user.setStyle("-fx-border-color: red");
            } else if (password_user.getCharacters().length() <= 3) {
                password_user.setStyle("-fx-border-color: red");
            } else if (login_user.getCharacters().length() <= 3) {
                address_user.setStyle("-fx-border-color: red");
            }

            String pass = md5String(password_user.getCharacters().toString());// вызов метода хэширования пароля

            try {
                boolean isAuth = db.userProfile(login_user.getCharacters().toString(), address_user.getCharacters().toString(), pass);
                System.out.println(isAuth);

                if (isAuth) {
                    login_user.setText("");
                    address_user.setText("");
                    password_user.setText("");

                    refactor_button.setText("Готово");

                } else {
                    refactor_button.setText("Ошибка");
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        back_button.setOnAction(actionEvent -> {
            try {
                /*
                Открытие нового окна по нажатию кнопки
                 */
                Parent rootUser = FXMLLoader.load(getClass().getResource("/sample/scenes/sample.fxml"));
                Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                primaryStage.setTitle("Регистрация");
                primaryStage.setScene(new Scene(rootUser, 600, 400));
                primaryStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
