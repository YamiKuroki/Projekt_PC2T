package model;
import java.util.ArrayList;

public abstract class Zamestnanec {
    public int id;
    public String jmeno;
    public String prijmeni;
    public int rokNarozeni;
    public String skupina;
    
    public ArrayList<String> spolupracovnici = new ArrayList<>(); 

    public Zamestnanec(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
    }

   
    public abstract void spustiDovednost();
}