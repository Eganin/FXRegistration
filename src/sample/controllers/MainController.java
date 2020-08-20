package sample.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.DB;
import sample.User;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_exit;

    @FXML
    private VBox paneVBox;

    @FXML
    private Button add_btn;

    private DB db = new DB();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        ResultSet res = db.getArticles();

        while(res.next()) {
            Node node = null;
            try {
                node = FXMLLoader.load(getClass().getResource("/sample/scenes/article.fxml"));

                Label title = (Label) node.lookup("#title");
                title.setText(res.getString("title"));

                Label intro = (Label) node.lookup("#intro");
                intro.setText(res.getString("intro"));

                final Node nodeSet = node;

                node.setOnMouseEntered(event -> {
                    nodeSet.setStyle("-fx-background-color: #707173");
                });

                node.setOnMouseExited(event -> {
                    nodeSet.setStyle("-fx-background-color: #343434");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            HBox hBox = new HBox();
            hBox.getChildren().add(node);
            hBox.setAlignment(Pos.BASELINE_CENTER);
            paneVBox.getChildren().add(hBox);
            paneVBox.setSpacing(10);
        }
        btn_exit.setOnAction(actionEvent -> {
            /*
            Выход на предыдущую страницу
             */

            try {
                FileOutputStream fos = new FileOutputStream("user.settings");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(new User(""));

                oos.close();


                Parent root = FXMLLoader.load(getClass().getResource("/sample/scenes/sample.fxml"));
                Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                primaryStage.setTitle("Регистрация");
                primaryStage.setScene(new Scene(root, 600, 400));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        add_btn.setOnAction(actionEvent -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/sample/scenes/addstat.fxml"));
                Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                primaryStage.setTitle("Добавление статьи");
                primaryStage.setScene(new Scene(root, 600, 400));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }
}