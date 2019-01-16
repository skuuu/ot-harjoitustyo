
package saastopossuapp.gui;
import com.sun.javafx.charts.Legend;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.logic.Logic;

/**
 * Class creates the graphic user interface. 
 * This is also application's Main Class.
 */
public class UserInterface extends Application {
    private String username;
    private Logic logic;
    private DatePicker afterDatePicker;
    private DatePicker beforeDatePicker;
    private LocalDate fromDate; 
    private LocalDate untilDate;
    private BorderPane startLayout;
    private ComboBox categoryChoice; 
    private DatePicker transactionDatePicker;
    private PasswordField passwordField;
    private Scene passwordScene;
    private Scene newUserScene;
    private Scene startScene;
    private Scene settingsScene;  
    private final HashMap<String, Label> labels = new HashMap<>();
    private final HashMap<String, Button> buttons = new HashMap<>();
    private final HashMap<String, TextField> textFields = new HashMap<>();
    private Database db;
    private BarChart<String, Number> bc;
    private CategoryAxis xAxis;
    private ArrayList<String> styles;
    private VBox activityNodes;
    private ScrollPane scrollPaneLayout;
    private Stage expenseStage;
    private HashMap <String, String> barColours;
    
    /**
     * Method initializes class 
     * @throws java.lang.ClassNotFoundException if class not found
     * @throws java.sql.SQLException if connection fails
     * @throws java.io.IOException if problems with confiuration
     */
    @Override
    public void init() throws ClassNotFoundException, SQLException, IOException {
        db = new Database();
        UserAccountDao userAccountDao = new UserAccountDao(db);
        ActivityDao activityDao = new ActivityDao (db, username);
        this.afterDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
        this.beforeDatePicker = new DatePicker(LocalDate.now());
        this.fromDate = LocalDate.now().minusMonths(1);
        this.untilDate = LocalDate.now(); 
        this.startLayout = new BorderPane();
        this.categoryChoice = new ComboBox();
        this.logic = new Logic(userAccountDao, activityDao, fromDate, untilDate);    
        this.bc = null;
        this.barColours = new HashMap<>();
        this.styles = new ArrayList<>();
            styles.add("-fx-bar-fill: #81D3DF");
            styles.add("-fx-bar-fill: #8F7FDF");
            styles.add("-fx-bar-fill: #CB1F52");
            styles.add("-fx-bar-fill: #E8524F");
            styles.add("-fx-bar-fill: #6C233B"); 
            styles.add("-fx-bar-fill: #065C65");
            styles.add("-fx-bar-fill: #F58995");
    }
    
    /**
     * Main method where application starts execution
     * @param args - Main method signature
     */
    public static void main(String[] args) {
        launch(UserInterface.class);
    }
    
    /**
     * Method start creates all the components for user interface
     * @param primaryStage - the only Stage the application uses with varying Scenes.
     */
    @Override
    public void start(Stage primaryStage) {
        //passwordScene:
        labels.put("loginInstructionLabel", new Label("Type username and press login"));
        buttons.put("loginButton", new Button("Login"));
        labels.put("passwordErrorLabel", new Label(""));
        buttons.put("createUserButton", new Button("Create new user account"));
        passwordField = new PasswordField();
        passwordScene = createPasswordScene();
        
        //newUserScene:
        labels.put("howToCreateText", new Label("Type username to create a new account:"));
        textFields.put("newUsernameField", new TextField());
        buttons.put("signInButton", new Button("Confirm"));
        buttons.put("backToLoginButton", new Button ("Back"));
        labels.put("errorMessage", new Label (" ")); //error message needs to be 
        newUserScene = createNewUserScene();
        
        //startScene:
        textFields.put("centsFieldTransaction", new TextField());
        textFields.put("eurosFieldTransaction", new TextField());
        textFields.put("newCategoryField", new TextField());
        textFields.put("descriptionField", new TextField());
        labels.put("descriptionLabel", new Label("Expense Description: "));
        labels.put("addExpenseLabel", new Label("Add Expense"));
        labels.put("inputErrorMessage", new Label (" "));
        labels.put("expenseLabel", new Label (" "));
        labels.put("totalExpenses", new Label (" "));
        labels.put("transactionLabel", new Label("Expense Amount:     "));
        labels.put("centsSeparator", new Label (" , "));
        labels.put("euroLabel", new Label (" €  "));
        labels.put("dateLabel", new Label ("Expense Date:           "));
        transactionDatePicker = new DatePicker(LocalDate.now());
        labels.put("showLabel", new Label("Show Expenses"));
        labels.put("fromLabel", new Label("From:  "));
        labels.put("minusLabel", new Label("To:      "));
        labels.put("dateErrorMessageLabel", new Label(""));
        labels.get("dateErrorMessageLabel").setTextFill(Color.RED);
        buttons.put("transactionButton", new Button ("Add Expense"));
        buttons.put("settingsButton", new Button ("Settings"));
        buttons.put("logoutButton", new Button ("Logout"));
        labels.put("chooseCategoryLabel", new Label("Expense Category:    "));
        startScene = createStartScene();  

        //settingsScene: 
        labels.put("budgetLabel", new Label("Set your daily budget:  "));
        textFields.put("eurosFieldBudget", new TextField());
        textFields.put("centsFieldBudget", new TextField());
        labels.put("settingsCentsSeparator", new Label (" , "));
        labels.put("settingsEuroLabel", new Label (" €  "));
        buttons.put("backButton", new Button("back"));
        labels.put("budgetErrorMessage",  new Label (" "));
        buttons.put("setBudgetButton", new Button("set"));       
        settingsScene = createSettingsScene();
    
        createActionsForButtons(primaryStage);
        
        buttons.get("backButton").requestFocus();
        labels.get("dateLabel").requestFocus();

        primaryStage.setTitle("Säästöpossu");
        primaryStage.setScene(passwordScene);
        primaryStage.show();             
    }
    
    /**
     * Method updates startScene and is called every time the user access the database
     * 
     */
    private void refreshScreen() {
        this.untilDate = beforeDatePicker.getValue();
        this.fromDate = afterDatePicker.getValue();
        createBarChart();
        setBarColours();
        setLegendColours();
        createTooltipsForBarChart(bc);
        createActionListenerForExpenses(bc);
        startLayout.setCenter(bc);
        categoryChoice.setItems(logic.createChoices());        
        labels.get("totalExpenses").setText(logic.getBudgetAnalysis());
        textFields.get("centsFieldTransaction").clear();
        textFields.get("eurosFieldTransaction").clear();
        textFields.get("newCategoryField").clear();
        textFields.get("descriptionField").clear();
        categoryChoice.getSelectionModel().clearSelection();
        textFields.get("newCategoryField").setVisible(false);
        labels.get("dateErrorMessageLabel").setText("");
        labels.get("inputErrorMessage").setText("");
        labels.get("inputErrorMessage").setMinHeight(50);
        labels.get("passwordErrorLabel").setText("");
    }
    
    /**
     * Method creates Tooltips for BarChart by adding mouse listener to bar chart bars. 
     * When mouse hovers over a bar, the YAxis-value (amount in euros) is visible on tooltip. 
     * @param barChart - BarChart where Tooltips are being installed.
     */
    private void createTooltipsForBarChart(BarChart <String, Number> barChart){
        Tooltip t = new Tooltip("");
        Tooltip.install(barChart, t);
        for (XYChart.Series<String, Number> serie: barChart.getData()) {
            for (XYChart.Data<String, Number> item: serie.getData()){
                item.getNode().setOnMouseEntered((MouseEvent entered) -> {
                    t.setText(item.getYValue() + " €");
                    labels.get("expenseLabel").setText(logic.getExpenseLabelText(item.getXValue()));
                });
                item.getNode().setOnMouseExited((MouseEvent exited) -> {
                    labels.get("expenseLabel").setText("");
                    t.hide();
                });
            }
        }    
    }
    
    /**
     * Method creates action listener for BarChart by adding mouse listener to bar chart bars. 
     * When user clicks on BarChart's single bar, new window with the bar's data opens.
     * @param barChart - BarChart where action listener is being installed.
     */
    private void createActionListenerForExpenses(BarChart <String, Number> barChart) {
        for (XYChart.Series<String, Number> serie: barChart.getData()) {
            for (XYChart.Data<String, Number> item: serie.getData()){
                item.getNode().setOnMouseClicked((MouseEvent clicked) -> {
                    expenseStage = new Stage();
                    expenseStage.setScene(this.createExpenseScene(serie, item));
                    expenseStage.setTitle(serie.getName() + " " + item.getXValue());
                    expenseStage.show();
                });
            }
        }    
    }
    
    /**
     * Method creates Scene with ScrollPane Layout for showing single bar's data
     * @param serie - data serie where bar belongs to.
     * @param item - the bar user has clicked
     * @return Scene expenseScene for Stage expenseStage
     */
    public Scene createExpenseScene(XYChart.Series<String, Number> serie, XYChart.Data<String, Number> item) {
        activityNodes = new VBox();
        scrollPaneLayout = new ScrollPane();
        scrollPaneLayout.setPadding(new Insets(20, 20, 20, 20));
        scrollPaneLayout.setPrefSize(300, 300);
        activityNodes.getChildren().clear();   
        Label expenseTitleLabel = new Label("Expenses on " + item.getXValue() + " in category " + serie.getName() +":\n\n");
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);        
        expenseTitleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
        activityNodes.getChildren().add(expenseTitleLabel);
        
        for (Activity a: logic.getDailyExpensesLabel(item.getXValue(), serie.getName(), username)) {
            activityNodes.getChildren().add(createActivityNode(a));
        }
        scrollPaneLayout.setContent(activityNodes);
        Scene expenseScene = new Scene(scrollPaneLayout);
        return expenseScene;
    }
    
    /**
     * Method creates Nodes from Activities related to the bar user has clicked.
     * Each node has Label and Button for defining and deleting Activity.
     * @param activity - activity from the category defined by the bar.
     * @return Node including Label and Button.
     */
    public Node createActivityNode(Activity activity) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        Label label  = new Label(activity.toString());
        Label label2 = new Label(activity.getDescription());
        label.setMinWidth(50);
        Button deleteActvityButton = new Button("Delete");
        deleteActvityButton.setAlignment(Pos.CENTER);
        deleteActvityButton.setOnAction(e-> {
            this.barColours.remove(activity.getCategory());
            logic.deleteActivity(activity);
            refreshScreen();
            this.setBarColours();
            this.setLegendColours();
            expenseStage.hide();
            expenseStage.close();
        });
        box.setPadding(new Insets(5,5,5,5));
        box.getChildren().addAll(deleteActvityButton, label, label2);
        return box;
    }
    
    /**
     * Method creates Layouts and adds components to them to create PasswordScene.
     * PasswordScene is the first Scene user sees when opening the application
     * It allows user to either login or change to newUserScene to create a new UserAccount.
     * @return Scene with passwordLayout
     */
    private Scene createPasswordScene(){              
        GridPane passwordLayout = new GridPane();
        passwordLayout.add(labels.get("loginInstructionLabel"), 0, 0);
        passwordLayout.add(passwordField, 0, 1);
        passwordLayout.add(buttons.get("loginButton"), 0, 2);
        passwordLayout.add(labels.get("passwordErrorLabel"), 0, 3);
        labels.get("passwordErrorLabel").setTextFill(Color.RED);
        passwordLayout.add(buttons.get("createUserButton"), 0, 4);
        passwordLayout.setPrefSize(1060, 700);
        passwordLayout.setAlignment(Pos.CENTER);
        passwordLayout.setVgap(10);
        passwordLayout.setHgap(10);
        passwordLayout.setPadding(new Insets(20, 20, 20, 20));
        return new Scene(passwordLayout);
    }
    
    /**
     * Method creates layout and sets components in it to create newUserScene.
     * newUserScene lets user create a new UserAcount or change back to passwordScene.
     * @return Scene with newUserLayout
     */
    private Scene createNewUserScene(){
        GridPane newUserLayout = new GridPane();
        newUserLayout.add(labels.get("howToCreateText"), 0, 0);
        newUserLayout.add(textFields.get("newUsernameField"), 0, 1);
        newUserLayout.add(buttons.get("signInButton"), 1, 1);
        newUserLayout.add(buttons.get("backToLoginButton"), 2, 1);
        newUserLayout.add(labels.get("errorMessage"), 0, 3);
        labels.get("errorMessage").setTextFill(Color.RED);
        newUserLayout.setPrefSize(1060, 700);
        newUserLayout.setAlignment(Pos.CENTER);
        newUserLayout.setVgap(10);
        newUserLayout.setHgap(10);
        newUserLayout.setPadding(new Insets(20, 20, 20, 20));
        return new Scene(newUserLayout);
        
    }
    /**
     * Method creates Layouts and sets components in it to create startScene. 
     * In startScene user is able to view and manage expenses. 
     * @return Scene with startLayout
     */
    private Scene createStartScene() {
        startLayout.setPrefSize(1060, 700);
        startLayout.setPadding(new Insets(20, 20, 20, 20));
        transactionDatePicker.setEditable(false);
        afterDatePicker.setEditable(false);
        beforeDatePicker.setEditable(false);
        textFields.get("newCategoryField").setVisible(false);
        
        VBox analysisLayout = new VBox();
        analysisLayout.setPadding(new Insets(20, 20, 20, 40));
        analysisLayout.getChildren().add(labels.get("totalExpenses"));
        analysisLayout.getChildren().add(labels.get("expenseLabel"));
        startLayout.setTop(analysisLayout);
        
        GridPane pane1 = new GridPane();
        pane1.setAlignment(Pos.CENTER);
        pane1.setStyle("-fx-border-color: #EAB9BA");
        pane1.setVgap(10);
        pane1.setHgap(10);
        pane1.setPadding(new Insets(20, 20, 20, 20));
        HBox transactionBox = new HBox();
        transactionBox.getChildren().add(labels.get("transactionLabel"));
        transactionBox.getChildren().add(textFields.get("eurosFieldTransaction"));
        transactionBox.getChildren().add(labels.get("centsSeparator"));
        transactionBox.getChildren().add(textFields.get("centsFieldTransaction"));
        transactionBox.getChildren().add(labels.get("euroLabel"));
        textFields.get("eurosFieldTransaction").setMaxWidth(40.0);
        textFields.get("centsFieldTransaction").setMaxWidth(40.0);
        transactionBox.setAlignment(Pos.CENTER_LEFT);
        pane1.add(labels.get("addExpenseLabel"), 0, 1);
        labels.get("addExpenseLabel").setFont(Font.font(null, FontWeight.BOLD, 12));
        pane1.add(transactionBox, 0, 2);
        HBox dateRowLayout = new HBox();
        dateRowLayout.setAlignment(Pos.CENTER_LEFT);
        dateRowLayout.getChildren().add(labels.get("dateLabel"));
        dateRowLayout.getChildren().add(transactionDatePicker);
        transactionDatePicker.setMaxWidth(130);
        pane1.add(dateRowLayout, 0, 3);
        HBox categoryRowLayout = new HBox();
        categoryRowLayout.setAlignment(Pos.CENTER_LEFT);
        categoryRowLayout.getChildren().add(labels.get("chooseCategoryLabel"));
        categoryRowLayout.getChildren().add(categoryChoice);
        categoryChoice.setPromptText("choose category");
        pane1.add(categoryRowLayout, 0, 4);
        pane1.add(textFields.get("newCategoryField"), 0, 5);
        
        HBox descriptionRowLayout = new HBox();
        descriptionRowLayout.setAlignment(Pos.CENTER_LEFT);
        descriptionRowLayout.getChildren().addAll(labels.get("descriptionLabel"), textFields.get("descriptionField"));
        textFields.get("descriptionField").setPromptText("write a description");
        pane1.add(descriptionRowLayout, 0, 6);
        textFields.get("newCategoryField").setPromptText("category name");
        textFields.get("newCategoryField").setText("");
        pane1.add(buttons.get("transactionButton"), 0, 7);
        pane1.add(labels.get("inputErrorMessage"), 0, 8);
        labels.get("inputErrorMessage").setTextFill(Color.RED);
        
        GridPane pane2 = new GridPane();
        pane2.setStyle("-fx-border-color: #EAB9BA");
        pane2.setAlignment(Pos.CENTER);
        pane2.setVgap(10);
        pane2.setHgap(10);
        pane2.setPadding(new Insets(20, 20, 20, 20));
        pane2.add(labels.get("showLabel"), 0, 0);
        pane2.add(labels.get("dateErrorMessageLabel"), 0, 3);
        labels.get("showLabel").setFont(Font.font(null, FontWeight.BOLD, 12));
        HBox showRowLayout = new HBox();
        showRowLayout.setAlignment(Pos.CENTER);
        HBox showRowLayout2 = new HBox();
        showRowLayout2.setAlignment(Pos.CENTER);
        showRowLayout.getChildren().add(labels.get("fromLabel"));
        showRowLayout.getChildren().add(afterDatePicker);
        showRowLayout2.getChildren().add(labels.get("minusLabel"));
        showRowLayout2.getChildren().add(beforeDatePicker);
        pane2.add(showRowLayout, 0, 1);
        pane2.add(showRowLayout2, 0, 2);
        
        GridPane pane3 = new GridPane();
        pane1.setAlignment(Pos.CENTER);
        pane3.setVgap(10);
        pane3.setHgap(10);
        pane3.setPadding(new Insets(20, 20, 20, 20));
        pane3.add(buttons.get("settingsButton"), 0, 0);
        pane3.add(buttons.get("logoutButton"), 2, 0);
        VBox rightLayout = new VBox();
        rightLayout.setSpacing(10);
        rightLayout.getChildren().add(pane1);
        rightLayout.getChildren().add(pane2);
        rightLayout.getChildren().add(pane3);
        startLayout.setRight(rightLayout);
        textFields.get("eurosFieldTransaction").setPromptText("00");
        textFields.get("centsFieldTransaction").setPromptText("00");
        return new Scene(startLayout);
    }
    
    /**
     * Method creates Layouts and sets components in it to create settingsScene.
     * In SettingsScene user is able to define budget or change Scene back to startScene.
     * @return Scene with settingsLayout
     */
    private Scene createSettingsScene(){
        GridPane settingsLayout = new GridPane();
        settingsLayout.setPrefSize(1060, 700);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.setVgap(10);
        settingsLayout.setHgap(10);
        settingsLayout.setPadding(new Insets(20, 20, 20, 20));
        HBox budgetRow = new HBox();
        budgetRow.setAlignment(Pos.BASELINE_CENTER);
        textFields.get("eurosFieldBudget").setPromptText("00");
        textFields.get("centsFieldBudget").setPromptText("00");
        budgetRow.getChildren().add(labels.get("budgetLabel"));
        budgetRow.getChildren().add(textFields.get("eurosFieldBudget"));
        budgetRow.getChildren().add(labels.get("settingsCentsSeparator"));
        budgetRow.getChildren().add(textFields.get("centsFieldBudget"));
        budgetRow.getChildren().add(labels.get("settingsEuroLabel"));
        budgetRow.getChildren().add(buttons.get("setBudgetButton"));
        budgetRow.getChildren().add(buttons.get("backButton"));
        textFields.get("centsFieldBudget").setMaxWidth(40);
        textFields.get("eurosFieldBudget").setMaxWidth(40);
        settingsLayout.add(budgetRow, 0, 0);
        settingsLayout.add(labels.get("budgetErrorMessage"), 0, 1);
        labels.get("budgetErrorMessage").setTextFill(Color.RED);
        return new Scene (settingsLayout);
    }
    
    /**
     * Method creates actions for buttons.
     * @param primaryStage - the only stage the application uses with varying Scenes
     */
    private void createActionsForButtons(Stage primaryStage){
        buttons.get("createUserButton").setOnMouseClicked(event -> {
            labels.get("errorMessage").setText("");
            textFields.get("newUsernameField").clear();
            primaryStage.setScene(newUserScene);
        });
       
        buttons.get("signInButton").setOnMouseClicked(event -> {
            labels.get("errorMessage").setText("");
            if (logic.createUser(textFields.get("newUsernameField").getText().trim())) {
               primaryStage.setScene(passwordScene);  
            }
            labels.get("errorMessage").setText("Invalid username. Username must be unique and 2-30 characters (A-Z, a-z, 0-9)");
        });
        
        buttons.get("backToLoginButton").setOnMouseClicked(event -> {
            passwordField.clear();
            labels.get("passwordErrorLabel").setText("");
            primaryStage.setScene(passwordScene);
        });
            
        buttons.get("setBudgetButton").setOnAction((event) -> {
            if (logic.changeBudget(textFields.get("eurosFieldBudget").getText().trim(), textFields.get("centsFieldBudget").getText().trim())) {
                refreshScreen();
                primaryStage.setScene(startScene);
                labels.get("budgetErrorMessage").setText("  ");
            } else {
                labels.get("budgetErrorMessage").setText("Invalid input");
            }
        });

        afterDatePicker.setOnAction((afterdayChosen) -> {
            if (logic.validateDate(afterDatePicker.getValue(), beforeDatePicker.getValue()) == false) {
                refreshScreen();
                labels.get("dateErrorMessageLabel").setText("Check the dates!");
            } else {
                refreshScreen();
            }
        });

        beforeDatePicker.setOnAction((beforedayChosen) -> {
            if (logic.validateDate(afterDatePicker.getValue(), beforeDatePicker.getValue()) == false) {
                refreshScreen();
                labels.get("dateErrorMessageLabel").setText("Check the dates!");
            } else {
                refreshScreen();
            }
        });

        buttons.get("transactionButton").setOnAction((event) -> {
            if (textFields.get("descriptionField").getText().trim().isEmpty()) {
                textFields.get("descriptionField").setText("no description");
            }
            if (notNull(categoryChoice.getValue()) && logic.addExpense(textFields.get("newCategoryField").getText().trim(), categoryChoice.getValue().toString(), textFields.get("eurosFieldTransaction").getText().trim(), textFields.get("centsFieldTransaction").getText().trim(), transactionDatePicker.getValue(), textFields.get("descriptionField").getText().trim())){
                this.createBarChart();
                refreshScreen();
            } else {
                labels.get("inputErrorMessage").setText(logic.getInputErrorMessage(textFields.get("newCategoryField").getText().trim(), categoryChoice.getValue(), textFields.get("eurosFieldTransaction").getText().trim(), textFields.get("centsFieldTransaction").getText().trim(), textFields.get("descriptionField").getText().trim()));
            }
        });

        buttons.get("settingsButton").setOnAction((event) -> {
            primaryStage.setScene(settingsScene);
        });

        buttons.get("logoutButton").setOnAction((event) -> {
            passwordField.clear();
            this.barColours.clear();
            primaryStage.setScene(passwordScene);
        });

        buttons.get("backButton").setOnAction((event) -> {
            refreshScreen();
            primaryStage.setScene(startScene);
        });
        
        buttons.get("loginButton").setOnAction((event) -> {
            this.username = passwordField.getText();
            if (logic.checkUsername(passwordField.getText().trim())==false) {
                labels.get("passwordErrorLabel").setText("Invalid username");
            } else {
                refreshScreen();
                primaryStage.setScene(startScene);
            }
        });         
        
        categoryChoice.setOnAction((categoryChosen) -> {
            if (notNull(categoryChoice.getValue())){
                if (categoryChoice.getValue().equals("create new")){
                    textFields.get("newCategoryField").setVisible(true);
                    primaryStage.setScene(startScene);
                } else {
                    textFields.get("newCategoryField").setVisible(false);
                }
            }
        });
    }
    
    /**
     * Method creates BarChart, where xAxis has dates of type String and Yaxis amount in euros.
     * @return BarChart 
     */
    private BarChart <String, Number> createBarChart() {
        xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("date");
        yAxis.setLabel("€");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(true);
        barChart.setTitle(logic.getBarChartTitle(afterDatePicker.getValue(), beforeDatePicker.getValue()));
        ArrayList<XYChart.Series> seriesList = logic.createSerie(afterDatePicker.getValue(), beforeDatePicker.getValue());
        seriesList.forEach((s) -> {
            barChart.getData().add(s);
        });
        xAxis.setCategories(logic.arrangedXAxisCategories());
        this.bc = barChart;
        return barChart;    
    }
    
    /**
     * Method sets BarChart's bar colours to match colours defined in the styles-list
     * One colour represents one category during one program-run.
     */
    private void setBarColours() {
        for (XYChart.Series<String, Number> serie: bc.getData()) {
            for (XYChart.Data<String, Number> item: serie.getData()) {
                if (barColours.containsKey(serie.getName())) {
                    item.getNode().setStyle(barColours.get(serie.getName()));
                } else {
                    for (String colour: styles) {
                        if (!barColours.values().contains(colour)) {
                            barColours.put(serie.getName(), colour);
                            item.getNode().setStyle(colour);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Method sets BarChart's legend colours to match colours defined in the styles-list
     * One colour represents one category during one program-run.
     */
    private void setLegendColours() {
        int i = 0;
        for (Node node : bc.getChildrenUnmodifiable()) {
            if (node instanceof Legend) {
                for (Legend.LegendItem legendItem : ((Legend)node).getItems()) {
                    XYChart.Series<String, Number> serie = bc.getData().get(i);
                    legendItem.getSymbol().setStyle(barColours.get(serie.getName()));
                    i++;
                }
            }
        }
    }
        
    /**
     * Method checks if value of the Object is null
     * @return true if not, else false 
     */
    private boolean notNull(Object object){
        return object != null;
    }
}

