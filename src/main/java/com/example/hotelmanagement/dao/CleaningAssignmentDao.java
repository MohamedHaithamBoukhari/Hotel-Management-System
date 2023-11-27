package com.example.hotelmanagement.dao;

import com.example.hotelmanagement.beans.CleaningAssignment;
import com.example.hotelmanagement.beans.Customer;
import com.example.hotelmanagement.beans.RoomType;
import com.example.hotelmanagement.daoFactory.CummonDbFcts;

import java.util.List;
import java.util.Map;

public class CleaningAssignmentDao extends CummonDbFcts {
    public static final String TABLE_NAME = "cleaningAssignment";
    public static final String[] TABLE_COLUMNS = {"cleaningId", "employeeId", "roomId","date"};
    public static List<Object> select(Map<String, Object> whereMap, String selectedCols) {
        List<Object> rows = superSelect(CleaningAssignment.class, TABLE_NAME, selectedCols, TABLE_COLUMNS, whereMap);
        return rows;
    }

    public static void insert(CleaningAssignment assignment){
        superInsert(assignment, TABLE_COLUMNS, TABLE_NAME);
    }

    public static int update(String updatedColumn, Object newColumnValue, String testColumn,Object testColumnValue){
        return superUpdate(TABLE_NAME, updatedColumn, newColumnValue, testColumn, testColumnValue);
    }

    public static int delete(String testColumn,Object testColumnValue){
        return superDelete(TABLE_NAME, testColumn, testColumnValue);
    }


    public static List<Object> selectAll() {
        List<Object> rows = superSelectAll(CleaningAssignment.class, TABLE_NAME, TABLE_COLUMNS);
        return rows;
    }
}
