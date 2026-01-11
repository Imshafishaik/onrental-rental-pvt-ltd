package com.onriderentals.controller;

import com.onriderentals.model.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class NotificationsController {

    @FXML
    private ListView<Notification> notificationsList;

    @FXML
    public void initialize() {
        // This is a placeholder for fetching real data.
        notificationsList.setItems(getDummyNotifications());

        notificationsList.setCellFactory(param -> new ListCell<Notification>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMessage());
                }
            }
        });
    }

    private ObservableList<Notification> getDummyNotifications() {
        ObservableList<Notification> notifications = FXCollections.observableArrayList();

        Notification notification1 = new Notification();
        notification1.setNotificationId(1);
        notification1.setUserId(1);
        notification1.setMessage("Your booking for the Honda CBR1000RR has been confirmed.");

        Notification notification2 = new Notification();
        notification2.setNotificationId(2);
        notification2.setUserId(1);
        notification2.setMessage("Your booking for the Yamaha YZF-R1 has been completed.");

        notifications.add(notification1);
        notifications.add(notification2);

        return notifications;
    }
}
