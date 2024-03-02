package com.example.taximetrie;

import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    private Service serv;
    @FXML
    private TextField usernameTextField;

    void setServ(Service serv){
        this.serv = serv;
    }
    @FXML
    void handleLogin(ActionEvent ev){

        String username = usernameTextField.getText();
        usernameTextField.setText("");

        var pers = serv.cautaPersoanaDupaUsername(username);

        if(pers.isPresent()) {
            var sofer = serv.cautaSoferDupaId(pers.get().getId());

            if(sofer.isEmpty())
                showClientWindow(pers.get());
            else
                showSoferWindow(sofer.get());
        }

    }

    private void showClientWindow(Persoana pers) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("client-view.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Client " + pers.getNume());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ClientController clientController = loader.getController();
            clientController.setServ(serv, pers);

            dialogStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void showSoferWindow(Sofer sofer) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("sofer-view.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Taximetrist " + sofer.getNume());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SoferController soferController = loader.getController();
            soferController.setServ(serv, sofer);

            dialogStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}