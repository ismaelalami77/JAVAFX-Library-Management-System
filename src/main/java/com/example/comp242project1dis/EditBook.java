package com.example.comp242project1dis;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditBook {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private GridPane grid;
    private HBox hbox;
    private Button editButton, cancelButton;
    private Text bookIdText, titleText, authorText, categoryText, publishedYearText, isbnText;
    private TextField bookIdTextField, titleTextField, authorTextField, categoryTextField, publishedYearTextField, isbnTextField;
    private Book selectedBook;

    public EditBook() {
        root = new BorderPane();
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));

        hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        hbox.setAlignment(Pos.CENTER);

        bookIdText = new Text("Book ID: ");
        bookIdTextField = new TextField();
        bookIdTextField.setEditable(false);

        titleText = new Text("Title: ");
        titleTextField = new TextField();

        authorText = new Text("Author: ");
        authorTextField = new TextField();

        categoryText = new Text("Category: ");
        categoryTextField = new TextField();

        publishedYearText = new Text("Published Year: ");
        publishedYearTextField = new TextField();

        isbnText = new Text("ISBN: ");
        isbnTextField = new TextField();

        grid.add(bookIdText, 0, 0);
        grid.add(bookIdTextField, 1, 0);
        grid.add(titleText, 0, 1);
        grid.add(titleTextField, 1, 1);
        grid.add(authorText, 0, 2);
        grid.add(authorTextField, 1, 2);
        grid.add(categoryText, 0, 3);
        grid.add(categoryTextField, 1, 3);
        grid.add(publishedYearText, 0, 4);
        grid.add(publishedYearTextField, 1, 4);
        grid.add(isbnText, 0, 5);
        grid.add(isbnTextField, 1, 5);

        editButton = myButton("Edit Book");
        cancelButton = myButton("Cancel");

        hbox.getChildren().addAll(editButton, cancelButton);

        scene = new Scene(root, 400, 500);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Book");
        stage.setResizable(false);

        root.setCenter(grid);
        root.setBottom(hbox);

        //actions
        cancelButton.setOnAction(e -> stage.close());
        editButton.setOnAction(e -> editBook(selectedBook));
    }

    public void showStage() {
        stage.show();
    }


    //fill fields method
    public void fillFields(Book book) {
        selectedBook = book;
        bookIdTextField.setText(String.valueOf(book.getBookId()));
        titleTextField.setText(book.getTitle());
        authorTextField.setText(book.getAuthor());
        categoryTextField.setText(book.getCategory());
        publishedYearTextField.setText(String.valueOf(book.getPublishedYear()));
        isbnTextField.setText(book.getIsbn());
    }

    private void editBook(Book book) {
        String title = titleTextField.getText();
        String author = authorTextField.getText();
        String category = categoryTextField.getText();
        String isbn = isbnTextField.getText();
        String publishedYearString = publishedYearTextField.getText();

        //check if all fields are field
        if (title.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Book Title");
            return;
        }
        book.setTitle(title);

        if (author.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Book Author");
            return;
        }
        book.setAuthor(author);

        if (category.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Book Category");
            return;
        }
        book.setCategory(category);

        if (isbn.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Book ISBN");
            return;
        }
        book.setIsbn(isbn);

        //year validation
        try {
            int publishedYear = Integer.parseInt(publishedYearString);
            int currentYear = LocalDate.now().getYear();

            if (publishedYear < 0) {
                myAlert(Alert.AlertType.ERROR, "Publish year cannot be negative: " + publishedYear);
                return;
            }
            if (publishedYear > currentYear) {
                myAlert(Alert.AlertType.ERROR, "Publish year cannot be greater than current year: " + publishedYear);
                return;
            }

            book.setPublishedYear(publishedYear);

        } catch (NumberFormatException e) {
            myAlert(Alert.AlertType.ERROR, "Please enter a valid publish year");
            return;
        }

        TableViewScene.tableView.refresh();
        stage.close();
    }

    //custom alert
    private Alert myAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert");
        alert.setContentText(content);
        alert.showAndWait();
        return alert;
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
}
