package com.example.taximetrie.Repository.DB;

import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Repository.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.sql.*;
import java.util.Set;

public class PersoanaDBRepository implements Repository<Long, Persoana> {

    protected String url;
    protected String username;
    protected  String password;

    public PersoanaDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Persoana> findOne(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null!");

        try(
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from persoane where id = ?");
            ) {

            statement.setLong(1, id);

            ResultSet result = statement.executeQuery();

            if(result.next()){

                String user = result.getString("username");
                String password = result.getString("password");

                Persoana pers = new Persoana(username, password);
                pers.setId(id);

                return Optional.of(pers);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<Persoana> findALL() {
        Set<Persoana> persoane = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from persoane")) {

            ResultSet result = statement.executeQuery();

            while(result.next()){

                Long id = result.getLong("id");
                String user = result.getString("username");
                String nume = result.getString("nume");

                Persoana persoana = new Persoana(user, nume);
                persoana.setId(id);

                persoane.add(persoana);
            }

            return persoane;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Persoana> save(Persoana entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> update(Persoana entity) {
        return Optional.empty();
    }
}
