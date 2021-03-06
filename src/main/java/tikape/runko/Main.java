package tikape.runko;

import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.Database;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:pirtelot.db");
        database.init();

        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database);
        AnnosDao annosDao = new AnnosDao(database);
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Saisiko olla pirtelö :-)");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/annokset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());

        get("/annos/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            String nimi = req.params("nimi");
            System.out.println(nimi);
            map.put("annos", annosDao.findOneByName(nimi));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            System.out.println(id);
            map.put("annos", annosDao.findOne(id));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        get("/raakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/raakaAine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            System.out.println(id);
            map.put("raakaAine", raakaAineDao.findOne(id));

            return new ModelAndView(map, "raakaAine");
        }, new ThymeleafTemplateEngine());

        get("/raakaAine/name", (req, res) -> {
            HashMap map = new HashMap<>();
            String nimi = req.params("nimi");
            System.out.println(nimi);
            map.put("raakaAine", raakaAineDao.findOneByName(nimi));

            return new ModelAndView(map, "raakaAine");
        }, new ThymeleafTemplateEngine());

        get("/annosRaakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annosRaakaAineet", annosRaakaAineDao.findAll());

            return new ModelAndView(map, "annosRaakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/annosRaakaAine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            System.out.println(Integer.toString(id));
            map.put("annosRaakaAineet", annosRaakaAineDao.findSmoothie(id));

            return new ModelAndView(map, "annosRaakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/annosResepti", (req, res) -> {
            HashMap map = new HashMap<>();
            String annoksenNimi = req.params("annoksenNimi");
            String raakaAineenNimi = req.params("raakaAineenNimi");

            System.out.println();
            System.out.println("annoksenNimi: " + annoksenNimi);
            System.out.println("raakaAineenNimi: " + raakaAineenNimi);
            System.out.println();

            Integer annos_id = annosDao.findOneByName(annoksenNimi).getId();
            Integer raaka_aine_id = raakaAineDao.findOneByName(raakaAineenNimi).getId();

            map.put("annosResepti", annosRaakaAineDao.findOneRawMaterialForSmoothie(annos_id, raaka_aine_id));

            return new ModelAndView(map, "annosResepti");
        }, new ThymeleafTemplateEngine());

        get("/uusiAnnos", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());

            return new ModelAndView(map, "uusiAnnos");
        }, new ThymeleafTemplateEngine());

        get("/uusiRaakaAine", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "uusiRaakaAine");
        }, new ThymeleafTemplateEngine());

        post("/lisaaRaakaAineReseptiin", (req, res) -> {
            String annoksenNimi = req.queryParams("annoksenNimi");
            String raakaAineenNimi = req.queryParams("raakaAineenNimi");
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            Integer maara = Integer.parseInt(req.queryParams("maara"));
            String ohje = req.queryParams("ohje");

            System.out.println();
            System.out.println("Lisätään raaka-aine annoksen reseptiin: ");
            System.out.println("annoksen nimi: " + annoksenNimi);
            System.out.println("raaka-aineen nimi: " + raakaAineenNimi);
            System.out.println("lisäämisjarjestys: " + Integer.toString(jarjestys));
            System.out.println("raaka-aineenmaara: " + Integer.toString(maara));
            System.out.println("valmistusohje: " + ohje);
            System.out.println();

            Annos annos = annosDao.findOneByName(annoksenNimi);
            if (annos == null) {
                annosDao.insertOne(annoksenNimi); // Lisää uusi annos tietokantaan
            }
            annos = annosDao.findOneByName(annoksenNimi); //Hakee Annoksen
            Integer annos_id = annos.getId(); //Hakee Id:n

            RaakaAine raaka_aine = raakaAineDao.findOneByName(raakaAineenNimi);
            if (raaka_aine == null) {
                raakaAineDao.insertOne(raakaAineenNimi); // Lisää uusi raaka-aine tietokantaan
            }
            raaka_aine = raakaAineDao.findOneByName(raakaAineenNimi); //Hakee Annoksen
            Integer raaka_aine_id = raaka_aine.getId(); //Hakee Id:n

            if ((annos_id != null) && (raaka_aine_id != null)) {
                AnnosRaakaAine ara = new AnnosRaakaAine(annos_id, raaka_aine_id, jarjestys, maara, ohje);
                annosRaakaAineDao.insertOne(ara); // Lisää uusi annosRaakaAine tietokantaan
            }
            res.redirect("/uusiAnnos");
            return "";
        });

        // Tämä POST luo uuden annoksen
        post("/uusiAnnos/:nimi", (req, res) -> {
            String nimi = req.queryParams("nimi");

            System.out.println();
            System.out.println("Luodaan uusi annos: " + nimi);
            System.out.println();

            Annos annos = annosDao.findOneByName(nimi);
            if (annos == null) {
                annosDao.insertOne(nimi); // Lisää uusi annos tietokantaan
            }
            //annos_id = annosDao.findOneByName(nimi).getId(); //Hakee Id:n

            res.redirect("/uusiAnnos");
            return "";
        });

        // Tämä poistaa annoksen
        get("/poistaAnnos/:nimi", (req, res) -> {
            String nimi = req.params("nimi");

            System.out.println();
            System.out.println("Poista annos: " + nimi);
            System.out.println();

            Annos annos = annosDao.findOneByName(nimi);
            if (annos != null) {
                annosDao.delete(annos.getId()); // Poista annos tietokannasta

                List<AnnosRaakaAine> arat = annosRaakaAineDao.findSmoothie(annos.getId());

                for (int i = 0; (i < arat.size()); i++) {
                    AnnosRaakaAine ara = arat.get(i);
                    Integer annos_id = ara.get_Annos_id();
                    annosRaakaAineDao.delete(annos_id); // Poista annos vielä resepti-tietokannasta
                }
            }

            res.redirect("/uusiAnnos");
            return "";
        });

        // Tämä POST luo uuden raaka-aineen
        post("/uusiRaakaAine/:nimi", (req, res) -> {
            String nimi = req.queryParams("nimi");

            System.out.println();
            System.out.println("Luodaan uusi raaka-aine: " + nimi);
            System.out.println();

            RaakaAine raaka_aine = raakaAineDao.findOneByName(nimi);
            if (raaka_aine == null) {
                raakaAineDao.insertOne(nimi); // Lisää uusi raaka-aine tietokantaan
            }
            //raaka_aine_id = raakaAineDao.findOneByName(nimi).getId(); //Hakee Id:n
            res.redirect("/uusiRaakaAine");
            return "";
        });

        // Tämä poistaa raaka-aineen
        get("/poistaRaakaAine/:nimi", (req, res) -> {
            String nimi = req.params("nimi");

            System.out.println();
            System.out.println("Poista raaka-aine: " + nimi);
            System.out.println();

            RaakaAine raaka_aine = raakaAineDao.findOneByName(nimi);
            if (raaka_aine != null) {
                raakaAineDao.delete(raaka_aine.getId()); // Poista raaka-aine tietokannasta

                List<AnnosRaakaAine> ara = annosRaakaAineDao.findRawMaterial(raaka_aine.getId());

                for (int i = 0; (i < ara.size()); i++) {
                    AnnosRaakaAine raakaAine = ara.get(i);
                    Integer raaka_aine_id = raakaAine.get_Raaka_aine_id();
                    annosRaakaAineDao.delete(raaka_aine_id); // Poista  raaka-aine vielä resepti-tietokannasta
                }
            }

            res.redirect("/uusiRaakaAine");
            return "";
        });

    }
}
