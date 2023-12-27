package com.example.hotelmanagement.controllers.customer;

import com.example.hotelmanagement.HelloApplication;
import com.example.hotelmanagement.beans.Customer;
import com.example.hotelmanagement.localStorage.CustomerManager;
import com.example.hotelmanagement.config.PathConfig;
import com.example.hotelmanagement.config.Validation;
import com.example.hotelmanagement.dao.CustomerDao;
import com.example.hotelmanagement.localStorage.SwitchedPageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerLoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Label errorInfoLabel, usernameError, passwordError;

    public boolean verifyFields(ActionEvent event, String username, String password){
        boolean verified = false;
        errorInfoLabel.setText("");

        if(!Validation.isValidEmail(username)){
//            errors.add("Field must be not empty");
            usernameError.setText("Please Enter Valid email");
            verified = false;
        }else {
            usernameError.setText("");
            verified = true;
        }

        if (!Validation.isValidPassword(password)) {
            passwordError.setText("Password must be at least 8 characters long");
            verified = false;
        }
        else {
            passwordError.setText("");
            verified = true;
        }

        return verified;
    }
    public void logInAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(verifyFields(event, username, password)){
            System.out.println("verivied");
            Map<String, Object> map = new HashMap<>();
            map.put("email", username);
            map.put("password", password);
            System.out.println(username+ " "+password);

            List<Object> custumers = CustomerDao.select(map, "*");
            System.out.println(custumers);
            if(custumers.size() == 1){
                Customer customer = (Customer) custumers.get(0);
                CustomerManager.getInstance().setCustomer(customer);
                if(customer.getAccount_status().equals("Active")){
                    switchToCustumerHomePage(event);
                } else if (customer.getAccount_status().equals("Blocked")) {
                    switchToCustumerBlockedAccountPage(event);
                }

            }else {
                errorInfoLabel.setText("Email or password is incorrect :(");
            }
        }

    }
    public void switchToCustumerHomePage(ActionEvent event) throws IOException {
        SwitchedPageManager.getInstance().setSwitchedPage("Home");
        FXMLLoader loader = new FXMLLoader(new URL(PathConfig.RESSOURCES_ABS_PATH + "views/customer/customerHomePage-view.fxml"));
        root = loader.load();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToCustumerBlockedAccountPage(ActionEvent event) throws IOException {
        SwitchedPageManager.getInstance().setSwitchedPage("Home");
        FXMLLoader loader = new FXMLLoader(new URL(PathConfig.RESSOURCES_ABS_PATH + "views/customer/BlockedAccountPage-view.fxml"));
        root = loader.load();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToPreviousScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(new URL(PathConfig.RESSOURCES_ABS_PATH + "views/welcome-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToSignUpScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(new URL(PathConfig.RESSOURCES_ABS_PATH + "views/customer/customerSignUp-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void closeStage(ActionEvent event){
        HelloApplication.stage.close();
    }

}
