package com.example.comp242project1dis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.*;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class MainView {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox leftVBox;
    private Button tableViewButton, statisticsButton, activeAuthorsButton, chooseFileButton, saveFileButton;
    public static ArrayList<Book> books = new ArrayList<>();
    public static ObservableList<Book> bookObservableList = FXCollections.observableArrayList(books);

    TableViewScene tableViewScene = new TableViewScene();
    StatisticsScene statisticsScene = new StatisticsScene();

    public MainView() {
        root = new BorderPane();

        leftVBox = new VBox();
        leftVBox.setSpacing(20);
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setPrefWidth(240);
        leftVBox.setStyle("-fx-background-color: #0073e6");

        tableViewButton = myButton("Table View");
        statisticsButton = myButton("Statistics");
        activeAuthorsButton = myButton("Active Authors");
        chooseFileButton = myButton("Choose File");
        saveFileButton = myButton("Save File");

        leftVBox.getChildren().addAll(tableViewButton, statisticsButton, activeAuthorsButton, chooseFileButton, saveFileButton);

        root.setLeft(leftVBox);
        root.setCenter(tableViewScene.getRoot());

        scene = new Scene(root, 800, 600);
        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);


        tableViewButton.setOnAction(e -> TableViewButtonAction());
        statisticsButton.setOnAction(e -> StatisticsButtonAction());
        chooseFileButton.setOnAction(e -> chooseFileButtonAction());
        saveFileButton.setOnAction(e -> saveFileButtonAction());
        activeAuthorsButton.setOnAction(e -> ActiveAuthors(books));
    }

    public void show() {
        stage.show();
    }

    //scenes changing
    private void TableViewButtonAction() {
        root.setCenter(tableViewScene.getRoot());
    }
    private void StatisticsButtonAction() {
        root.setCenter(statisticsScene.getRoot());
    }


    //read file method
    private void readFile(File file) {
        //counter for invalid lines
        int counter = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                //length validation
                if (parts.length != 6) {
                    continue;
                }

                String title = parts[1].trim();
                String author = parts[2].trim();
                String category = parts[3].trim();

                //year validation
                String publishedYearString = parts[4].trim();
                int publishedYear;

                try {
                    publishedYear = Integer.parseInt(publishedYearString);
                } catch (NumberFormatException e) {
                    counter++;
                    continue;
                }

                if (publishedYear < 0) {
                    counter++;
                    continue;
                }
                if (publishedYear > LocalDate.now().getYear()) {
                    counter++;
                    continue;
                }

                String isbn = parts[5].trim();


                //check for duplicate isbn
                boolean isbnExists = false;
                for (Book book : books) {
                    if (book.getIsbn().equals(isbn)) {
                        isbnExists = true;
                        break;
                    }
                }

                if (isbnExists) {
                    counter++;
                    continue;
                }

                //adding new book to the list
                Book book = new Book(title, author, category, publishedYear, isbn);
                books.add(book);
                bookObservableList.add(book);
            }
        } catch (FileNotFoundException e) {
            myAlert(Alert.AlertType.ERROR, "Error reading File " + file.getName());
        }
        if (counter > 0) {
            myAlert(Alert.AlertType.ERROR, "Error adding " + counter + " books");
        }
    }

    //save file method
    private void saveFileButtonAction() {
        try (PrintWriter printWriter = new PrintWriter("updatedBooks.txt")) {
            for (Book book : books) {
                int bookId = book.getBookId();
                String title = book.getTitle();
                String author = book.getAuthor();
                String category = book.getCategory();
                int publishedYear = book.getPublishedYear();
                String isbn = book.getIsbn();

                printWriter.println(bookId + ", " + title + ", " + author + ", " + category + ", " + publishedYear + ", " + isbn);
            }
            myAlert(Alert.AlertType.INFORMATION, "File saved successfully");
        } catch (IOException e) {
            myAlert(Alert.AlertType.ERROR, "Error saving file");
        }
    }

    //check active authors method
    public void ActiveAuthors(ArrayList<Book> books) {
        //getting current year
        int currentYear = LocalDate.now().getYear();

        //new array list for adding active authors
        ArrayList<String> activeAuthors = new ArrayList<>();
        for (Book book : books) {
            //check if book is published in the last 5 years
            if (book.getPublishedYear() >= (currentYear - 5)) {
                //check if the author is already added to the list
                if (activeAuthors.contains(book.getAuthor())) {
                    //skip added authors
                    continue;
                }
                //add author to the list
                activeAuthors.add(book.getAuthor());
            }
        }
        //check if there is no active authors
        if (activeAuthors.isEmpty()) {
            myAlert(Alert.AlertType.INFORMATION, "No active authors found");
            return;
        }
        myAlert(Alert.AlertType.INFORMATION, "Active Authors: " + activeAuthors);
    }

    //file chooser
    private void chooseFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            readFile(selectedFile);
        }
    }


    //custom alert method
    private Alert myAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert");
        alert.setContentText(content);
        alert.showAndWait();
        return alert;
    }

    //custom button method
    private Button myButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #f4f4f4; -fx-font-size: 24px; -fx-font-style: italic;");
        button.setMinWidth(200);
        button.setMaxWidth(200);
        button.setPrefWidth(200);
        button.setMinHeight(50);
        button.setMaxHeight(50);
        button.setPrefHeight(50);
        return button;
    }
}
