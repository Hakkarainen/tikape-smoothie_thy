package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.Database;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.RaakaAineDao;

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

        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annos", annosDao.findOne(tmp));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());   
        
        get("/uusiAnnos/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            String tmp = req.params("nimi");
            //System.out.println(tmp);
            map.put("uusiAnnos", annosDao.insertOne(tmp));

            return new ModelAndView(map, "uusiAnnos");
        }, new ThymeleafTemplateEngine());
        
        post("/uusiAnnos/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            String tmp = req.params("nimi");
            //System.out.println(tmp);
            map.put("uusiAnnos", annosDao.insertOne(tmp));

            return new ModelAndView(map, "uusiAnnos");
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
            map.put("raakaAine", annosDao.findOne(tmp));

            return new ModelAndView(map, "raakaAine");
        }, new ThymeleafTemplateEngine());
        
        get("/uusiRaakaAine/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            String tmp = req.params("nimi");
            System.out.println(tmp);
            map.put("uusiRaakaAine", annosDao.insertOne(tmp));

            return new ModelAndView(map, "uusiRaakaAine");
        }, new ThymeleafTemplateEngine());

        post("/uusiRaakaAine/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            String tmp = req.params("nimi");
            System.out.println(tmp);
            map.put("uusiRaakaAine", annosDao.insertOne(tmp));

            return new ModelAndView(map, "uusiRaakaAine");
        }, new ThymeleafTemplateEngine());

        get("/annosRaakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annosRaakaAineet", annosRaakaAineDao.findAll());

            return new ModelAndView(map, "annosRaakaAineet");
        }, new ThymeleafTemplateEngine());

        get("/annosResepti/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annosResepti", annosRaakaAineDao.findRecipe(tmp));

            return new ModelAndView(map, "annosResepti");
        }, new ThymeleafTemplateEngine());

        get("/annosRaakaAine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annosRaakaAine", annosRaakaAineDao.findOne(tmp));

            return new ModelAndView(map, "annosRaakaAine");
        }, new ThymeleafTemplateEngine());
    }
}
