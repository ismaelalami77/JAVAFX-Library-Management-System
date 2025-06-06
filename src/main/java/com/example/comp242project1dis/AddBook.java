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

public class AddBook {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private GridPane grid;
    private HBox hbox;
    private Button addButton, cancelButton;
    private Text bookIdText, titleText, authorText, categoryText, publishedYearText, isbnText;
    public TextField bookIdTextField;
    private TextField titleTextField, authorTextField, categoryTextField, publishedYearTextField, isbnTextField;

    public AddBook() {
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

        addButton = myButton("Add Book");
        cancelButton = myButton("Cancel");
        hbox.getChildren().addAll(addButton, cancelButton);

        root.setCenter(grid);
        root.setBottom(hbox);

        scene = new Scene(root, 400, 500);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Book");
        stage.setResizable(false);

        //Actions
        cancelButton.setOnAction(e -> stage.close());
        addButton.setOnAction(e -> addButtonAction());
    }

    public void showStage() {
        stage.show();
    }

    //add book method
    private void addButtonAction() {
        String title = titleTextField.getText();
        String author = authorTextField.getText();
        String category = categoryTextField.getText();
        String publishedYearString = publishedYearTextField.getText();
        String isbn = isbnTextField.getText();


        //check for empty fields
        if (title == null || title.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Book Title");
            return;
        }
        if (author == null || author.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Author Name");
            return;
        }
        if (category == null || category.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter Book Category");
            return;
        }

        int publishedYear;
        try {
            if (publishedYearString == null || publishedYearString.isEmpty()) {
                myAlert(Alert.AlertType.ERROR, "Please enter Published Year");
                return;
            }
            publishedYear = Integer.parseInt(publishedYearString);

            //check if year is negative value
            if (publishedYear < 0) {
                myAlert(Alert.AlertType.ERROR, "Publish year cannot be negative " + publishedYear);
                return;
            }
            //check if year is greater that current year
            if (publishedYear > LocalDate.now().getYear()) {
                myAlert(Alert.AlertType.ERROR, "Publish year cannot be greater than current year " + publishedYear);
                return;
            }
        } catch (NumberFormatException e) {
            myAlert(Alert.AlertType.ERROR, "Invalid Publish Year!");
            return;
        }

        if (isbn == null || isbn.isEmpty()) {
            myAlert(Alert.AlertType.ERROR, "Please enter ISBN");
            return;
        }

        //check if isbn already exists
        boolean isbnExists = false;

        for (Book book : MainView.books) {
            if (book.getIsbn().equals(isbn)) {
                isbnExists = true;
            }
        }
        if (isbnExists) {
            myAlert(Alert.AlertType.ERROR, "ISBN already exists");
            return;
        }

        Book book = new Book(title, author, category, publishedYear, isbn);

        myAlert(Alert.AlertType.INFORMATION, "Book Added Successfully");
        MainView.books.add(book);
        MainView.bookObservableList.add(book);
        TableViewScene.tableView.refresh();
        clearTextFields();
        stage.close();
    }

    //clear fields method
    private void clearTextFields() {
        bookIdTextField.clear();
        titleTextField.clear();
        authorTextField.clear();
        categoryTextField.clear();
        publishedYearTextField.clear();
        isbnTextField.clear();
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
