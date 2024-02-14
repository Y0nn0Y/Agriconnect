package capteur;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import centrale.CentraleGestion;

public class Capteur extends UnicastRemoteObject implements InterfaceCapteur {

    // Déclaration variable
    private String code;
    private double latitude;
    private double longitude;
    private float temperature;
    private float humidite;

    // Constructeur (code et coordonnées)
    public Capteur(String code, double latitude, double longitude) throws RemoteException {
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

// Getters et setters
    public String getCode() {
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
    private float genererTemperature() {
        Random random = new Random();
        Float temperature =  -5 + random.nextFloat() * 30; // Température entre -5 et 30 degrés Celsius
        return temperature;
    }

    //Génère une humidité aléatoirement
    private float genererHumidite() {
        Random random = new Random();
        Float humidite = random.nextFloat() * 100; // Humidité entre 0% et 100%
        return humidite;
    }

    //Remonte une temperature a la central
    public void remonterTemperature(CentraleGestion centrale) throws RemoteException {
        this.temperature = genererTemperature();
        centrale.enregistrerTemperature(this.code, this.temperature);
    }

    //Remonte une humidite 
    public void remonterHumidite(CentraleGestion centrale) throws RemoteException {
        this.humidite = genererHumidite();
        centrale.enregistrerHumidite(this.code, this.humidite);
    }

    //Daclare un ajout auprès de la centrale
    public void declarerAjout(CentraleGestion centrale) throws RemoteException {
        centrale.ajouterCapteur(this);
        System.out.println("\nCapteur " + code + " ajouté avec succès.");
    }

    //Declare un retrait auprès de la centrale
    public void declarerRetrait(CentraleGestion centrale) throws RemoteException {
        centrale.retirerCapteur(this);
        System.out.println("\nCapteur " + code + " retiré avec succès.");
    }

}