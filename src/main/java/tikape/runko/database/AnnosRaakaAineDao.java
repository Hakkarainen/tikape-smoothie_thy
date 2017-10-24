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

    public AnnosRaakaAine mixRecipe(AnnosRaakaAine ara) throws SQLException {
        Connection connection = this.database.getConnection();

        System.out.println("LISÄTÄÄN RAAKA AINE ANNOKSEEN");
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO AnnosRaakaAine (annos_id, raaka_aine_id, jarjestys, maara, ohje) VALUES (?, ?, ?)");

        stmt.setInt(1, ara.get_Annos_id());
        stmt.setInt(2, ara.get_Raaka_aine_id());
        stmt.setInt(3, ara.getJarjestys());
        stmt.setFloat(4, ara.getMaara());
        stmt.setString(5, ara.getOhje());

        stmt.executeUpdate();

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
        Integer raaka_aine_id = rs.getInt("raaka_aine_id");
        Integer jarjestys = rs.getInt("jarjestys");
        Integer maara = rs.getInt("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine ara = new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return ara;
    }

    public List<AnnosRaakaAine> findSmoothie(Integer annos_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setObject(1, annos_id);

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        while (rs.next()) {

            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            annosRaakaAineet.add(new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annosRaakaAineet;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> kaikkiAnnosRaakaAineet = new ArrayList<>();
        while (rs.next()) {

            Integer annos_id = rs.getInt("annos_id");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            kaikkiAnnosRaakaAineet.add(new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kaikkiAnnosRaakaAineet;
    }

    public AnnosRaakaAine findRawmaterialRecipe(Integer annos_id, Integer raaka_aine_id) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine where annos_id = ? AND raaka_aine_id = ?");
        stmt.setObject(1, annos_id);
        stmt.setObject(2, raaka_aine_id);

        ResultSet rs = stmt.executeQuery();

        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        Integer jarjestys = rs.getInt("jarjestys");
        Integer maara = rs.getInt("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine ara = new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje);

        rs.close();

        stmt.close();

        connection.close();

        return ara;
    }

    public void save(AnnosRaakaAine annosRaakaAine) throws SQLException {
        this.database.update("INSERT INTO AnnosRaakaAine(annos_id) VALUES (?)", annosRaakaAine.get_Annos_id());
    }

    @Override
    public void delete(Integer key) throws SQLException {
        this.database.update("DELETE FROM AnnosRaakaAine WHERE id = ?", key);
    }
}
