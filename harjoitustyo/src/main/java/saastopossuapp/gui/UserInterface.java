
package saastopossuapp.gui;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.logic.Logic;

/**
 * Class creates the graphic user interface. 
 * This is also application's Main Class.
 */
public class UserInterface extends Application {
    private Logic logic;
    private String username; 
    private DatePicker afterDatePicker;
    private DatePicker beforeDatePicker;
    

    @Override
    public void init() throws ClassNotFoundException {
        Database db = new Database();
        UserAccountDao userAccountDao = new UserAccountDao(db);
        ActivityDao activityDao = new ActivityDao(db);
        this.afterDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
        this.beforeDatePicker = new DatePicker(LocalDate.now());
        this.logic = new Logic(userAccountDao, activityDao);
        
        
    }
    @Override
    public void start(Stage primaryStage) throws SQLException {
        //passwordScene
        Label loginInstructionLabel = new Label("Type username and press login");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label passwordErrorLabel = new Label("");
        Button createUserButton = new Button("Create new user account");
        Scene passwordScene = createPasswordScene(loginInstructionLabel, loginButton, passwordErrorLabel, passwordField, createUserButton);
        
        //createNewUser Scene:
        Label howToCreateText = new Label("Type username to create a new account:");
        TextField newUsernameField = new TextField();
        Button signInButton = new Button("Sign in");
        Button backToLoginButton = new Button ("Back");
        Label errorMessage = new Label (" ");
        Scene newUserScene = createNewUserScene(errorMessage, backToLoginButton, howToCreateText, newUsernameField, signInButton);
        
        // Start-Scene
        Label transactionLabel = new Label("Expense amount:   ");
        Label centsSeparator = new Label (" , ");
        Label euroLabel = new Label (" €  ");
        TextField eurosFieldTransaction = new TextField();
        TextField centsFieldTransaction = new TextField();
        Label inputErrorMessage = new Label (" ");
        Label dateLabel = new Label ("Expense date:        ");
        DatePicker transactionDatePicker = new DatePicker(LocalDate.now());
        Label showLabel = new Label("Show expenses");
        Label fromLabel = new Label("From:  ");
        Label minusLabel = new Label("To:     ");
        Label expenseLabel = new Label (" ");
        Label totalExpenses = new Label (" ");
        Button transactionButton = new Button ("Add Expense");
        Button settingsButton = new Button ("Settings");
        Button logoutButton = new Button ("logout");
        Label chooseCategoryLabel = new Label("Expense category: ");
        ComboBox categoryChoice = new ComboBox();
        TextField newCategoryField = new TextField();
                
        BorderPane startLayout = new BorderPane();            
        Scene startScene = createStartScene(euroLabel, centsSeparator, newCategoryField, chooseCategoryLabel, categoryChoice, startLayout, transactionLabel, eurosFieldTransaction, centsFieldTransaction, inputErrorMessage, dateLabel, transactionDatePicker,
        beforeDatePicker, afterDatePicker, showLabel, fromLabel, minusLabel, expenseLabel, totalExpenses, transactionButton, settingsButton, logoutButton);        

        //Settings-Scene: 
        Label budgetLabel = new Label("Set your daily budget:  ");
        TextField eurosFieldBudget = new TextField();
        TextField centsFieldBudget = new TextField();
        Label settingsCentsSeparator = new Label (" , ");
        Label settingsEuroLabel = new Label (" €  ");
        Button backButton = new Button("back");
        Label budgetErrorMessage = new Label (" ");
        Button setBudgetButton = new Button("set");       
        Scene settingsScene = createSettingsScene(settingsEuroLabel, settingsCentsSeparator, eurosFieldBudget, backButton, centsFieldBudget, budgetLabel, setBudgetButton, budgetErrorMessage);
                
        //actions for buttons: 
        createUserButton.setOnMouseClicked(event -> {
            errorMessage.setText(" ");
            newUsernameField.clear();
            primaryStage.setScene(newUserScene);
        });
       
        signInButton.setOnMouseClicked(event -> {
            errorMessage.setText(" ");
            if(logic.createUser(newUsernameField.getText().trim())){
               passwordField.clear();
               primaryStage.setScene(passwordScene);  
            }
            errorMessage.setText("Invalid username. Username must be unique and 2-30 characters (A-Z, a-z, 0-9)");
            errorMessage.setTextFill(Color.RED);
        });
        
        backToLoginButton.setOnMouseClicked(event -> {
            primaryStage.setScene(passwordScene);
        });
            
        setBudgetButton.setOnAction((setPressed) -> {
            if (logic.changeBudget(eurosFieldBudget.getText().trim(), centsFieldBudget.getText().trim())){
                refreshScreen(newCategoryField, categoryChoice, afterDatePicker,  beforeDatePicker,  expenseLabel,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
                primaryStage.setScene(startScene);
                budgetErrorMessage.setText("  ");
            }else{
                budgetErrorMessage.setTextFill(Color.RED);
                budgetErrorMessage.setText("Invalid input");
            }
        });

        afterDatePicker.setOnAction((afterdaychosen) -> {
            refreshScreen(newCategoryField, categoryChoice, afterDatePicker,  beforeDatePicker,  expenseLabel,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
            
        });

        beforeDatePicker.setOnAction((beforedaychosen) -> {
            refreshScreen(newCategoryField, categoryChoice, afterDatePicker,  beforeDatePicker,  expenseLabel,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
        });

        transactionButton.setOnAction((beforedaychosen) -> {
            if (notNull(categoryChoice.getValue()) && logic.addExpense(newCategoryField.getText().trim(), categoryChoice.getValue().toString(), eurosFieldTransaction.getText().trim(), centsFieldTransaction.getText().trim(), transactionDatePicker.getValue())){
                refreshScreen(newCategoryField, categoryChoice, afterDatePicker,  beforeDatePicker,  expenseLabel,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
            } else {
                inputErrorMessage.setTextFill(Color.RED);
                inputErrorMessage.setText("Invalid input.\nExpense must be > 0, cents max. 2 digits, category 3-20 characters.");
            }
        });

        settingsButton.setOnAction((settings) -> {
            primaryStage.setScene(settingsScene);
        });

        logoutButton.setOnAction((logout) -> {
            passwordField.clear();
            primaryStage.setScene(passwordScene);
        });

        backButton.setOnAction((setPressed) -> {
            primaryStage.setScene(startScene);
        });
        
        loginButton.setOnAction((event) -> {
            this.username = passwordField.getText().trim();
            try {
                if (logic.checkUsername(passwordField.getText().trim())==false) {
                    passwordErrorLabel.setTextFill(Color.RED);
                    passwordErrorLabel.setText("Invalid username");
                }else{
                    refreshScreen(newCategoryField, categoryChoice, afterDatePicker,  beforeDatePicker,  expenseLabel,  totalExpenses,  startLayout,  centsFieldTransaction,  eurosFieldTransaction,  inputErrorMessage);
                    primaryStage.setScene(startScene);
                }
            }catch (SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        });         
        
        categoryChoice.setOnAction((chosen) -> {
            if (notNull(categoryChoice.getValue())){
                if (categoryChoice.getValue().equals("create new")){
                    newCategoryField.setVisible(true);
                    primaryStage.setScene(startScene);
                }else{
                    newCategoryField.setVisible(false);
                }
            }
        });
        primaryStage.setTitle("Säästöpossu");
        primaryStage.setScene(passwordScene);
        primaryStage.show();
        
    }
    public static void main(String[] args) {
        launch(UserInterface.class);
        
    }
    private void refreshScreen(TextField newCategoryField, ComboBox categoryChoice, DatePicker afterDatePicker, DatePicker beforeDatePicker, Label expenseLabel, Label totalExpenses, BorderPane startLayout, TextField centsFieldTransaction, TextField eurosFieldTransaction, Label inputErrorMessage){
        BarChart <String, Number> barChart = null;
        try {
            barChart = createBarChart(afterDatePicker.getValue(), beforeDatePicker.getValue());
            listenBars (barChart, expenseLabel);
            startLayout.setCenter(barChart);
            categoryChoice.setItems(logic.createChoices(afterDatePicker.getValue(), beforeDatePicker.getValue()));

        } catch (SQLException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        totalExpenses.setText(logic.getBudgetAnalysis(afterDatePicker.getValue(), beforeDatePicker.getValue()));
        centsFieldTransaction.clear();
        eurosFieldTransaction.clear();
        newCategoryField.clear();
        categoryChoice.getSelectionModel().clearSelection();
        newCategoryField.setVisible(false);
        inputErrorMessage.setText(" ");
    
    }
    private void listenBars(BarChart <String, Number> barChart, Label expenseLabel){
        Tooltip t = new Tooltip("tyhja");
        Tooltip.install(barChart, t);
        for (XYChart.Series<String, Number> serie: barChart.getData()) {
            for (XYChart.Data<String, Number> item: serie.getData()){
                item.getNode().setOnMouseEntered((MouseEvent entered) -> {
                    t.setText("" + item.getYValue() + " €");
                    expenseLabel.setText(logic.getExpenseLabelText(item.getXValue()));
                });
                item.getNode().setOnMouseExited((MouseEvent exited) -> {
                    expenseLabel.setText(" ");
                    t.hide();
                });
            }
        }
        
    }
    private Scene createPasswordScene(Label loginInstructionLabel, Button loginButton, Label passwordErrorLabel, PasswordField passwordField, Button createUserButton){              
        GridPane passwordLayout = new GridPane();
        passwordLayout.add(loginInstructionLabel, 0, 0);
        passwordLayout.add(passwordField, 0, 1);
        passwordLayout.add(loginButton, 0, 2);
        passwordLayout.add(passwordErrorLabel, 0, 3);
        passwordLayout.add(createUserButton, 0, 4);
        passwordLayout.setPrefSize(1060, 700);
        passwordLayout.setAlignment(Pos.CENTER);
        passwordLayout.setVgap(10);
        passwordLayout.setHgap(10);
        passwordLayout.setPadding(new Insets(20, 20, 20, 20));
        return new Scene(passwordLayout);
        
    }
    private Scene createNewUserScene(Label errorMessage, Button backToLoginButton, Label howToCreateText, TextField newUsernameField, Button signInButton){
        GridPane newUserLayout = new GridPane();
        newUserLayout.add(howToCreateText, 0, 0);
        newUserLayout.add(newUsernameField, 0, 1);
        newUserLayout.add(signInButton, 1, 1);
        newUserLayout.add(backToLoginButton, 2, 1);
        newUserLayout.add(errorMessage, 0,3);
        newUserLayout.setPrefSize(1060, 700);
        newUserLayout.setAlignment(Pos.CENTER);
        newUserLayout.setVgap(10);
        newUserLayout.setHgap(10);
        newUserLayout.setPadding(new Insets(20, 20, 20, 20));
        return new Scene(newUserLayout);
        
    }
    public Scene createStartScene(Label euroLabel, Label centsSeparator, TextField newCategoryField, Label chooseCategoryLabel, ComboBox categoryChoice, BorderPane startLayout, Label transactionLabel, TextField eurosFieldTransaction, TextField centsFieldTransaction, Label inputErrorMessage, Label dateLabel, DatePicker transactionDatePicker,
        DatePicker beforeDatePicker, DatePicker afterDatePicker, Label showLabel, Label fromLabel, Label minusLabel, Label expenseLabel, Label totalExpenses, Button transactionButton, Button settingsButton, Button logoutButton) throws SQLException{
        startLayout.setPrefSize(1060, 700);
        startLayout.setPadding(new Insets(20, 20, 20, 20));
        dateLabel.requestFocus();
        transactionDatePicker.setEditable(false);
        afterDatePicker.setEditable(false);
        beforeDatePicker.setEditable(false);
        newCategoryField.setVisible(false);
        
        VBox analysisLayout = new VBox();
        analysisLayout.setPadding(new Insets(20, 20, 20, 40));
        analysisLayout.getChildren().add(totalExpenses);
        analysisLayout.getChildren().add(expenseLabel);
        startLayout.setTop(analysisLayout);
        
        GridPane pane1 = new GridPane();
        pane1.setAlignment(Pos.CENTER);
        pane1.setVgap(10);
        pane1.setHgap(10);
        pane1.setPadding(new Insets(20, 20, 80, 20));
        HBox transactionBox = new HBox();
        transactionBox.getChildren().add(transactionLabel);
        transactionBox.getChildren().add(eurosFieldTransaction);
        transactionBox.getChildren().add(centsSeparator);
        transactionBox.getChildren().add(centsFieldTransaction);
        transactionBox.getChildren().add(euroLabel);
        eurosFieldTransaction.setMaxWidth(40.0);
        centsFieldTransaction.setMaxWidth(40.0);
        transactionBox.setAlignment(Pos.CENTER_LEFT);
        pane1.add(transactionBox, 0, 2);
        HBox dateRowLayout = new HBox();
        dateRowLayout.setAlignment(Pos.CENTER_LEFT);
        dateRowLayout.getChildren().add(dateLabel);
        dateRowLayout.getChildren().add(transactionDatePicker);
        transactionDatePicker.setMaxWidth(130);
        pane1.add(dateRowLayout, 0, 3);
        HBox categoryRowLayout = new HBox();
        categoryRowLayout.setAlignment(Pos.CENTER_LEFT);
        categoryRowLayout.getChildren().add(chooseCategoryLabel);
        categoryRowLayout.getChildren().add(categoryChoice);
        categoryChoice.setPromptText("choose category");
        
        pane1.add(categoryRowLayout, 0, 4);
        pane1.add(newCategoryField, 0, 5);
        newCategoryField.setPromptText("category name");
        pane1.add(transactionButton, 0, 6);
        pane1.add(inputErrorMessage, 0, 7);

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
        BarChart b = createBarChart(afterDatePicker.getValue(), beforeDatePicker.getValue());
        startLayout.setCenter(b);
        
        eurosFieldTransaction.setPromptText("00");
        centsFieldTransaction.setPromptText("00");
        return new Scene(startLayout);
        
    }
    private Scene createSettingsScene(Label eurosLabel, Label centsSeparator, TextField eurosFieldBudget, Button backButton, TextField centsFieldBudget, Label budgetLabel, Button setBudgetButton, Label budgetErrorMessage){
        GridPane settingsLayout = new GridPane();
        settingsLayout.setPrefSize(1060, 700);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.setVgap(10);
        settingsLayout.setHgap(10);
        settingsLayout.setPadding(new Insets(20, 20, 20, 20));
        HBox budgetRow = new HBox();
        budgetRow.setAlignment(Pos.BASELINE_CENTER);
        eurosFieldBudget.setPromptText("00");
        backButton.requestFocus();
        centsFieldBudget.setPromptText("00");
        budgetRow.getChildren().add(budgetLabel);
        budgetRow.getChildren().add(eurosFieldBudget);
        budgetRow.getChildren().add(centsSeparator);
        budgetRow.getChildren().add(centsFieldBudget);
        budgetRow.getChildren().add(eurosLabel);
        budgetRow.getChildren().add(setBudgetButton);
        budgetRow.getChildren().add(backButton);
        centsFieldBudget.setMaxWidth(40);
        eurosFieldBudget.setMaxWidth(40);
        settingsLayout.add(budgetRow, 0, 0);
        settingsLayout.add(budgetErrorMessage, 0, 1);
        return new Scene (settingsLayout);
        
    }
    public BarChart <String, Number> createBarChart(LocalDate afterDatePicker, LocalDate beforeDatePicker) throws SQLException{
        CategoryAxis xAkseli = new CategoryAxis();
        NumberAxis yAkseli = new NumberAxis();
        xAkseli.setLabel("date");
        yAkseli.setLabel("€");
        BarChart<String, Number> pylvaskaavio = new BarChart<>(xAkseli, yAkseli);
        pylvaskaavio.setLegendVisible(true);
        pylvaskaavio.setTitle(logic.setBarChartTitle(afterDatePicker, beforeDatePicker));
        ArrayList<XYChart.Series> seriesList = logic.createSerie(afterDatePicker, beforeDatePicker);
        seriesList.forEach((s) -> {
            pylvaskaavio.getData().add(s);
        });
        xAkseli.setCategories(logic.arrangedXAxisCategories(afterDatePicker, beforeDatePicker));
        return pylvaskaavio;
    }
    
    private boolean notNull(Object o){
        return o != null;
    }
}

