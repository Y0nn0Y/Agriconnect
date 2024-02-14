import java.util.ArrayList;
import java.util.List;

public class CentraleGestion {
    private List<Capteur> capteurs;
    private int intervalleMesure; // en secondes

    // Constructeur
    public CentraleGestion() {
        this.capteurs = new ArrayList<>();
        this.intervalleMesure = intervalleMesure;
    }

    // Méthode pour ajouter un capteur à la liste
    public void ajouterCapteur(Capteur capteur) {
        capteurs.add(capteur);
    }

    // Méthode pour retirer un capteur de la liste
    public void retirerCapteur(Capteur capteur) {
        capteurs.remove(capteur);
    }

    // Méthode pour enregistrer la température d'un capteur
    public void enregistrerTemperature(String codeCapteur, double temperature) {
        return;
    }

    // Méthode pour enregistrer l'humidité d'un capteur
    public void enregistrerHumidite(String codeCapteur, double humidite) {
        return;
    }

    // Méthode pour afficher les informations de tous les capteurs
    public void afficherInformations() {
        for (Capteur capteur : capteurs) {
            System.out.println("Capteur: " + capteur.getCode() + ", Coordonnées latitude: " + capteur.getLatitude() + ", Coordonnées longitude: " + capteur.getLongitude());
            System.out.println("Température: " + capteur.getTemperature() + " °C, Humidité: " + capteur.getHumidite() + "%");
        }
    }
}
