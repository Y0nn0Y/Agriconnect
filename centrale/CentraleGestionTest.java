package centrale;

import capteur.Capteur;

public class CentraleGestionTest {
    public static void main(String[] args) {
        try {
            CentraleGestion centrale = new CentraleGestion();

            System.out.println("Centrale prête à recevoir les capteurs.");

            Capteur capteur1 = new Capteur("C1", 48.858844, 2.294350);
            capteur1.declarerAjout(centrale);

            // Boucle pour remonter des informations toutes les 5 secondes
            while (true) {
                capteur1.remonterTemperature(centrale);
                capteur1.remonterHumidite(centrale);
                Thread.sleep(5000); // Pause de 5 secondes
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
