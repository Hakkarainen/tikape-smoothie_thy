package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    }
    
    public AnnosRaakaAine insertOne(AnnosRaakaAine ara) throws SQLException {
        Connection connection = this.database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO AnnosRaakaAine (jarjestys, maara, ohje) VALUES (?, ?, ?)");
        stmt.setInt(1, ara.get_Annos_id());
        stmt.setInt(2, ara.get_Raaka_aine_id());
        stmt.setInt(3, ara.getJarjestys());
        stmt.setFloat(4, ara.getMaara());
        stmt.setString(5, ara.getOhje());
        
        stmt.executeUpdate();
        
        System.out.println("LISÄTÄÄN RAAKA AINE ANNOKSEEN");

        //int i = getNewid(connection); Tarvitaanko tätä?
        //ara.setId(i); 

        stmt.close();
        connection.close();
        return ara; 
    }

    @Override
    public AnnosRaakaAine findOne(Integer annos_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setObject(1, annos_id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        annos_id = rs.getInt("annos_id");
        Integer raaka_aine_id = rs.getInt("raaka_aine_id");
        Integer jarjestys = rs.getInt("jarjestys");
        Float maara = rs.getFloat("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine ara = new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return ara;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        while (rs.next()) {

            Integer annos_id = rs.getInt("annos_id");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Float maara = rs.getFloat("maara");
            String ohje = rs.getString("ohje");

            annosRaakaAineet.add(new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annosRaakaAineet;
    }

    public List<AnnosRaakaAine> findRecipe(Integer annos_id) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine where AnnosRaakaAine.annos_id = annos_id");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> annosResepti = new ArrayList<>();
        while (rs.next()) {

            annos_id = rs.getInt("annos_id");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Float maara = rs.getFloat("maara");
            String ohje = rs.getString("ohje");

            annosResepti.add(new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annosResepti;
    }

    public void mixRecipe(String annoksenNimi, String raakaAineenNimi, Integer jarjestys, Float maara, String ohje) throws SQLException {
        
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection connection = database.getConnection()) {
            Statement stmt = connection.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                stmt.executeUpdate(lause);
            }

//            stmt.close();
//            connection.close();

        } catch (Throwable t) {
            // virheilmoitus
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

//      AnnosRaakaAine liitostaulun ylläpitoon tarvittavat komennot suoritusjärjestyksessä
        lista.add("SELECT annos_id FROM Annos WHERE name = annos");
        lista.add("SELECT raaka_aine_id FROM RaakaAine WHERE name = raaka_aine");

        lista.add("INSERT INTO AnnosRaakaAine (Annos.annos_id, RaakaAine.raaka_aine_id, jarjestys, maara, ohje) "
                + "VALUES (annos_id, raaka_aine_id, jarjestys, maara, (annos + 'pirtelön ohje'));");

    return lista ;
}
    
    public void save(AnnosRaakaAine annosRaakaAine) throws SQLException {
        this.database.update("INSERT INTO AnnosRaakaAine(annos_id) VALUES (?)", annosRaakaAine.get_Annos_id());
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        this.database.update("DELETE FROM AnnosRaakaAine WHERE id = ?", key);        
    }
}
