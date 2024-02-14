public class CapteurTest {

    public static void main(String[] args) {
        // Création d'une instance de CentraleGestion (pour simuler la centrale)
        CentraleGestion centrale = new CentraleGestion();

        // Création d'un capteur avec un code et des coordonnées GPS
        Capteur capteur = new Capteur("ABC123", 69.666, 2.3522);

        // Remonter une température et une humidité à la centrale
        capteur.remonterTemperature(centrale);
        capteur.remonterHumidite(centrale);

        // Affichage des informations du capteur après remontée des données
        afficherInformationsCapteur(capteur);
    }

    // Méthode pour afficher les informations d'un capteur
    private static void afficherInformationsCapteur(Capteur capteur) {
        System.out.println("Code du capteur: " + capteur.getcode());
        System.out.println("Latitude: " + capteur.getLatitude());
        System.out.println("Longitude: " + capteur.getLongitude());
        System.out.println("Température: " + capteur.getTemperature() + " °C");
        System.out.println("Humidité: " + capteur.getHumidite() + " %");
    }
}