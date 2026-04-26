package model;
public class Specialista extends Zamestnanec {
    public Specialista(int id, String jmeno, String prijmeni, int rok) {
        super(id, jmeno, prijmeni, rok);
        this.skupina = "Bezpecnostni specialista";
    }

    @Override
    public void spustiDovednost() {
        double skore = 0;
        
        for (String kvalita : spolupracovnici) {
            if (kvalita.equalsIgnoreCase("spatna")) {
                skore += 3.0;  
            } else if (kvalita.equalsIgnoreCase("priemerna")) {
                skore += 1.5;
            } else if (kvalita.equalsIgnoreCase("dobra")) {
                skore += 0.5;  
            }
        }

        System.out.println(">>> VÝPOČET RIZIKA pre: " + prijmeni);
        System.out.println(">>> Počet väzieb: " + spolupracovnici.size());
        System.out.println(">>> Celkové rizikové skóre: " + skore);
    }
}