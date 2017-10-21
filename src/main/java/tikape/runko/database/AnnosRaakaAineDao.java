package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
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

    
    public void makeRecipe(Integer annos_id, Integer raaka_aine_id,  Integer jarjestys, Float maara, String ohje) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO AnnosRaakaAine (annos_id, raaka_aine_id, jarjestys, maara, ohje) "
                + "VALUES (1, 1, 1, 100, 'Ananas-banaani pirtelön ohje');");

        stmt.close();
        connection.close();

    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
