package com.example.taximetrie.Repository.DB;

import com.example.taximetrie.Domain.Cerere;
import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CerereDBRepository implements Repository<Long, Cerere> {

    protected String url;
    protected String username;
    protected  String password;

    public CerereDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Cerere> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Cerere> findALL() {
        Set<Cerere> cereri = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select c.id, c.id_persoana, c.locatie, " +
                    "p.username, p.nume " +
                    "from cereri c inner join persoane p on c.id_persoana = p.id")) {

            ResultSet result = statement.executeQuery();

            while(result.next()){

                Long id_persoana = result.getLong("id_persoana");
                String user = result.getString("username");
                String nume = result.getString("nume");

                Persoana persoana = new Persoana(user, nume);
                persoana.setId(id_persoana);

                Long id = result.getLong("id");
                String loc = result.getString("locatie");

                Cerere cerere = new Cerere(persoana, loc);
                cerere.setId(id);

                cereri.add(cerere);
            }

            return cereri;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Cerere> save(Cerere entity) {

        if(entity == null)
            throw new IllegalArgumentException("Entity must not bee null!");

        try(

                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("insert into " +
                        "cereri(id_persoana, locatie) " +
                        "values(?, ?)")

        ){

            statement.setLong(1, entity.getClient().getId());
            statement.setString(2, entity.getLocatie());

            return statement.executeUpdate() == 0 ? Optional.empty() : Optional.of(entity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Cerere> delete(Long id_persoana) {
        if(id_persoana == null)
            throw new IllegalArgumentException("Id must not be null!");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("delete from cereri " +
                        "where id_persoana = ?")
        ) {

            statement.setLong(1, id_persoana);

            var fr_r = findOne(id_persoana);
            statement.executeUpdate();

            return fr_r;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Cerere> update(Cerere entity) {
        return Optional.empty();
    }
}
