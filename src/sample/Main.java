package sample;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;


public class Main extends Application {
    private WebView webView;
    private WebEngine webEngine;
    private boolean ready;


    @Override
    public void start(Stage stage) throws Exception{
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.load(getClass().getResource("map.html").toExternalForm());
        VBox root = new VBox();
        root.getChildren().addAll(browser);
        Scene scene = new Scene(root);
        stage.setTitle("JavaFX WebView");
        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(600);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
