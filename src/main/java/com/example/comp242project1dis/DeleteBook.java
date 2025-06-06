package com.example.comp242project1dis;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DeleteBook {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private HBox centerHbox, bottomHbox;
    private Text bookIdText;
    private TextField bookIdTextField;
    private Button deleteButton, cancelButton;

    public DeleteBook(){
        root = new BorderPane();

        centerHbox = new HBox();
        centerHbox.setSpacing(20);
        centerHbox.setAlignment(Pos.CENTER);

        bottomHbox = new HBox();
        bottomHbox.setSpacing(20);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setPadding(new Insets(20,20,20,20));

        bookIdText = new Text("Book ID: ");
        bookIdTextField = new TextField();

        centerHbox.getChildren().addAll(bookIdText, bookIdTextField);

        deleteButton = myButton("Delete");
        cancelButton = myButton("Cancel");

        bottomHbox.getChildren().addAll(deleteButton, cancelButton);

        root.setCenter(centerHbox);
        root.setBottom(bottomHbox);

        scene = new Scene(root,300,200);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Delete Book");
        stage.setResizable(false);
        
        //Actions
        cancelButton.setOnAction(e -> stage.close());
        deleteButton.setOnAction(e -> deleteBookAction());
    }

    //delete book action
    private void deleteBookAction() {

        try {
            int bookId = Integer.parseInt(bookIdTextField.getText());

            Book bookToDelete = null;

            //check if book exists
            for (Book book : MainView.books) {
                if (book.getBookId() == bookId) {
                    bookToDelete = book;
                }
            }

            if (bookToDelete != null) {
                //remove book from list
                MainView.books.remove(bookToDelete);
                MainView.bookObservableList.remove(bookToDelete);
                myAlert(Alert.AlertType.INFORMATION, "Book deleted successfully!");
            } else {
                //if book not found
                myAlert(Alert.AlertType.ERROR, "Book not Found!");
            }

        }catch (NumberFormatException e){
            myAlert(Alert.AlertType.ERROR, "Invalid Book ID!");
        }
        bookIdTextField.clear();
        stage.close();

    }

    public void showStage(){
        stage.show();
    }

    //custom button
    private Button myButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(100);
        button.setMaxWidth(100);
        button.setPrefWidth(100);

        button.setMinHeight(50);
        button.setMaxHeight(50);
        button.setPrefHeight(50);

        return button;
    }

    //custom alert
    private Alert myAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert");
        alert.setContentText(content);
        alert.showAndWait();
        return alert;
    }
}
