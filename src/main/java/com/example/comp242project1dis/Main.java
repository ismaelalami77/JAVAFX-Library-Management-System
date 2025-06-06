package com.example.comp242project1dis;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView mainView = new MainView();
        mainView.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
