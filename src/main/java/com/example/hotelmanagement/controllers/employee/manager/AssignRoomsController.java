package com.example.hotelmanagement.controllers.employee.manager;

import com.example.hotelmanagement.beans.Task;
import com.example.hotelmanagement.dao.*;
import com.example.hotelmanagement.daoFactory.CummonDbFcts;
import com.example.hotelmanagement.localStorage.VarsManager;
import com.example.hotelmanagement.tablesView.AffectedTasksTableView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AssignRoomsController implements Initializable {
    private String action;
    @FXML private AnchorPane assignPane, confirmAssigningPane;
    @FXML private Label pageTitle, noRowsMsg, rowSelectedError;
    @FXML private Label cleaningAssignError, maintenanceAssignError, confirmationMsg;
    @FXML private CheckBox Cleaner, MaintenanceStaff,assingedRoomnbrAsc,assingedRoomnbrDesc;
    @FXML private TextField fullnameField, emailField;
    @FXML private TableView<AffectedTasksTableView> empsTable;
    @FXML private TableColumn<AffectedTasksTableView, Object> idCol, fullNameCol, emailCol, positionCol, assignedRoomsNbrCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noRowsMsg.setVisible(false);
        rowSelectedError.setVisible(false);
        confirmAssigningPane.setVisible(false);
        cleaningAssignError.setVisible(false);
        maintenanceAssignError.setVisible(false);
        if(VarsManager.actionStarted.equals("assignMaintenanceTask")){
            action = "assignMaintenanceTask";
            pageTitle.setText("Assign Room to maintenance staff");
            loadDataOnTaskTable(new ArrayList<>(),"","","");
        } else if (VarsManager.actionStarted.equals("assignCleaningTask")) {
            action = "assignCleaningTask";
            pageTitle.setText("Assign Room to cleaner");
            loadDataOnTaskTable(new ArrayList<>(),"","","");
        }
    }


    public void loadDataOnTaskTable(List<String> jobsList, String  fullname, String email, String assignedRoomNbrOrder){
        noRowsMsg.setVisible(false);
        List<AffectedTasksTableView> empsList = new ArrayList<>();
        empsTable.getItems().clear();
        AffectedTasksTableView.setNBR(1);

        String query = "SELECT e.employeeId, e.fullName, e.email, e.position, COUNT(t.roomId) AS total_tasks " +
                       "FROM employee e " +
                       "LEFT JOIN task t ON e.employeeId = t.employeeId ";

        List<String> colToSelect =  new ArrayList<String>(List.of("e.employeeId", "e.fullName","e.email", "e.position", "total_tasks"));
        if(jobsList.isEmpty() && fullname.isEmpty() && email.isEmpty() && assignedRoomNbrOrder.isEmpty()){
            query += "WHERE e.position IN ('Maintenance Staff','Cleaner') ";
            query += "GROUP BY e.employeeId, e.fullName, e.email, e.position";
            List<Object[]> empsDetails = CummonDbFcts.querySelect(query, colToSelect);
            for (Object[] row : empsDetails) {
                AffectedTasksTableView empRow = new AffectedTasksTableView(row[0],row[1],row[2],row[3],row[4]);
                empsList.add(empRow);
            }
        }else{
            String col1 = "e.position", col2 = "e.fullName", col3 = "e.email";
            query += " WHERE ";
            if(!jobsList.isEmpty()){
                query += "(";
                for (String job: jobsList){
                        query =query  + col1 + " = '" + job + "' OR ";
                }
                query =query .substring(0,query .length() - 4); //delete last " OR "
                query += ") AND ";
            }else {
                query += "e.position IN ('Maintenance Staff','Cleaner') AND ";
            }
            if(!fullname.isEmpty()){
                    query += "("+ col2 + " LIKE '%" + fullname + "%') AND ";
                }
            if(!email.isEmpty()){
                    query += "("+ col3 + " LIKE '%" + email + "%') AND ";
                }
            query = query.substring(0, query.length() - 5);//delete last "AND "

            query += " GROUP BY e.employeeId, e.fullName, e.email, e.position AND ";
            query = query.substring(0, query.length() - 5);//delete last " AND "
            if(!assignedRoomNbrOrder.equals("")){
                query = query + " ORDER BY total_tasks " + assignedRoomNbrOrder;
            }
            System.out.println(query);
            List<Object[]> empsDetails = CummonDbFcts.querySelect(query, colToSelect);
            for (Object[] row : empsDetails) {
                AffectedTasksTableView empRow = new AffectedTasksTableView(row[0],row[1],row[2],row[3],row[4]);
                empsList.add(empRow);
            }
        }

        idCol.setCellValueFactory(new PropertyValueFactory<>("i"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        assignedRoomsNbrCol.setCellValueFactory(new PropertyValueFactory<>("assignedRoomsNbr"));
        // this line to verify because  i add it , because i don't trouve it
        empsTable.getItems().addAll(empsList);

        if(empsList.isEmpty()){
            noRowsMsg.setVisible(true);
        }

    }
    public void filterEmployees(ActionEvent event){
        List<String> jobList = new ArrayList<>();
        String assignedRoomNbrOrder = "";

        if(Cleaner.isSelected()) jobList.add("Cleaner");
        if(MaintenanceStaff.isSelected()) jobList.add("Maintenance Staff");

        if(assingedRoomnbrDesc.isSelected()) {
            assignedRoomNbrOrder = "DESC";
            assingedRoomnbrAsc.setSelected(false);
        }

        if(assingedRoomnbrAsc.isSelected()) {
            assignedRoomNbrOrder = "ASC";
            assingedRoomnbrDesc.setSelected(false);
        }
        String fullname = fullnameField.getText();
        String email = emailField.getText();

        loadDataOnTaskTable(jobList, fullname, email, assignedRoomNbrOrder);
    }
    public void confirmAssigning(ActionEvent event){
        cleaningAssignError.setVisible(false);
        maintenanceAssignError.setVisible(false);

        int employeeId = (int) empsTable.getSelectionModel().getSelectedItem().getEmployeeId();
        int roomId = VarsManager.selectedRoomId;
        String newStatus = action.equals("assignMaintenanceTask")? "Under Maintenance":"Under Cleaning";
        String taskType = action.equals("assignMaintenanceTask")? "Maintenance":"Cleaning";

        RoomDao.update("status",newStatus,"roomId",roomId);
        Task task = new Task(employeeId, roomId, "On Hold",taskType);
        TaskDao.insert(task);

        VarsManager.actionCompleted = action;
        closeStage(event);

    }
    public void displayConfirmAssignPane(ActionEvent event){
        cleaningAssignError.setVisible(false);
        maintenanceAssignError.setVisible(false);
        rowSelectedError.setVisible(false);

        if(empsTable.getSelectionModel().getSelectedItem() == null){
            rowSelectedError.setVisible(true);
            hideMsg(rowSelectedError,4);
            return;
        }

        String position = (String) empsTable.getSelectionModel().getSelectedItem().getPosition();
        if(action.equals("assignMaintenanceTask") && position.equals("Cleaner")){
            cleaningAssignError.setVisible(false);
            maintenanceAssignError.setVisible(true);
        } else if (action.equals("assignCleaningTask") && position.equals("Maintenance Staff")) {
            maintenanceAssignError.setVisible(false);
            cleaningAssignError.setVisible(true);
        } else { // we can affected task to the correct emp position
            assignPane.setVisible(false);
            confirmAssigningPane.setVisible(true);
            String employeeFullname = (String) empsTable.getSelectionModel().getSelectedItem().getFullName();

            confirmationMsg.setText("Are you sure you want to assign this task to " + employeeFullname +" ?");
        }
    }
    public void hideConfirmAssignPane(ActionEvent event){
        confirmAssigningPane.setVisible(false);
        assignPane.setVisible(true);
    }
    public void hideMsg(Label msg,double time){
        Duration duration = Duration.seconds(time);
        Timeline timeline = new Timeline(new KeyFrame(duration, e -> msg.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }
    public void closeStage(ActionEvent event){
        HomePageController.childStage.close();
        VarsManager.actionStarted = "";
    }
}
