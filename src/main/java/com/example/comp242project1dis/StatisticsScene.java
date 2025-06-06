package com.example.comp242project1dis;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class StatisticsScene {
    private BorderPane root;
    private GridPane grid;
    private TextField categoryTextField, authorNameTextField, yearTextField;
    private Text categoryText, authorNameText, yearText;
    private Button maxYearButton, minYearButton, maxAuthorButton, minAuthorButton;

    public StatisticsScene() {
        root = new BorderPane();

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));

        categoryText = myText("Category: ");
        categoryTextField = myTextField();

        authorNameText = myText("Author Name: ");
        authorNameTextField = myTextField();

        yearText = myText("Year: ");
        yearTextField = myTextField();

        maxAuthorButton = myButton("Max Books by Author");
        minAuthorButton = myButton("Min Books by Author");
        maxYearButton = myButton("Max Books by Year");
        minYearButton = myButton("Min Books by Year");

        //adding fields to grid
        grid.add(categoryText, 0, 0);
        grid.add(categoryTextField, 1, 0);

        grid.add(authorNameText, 0, 1);
        grid.add(authorNameTextField, 1, 1);

        grid.add(yearText, 0, 2);
        grid.add(yearTextField, 1, 2);

        grid.add(maxYearButton, 0, 3);
        grid.add(minYearButton, 1, 3);
        grid.add(maxAuthorButton, 0, 4);
        grid.add(minAuthorButton, 1, 4);

        root.setCenter(grid);

        //actions
        authorNameTextField.setOnAction(e -> numberOfBooksByAuthor());
        categoryTextField.setOnAction(e -> numberOfBooksByCategory());
        yearTextField.setOnAction(e -> numberOfBooksByYear());

        maxYearButton.setOnAction(e -> getMaxBooksByYear());
        minYearButton.setOnAction(e -> getMinBooksByYear());
        maxAuthorButton.setOnAction(e -> getMaxBooksByAuthor());
        minAuthorButton.setOnAction(e -> getMinBooksByAuthor());
    }

    public BorderPane getRoot() {
        return root;
    }


    private void numberOfBooksByCategory() {
        //getting the category
        String category = categoryTextField.getText();
        //counter to count number of books
        int count = 0;
        //for loop to check for books
        for (Book book : MainView.books) {
            //check if the category matches
            if (book.getCategory().equalsIgnoreCase(category)) {
                //increment the counter
                count++;
            }
        }
        //check if the counter is empty
        if (count == 0) {
            myAlert(Alert.AlertType.INFORMATION, "No books found for the category: " + category);
            return;
        }
        myAlert(Alert.AlertType.INFORMATION, "There are " + count + " books in the " + category + " category");
    }

    private void numberOfBooksByAuthor() {
        String authorName = authorNameTextField.getText();
        int count = 0;
        for (Book book : MainView.books) {
            //check if names matches
            if (book.getAuthor().equalsIgnoreCase(authorName)) {
                count++;
            }
        }
        //check if the counter is empty
        if (count == 0) {
            myAlert(Alert.AlertType.INFORMATION, "No books found for the author: " + authorName);
            return;
        }
        myAlert(Alert.AlertType.INFORMATION, "There are " + count + " books written by " + authorName);
    }

    private void numberOfBooksByYear() {
        try {
            //converting the year from string to int
            String yearString = yearTextField.getText();
            int count = 0;
            int year = Integer.parseInt(yearString);
            for (Book book : MainView.books) {
                //check if the years equals
                if (book.getPublishedYear() == year) {
                    count++;
                }
            }
            //check if the counter is empty
            if (count == 0) {
                myAlert(Alert.AlertType.INFORMATION, "No books found for the year: " + yearString);
                return;
            }
            myAlert(Alert.AlertType.INFORMATION, count + " Books found for the year: " + year);
        } catch (NumberFormatException e) {
            //number format exception to check if user is searching for strings
            myAlert(Alert.AlertType.ERROR, "Please enter a valid year");
        }
    }

    private void getMaxBooksByYear() {
        int year = 0;
        int maxBooks = 0;

        for (Book book : MainView.books) {
            //defining selected year and a counter to check for number of books
            int selectedYear = book.getPublishedYear();
            int counter = 0;

            //for loop to check if the book year equal the selected year
            for (Book book2 : MainView.books) {
                if (book2.getPublishedYear() == selectedYear) {
                    //if the years are the same the counter will increment
                    counter++;
                }
            }

            //check if the new counter is greater than the maxBooks counter
            //if yes the maxBooks counter will equal the counter
            //and the year will be set to selected year
            if (counter > maxBooks) {
                maxBooks = counter;
                year = selectedYear;
            }
        }

        String maxYearString = "Year with the most books: " + year + " Number of books: " + maxBooks;

        if (!MainView.books.isEmpty()) {
            myAlert(Alert.AlertType.INFORMATION, maxYearString);
        }
        else {
            myAlert(Alert.AlertType.INFORMATION, "No books found");
        }
    }

    private void getMinBooksByYear() {
        int year = 0;
        //minBooks set to 9999 to compare books with fewer counts
        int minBooks = 9999;

        for (Book book : MainView.books) {
            int selectedYear = book.getPublishedYear();
            int counter = 0;

            for (Book book2 : MainView.books) {
                if (book2.getPublishedYear() == selectedYear) {
                    counter++;
                }
            }
            //if the counter is less than minBooks.
            //minBooks will be set to the new counter
            //year will be set to selected year
            if (counter < minBooks) {
                minBooks = counter;
                year = selectedYear;
            }
        }

        String minYearString = "Year with the fewest books: " + year + " Number of books: " + minBooks;
        if (!MainView.books.isEmpty()) {
            myAlert(Alert.AlertType.INFORMATION, minYearString);
        }
        else {
            myAlert(Alert.AlertType.INFORMATION, "No books found");
        }

    }

    private void getMaxBooksByAuthor() {
        //array list to store book titles
        ArrayList<String> booksTitles = new ArrayList<>();
        String authorName = null;
        int maxBooks = 0;

        for (Book book : MainView.books) {
            //temporary array list to add books written by same author
            ArrayList<String> temp = new ArrayList<>();
            String selectedAuthor = book.getAuthor();
            int counter = 0;

            for (Book book2 : MainView.books) {
                //check if the author names equal each other
                if (book2.getAuthor().equalsIgnoreCase(selectedAuthor)) {
                    //increment the counter and adding selected book to the temp array list
                    counter++;
                    temp.add(book2.getTitle());
                }
            }

            //check if the new counter is greater than the maxBooks counter
            //if yes the maxBooks counter will equal the counter
            //author name will be set to selected author
            //book titles list will be set to temp list
            if (counter > maxBooks) {
                maxBooks = counter;
                authorName = selectedAuthor;
                booksTitles = new ArrayList<>(temp);
            }
        }

        String maxAuthorString = "Author with the most books: " + authorName +
                " Number of books: " + maxBooks + " Book Titles: " + booksTitles;
        if (!MainView.books.isEmpty()) {
            myAlert(Alert.AlertType.INFORMATION, maxAuthorString);
        }else {
            myAlert(Alert.AlertType.INFORMATION, "No books found");
        }
    }

    private void getMinBooksByAuthor() {
        ArrayList<String> booksTitles = new ArrayList<>();
        String authorName = null;
        //minBooks set to 9999 to compare books with fewer counts
        int minBooks = 9999;

        for (Book book : MainView.books) {
            ArrayList<String> temp = new ArrayList<>();
            String selectedAuthor = book.getAuthor();
            int counter = 0;

            for (Book book2 : MainView.books) {
                if (book2.getAuthor().equalsIgnoreCase(selectedAuthor)) {
                    counter++;
                    temp.add(book2.getTitle());
                }
            }

            //check if the new counter is less than the minBooks counter
            //if yes the minBooks counter will equal the counter
            //author name will be set to selected author
            //book titles list will be set to temp list

            if (counter < minBooks) {
                minBooks = counter;
                authorName = selectedAuthor;
                booksTitles = new ArrayList<>(temp);
            }
        }


        String minAuthorString = "Author with the fewest books: " + authorName +
                " Number of books: " + minBooks + " Book Titles: " + booksTitles;

        if (!MainView.books.isEmpty()) {
            myAlert(Alert.AlertType.INFORMATION, minAuthorString);
        }
        else {
            myAlert(Alert.AlertType.INFORMATION, "No books found");
        }

    }

    //custom text
    private Text myText(String text) {
        Text myText = new Text(text);
        myText.setStyle("-fx-font-size: 24px; -fx-font-style: italic;");
        return myText;
    }

    //custom button
    private Button myButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
        button.setMinWidth(200);
        button.setMaxWidth(200);
        button.setPrefWidth(200);
        button.setMinHeight(50);
        button.setMaxHeight(50);
        button.setPrefHeight(50);
        return button;
    }

    //custom text field
    private TextField myTextField() {
        TextField myTextField = new TextField();
        myTextField.setStyle("-fx-font-size: 20px;");
        myTextField.setMinHeight(50);
        myTextField.setMaxHeight(50);
        myTextField.setPrefHeight(50);
        return myTextField;
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