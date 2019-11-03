package com.codetreatise.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.User;
import com.codetreatise.config.StageManager;
import com.codetreatise.service.UserService;
import com.codetreatise.view.FxmlView;
/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class UserController implements Initializable{

	@FXML
    private Button btnLogout;
	
	@FXML
    private Label userId;
	
	@FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField mobileNumber;
    
    @FXML
    private TextField address;

    @FXML
    private TextField city;

    @FXML
    private TextField state;
    
    @FXML
    private TextField country;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;
    
    @FXML
    private Button reset;
	
	@FXML
    private Button saveUser;
	
	@FXML
	private TableView<User> userTable;

	@FXML
	private TableColumn<User, Long> colUserId;

	@FXML
	private TableColumn<User, String> colFirstName;

	@FXML
	private TableColumn<User, String> colLastName;

	@FXML
	private TableColumn<User, LocalDate> colMobileNumber;

	@FXML
	private TableColumn<User, String> colAddress;
	
	@FXML
    private TableColumn<User, String> colCity;
	
	@FXML
    private TableColumn<User, String> colState;
	
	@FXML
    private TableColumn<User, String> colCountry;

	@FXML
	private TableColumn<User, String> colEmail;
	
	@FXML
    private TableColumn<User, Boolean> colEdit;
	
	@FXML
    private MenuItem deleteUsers;
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private UserService userService;
	
	private ObservableList<User> userList = FXCollections.observableArrayList();
	
	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
    }

	/**
	 * Logout and go to the login page
	 */
    @FXML
    private void logout(ActionEvent event) throws IOException {
    	stageManager.switchScene(FxmlView.LOGIN);    	
    }
    
    @FXML
    void reset(ActionEvent event) {
    	clearFields();
    }
    
    @FXML
    private void saveUser(ActionEvent event){
    	
    	if(validate("First Name", getFirstName(), "[a-zA-Z]+") &&
    	   validate("Last Name", getLastName(), "[a-zA-Z]+") &&
    	   emptyValidation("Mobile Number", getMobileNumber().isEmpty()) &&
    	   validate("Mobile Number", getMobileNumber(), "[0-9]{10}")  ){
    		
    		if(userId.getText() == null || userId.getText() == ""){
    			if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
    				emptyValidation("Password", getPassword().isEmpty())){
    				
    				User user = new User();
        			user.setFirstName(getFirstName());
        			user.setLastName(getLastName());
        			user.setMobileNumber(getMobileNumber());
        			user.setAddress(getAddress());
        			user.setCity(getCity());
        			user.setState(getState());
        			user.setCountry(getCountry());
        			user.setPassword(getPassword());
        			user.setEmail(getEmail());
        			User newUser = userService.save(user);
        			
        			saveAlert(newUser);
    			}
    			
    		}else{
    			User user = userService.find(Long.parseLong(userId.getText()));
    			user.setFirstName(getFirstName());
    			user.setLastName(getLastName());
    			user.setMobileNumber(getMobileNumber());
    			user.setAddress(getAddress());
    			user.setCity(getCity());
    			user.setState(getState());
    			user.setCountry(getCountry());
    			user.setEmail(getEmail());
    			User updatedUser =  userService.update(user);
    			updateAlert(updatedUser);
    		}
    		
    		clearFields();
    		loadUserDetails();
    	}
    	
    	
    }
    
    @FXML
    private void deleteUsers(ActionEvent event){
    	List<User> users = userTable.getSelectionModel().getSelectedItems();
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK) userService.deleteInBatch(users);
    	
    	loadUserDetails();
    }
    
   	private void clearFields() {
		userId.setText(null);
		firstName.clear();
		lastName.clear();
		mobileNumber.clear();
		address.clear();
		city.clear();
		state.clear();
		country.clear();
		email.clear();
		password.clear();
	}
	
	private void saveAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User saved successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been created and \n id is "+ user.getId() +".");
		alert.showAndWait();
	}
	
	private void updateAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been updated.");
		alert.showAndWait();
	}
	
	private String getGenderTitle(String gender){
		return (gender.equals("Male")) ? "his" : "her";
	}

	public String getFirstName() {
		return firstName.getText();
	}

	public String getLastName() {
		return lastName.getText();
	}

	public String getMobileNumber() {
		return mobileNumber.getText();
	}

	public String getAddress() {
		return address.getText();
	}

	public String getCity() {
		return city.getText();
	}
	
	public String getState() {
		return state.getText();
	}

	public String getCountry() {
		return country.getText();
	}

	public String getEmail() {
		return email.getText();
	}

	public String getPassword() {
		return password.getText();
	}
  

	@Override
	public void initialize(URL location, ResourceBundle resources) {
				
		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setColumnProperties();
		
		// Add all users into table
		loadUserDetails();
	}
	
	
	
	/*
	 *  Set All userTable column properties
	 */
	private void setColumnProperties(){
		/* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/
		
		colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colMobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
		colState.setCellValueFactory(new PropertyValueFactory<>("state"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colEdit.setCellFactory(cellFactory);
	}
	
	Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory = 
			new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>()
	{
		@Override
		public TableCell<User, Boolean> call( final TableColumn<User, Boolean> param)
		{
			final TableCell<User, Boolean> cell = new TableCell<User, Boolean>()
			{
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();
				
				@Override
				public void updateItem(Boolean check, boolean empty)
				{
					super.updateItem(check, empty);
					if(empty)
					{
						setGraphic(null);
						setText(null);
					}
					else{
						btnEdit.setOnAction(e ->{
							User user = getTableView().getItems().get(getIndex());
							updateUser(user);
						});
						
						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
				        iv.setImage(imgEdit);
				        iv.setPreserveRatio(true);
				        iv.setSmooth(true);
				        iv.setCache(true);
						btnEdit.setGraphic(iv);
						
						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateUser(User user) {
					userId.setText(Long.toString(user.getId()));
					firstName.setText(user.getFirstName());
					lastName.setText(user.getLastName());
					mobileNumber.setText(user.getMobileNumber());
	    			address.setText(user.getAddress());
	    			city.setText(user.getCity());
	    			state.setText(user.getState());
	    			country.setText(user.getCountry());
	    			email.setText(user.getEmail());
				}
			};
			return cell;
		}
	};

	
	
	/*
	 *  Add All users to observable list and update table
	 */
	private void loadUserDetails(){
		userList.clear();
		userList.addAll(userService.findAll());

		userTable.setItems(userList);
	}
	
	/*
	 * Validations
	 */
	private boolean validate(String field, String value, String pattern){
		if(!value.isEmpty()){
			Pattern p = Pattern.compile(pattern);
	        Matcher m = p.matcher(value);
	        if(m.find() && m.group().equals(value)){
	            return true;
	        }else{
	        	validationAlert(field, false);            
	            return false;            
	        }
		}else{
			validationAlert(field, true);            
            return false;
		}        
    }
	
	private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
        	validationAlert(field, true);            
            return false;            
        }
    }	
	
	private void validationAlert(String field, boolean empty){
		Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if(field.equals("Role")) alert.setContentText("Please Select "+ field);
        else{
        	if(empty) alert.setContentText("Please Enter "+ field);
        	else alert.setContentText("Please Enter Valid "+ field);
        }
        alert.showAndWait();
	}
}
