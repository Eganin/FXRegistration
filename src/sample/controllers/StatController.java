package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DB;

public class StatController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add_stat;

    @FXML
    private TextArea anons_stat;

    @FXML
    private TextArea text_stat;

    @FXML
    private Button back_btn;

    @FXML
    private TextField name_stating;

    private DB db = new DB();

    @FXML
    void initialize() {
        add_stat.setOnAction(actionEvent -> {
            name_stating.setStyle("-fx-border-color: #fafafa");
            anons_stat.setStyle("-fx-border-color: #fafafa");
            text_stat.setStyle("-fx-border-color: #fafafa");
            String anons_text = anons_stat.getText();
            String text = text_stat.getText();
            String name = name_stating.getCharacters().toString();
            try {
                boolean isAdd =db.insertStat(name,anons_text,text);
                if (isAdd){
                    name_stating.setText("");
                    anons_stat.setText("");
                    text_stat.setText("");
                    add_stat.setText("Готово");
                }else {
                    add_stat.setText("Введите другое");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        back_btn.setOnAction(actionEvent -> {
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
        });

    }
}
