/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Annos;

public class AnnosDao implements Dao<Annos, Integer> {

    private Database database;

    public AnnosDao(Database database) {
        this.database = database;
    }
    
        
    public void insertOne(String nimi) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
        stmt.setString(1, nimi);
        
        System.out.println("LISÄTÄÄN UUSI ANNOS TIETOKANTAAN");
        stmt.executeUpdate();

        stmt.close();
        connection.close();

    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String nimi = rs.getString("nimi");

        Annos a = new Annos(nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }
    
     public Annos findOneByName(String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet rs = stmt.executeQuery(); 
        Annos a = null;
        if (rs.next()) {
            a = new Annos(rs.getString("nimi"));
        }

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }
    
    
    @Override
    public List<Annos> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            annokset.add(new Annos(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annokset;
    }
    
    public void save(Annos annos) throws SQLException {
        this.database.update("INSERT INTO Annos(nimi) VALUES (?)", annos.getNimi());
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        this.database.update("DELETE FROM Annos WHERE id = ?", key);        
    }

}
