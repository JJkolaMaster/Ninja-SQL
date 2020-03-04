package tietokanta2;

import java.sql.SQLException;
import java.util.Scanner;

public class Kayttoliittyma {

    private final Scanner lukija;
    private final Rajapinta rajapinta;

  
    public Kayttoliittyma(Scanner lukija, Rajapinta rajapinta) {

        this.lukija = lukija;
        this.rajapinta = rajapinta;
    }

    public void kaynnista() throws SQLException {
        //Tulostaa ohjeet alussa
        this.tulostaOhje();

        while (true) {

            System.out.print("Valitse toiminto: ");
            String komento = lukija.nextLine();
            System.out.println("");

            if (komento.equals("10")) {
                System.out.println("Ohjelma pysäytetään");
                System.out.println("Hei hei!");
                break;

            } else if (komento.equals("1")) {

                this.rajapinta.luoUusiTietokanta();

            } else if (komento.equals("2")) {

                System.out.print("Anna paikan nimi: ");
                String paikka = lukija.nextLine();
                rajapinta.luoPaikka(paikka);

            } else if (komento.equals("3")) {

                System.out.print("Anna asiakkaan nimi: ");
                String asiakas = lukija.nextLine();
                rajapinta.luoAsiakas(asiakas);

            } else if (komento.equals("4")) {

                System.out.print("Anna paketin seurantakoodi: ");
                String seurantakoodi = lukija.nextLine();
                System.out.print("Anna asiakkaan nimi: ");
                String asiakas = lukija.nextLine();
                rajapinta.luoPaketti(seurantakoodi, asiakas);

            } else if (komento.equals("5")) {

                System.out.print("Anna paketin seurantakoodi: ");
                String seurantakoodi = lukija.nextLine();
                System.out.print("Anna tapahtuman paikka: ");
                String paikka = lukija.nextLine();
                System.out.print("Anna tapahtuman kuvaus: ");
                String tapahtuma = lukija.nextLine();
                rajapinta.luoTapahtuma(seurantakoodi, paikka, tapahtuma);

            } else if (komento.equals("6")) {

                System.out.print("Anna paketin seurantakoodi: ");
                String seurantakoodi = lukija.nextLine();
                rajapinta.haePaketinTapahtumatKoodilla(seurantakoodi);

            } else if (komento.equals("7")) {

                System.out.print("Anna asiakkaan nimi: ");
                String asiakas = lukija.nextLine();
                rajapinta.haeAsikkaanPaketitNimella(asiakas);

            } else if (komento.equals("8")) {

                System.out.print("Anna paikan nimi: ");
                String paikka = lukija.nextLine();
                System.out.print("Anna päivämäärä (muodossa p.k.vvvv): ");
                String paivamaara = lukija.nextLine();
                rajapinta.haePaikastaTapahtumienMaara(paikka, paivamaara);

            } else if (komento.equals("9")) {

                rajapinta.tehokkkuusTesti();

            } else if (komento.equals("help")) {

                this.tulostaOhje();

            } else {
                System.out.println("Virheellinen syöte");
                System.out.println("Syötä arvo 1-10");
                System.out.println("");
            }
        }
    }

    public void tulostaOhje() {
        System.out.println("------ SQL OHJELMA V 1.0 ------");
        System.out.println(" help - Ohjeet");
        System.out.println(" 1 - Luo tietokanta / Yhdistä tietokantaan");
        System.out.println(" 2 - Lisää paikka");
        System.out.println(" 3 - Lisää asiakas");
        System.out.println(" 4 - Lisää paketti = (seurantakoodi + asiakas)");
        System.out.println(" 5 - Lisää tapahtuma = (seurantakoodi + kuvaus)");
        System.out.println(" 6 - Hae kaikki paketin tapahtumat seurantakoodin perusteella");
        System.out.println(" 7 - Hae kaikki asiakkaan paketit ja niihin liittyvien tapahtumien määrä");
        System.out.println(" 8 - Hae annetusta paikasta tapahtumien määrä tiettynä päivänä");
        System.out.println(" 9 - Suorita tietokannan tehokkuustesti");
        System.out.println(" 10 - Poistu ohjelmasta");
        System.out.println("");
    }
}
