package com.example.taximetrie;

import com.example.taximetrie.Domain.Oferta;
import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Utils.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.example.taximetrie.Service.Service;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientController implements Observer {

    private Service serv;
    private Persoana client;
    ObservableList<Oferta> model = FXCollections.observableArrayList();
    @FXML
    private TextField locatieTextField;
    @FXML
    private TableView<Oferta> oferteTableView;
    @FXML
    private TableColumn<Oferta, String> masinaColumn;
    @FXML
    private TableColumn<Oferta, Integer> timpColumn;


    public void setServ(Service serv, Persoana client)
    {
        this.serv = serv;
        this.client = client;
        serv.addObserver(this);
        initModel();
    }

    @FXML
    public void handleCauta(ActionEvent ev){

        String locatie = locatieTextField.getText();
        locatieTextField.setText("");
        serv.adaugaCerere(client, locatie);

    }

    private void initModel() {

        var op = serv.getOferta(client);
        if(op.isPresent())
            model.setAll(op.get());
        else model.clear();
    }

    @Override
    public void update() {
        initModel();
    }

    @FXML
    public void initialize(){
        masinaColumn.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getSofer().getIndicativMasina()));
        timpColumn.setCellValueFactory(new PropertyValueFactory<>("asteptare"));

        oferteTableView.setItems(model);

    }

    @FXML
    public void handleAccept(ActionEvent ev){

        var oferta = oferteTableView.getSelectionModel().getSelectedItem();

        if(oferta != null){
            serv.adaugaComanda(client, oferta.getSofer());
            serv.stergeOferta(client);
        }

    }

    @FXML
    public void handleCancel(ActionEvent ev){
        var oferta = oferteTableView.getSelectionModel().getSelectedItem();

        if(oferta != null){
            serv.stergeOferta(client);
        }

    }
}
