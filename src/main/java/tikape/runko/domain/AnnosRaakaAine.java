package tikape.runko.domain; 

public class AnnosRaakaAine {
    
    private Integer annos_id;
    private Integer raaka_aine_id;
    private Integer jarjestys;
    private Integer maara;
    private String ohje;

    public AnnosRaakaAine(Integer annos_id, Integer raaka_aine_id,  Integer jarjestys, Integer maara, String ohje) { 
        this.annos_id = annos_id;
        this.raaka_aine_id = raaka_aine_id;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }
    
    public Integer get_Annos_id() {
        return annos_id;
    }
    
    public void set_Annos_id(Integer annos_id) {
        this.annos_id = annos_id;
    }

    public Integer get_Raaka_aine_id() {
        return raaka_aine_id;
    }

    public void set_Raaka_aine_id(Integer raaka_aine_id) {
        this.raaka_aine_id = raaka_aine_id;
    }   
    
    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }
    
    
    public Integer getMaara() {
        return maara;
    }

    public void setMaara(Integer maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

}
