package tietokanta2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Rajapinta {

    private Connection db;

    public Rajapinta() {

    }

    public void luoUusiTietokanta() throws SQLException {
        //Tarkistuskysely, onko tietokantaa jo olemassa
        File f = new File("./paketinSeuranta.db");
        if (f.exists() && !f.isDirectory()) {
            //Luo tietokanta // yhdistä tietokantaan
            this.db = DriverManager.getConnection("jdbc:sqlite:paketinSeuranta.db");
            //indikointi
            System.out.println("Tietokanta paketinSeuranta.db on jo olemassa");
            System.out.println("Ei tehdä tauluja, vain pelkkä yhdistäminen");
            System.out.println("");
        } else {
            this.db = DriverManager.getConnection("jdbc:sqlite:paketinSeuranta.db");
            //Luo statement
            Statement s = db.createStatement();
            //Luo taulut
//------------------------------------------------------------------------------
            //Paikat***
            s.execute("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, paikka_nimi TEXT)");
            //Asiakkaat***
            s.execute("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, asiakas_nimi TEXT)");
            //Paketit***
            s.execute("CREATE TABLE Paketit (id INTEGER PRIMARY KEY, koodi TEXT, asiakas_id INTEGER REFERENCES Asiakkaat(id))");
            //Lisätään indeksi jos halutaan
//            s.execute("CREATE INDEX idx_asiakas ON Paketit (asiakas_id)");
            //Tapahtumat***
            s.execute("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paketti_id INTEGER REFERENCES Paketit(id), paikka_id INTEGER REFERENCES Paikat(id), kuvaus TEXT, aika TEXT)");
            //Lisätään indeksi jos halutaan
//            s.execute("CREATE INDEX idx_paketti ON Tapahtumat (paketti_id)");
            //Indikointi
            System.out.println("Yhdistetty tietokantaan");
            System.out.println("Uusi tietokanta ja taulut luotu");
            System.out.println("");
        }
    }

    public void luoPaikka(String paikka) throws SQLException {
        PreparedStatement p = db.prepareStatement("SELECT * FROM Paikat WHERE paikka_nimi=?");
        p.setString(1, paikka);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            System.out.println("");
            System.out.println("Paikka tällä nimellä löytyy jo tietokannasta");
            System.out.println("Muokkaa paikan nimeä ja yritä uudestaan");
            System.out.println("");

        } else {

            try {
                p = db.prepareStatement("INSERT INTO Paikat(paikka_nimi) VALUES (?)");
                p.setString(1, paikka);
                p.executeUpdate();
                System.out.println("Paikka lisätty");
                System.out.println("");

            } catch (SQLException ex) {
                System.out.println("Paikan lisäys epäonnistui");
            }

        }
    }

    public void luoPaikkaTesti() throws SQLException {
        Statement s = db.createStatement();
        s.execute("BEGIN TRANSACTION");

        for (int i = 1; i <= 1000; i++) {
            String paikka = "P" + i;
            PreparedStatement p = db.prepareStatement("INSERT INTO Paikat(paikka_nimi) VALUES (?)");
            p.setString(1, paikka);
            p.executeUpdate();
        }
        s.execute("COMMIT");
    }

    public void luoAsiakas(String asiakas) throws SQLException {
        
        PreparedStatement p = db.prepareStatement("SELECT * FROM Asiakkaat WHERE asiakas_nimi=?");
        p.setString(1, asiakas);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            System.out.println("");
            System.out.println("Asiakas tällä nimellä löytyy jo tietokannasta");
            System.out.println("Muokkaa nimeä ja yritä uudestaan");
            System.out.println("");

        } else {

            try {
                p = db.prepareStatement("INSERT INTO Asiakkaat(asiakas_nimi) VALUES (?)");
                p.setString(1, asiakas);
                p.executeUpdate();
                System.out.println("Asiakas lisätty");
                System.out.println("");

            } catch (SQLException ex) {
                System.out.println("Asiakkaan lisäys epäonnistui");
            }
        }
    }

    public void luoAsiakasTesti() throws SQLException {
        Statement s = db.createStatement();
        s.execute("BEGIN TRANSACTION");

        for (int i = 1; i <= 1000; i++) {
            String asiakas = "A" + i;
            PreparedStatement p = db.prepareStatement("INSERT INTO Asiakkaat(asiakas_nimi) VALUES (?)");
            p.setString(1, asiakas);
            p.executeUpdate();
        }
        s.execute("COMMIT");
    }

    public void luoPaketti(String koodi, String asiakas) throws SQLException {

        PreparedStatement p = db.prepareStatement("SELECT * FROM Paketit WHERE koodi=?");
        p.setString(1, koodi);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            System.out.println("");
            System.out.println("Seurantakoodi on jo tietokannassa");
            System.out.println("Anna uusi koodi ja yritä uudestaan");
            System.out.println("");

        } else {

            int asiakas_id;

            p = db.prepareStatement("SELECT id FROM Asiakkaat WHERE asiakas_nimi=?");
            p.setString(1, asiakas);
            r = p.executeQuery();
            asiakas_id = r.getInt("id");

            try {
                p = db.prepareStatement("INSERT INTO Paketit(koodi, asiakas_id) VALUES (?,?)");
                p.setString(1, koodi);
                p.setInt(2, asiakas_id);
                p.executeUpdate();
                System.out.println("Paketti lisätty");
                System.out.println("");

            } catch (SQLException ex) {
                System.out.println("Paketin lisäys epäonnistui");
            }
        }
    }

    public void luoPakettiTesti() throws SQLException {
        Statement s = db.createStatement();
        s.execute("BEGIN TRANSACTION");

        int c = 1000000000;
        int asiakas_id = 1;
        for (int i = 1; i <= 1000; i++) {

            String koodi = "F8C" + c;
            PreparedStatement p = db.prepareStatement("INSERT INTO Paketit(koodi, asiakas_id) VALUES (?,?)");
            p.setString(1, koodi);
            p.setInt(2, asiakas_id);
            p.executeUpdate();
            c++;
            asiakas_id++;
        }
        s.execute("COMMIT");
    }

    public void luoTapahtuma(String koodi, String paikka, String kuvaus) throws SQLException {

        int paketti_id;
        int paikka_id;
        String aika = new SimpleDateFormat("d.M.yyyy HH:mm:ss").format(new java.util.Date());

        PreparedStatement p = db.prepareStatement("SELECT id FROM Paketit WHERE koodi=?");
        p.setString(1, koodi);
        ResultSet r = p.executeQuery();
        paketti_id = r.getInt("id");

        p = db.prepareStatement("SELECT id FROM Paikat WHERE paikka_nimi=?");
        p.setString(1, paikka);
        ResultSet g = p.executeQuery();
        paikka_id = g.getInt("id");

        try {
            p = db.prepareStatement("INSERT INTO Tapahtumat(paketti_id, paikka_id, kuvaus, aika) VALUES (?,?,?,?)");
            p.setInt(1, paketti_id);
            p.setInt(2, paikka_id);
            p.setString(3, kuvaus);
            p.setString(4, aika);
            p.executeUpdate();
            System.out.println("Tapahtuma lisätty");
            System.out.println("");

        } catch (SQLException ex) {
            System.out.println("Paketin lisäys epäonnistui");
        }

    }

    public void luoTapahtumaTesti() throws SQLException {
        Statement s = db.createStatement();
        s.execute("BEGIN TRANSACTION");

        String aika = "";
        int c = 0;

        for (int j = 1; j <= 1000; j++) {

            int paketti_id = 1;
            int paikka_id = 1;
            c++;

            for (int i = 1; i <= 1000; i++) {

                String kuvaus = "testi" + c;
                aika = new SimpleDateFormat("d.M.yyyy HH:mm:ss").format(new java.util.Date());
                PreparedStatement p = db.prepareStatement("INSERT INTO Tapahtumat(paketti_id, paikka_id, kuvaus, aika) VALUES (?,?,?,?)");
                p.setInt(1, paketti_id);
                p.setInt(2, paikka_id);
                p.setString(3, kuvaus);
                p.setString(4, aika);
                p.executeUpdate();
                paikka_id++;
                paketti_id++;
            }
        }
        s.execute("COMMIT");
    }

    public void haePaketinTapahtumatKoodilla(String koodi) throws SQLException {
        System.out.println("");
        System.out.println("Paketin seurantakoodin tapahtumat ovat seuraavat:");
        System.out.println("");
        PreparedStatement p = db.prepareStatement("SELECT * FROM Tapahtumat T "
                + "LEFT JOIN Paketit P ON T.paketti_id = P.id "
                + "LEFT JOIN Paikat C ON T.paikka_id = C.id "
                + "WHERE P.koodi=?");
        p.setString(1, koodi);
        ResultSet r = p.executeQuery();

        while (r.next()) {
            System.out.println(r.getString("aika") + ", " + r.getString("paikka_nimi") + ", " + r.getString("kuvaus"));
            System.out.println("");
        }
    }

    public void haeAsikkaanPaketitNimella(String asiakas) throws SQLException {
        System.out.println("");
        System.out.println("Asiakkaalla on seuraavat paketit:");
        System.out.println("");
        PreparedStatement p = db.prepareStatement("SELECT P.koodi, Count(T.kuvaus) AS lkm"
                + " FROM Tapahtumat T LEFT JOIN Paketit P ON T.paketti_id = P.id"
                + " LEFT JOIN Asiakkaat A ON P.asiakas_id = A.id"
                + " WHERE A.asiakas_nimi in (?)");
        p.setString(1, asiakas);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            System.out.println(r.getString("koodi") + ", " + r.getInt("lkm") + " tapahtumaa");
            System.out.println("");
        }
    }

    public void haeAsiakkaanPaketitMaaraTest() throws SQLException {

        int asiakas_id = 1;
        for (int i = 0; i < 1000; i++) {
            PreparedStatement p = db.prepareStatement("SELECT Count(T.kuvaus) AS lkm"
                    + " FROM Tapahtumat T LEFT JOIN Paketit P ON T.paketti_id = P.id"
                    + " LEFT JOIN Asiakkaat A ON P.asiakas_id = A.id"
                    + " WHERE A.id=?");
            p.setInt(1, asiakas_id);
            asiakas_id++;

        }
    }

    public void haePaketinTapahtumienMaaraTest() throws SQLException {

        int paketti_id = 1;
        for (int i = 0; i < 1000; i++) {
            PreparedStatement p = db.prepareStatement("SELECT Count(*) AS lkm"
                    + " FROM Tapahtumat T LEFT JOIN Paketit P ON T.paketti_id = P.id "
                    + "WHERE P.id=?");
            p.setInt(1, paketti_id);
            paketti_id++;
        }
    }

    public void haePaikastaTapahtumienMaara(String paikka, String paivamaara) throws SQLException {
        System.out.println("");
        PreparedStatement p = db.prepareStatement("SELECT Count(*) AS lkm"
                + " FROM Tapahtumat T LEFT JOIN Paikat P ON T.paikka_id = P.id "
                + "WHERE P.paikka_nimi in (?) AND T.aika LIKE ?");
        p.setString(1, paikka);
        p.setString(2, "%" + paivamaara + "%");
        ResultSet r = p.executeQuery();
        while (r.next()) {
            System.out.println("Tapahtumien määrä: " + r.getInt("lkm"));
            System.out.println("");
        }
    }

    public void tehokkkuusTesti() throws SQLException {
        System.out.println("Aloitetaan tehokkuustesti");
        //TESTI 1.
        long alku1 = System.currentTimeMillis();
        luoPaikkaTesti();
        System.out.println("TESTI 1. Lisätään 1000 paikkaa, mikä kesti: " + (System.currentTimeMillis() - alku1) + "ms");

        //TESTI 2.
        long alku2 = System.currentTimeMillis();
        luoAsiakasTesti();
        System.out.println("TESTI 2. Lisätään 1000 asiakasta, mikä kesti: " + (System.currentTimeMillis() - alku2) + "ms");

        //TESTI 3.
        long alku3 = System.currentTimeMillis();
        luoPakettiTesti();
        System.out.println("TESTI 3. Lisätään 1000 pakettia, mikä kesti: " + (System.currentTimeMillis() - alku3) + "ms");

        //TESTI 4.
        long alku4 = System.currentTimeMillis();
        luoTapahtumaTesti();
        System.out.println("TESTI 4. Lisätään 1000000 tapahtumaan, mikä kesti: " + (System.currentTimeMillis() - alku4) + "ms");

        //TESTI 5.
        long alku5 = System.currentTimeMillis();
        haeAsiakkaanPaketitMaaraTest();
        System.out.println("TESTI 5. Suoritetaan tuhat kyselyä, joista jokaisessa haetaan jonkin asiakkaan pakettien määrä. Kestoaika: " + (System.currentTimeMillis() - alku5) + "ms");

        //TESTI 6.
        long alku6 = System.currentTimeMillis();
        haePaketinTapahtumienMaaraTest();
        System.out.println("TESTI 6. Suoritetaan tuhat kyselyä, joista jokaisessa haetaan jonkin paketin tapahtumien määrä. Kestoaika: " + (System.currentTimeMillis() - alku6) + "ms");

        //TESTI LOPPU...
        System.out.println("Kokonaisaika: " + (System.currentTimeMillis() - alku1) + "ms");
        System.out.println("Tehokkuustesti suoritettu!");
        System.out.println("");
    }
}
