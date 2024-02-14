package capteur;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import centrale.InterfaceCentrale;

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

    // Getters
    public String getCode() throws RemoteException {
        return code;
    }

    public double getLatitude() throws RemoteException {
        return latitude;
    }

    public double getLongitude() throws RemoteException {
        return longitude;
    }

    public float getTemperature() throws RemoteException {
        return temperature;
    }

    public float getHumidite() throws RemoteException {
        return humidite;
    }


    // Remonte une temperature a la central
    public void remonterTemperature(InterfaceCentrale centrale) throws RemoteException {
        this.temperature = genererTemperature();
        centrale.enregistrerTemperature(this.code, this.temperature);
    }

    // Remonte une humidite 
    public void remonterHumidite(InterfaceCentrale centrale) throws RemoteException {
        this.humidite = genererHumidite();
        centrale.enregistrerHumidite(this.code, this.humidite);
    }

    // Déclare un ajout auprès de la centrale
    public void declarerAjout(InterfaceCentrale centrale) throws RemoteException {
        centrale.ajouterCapteur(this);
    }

    // Déclare un retrait auprès de la centrale
    public void declarerRetrait(InterfaceCentrale centrale) throws RemoteException {
        centrale.retirerCapteur(this);
    }


    // Génère une température aléatoirement
    public float genererTemperature() {
        Random random = new Random();
        Float temperature =  -5 + random.nextFloat() * 30; // Température entre -5 et 30 degrés Celsius
        return temperature;
    }

    // Génère une humidité aléatoirement
    public float genererHumidite() {
        Random random = new Random();
        Float humidite = random.nextFloat() * 100; // Humidité entre 0% et 100%
        return humidite;
    }

}