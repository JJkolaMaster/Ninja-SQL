package tietokanta2;

import java.sql.SQLException;
import java.util.Scanner;

public class Tietokanta2 {

    public static void main(String[] args) throws SQLException {

        System.out.println("");
        System.out.println("");
        System.out.println("         .B7         vB.         ");
        System.out.println("         .Bu         uB.         ");
        System.out.println("          B7         YQ          ");
        System.out.println("         .B7         vB          ");
        System.out.println("          B7         vB.         ");
        System.out.println("    :B    Qr    1B   LB          ");
        System.out.println("    .QB25BB     7BB2qBQ          ");
        System.out.println("      JRgv        Kgg:      Joni Jaakkola");
        System.out.println("");

        Scanner lukija = new Scanner(System.in);
        Rajapinta rajapinta = new Rajapinta();

        Kayttoliittyma liittyma = new Kayttoliittyma(lukija, rajapinta);
        liittyma.kaynnista();

    }
}
