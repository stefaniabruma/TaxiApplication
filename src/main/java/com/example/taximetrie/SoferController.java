package com.example.taximetrie;

import com.example.taximetrie.Domain.Cerere;
import com.example.taximetrie.Domain.Comanda;
import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Repository.PagingUtils.PagingInformation;
import com.example.taximetrie.Repository.PagingUtils.PagingInformationObject;
import com.example.taximetrie.Service.Service;
import com.example.taximetrie.Utils.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;


public class SoferController implements Observer {

    private Service serv;
    private Sofer sofer;
    private ObservableList<Persoana> model = FXCollections.observableArrayList();
    private ObservableList<Comanda> model2 = FXCollections.observableArrayList();
    private ObservableList<Cerere> model3 = FXCollections.observableArrayList();
    private PagingInformation pi = new PagingInformationObject(1, 10);
    @FXML
    private TableView<Persoana> persoaneTableView;
    @FXML
    private TableColumn<Persoana, Long> idColumn;
    @FXML
    private TableColumn<Persoana, String> usernameColumn;
    @FXML
    private TableColumn<Persoana, String> numeColumn;
    @FXML
    private TextField pageSizeTextField;
    @FXML
    private TableView<Cerere> cereriTableView;
    @FXML
    private TableColumn<Cerere, String> numeClientColumn;
    @FXML
    private TableColumn<Cerere, String> locatieColumn;
    @FXML
    private TextField onoreazaTextField;



    @FXML
    private TableView<Comanda> comenziTableView;
    @FXML
    private TableColumn<Comanda, Long> idComandaColumn;
    @FXML
    private TableColumn<Comanda, Long> clientColumn;
    @FXML
    private TableColumn<Comanda, LocalDateTime> dataColumn;
    @FXML
    private DatePicker comandaDatePicker;
    @FXML
    private Label mediaLabel;
    @FXML
    private Label clientFidelLabel;


    void setServ(Service serv, Sofer sofer){
        this.serv = serv;
        this.sofer = sofer;
        serv.addObserver(this);
        initModel(LocalDate.now());

    }

    void initModel(LocalDate data){
        model.setAll(serv.listaClientilor(pi, sofer));
        model2.setAll(serv.comenziSoferDatDataData(sofer, data));
        mediaLabel.setText("Media comenzilor pe zi din ultimele 3 luni:"+ " " + serv.media(sofer));

        var cmf = serv.celMaiFidel(sofer);
        if(cmf != null)
            clientFidelLabel.setText("Cel mai fidel client:" + " " + cmf.getNume());
        else
            clientFidelLabel.setText("Cel mai fidel client:" + " -");

        model3.setAll(StreamSupport.stream(serv.getCereri().spliterator(), false).toList());
    }

    @Override
    public void update() {

        var picked = comandaDatePicker.getValue();

        if(picked != null)
            initModel(picked);

        else initModel(LocalDate.now());

    }

    @FXML
    void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Long>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("username"));
        numeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nume"));

        idComandaColumn.setCellValueFactory(new PropertyValueFactory<Comanda, Long>("id"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<Comanda, Long>("persoana"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<Comanda, LocalDateTime>("data"));

        persoaneTableView.setItems(model);
        comenziTableView.setItems(model2);

        pageSizeTextField.setText(String.valueOf(pi.getPageSize()));
        pageSizeTextField.textProperty().addListener(o -> handlePageSizeChange());

        comandaDatePicker.valueProperty().addListener(o -> handleDateChange());


        numeClientColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getClient().getNume()));
        locatieColumn.setCellValueFactory(new PropertyValueFactory<Cerere, String>("locatie"));
        cereriTableView.setItems(model3);

    }

    private void handleDateChange() {

        var picked = comandaDatePicker.getValue();
        initModel(picked);
    }

    @FXML
    public void handleNext(){

        if(serv.listaClientilor(new PagingInformationObject(pi.getPageNumber() + 1, pi.getPageSize()), sofer).isEmpty())
            return;

        pi = new PagingInformationObject(pi.getPageNumber() + 1, pi.getPageSize());

        var picked = comandaDatePicker.getValue();
        initModel(picked);
    }

    @FXML
    public void handlePrevious(){

        if(pi.getPageNumber() == 1)
            return;

        pi = new PagingInformationObject(pi.getPageNumber() - 1, pi.getPageSize());
        var picked = comandaDatePicker.getValue();
        initModel(picked);
    }

    @FXML
    public void handlePageSizeChange(){

        if(pageSizeTextField.getText().isEmpty())
            return;

        int pageSize = Integer.parseInt(pageSizeTextField.getText());

        pi = new PagingInformationObject(1, pageSize);
        var picked = comandaDatePicker.getValue();
        initModel(picked);
    }

    @FXML
    public void handlleOnoreaza(ActionEvent ev){

        var client = cereriTableView.getSelectionModel().getSelectedItem().getClient();

        int asteptare;
        if(!onoreazaTextField.getText().isEmpty()) {
            asteptare = Integer.parseInt(onoreazaTextField.getText());
        }
        else
            asteptare = 0;

        if(client != null && asteptare != 0){
            serv.adaugaOferta(sofer, client, asteptare);
            serv.stergeCerere(client);
        }

    }
}
