import java.util.Random;

public class Capteur {

// Déclaration variable
    private String code;
    private double latitude;
    private double longitude;
    private float temperature;
    private float humidite;

// Constructeur (code et coordonnées)
    public Capteur(String code, double latitude, double longitude) {
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

// Getters et setters
    public String getcode() {
        return code;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidite() {
        return humidite;
    }

// Fonctions

    //Génère une température aléatoirement
    private float genereTemperature() {
        Random random = new Random();
        return -5 + random.nextFloat() * 30; // Température entre -5 et 30 degrés Celsius
    }

    //Génère une humidité aléatoirement
    private float genereHumidite() {
        Random random = new Random();
        return random.nextFloat() * 100; // Humidité entre 0% et 100%
    }

    //Remonte une temperature a la central
    public void remonterTemperature(CentraleGestion centrale) {
        this.temperature = genereTemperature();
        centrale.enregistrerTemperature(this.code, this.temperature);
    }

    //Remonte une humidite 
    public void remonterHumidite(CentraleGestion centrale) {
        this.humidite = genereHumidite();
        centrale.enregistrerHumidite(this.code, this.humidite);
    }

    //Daclare un ajout auprès de la centrale
    public void declarerAjout(CentraleGestion centrale) {
        centrale.ajoutCapteur(this.code);
    }

    //Declare un retrait auprès de la centrale
    public void declarerRetrait(CentraleGestion centrale) {
        centrale.retirerCapteur(this.code);
    }

}