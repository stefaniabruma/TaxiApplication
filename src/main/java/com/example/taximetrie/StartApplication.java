package com.example.taximetrie;

import com.example.taximetrie.Domain.*;
import com.example.taximetrie.Repository.DB.CerereDBRepository;
import com.example.taximetrie.Repository.DB.OfertaDBRepository;
import com.example.taximetrie.Repository.DB.PersoanaDBRepository;
import com.example.taximetrie.Repository.DB.SoferDBRepository;
import com.example.taximetrie.Repository.Paging.ComandaPagingDBRepository;
import com.example.taximetrie.Repository.PagingRepository;
import com.example.taximetrie.Repository.Repository;
import com.example.taximetrie.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    Service serv;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        String url = "jdbc:postgresql://localhost:5432/taximetrie";
        String username = "postgres";
        String password = "postgres";

        Repository<Long, Persoana> repo_persoane = new PersoanaDBRepository(url, username, password);
        Repository<Long, Sofer> repo_sofer = new SoferDBRepository(url, username, password);
        ComandaPagingDBRepository repo_comenzi = new ComandaPagingDBRepository(url, username, password);
        Repository<Long, Cerere> repo_cereri = new CerereDBRepository(url, username, password);
        Repository<Long, Oferta> repo_oferte = new OfertaDBRepository(url, username, password);
        serv = new Service(repo_persoane, repo_sofer, repo_comenzi, repo_cereri, repo_oferte);

        initView(stage);
        stage.show();
    }

    private void initView(Stage stage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("login-view.fxml"));
        AnchorPane userLayout = loginLoader.load();
        stage.setScene(new Scene(userLayout));
        stage.setTitle("LogIn");

        LogInController loginController = loginLoader.getController();
        loginController.setServ(serv);
    }
}