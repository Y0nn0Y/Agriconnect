package capteur;

import java.rmi.RemoteException;
import centrale.CentraleGestion;

public class CapteurTest {

    public static void main(String[] args) throws RemoteException {
        // Création d'une instance de CentraleGestion (pour simuler la centrale)
        CentraleGestion centrale = new CentraleGestion();

        // Création d'un capteur avec un code et des coordonnées GPS
        Capteur capteur = new Capteur("ABC123", 69.666, 2.3522);

        // Déclarer l'ajout du capteur à la centrale
        capteur.declarerAjout(centrale);

        // Affichage des informations du capteur avant remontée des données
        System.out.println("Affichage des données AVANT remontée des données");
        afficherInformationsCapteur(capteur);

        // Remonter une température et une humidité à la centrale
        capteur.remonterTemperature(centrale);
        capteur.remonterHumidite(centrale);

        // Affichage des informations du capteur après le retrait
        System.out.println("Affichage des données APRES remontée des données");
        afficherInformationsCapteur(capteur);

        // Déclarer le retrait du capteur de la centrale
        capteur.declarerRetrait(centrale);
    }

    // Méthode pour afficher les tester affichage d'un capteur
    private static void afficherInformationsCapteur(Capteur capteur) {
        System.out.println("Code du capteur: " + capteur.getCode());
        System.out.println("Latitude: " + capteur.getLatitude());
        System.out.println("Longitude: " + capteur.getLongitude());
        System.out.println("Température: " + capteur.getTemperature() + " °C");
        System.out.println("Humidité: " + capteur.getHumidite() + " %");
    }
}