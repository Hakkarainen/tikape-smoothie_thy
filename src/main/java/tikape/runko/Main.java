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

        get("/annos/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annos", annosDao.findOne(tmp));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        get("/uusiAnnos", (req, res) -> {
            HashMap map = new HashMap<>();
   
            map.put("annos", annosDao.findOne(tmp));

            return new ModelAndView(map, "annos");
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

        get("/annosRaakaAine/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annosRaakaAine", annosRaakaAineDao.findOne(tmp));

            return new ModelAndView(map, "annosRaakaAine");
        }, new ThymeleafTemplateEngine());

        get("/annosResepti/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int tmp = Integer.parseInt(req.params("id"));
            System.out.println(tmp);
            map.put("annosResepti", annosRaakaAineDao.findRecipe(tmp));

            return new ModelAndView(map, "annosResepti");
        }, new ThymeleafTemplateEngine());

        post("/lisaaAnnosRaakaAineReseptiin/:annoksenNimi :raakaAineenNimi :jarjestys :maara :ohje", (req, res) -> {
            HashMap map = new HashMap<>();
            String annoksenNimi = req.params(":annoksenNimi");
            String raakaAineenNimi = req.params(":raakaAineenNimi");
            int jarjestys = Integer.parseInt(req.params(":jarjestys"));
            Float maara = Float.parseFloat(req.params(":maara"));
            String ohje = req.params(":ohje");
            System.out.println();
            System.out.println(":annoksenNimi" + " " + annoksenNimi);
            System.out.println(":raakaAineenNimi" + " " + raakaAineenNimi);
            System.out.println(":jarjestys" + " " + jarjestys);
            System.out.println(":maara" + " " + maara);
            System.out.println(":ohje" + " " + ohje);
            System.out.println();
            map.put("annosRaakaAine", annosRaakaAineDao.mixRecipe(annoksenNimi, raakaAineenNimi, jarjestys, maara, ohje));

            return new ModelAndView(map, "annosRaakaAineet");
        }, new ThymeleafTemplateEngine());
        

        // Tämä POST luo uuden annoksen
        post("/lisaaAnnosRaakaAineReseptiin/:annoksenNimi :raakaAineenNimi :jarjestys :maara :ohje", (req, res) -> {
            String nimi = req.queryParams("nimi");
            
            System.out.println("Luodaan uusi annos");
            int annos_Id = annosDao.findOneByNimi(req.params("nimi")).getId();
            annosDao.insertOne(new Annos(annos_Id, nimi));
            
            res.redirect("/" + req.params("annos") + "/");
            
            return "";

        });  
        
         // Tämä POST käsittelee raaka-aineen lisäyksen olemassa olevaan annokseen
        post("/:tyyppi/:annos", (req, res) -> {
            String viesti = req.queryParams("viesti");
            System.out.println("Miksaaja: " + miksaaja + " RaakaAine: " + viesti);
            
            Kayttaja k = kayttajaDao.findOne(miksaaja);
            if(k == null) {
                System.out.println("Luodaan uusi käyttäjä");
                k = kayttajaDao.add(new Kayttaja(miksaaja));
            }
                
            int annos = annosDao.findOne(Integer.parseInt(req.params("annos"))).getId();
            
            viestiDao.add(new RaakaAine(k.getKayttajaID(), k.getTunnus(), annos, viesti));

            
            res.redirect("/" + req.params("tyyppi") + "/" + req.params("annos"));
            return "";
        });
    }
}

        get("/uusiAnnos", (req, res) -> {
            HashMap map = new HashMap<>();
   
            map.put("annos", annosDao.insertOne(tmp));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        post("/uusiAnnos/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            String tmp = req.params("nimi");
            //System.out.println(tmp);
            map.put("uusiAnnos", annosDao.insertOne(tmp));

            return new ModelAndView(map, "uusiAnnos");
        }, new ThymeleafTemplateEngine());

            res.redirect("/" + req.params("tyyppi") + "/");
            
            return "";
