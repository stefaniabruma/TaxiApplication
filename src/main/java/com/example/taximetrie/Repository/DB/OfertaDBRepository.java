package com.example.taximetrie.Repository.DB;

import com.example.taximetrie.Domain.Oferta;
import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Repository.Repository;

import java.sql.*;
import java.util.Optional;

public class OfertaDBRepository implements Repository<Long, Oferta> {

    protected String url;
    protected String username;
    protected  String password;

    public OfertaDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Oferta> findOne(Long id_persoana) {

        if(id_persoana == null)
            throw new IllegalArgumentException("Id cannot be null!");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select o.id_sofer, o.id_persoana, " +
                        "o.asteptare, p.username, p.nume, ps.username as username_sofer, ps.nume as nume_sofer, s.indicativ_masina " +
                        "from oferte o inner join persoane p on p.id = o.id_persoana " +
                        "inner join soferi s on s.id = o.id_sofer " +
                        "inner join persoane ps on ps.id = s.id " +
                        "where o.id_persoana = ?");
        ) {

            statement.setLong(1, id_persoana);

            ResultSet result = statement.executeQuery();

            if(result.next()){

                Long id_sofer = result.getLong("id_sofer");
                String un_sofer = result.getString("username_sofer");
                String nume_sofer = result.getString("nume_sofer");
                String indm = result.getString("indicativ_masina");

                Sofer sofer = new Sofer(un_sofer, nume_sofer, indm);
                sofer.setId(id_sofer);

                String un_client = result.getString("username");
                String nume_client = result.getString("nume");

                Persoana pers = new Persoana(un_client, nume_client);
                pers.setId(id_persoana);

                int asteptare = result.getInt("asteptare");

                Oferta oferta = new Oferta(sofer, pers, asteptare);

                return Optional.of(oferta);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Oferta> findALL() {
        return null;
    }

    @Override
    public Optional<Oferta> save(Oferta entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not bee null!");

        try(

                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("insert into " +
                        "oferte(id_sofer, id_persoana, asteptare) " +
                        "values(?, ?, ?)")

        ){

            statement.setLong(1, entity.getSofer().getId());
            statement.setLong(2, entity.getClient().getId());
            statement.setInt(3, entity.getAsteptare());

            return statement.executeUpdate() == 0 ? Optional.empty() : Optional.of(entity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Oferta> delete(Long id_persoana) {
        if(id_persoana == null)
            throw new IllegalArgumentException("Id must not be null!");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("delete from oferte " +
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
    public Optional<Oferta> update(Oferta entity) {
        return Optional.empty();
    }
}
