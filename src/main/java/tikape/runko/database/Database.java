package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

//      tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Annos (id INTEGER PRIMARY KEY, nimi varchar(100) not null);");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Mango-banaani pirtelö');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Pinaatti-nokkos pirtelö');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Mansikka-suklaa pirtelö');");
        
        lista.add("CREATE TABLE RaakaAine (id INTEGER PRIMARY KEY, nimi varchar(100) not null);");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('mango');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('pinaatti');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('mansikka');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('banaani');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('nokkonen');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('suklaa');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('vaniljasokeri');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('sokeri');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('hunaja');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('maito');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('kerma');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('jogurtti');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('vaniljajäätelö');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('mansikkajäätelö');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('suklaajäätelö');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('vesi');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('rommi');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('kolajuoma');");
        
        lista.add("CREATE TABLE AnnosRaakaAine("    
                + "annos_id integer not null,"
                + "raaka_aine_id integer not null,"
                + "jarjestys integer not null,"
                + "maara float not null,"
                + "ohje varchar(255) not null,"
                + "FOREIGN KEY (annos_id) REFERENCES Annos(id),"
                + "FOREIGN KEY (raaka_aine_id) REFERENCES RaakaAine(id),"
                + "PRIMARY KEY (annos_id, raaka_aine_id));");

        lista.add("INSERT INTO AnnosRaakaAine (annos_id, raaka_aine_id, jarjestys, maara, ohje) "
                + "VALUES (1, 1, 1, 100, 'Mango-banaani pirtelön ohje');");
        
        lista.add("INSERT INTO AnnosRaakaAine (annos_id, raaka_aine_id, jarjestys, maara, ohje) "
                + "VALUES (2, 2, 2, 200, 'Pinaatti-nokkos pirtelön ohje');");
        
        lista.add("INSERT INTO AnnosRaakaAine (annos_id, raaka_aine_id, jarjestys, maara, ohje) "
                + "VALUES (3, 3, 3, 150, 'Mansikka-suklaa pirtelön ohje');");

        return lista;
    }
}
