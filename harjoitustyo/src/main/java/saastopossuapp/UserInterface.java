
package saastopossuapp;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserDao;
import saastopossuapp.database.Database;
import saastopossuapp.domain.Logic;


public class UserInterface extends Application {
    private Database db;
    private UserDao userDao;
    private ActivityDao activityDao;
    private Logic logic;

    public void init() throws ClassNotFoundException {
        db = new Database("data.db");
        userDao = new UserDao(db);
        activityDao = new ActivityDao(db);
        logic = new Logic(userDao, activityDao);
    }
    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Säästöpossu");
        Label loginInstructionLabel = new Label("Type username and press login");
        Button loginButton = new Button("Login");
        Label passwordErrorLabel = new Label("");
        PasswordField passwordField = new PasswordField();
        passwordErrorLabel.setUserData("passwordErrorLabel");   
        Button createUserLabel = new Button("Create new user account");
              
        
        GridPane passwordLayout = new GridPane();
        passwordLayout.add(loginInstructionLabel, 0, 0);
        passwordLayout.add(passwordField, 0, 1);
        passwordLayout.add(loginButton, 0, 2);
        passwordLayout.add(passwordErrorLabel, 0, 3);
        passwordLayout.add(createUserLabel, 0, 4);
        passwordLayout.setPrefSize(1000, 480);
        passwordLayout.setAlignment(Pos.CENTER);
        passwordLayout.setVgap(10);
        passwordLayout.setHgap(10);
        passwordLayout.setPadding(new Insets(20, 20, 20, 20));

        Label welcomeLabel = new Label("Welcome!");
        StackPane welcomeLayout = new StackPane();
        welcomeLayout.setPrefSize(1000, 480);
        welcomeLayout.getChildren().add(welcomeLabel);
        welcomeLayout.setAlignment(Pos.CENTER);
        
        //create user layout:
        GridPane createUserLayout = new GridPane();
        Label howToCreateText = new Label("Type username to create a new account:");
        TextField newUsernameField = new TextField();
        Button signInButton = new Button("Sign in");
        createUserLayout.add(howToCreateText, 0, 0);
        createUserLayout.add(newUsernameField, 0, 1);
        createUserLayout.add(signInButton, 1, 1);
        Button backToLoginButton = new Button ("Back");
        createUserLayout.add(backToLoginButton, 2, 1);
        createUserLayout.setPrefSize(1000, 480);
        createUserLayout.setAlignment(Pos.CENTER);
        createUserLayout.setVgap(10);
        createUserLayout.setHgap(10);
        createUserLayout.setPadding(new Insets(20, 20, 20, 20));
        
        
        //SCENES:
        Scene passwordScene = new Scene(passwordLayout);
        Scene welcomeScene = new Scene(welcomeLayout);   //EI NÄY
        Scene createUserScene = new Scene(createUserLayout);
        
        createUserLabel.setOnMouseClicked(event -> {
            primaryStage.setScene(createUserScene);
        });
        
        signInButton.setOnMouseClicked(event -> {
            primaryStage.setScene(passwordScene);
        });
        
        backToLoginButton.setOnMouseClicked(event -> {
            primaryStage.setScene(passwordScene);
        });
        
                   
        loginButton.setOnAction((event) -> {
            try {
                if (logic.checkUsername(passwordField.getText())==false) {
                    passwordErrorLabel.setTextFill(Color.RED);
                    passwordErrorLabel.setText("Invalid username");
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            primaryStage.setScene(welcomeScene);  

            // Start-Scene and start-layout:
            BorderPane startLayout = new BorderPane();            
            Scene startScene = new Scene(startLayout);  
            startLayout.setPrefSize(1000, 480);
            startLayout.setPadding(new Insets(20, 20, 20, 20));
            
            //components for right side of the screen: 
            Label transactionLabel = new Label("Expense:");
            TextField eurosFieldTransaction = new TextField();
            eurosFieldTransaction.setPromptText("euros");
            TextField centsFieldTransaction = new TextField();
            centsFieldTransaction.setPromptText("cents");
            Button addActivityButton = new Button("Add");
            Label inputErrorMessage = new Label (" ");
            Label dateLabel = new Label ("Date:  ");
            DatePicker transactionDatePicker = new DatePicker(LocalDate.now());
            transactionDatePicker.setEditable(false);
            Label showLabel = new Label("Show expenses");
            Label fromLabel = new Label("From:  ");
            Label minusLabel = new Label("To:     ");
            DatePicker afterDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
            afterDatePicker.setEditable(false);
            DatePicker beforeDatePicker = new DatePicker(LocalDate.now());
            beforeDatePicker.setEditable(false);
            Label expenseLabel = new Label (" ");
            Label totalExpenses = new Label (" ");

            totalExpenses.setText(logic.getBudgetAnalysis(afterDatePicker.getValue(), beforeDatePicker.getValue(), passwordField.getText()));
            
            //analysis: 
            VBox analysisLayout = new VBox();
            analysisLayout.setPadding(new Insets(20, 20, 20, 40));
            analysisLayout.getChildren().add(totalExpenses);
            analysisLayout.getChildren().add(expenseLabel);
            startLayout.setTop(analysisLayout);
            
            
            afterDatePicker.setOnAction((afterdaychosen) -> {
                try {
                    startLayout.setCenter(logic.createChart(afterDatePicker.getValue(), beforeDatePicker.getValue(), expenseLabel.getText(), passwordField.getText()));
                    refreshScreen(afterDatePicker,  beforeDatePicker,  expenseLabel,  passwordField,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
                } catch (SQLException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            });
            
            beforeDatePicker.setOnAction((beforedaychosen) -> {
                try {
                    startLayout.setCenter(logic.createChart(afterDatePicker.getValue(), beforeDatePicker.getValue(), expenseLabel.getText(), passwordField.getText()));
                    refreshScreen(afterDatePicker,  beforeDatePicker,  expenseLabel,  passwordField,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
                } catch (SQLException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            Button transactionButton = new Button ("Add");
            transactionButton.setOnAction((beforedaychosen) -> {
                if (logic.addExpense(eurosFieldTransaction.getText(), centsFieldTransaction.getText(), logic.convertToDate(transactionDatePicker.getValue()), passwordField.getText())){
                    refreshScreen(afterDatePicker,  beforeDatePicker,  expenseLabel,  passwordField,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
                }else{
                    inputErrorMessage.setTextFill(Color.RED);
                    inputErrorMessage.setText("Invalid input");
                }
            });
            
            Button settingsButton = new Button ("Settings");
            Button logoutButton = new Button ("logout");
            logoutButton.setOnAction((logout) -> {
                primaryStage.setScene(passwordScene);
                passwordField.clear();
            });

            // transaction-layout: 
            GridPane pane1 = new GridPane();
            pane1.setAlignment(Pos.CENTER);
            pane1.setVgap(10);
            pane1.setHgap(10);
            pane1.setPadding(new Insets(20, 20, 20, 20));
            pane1.add(transactionLabel, 0, 0);
            pane1.add(eurosFieldTransaction, 0, 1);
            pane1.add(centsFieldTransaction, 0, 2); //col, row
            HBox dateRowLayout = new HBox();
            dateRowLayout.setAlignment(Pos.CENTER);
            dateRowLayout.getChildren().add(dateLabel);
            dateRowLayout.getChildren().add(transactionDatePicker);
            pane1.add(dateRowLayout, 0, 3);
            pane1.add(transactionButton, 0, 4);
            pane1.add(inputErrorMessage, 0, 5);
            
            //show dates -layout: 
            GridPane pane2 = new GridPane();
            pane2.setAlignment(Pos.CENTER);
            pane2.setVgap(10);
            pane2.setHgap(10);
            pane2.setPadding(new Insets(20, 20, 20, 20));
            pane2.add(showLabel, 0, 0);
            HBox showRowLayout = new HBox();
            showRowLayout.setAlignment(Pos.CENTER);
            HBox showRowLayout2 = new HBox();
            showRowLayout2.setAlignment(Pos.CENTER);
            showRowLayout.getChildren().add(fromLabel);
            showRowLayout.getChildren().add(afterDatePicker);
            
            showRowLayout2.getChildren().add(minusLabel);
            showRowLayout2.getChildren().add(beforeDatePicker);
            pane2.add(showRowLayout, 0, 1);
            pane2.add(showRowLayout2, 0,2);
            
            //Layout for settings and logout: 
            GridPane pane3 = new GridPane();
            pane1.setAlignment(Pos.CENTER);
            pane1.setVgap(10);
            pane1.setHgap(10);
            pane3.setPadding(new Insets(20, 20, 20, 20));
            pane3.add(settingsButton, 0, 0);
            pane3.add(logoutButton, 2, 0);
            
            VBox rightLayout = new VBox();
            rightLayout.getChildren().add(pane1);
            rightLayout.getChildren().add(pane2);
            rightLayout.getChildren().add(pane3);
            startLayout.setRight(rightLayout);
         
            refreshScreen(afterDatePicker,  beforeDatePicker,  expenseLabel,  passwordField,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
            
            //Settings-Scene: 
            GridPane settingsLayout = new GridPane();
            settingsLayout.setPrefSize(600, 480);
            settingsLayout.setAlignment(Pos.CENTER);
            settingsLayout.setVgap(10);
            settingsLayout.setHgap(10);
            settingsLayout.setPadding(new Insets(20, 20, 20, 20));
            
            HBox budgetRow = new HBox();
            budgetRow.setAlignment(Pos.CENTER);
            Label budgetLabel = new Label("Set your daily budget:  ");
            TextField eurosFieldBudget = new TextField();
            eurosFieldBudget.setPromptText("euros");
            TextField centsFieldBudget = new TextField();
            centsFieldBudget.setPromptText("cents");
            Button backButton = new Button("back");
            Label budgetErrorMessage = new Label (" ");
            backButton.setOnAction((setPressed) -> {
                primaryStage.setScene(startScene);
            });
            
            Button setBudgetButton = new Button("set");
            setBudgetButton.setOnAction((setPressed) -> {
                if (logic.changeBudget(eurosFieldBudget.getText(), centsFieldBudget.getText(), passwordField.getText())){
                    totalExpenses.setText(logic.getBudgetAnalysis(afterDatePicker.getValue(), beforeDatePicker.getValue(), passwordField.getText()));
                    primaryStage.setScene(startScene);
                    budgetErrorMessage.setText("  ");
                }else{
                    budgetErrorMessage.setTextFill(Color.RED);
                    budgetErrorMessage.setText("Invalid input");
                }
            });
            
            budgetRow.getChildren().add(budgetLabel);
            budgetRow.getChildren().add(eurosFieldBudget);
            budgetRow.getChildren().add(centsFieldBudget);
            budgetRow.getChildren().add(setBudgetButton);
            budgetRow.getChildren().add(backButton);
            Scene settingsScene = new Scene(settingsLayout);
            settingsLayout.add(budgetRow, 0, 0);
            settingsLayout.add(budgetErrorMessage, 0, 1);
            
            settingsButton.setOnAction((settings) -> {
                primaryStage.setScene(settingsScene);
            });
                
            dateLabel.requestFocus();
            backButton.requestFocus();
            primaryStage.setScene(startScene);
            
        
        });
        primaryStage.setScene(passwordScene);
        primaryStage.show();
        
    }
    public static void main(String[] args) {
        launch(UserInterface.class);
        
    }
    public void refreshScreen(DatePicker afterDatePicker, DatePicker beforeDatePicker, Label expenseLabel, PasswordField passwordField, Label totalExpenses, BorderPane startLayout, TextField centsFieldTransaction, TextField eurosFieldTransaction, Label inputErrorMessage){
        //pylväskaavio:
        try {
            BarChart <String, Number> pylvaskaavio = logic.createChart(afterDatePicker.getValue(), beforeDatePicker.getValue(), expenseLabel.getText(), passwordField.getText());
            for (XYChart.Series<String,Number> serie: pylvaskaavio.getData()){
                for (XYChart.Data<String, Number> item: serie.getData()){
                    item.getNode().setOnMouseEntered((MouseEvent entered) -> {
                        expenseLabel.setText("Expenses on "+ item.getXValue()+ ": "+item.getYValue().toString()+ " €");
                    });
                    item.getNode().setOnMouseExited((MouseEvent exited) -> {
                        expenseLabel.setText(" ");
                    });
                }
            }
            totalExpenses.setText(logic.getBudgetAnalysis(afterDatePicker.getValue(), beforeDatePicker.getValue(), passwordField.getText()));
            startLayout.setCenter(pylvaskaavio);
        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        totalExpenses.setText(logic.getBudgetAnalysis(afterDatePicker.getValue(), beforeDatePicker.getValue(), passwordField.getText()));
        centsFieldTransaction.clear();
        eurosFieldTransaction.clear();
        inputErrorMessage.setText(" ");
    }
}

