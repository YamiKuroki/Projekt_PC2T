package model;
public class Analytik extends Zamestnanec {
    public Analytik(int id, String jmeno, String prijmeni, int rok) {
        super(id, jmeno, prijmeni, rok);
        this.skupina = "Datovy analytik";
    }

    @Override
    public void spustiDovednost() {
        System.out.println(">>> Analytik " + prijmeni + " hľadá spoločných spolupracovníkov medzi kolegami...");
    }
}