package com.example.taximetrie.Repository.DB;

import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Repository.Repository;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SoferDBRepository implements Repository<Long, Sofer> {

    protected String url;
    protected String username;
    protected  String password;

    public SoferDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Sofer> findOne(Long id) {

        if(id == null)
            throw new IllegalArgumentException("Id cannot be null!");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from " +
                        "persoane p inner join soferi s " +
                        "on p.id == s.id " +
                        "and p.id = ?");
        ) {

            statement.setLong(1, id);

            ResultSet result = statement.executeQuery();

            if(result != null){

                String user = result.getString("username");
                String password = result.getString("password");
                String indMasina = result.getString("indicativMasina");

                Sofer sofer = new Sofer(username, password, indMasina);
                sofer.setId(id);

                return Optional.of(sofer);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Sofer> findALL() {

        Set<Sofer> soferi = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement("select * from soferi s inner join persoane p " +
                "on s.id = p.id")) {

            ResultSet result = statement.executeQuery();

            while(result.next()){

                Long id = result.getLong("id");
                String user = result.getString("username");
                String nume = result.getString("nume");
                String vehicul = result.getString("indicativ_masina");

                Sofer sofer = new Sofer(user, nume, vehicul);
                sofer.setId(id);

                soferi.add(sofer);
            }

            return soferi;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Sofer> save(Sofer entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Sofer> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Sofer> update(Sofer entity) {
        return Optional.empty();
    }
}
