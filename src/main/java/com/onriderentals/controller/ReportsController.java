package com.onriderentals.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class ReportsController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    public void generateReport() {
        // This is a placeholder for the report generation logic.
        System.out.println("Generating report from " + startDatePicker.getValue() + " to " + endDatePicker.getValue());
    }
}
