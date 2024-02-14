package centrale;

import capteur.InterfaceCapteur;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class CentraleGestion extends UnicastRemoteObject implements InterfaceCentrale {

    private Map<String, InterfaceCapteur> capteurs;

    public CentraleGestion() throws RemoteException {
        capteurs = new HashMap<>();
    }

    @Override
    public void ajouterCapteur(InterfaceCapteur capteur) throws RemoteException {
        capteurs.put(capteur.getCode(), capteur);
        System.out.println("Capteur " + capteur.getCode() + " ajouté à la centrale.");
    }

    @Override
    public void retirerCapteur(InterfaceCapteur capteur) throws RemoteException {
        capteurs.remove(capteur.getCode());
        System.out.println("Capteur " + capteur.getCode() + " retiré de la centrale.");
    }

    @Override
    public void enregistrerTemperature(String codeCapteur, float temperature) throws RemoteException {
        System.out.println("Nouvelle température enregistrée pour le capteur " + codeCapteur + ": " + temperature + " °C");
    }

    @Override
    public void enregistrerHumidite(String codeCapteur, float humidite) throws RemoteException {
        System.out.println("Nouvelle humidité enregistrée pour le capteur " + codeCapteur + ": " + humidite + "%");
    }

    // Fonction pour afficher les informations des capteurs
    public void afficherInformationsCapteurs() throws RemoteException {
        System.out.println("\n--- Informations des capteurs ---");
        for (InterfaceCapteur capteur : capteurs.values()) {
            System.out.println("Capteur " + capteur.getCode() +
                    " - Température: " + capteur.getTemperature() + " °C, Humidité: " + capteur.getHumidite() + "%");
        }
    }
}