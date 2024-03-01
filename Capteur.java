/*
 * Cette classe simule le comportement d'un Capteur en générant des valeurs aléatoires pour ses attributs,
 * l'ajoute à une instance de l'interface Centrale récupérée à partir du registre RMI,
 * puis simule la transmission périodique de données de température et d'humidité à la Cerntrale à l'aide d'une tâche de minuterie.
 * Après dix transmissions, le capteur est retiré de la Centrale.
 */

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Capteur {
    private Centrale centrale;
    private String codeUnique;                          // Code identifiant le capteur
    private Double latitude;                            // Latitude du capteur
    private Double longitude;                           // Longitude du capteur
    private Timer timer;                                // Création d'une tâche de minuterie pour simuler l'enregistrement de données à intervalles réguliers
    private static Random random = new Random();        // Génération aléatoire de coordonnées
    
    // Constructeur de la classe
    public Capteur() throws RemoteException, NotBoundException, MalformedURLException {
        // Récupération de la référence de l'objet distant Centrale
        this.centrale = (Centrale) Naming.lookup("rmi://localhost/Centrale");
        // Génération aléatoire des attributs du capteur
        this.codeUnique = generateCodeUnique();
        this.latitude = generateRandomCoordinate();
        this. longitude = generateRandomCoordinate();
        this.timer = new Timer();
    }

    // Getter sur le code identifiant le capteur
    public String getCodeUnique() {
        return codeUnique;
    }

    // Getter sur le code identifiant le capteur
    public String getCoordonneesGPS() {
        return latitude + ", " + longitude;
    }

    // Reset le timer du capteur
    public void resetTimer(int nouvelIntervalle) throws MalformedURLException, RemoteException, NotBoundException {
        timer.cancel();                 // Annulation de la tâche actuelle
        timer = new Timer();            // Création d'un nouveau Timer et planification de la tâche avec le nouvel intervalle
        demarrer(nouvelIntervalle);
    }

    // Génère un entier compris entre min et max (pour la température et l'humidité)
    private static int generateRandomValue(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    // Génère une chaine de caractères unique basée sur la notation UNIX du temps
    private static String generateCodeUnique() {
        return Long.toString(System.currentTimeMillis());
    }

    // Génère des coordonées GPS aléatoires
    private static double generateRandomCoordinate() {
        return -90 + (180 * random.nextDouble());
    }

    public void demarrer(int intervalle) throws MalformedURLException, RemoteException, NotBoundException {
        TimerTask task = new TimerTask() {
            public void run() {
                // Génération de valeurs aléatoires pour la température et l'humidité
                int temperature = generateRandomValue(20, 30);
                int humidite = generateRandomValue(40, 60);
                try {
                    centrale.enregistrerMesures(codeUnique, temperature, humidite);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        // Planification de la tâche pour être exécutée à intervalle définie par le client
        timer.schedule(task, 0, intervalle);
    }

    public void arreter(String ID) throws RemoteException {
        timer.cancel();
    }
}
