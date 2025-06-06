package com.example.comp242project1dis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collections;

public class TableViewScene {
    private BorderPane root;
    private HBox buttonsHBox, sortHBox, topHBox;
    private VBox bottomVBox;
    private Button addBookButton, editBookButton, deleteBookButton;
    private TextField searchTextField;
    private RadioButton bookIdRadioButton, titleRadioButton;
    private ToggleGroup toggleGroup;
    private RadioButton sortByTitleRadioButton, sortByAuthorRadioButton, sortByYearRadioButton;
    private ToggleGroup sortToggleGroup;
    public static TableView tableView = new TableView();

    AddBook addBook = new AddBook();
    DeleteBook deleteBook = new DeleteBook();
    EditBook editBook = new EditBook();

    public TableViewScene() {
        root = new BorderPane();

        buttonsHBox = new HBox();
        buttonsHBox.setPadding(new Insets(15, 15, 15, 15));
        buttonsHBox.setSpacing(20);
        buttonsHBox.setAlignment(Pos.CENTER);

        topHBox = new HBox();
        topHBox.setPadding(new Insets(15, 15, 15, 15));
        topHBox.setSpacing(20);
        topHBox.setAlignment(Pos.CENTER);

        sortHBox = new HBox();
        sortHBox.setPadding(new Insets(15, 15, 15, 15));
        sortHBox.setSpacing(20);
        sortHBox.setAlignment(Pos.CENTER);

        bottomVBox = new VBox();
        bottomVBox.setPadding(new Insets(5, 5, 5, 5));
        bottomVBox.setAlignment(Pos.CENTER);

        //Table View
        TableColumn<Book, Integer> bookIdCol = new TableColumn<>("Book ID");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book, String> Category = new TableColumn<>("Category");
        Category.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Book, Integer> publishedYearCol = new TableColumn<>("Published Year");
        publishedYearCol.setCellValueFactory(new PropertyValueFactory<>("publishedYear"));
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        tableView.getColumns().addAll(bookIdCol, titleCol, authorCol, Category, publishedYearCol, isbnCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setItems(MainView.bookObservableList);

        //Buttons
        addBookButton = myButton("Add Book");
        editBookButton = myButton("Edit Book");
        deleteBookButton = myButton("Delete Book");

        buttonsHBox.getChildren().addAll(addBookButton, editBookButton, deleteBookButton);

        bottomVBox.getChildren().addAll(sortHBox, buttonsHBox);

        root.setTop(topHBox);
        root.setCenter(tableView);
        root.setBottom(bottomVBox);

        //search
        searchTextField = new TextField();

        bookIdRadioButton = new RadioButton("Book ID");

        titleRadioButton = new RadioButton("Title");

        toggleGroup = new ToggleGroup();
        bookIdRadioButton.setToggleGroup(toggleGroup);
        titleRadioButton.setToggleGroup(toggleGroup);

        topHBox.getChildren().addAll(searchTextField, titleRadioButton, bookIdRadioButton);

        //sort
        sortByTitleRadioButton = new RadioButton("Sort by Title");
        sortByAuthorRadioButton = new RadioButton("Sort by Author");
        sortByYearRadioButton = new RadioButton("Sort by Year");

        sortToggleGroup = new ToggleGroup();
        sortByAuthorRadioButton.setToggleGroup(sortToggleGroup);
        sortByYearRadioButton.setToggleGroup(sortToggleGroup);
        sortByTitleRadioButton.setToggleGroup(sortToggleGroup);

        sortHBox.getChildren().addAll(sortByTitleRadioButton, sortByAuthorRadioButton, sortByYearRadioButton);

        //Actions

        addBookButton.setOnAction(e -> {
            //check if the list is empty
            if (MainView.books.isEmpty()) {
                addBook.bookIdTextField.setText("1");
            } else {
                addBook.bookIdTextField.setText(String.valueOf(MainView.books.getLast().getBookId() + 1));
            }
            addBook.showStage();
        });
        deleteBookButton.setOnAction(e -> deleteBook.showStage());
        editBookButton.setOnAction(e -> editBookAction());
        searchTextField.setOnAction(e -> searchAction());

        sortByAuthorRadioButton.setOnAction(e -> sortAction());
        sortByTitleRadioButton.setOnAction(e -> sortAction());
        sortByYearRadioButton.setOnAction(e -> sortAction());

    }

    public BorderPane getRoot() {
        return root;
    }

    public void editBookAction() {
        Book selectedBook = (Book) tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            myAlert(Alert.AlertType.ERROR, "Please select a Book to edit");
            return;
        }
        editBook.fillFields(selectedBook);
        editBook.showStage();
    }


    private void searchAction() {
        String searchText = searchTextField.getText();
        //new observable list for filtered books
        ObservableList<Book> filteredList = FXCollections.observableArrayList();
        //check which radio button is selected
        if (toggleGroup.getSelectedToggle() != null) {
            RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();

            //search by id
            if (selectedToggle == bookIdRadioButton) {
                //check if id is available
                try {
                    for (Book book : MainView.bookObservableList) {
                        if (book.getBookId() == Integer.parseInt(searchText)) {
                            //add the book details to the list
                            filteredList.add(book);
                            //set the table content to the new list items
                            TableViewScene.tableView.setItems(filteredList);
                        }
                    }
                    //check if a book is not found the table view will be reset.
                    if (filteredList.isEmpty()) {
                        myAlert(Alert.AlertType.INFORMATION, "No Book Found");
                        TableViewScene.tableView.setItems(MainView.bookObservableList);
                    }
                } catch (NumberFormatException e) {
                    myAlert(Alert.AlertType.ERROR, "Invalid Book ID!");
                }

            }
            //search by title
            else if (selectedToggle == titleRadioButton) {
                //check for every book if it contains any word from the title
                for (Book book : MainView.bookObservableList) {
                    if (book.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                        filteredList.add(book);
                        TableViewScene.tableView.setItems(filteredList);
                    }
                }

                if (filteredList.isEmpty()) {
                    myAlert(Alert.AlertType.INFORMATION, "No Book Found");
                    TableViewScene.tableView.setItems(MainView.bookObservableList);
                }
            }
        }
    }

    private void sortAction() {
        //check which radio button is selected
        if (sortToggleGroup.getSelectedToggle() != null) {
            RadioButton selectedToggle = (RadioButton) sortToggleGroup.getSelectedToggle();
            if (selectedToggle == sortByAuthorRadioButton) {
                sortByAuthor();
            }
            if (selectedToggle == sortByTitleRadioButton) {
                sortByTitle();
            }
            if (selectedToggle == sortByYearRadioButton) {
                sortByYear();
            }
        }
    }

    //sort methods
    private void sortByTitle() {
        Collections.sort(MainView.bookObservableList, new SortByTitle());
        tableView.refresh();
    }

    private void sortByAuthor() {
        Collections.sort(MainView.bookObservableList, new SortByAuthor());
        tableView.refresh();
    }

    private void sortByYear() {
        Collections.sort(MainView.bookObservableList, new SortByYear());
        tableView.refresh();
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
        button.setStyle("-fx-font-size: 20px; -fx-font-style: italic;");
        button.setMinWidth(150);
        button.setMaxWidth(150);
        button.setPrefWidth(150);
        button.setMinHeight(50);
        button.setMaxHeight(50);
        button.setPrefHeight(50);
        return button;
    }
}
