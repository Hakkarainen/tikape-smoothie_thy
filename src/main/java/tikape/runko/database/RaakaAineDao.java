
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }
       
    public RaakaAine insertOne(RaakaAine ar) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
        stmt.setString(1, ar.getNimi());
        
        stmt.executeUpdate();
        
        System.out.println("LISÄTÄÄN RAAKA AINE");

        //int i = getNewid(connection); Tarvitaanko tätä?
        //ara.setId(i); 

        stmt.close();
        connection.close();
        return ar; 
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE key = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine ra = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return ra;
    }
    
         public RaakaAine findOneByNimi(String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet rs = stmt.executeQuery(); 
        RaakaAine ra = null;
        if (rs.next()) {
            ra = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ra;
    }
    
        public void insertOne(Integer nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Annos(nimi) VALUES (?)");
        //stmt.setObject(1, key);

        stmt.close();
        connection.close();

    }

        
    @Override
    public List<RaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }
    
    public void save(RaakaAine raakaAine) throws SQLException {
        this.database.update("INSERT INTO RaakaAine(nimi) VALUES (?)", raakaAine.getNimi());
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        this.database.update("DELETE FROM RaakaAine WHERE id = ?", key);        
    }

}
