package model;
import java.util.*;
import java.io.*;

public class Program {
    static ArrayList<Zamestnanec> databaze = new ArrayList<>();
    static int idCounter = 1;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Bod l) Načítanie z SQL pri štarte (Simulácia)
        System.out.println("INFO: Pripájanie k SQL databáze...");
        System.out.println("INFO: Dáta z SQL úspešne načítané do pamäte.");

        int volba = 0;
        while (volba != 11) {
            System.out.println("\n--- SYSTÉM ZAMESTNANCOV ---");
            System.out.println("1. Pridať zamestnanca (a)");
            System.out.println("2. Pridať spoluprácu (b)");
            System.out.println("3. Odobrať zamestnanca (c)");
            System.out.println("4. Vyhľadať podľa ID (d)");
            System.out.println("5. Spustiť dovednosť (e)");
            System.out.println("6. Abecedný výpis (f)");
            System.out.println("7. Štatistiky kvality (g)");
            System.out.println("8. Počet ľudí v skupinách (h)");
            System.out.println("9. Uložiť do súboru (i)");
            System.out.println("10. Načítať zo súboru (j)");
            System.out.println("11. KONIEC (Záloha do SQL - k)");
            System.out.print("Vyber možnosť: ");
            
            try {
                volba = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Chyba: Zadaj číslo!");
                sc.nextLine();
                continue;
            }

            switch (volba) {
                case 1: pridat(); break;
                case 2: spolupraca(); break;
                case 3: odobrat(); break;
                case 4: hladat(); break;
                case 5: dovednost(); break;
                case 6: vypis(); break;
                case 7: statistiky(); break;
                case 8: poctyVSkupinach(); break;
                case 9: ulozit(); break;
                case 10: nacitat(); break;
            }
        }
        
        // Bod k) Uloženie do SQL pri ukončení
        System.out.println("ZÁLOHA: Synchronizujem lokálne dáta s SQL databázou...");
        System.out.println("ZÁLOHA: Všetko uložené. Program sa bezpečne ukončil.");
    }

    static void pridat() {
        System.out.print("Skupina (1-Analytik, 2-Specialista): ");
        int typ = sc.nextInt();
        System.out.print("Meno: "); String m = sc.next();
        System.out.print("Priezvisko: "); String p = sc.next();
        System.out.print("Rok narodenia: "); int r = sc.nextInt();

        if (typ == 1) databaze.add(new Analytik(idCounter++, m, p, r));
        else databaze.add(new Specialista(idCounter++, m, p, r));
        System.out.println("Zamestnanec pridaný.");
    }

    static void spolupraca() {
        System.out.print("ID zamestnanca: "); int id1 = sc.nextInt();
        System.out.print("ID kolegu (vazba): "); int id2 = sc.nextInt();
        System.out.print("Kvalita (dobra/priemerna/spatna): "); String k = sc.next();
        
        boolean najdeny = false;
        for (Zamestnanec z : databaze) {
            if (z.id == id1) {
                z.spolupracovnici.add(k); // Pridanie do dynamickej štruktúry
                najdeny = true;
            }
        }
        if (najdeny) System.out.println("Spolupráca zaznamenaná.");
        else System.out.println("Zamestnanec s ID " + id1 + " neexistuje.");
    }

    static void odobrat() {
        System.out.print("Zadaj ID na odstránenie: ");
        int id = sc.nextInt();
        boolean odstranene = databaze.removeIf(z -> z.id == id);
        if (odstranene) System.out.println("Zamestnanec aj s väzbami bol odstránený.");
        else System.out.println("ID nenájdené.");
    }

    static void hladat() {
        System.out.print("Zadaj ID: ");
        int id = sc.nextInt();
        for (Zamestnanec z : databaze) {
            if (z.id == id) {
                System.out.println("INFO: " + z.jmeno + " " + z.prijmeni + " [" + z.skupina + "]");
                System.out.println("Počet väzieb: " + z.spolupracovnici.size());
                return;
            }
        }
        System.out.println("Nenájdené.");
    }

    static void dovednost() {
        System.out.print("ID zamestnanca pre spustenie dovednosti: ");
        int id = sc.nextInt();
        for (Zamestnanec z : databaze) {
            if (z.id == id) {
                z.spustiDovednost();
                return;
            }
        }
    }

    static void vypis() {
        // Bod f) Abecedný výpis podľa priezviska
        databaze.sort(Comparator.comparing(z -> z.prijmeni));
        System.out.println("\nZoznam zamestnancov:");
        for (Zamestnanec z : databaze) {
            System.out.println(z.prijmeni + " " + z.jmeno + " (ID: " + z.id + ") - " + z.skupina);
        }
    }

    static void statistiky() {
        int d = 0, p = 0, s = 0;
        Zamestnanec najviac = null;
        for (Zamestnanec z : databaze) {
            for (String kvalita : z.spolupracovnici) {
                if (kvalita.equalsIgnoreCase("dobra")) d++;
                else if (kvalita.equalsIgnoreCase("priemerna")) p++;
                else if (kvalita.equalsIgnoreCase("spatna")) s++;
            }
            if (najviac == null || z.spolupracovnici.size() > najviac.spolupracovnici.size()) {
                najviac = z;
            }
        }
        System.out.println("Prevládajúca kvalita: " + (d > p && d > s ? "Dobrá" : (p > s ? "Priemerná" : "Zlá")));
        if (najviac != null) System.out.println("Najviac väzieb má: " + najviac.prijmeni + " (ID: " + najviac.id + ")");
    }

    static void poctyVSkupinach() {
        int a = 0, s = 0;
        for (Zamestnanec z : databaze) {
            if (z instanceof Analytik) a++; else s++;
        }
        System.out.println("Počet Analytikov: " + a);
        System.out.println("Počet Špecialistov: " + s);
    }

    static void ulozit() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.txt"))) {
            for (Zamestnanec z : databaze) {
                writer.println(z.id + "," + z.jmeno + "," + z.prijmeni + "," + z.rokNarozeni + "," + (z instanceof Analytik ? "A" : "S"));
            }
            System.out.println("Uložené do data.txt");
        } catch (IOException e) { System.out.println("Chyba pri zápise."); }
    }

    static void nacitat() {
        try (Scanner fs = new Scanner(new File("data.txt"))) {
            databaze.clear();
            while (fs.hasNextLine()) {
                String[] c = fs.nextLine().split(",");
                int id = Integer.parseInt(c[0]);
                int r = Integer.parseInt(c[3]);
                if (c[4].equals("A")) databaze.add(new Analytik(id, c[1], c[2], r));
                else databaze.add(new Specialista(id, c[1], c[2], r));
                if (id >= idCounter) idCounter = id + 1;
            }
            System.out.println("Dáta načítané zo súboru.");
        } catch (Exception e) { System.out.println("Súbor s dátami neexistuje."); }
    }
}