package tikape.runko;

import java.util.HashMap;
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

        get("/annos", (req, res) -> {
            HashMap map = new HashMap<>();
            String nimi = ("nimi");
            System.out.println(nimi);
            map.put("annos", annosDao.findOneByName(nimi));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annos", annosDao.findOne(tmp));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        get("/raakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/raakaAine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("raakaAine", raakaAineDao.findOne(tmp));

            return new ModelAndView(map, "raakaAine");
        }, new ThymeleafTemplateEngine());

        get("/annosRaakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annosRaakaAineet", annosRaakaAineDao.findAll());

            return new ModelAndView(map, "annosRaakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/annosRaakaAine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annosRaakaAineet", annosRaakaAineDao.findSmoothie(tmp));

            return new ModelAndView(map, "annosRaakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/annosResepti", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer annos_id = Integer.parseInt(req.params("annos_id"));
            Integer raaka_aine_id = Integer.parseInt(req.params("raaka_aine_id"));
            System.out.println();
            System.out.println("annos_id: " + annos_id);
            System.out.println("raaka_aine_id: " + raaka_aine_id);
            System.out.println();
            map.put("annosResepti", annosRaakaAineDao.findRawmaterialRecipe(annos_id, raaka_aine_id));

            return new ModelAndView(map, "annosResepti");
        }, new ThymeleafTemplateEngine());

        get("/uusiAnnos", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("uusiAnnost", annosDao.findAll());

            return new ModelAndView(map, "uusiAnnos");
        }, new ThymeleafTemplateEngine());

        get("/uusiRaakaAine", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("uusiRaakaAine", raakaAineDao.findAll());

            return new ModelAndView(map, "uusiRaakaAine");
        }, new ThymeleafTemplateEngine());

        post("/lisaaAnnosRaakaAineReseptiin", (req, res) -> {
            String annoksenNimi = req.params("annoksenNimi");
            String raakaAineenNimi = req.params("raakaAineenNimi");
            Integer jarjestys = Integer.parseInt(req.params("jarjestys"));
            Integer maara = Integer.parseInt(req.params("maara"));
            String ohje = req.params("ohje");

            System.out.println();
            System.out.println("Lisätään raaka-aine annoksen reseptiin: ");
            System.out.println("annoksen nimi: " + annoksenNimi);
            System.out.println("raaka-aineen nimi: " + raakaAineenNimi);
            System.out.println("lisäämisjarjestys: " + jarjestys);
            System.out.println("raaka-aineenmaara: " + maara);
            System.out.println("valmistusohje: " + ohje);
            System.out.println();

            Integer annos_id = annosDao.findOneByName(annoksenNimi).getId();
            Integer raaka_aine_id = annosDao.findOneByName(raakaAineenNimi).getId();

            AnnosRaakaAine ara = annosRaakaAineDao.findRawmaterialRecipe(annos_id, raaka_aine_id);

            annosRaakaAineDao.mixRecipe(ara);

            res.redirect("/annokset");
            return "";
        });

        // Tämä POST luo uuden annoksen
        post("/uusiAnnos/:nimi", (req, res) -> {
            String nimi = req.queryParams("nimi");
            System.out.println();
            System.out.println("Luodaan uusi annos: " + nimi);
            System.out.println();
            annosDao.insertOne(nimi);

            res.redirect("/annokset");
            return "";
        });

        // Tämä poistaa annoksen
        post("/poistaAnnos/:nimi", (req, res) -> {
            String nimi = req.queryParams("nimi");
            System.out.println();
            System.out.println("Poista annos: " + nimi);
            System.out.println();
            Integer annos_id = annosDao.findOneByName(nimi).getId();
            annosDao.delete(annos_id);

            res.redirect("/annokset");
            return "";
        });

        post("/uusiRaakaAine", (req, res) -> {
            String nimi = req.params("nimi");
            System.out.println();
            System.out.println("Luodaan uusi raaka-aine: " + nimi);
            System.out.println();
            raakaAineDao.insertOne(nimi);

            res.redirect("/raakaAineet");
            return "";
        });

        // Tämä poistaa raaka-aineen
        post("/poistaRaakaAine", (req, res) -> {
            String nimi = req.queryParams("nimi");
            System.out.println();
            System.out.println("Poista raaka-aine: " + nimi);
            System.out.println();
            Integer raaka_aine_id = annosDao.findOneByName(nimi).getId();
            annosDao.delete(raaka_aine_id);

            res.redirect("/raakaAineet");
            return "";
        });

    }
}
