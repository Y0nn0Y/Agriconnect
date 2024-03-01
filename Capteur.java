/*
 * Cette classe simule le comportement d'un Capteur en générant des valeurs aléatoires pour ses attributs,
 * l'ajoute à une instance de l'interface Centrale récupérée à partir du registre RMI,
 * puis simule la transmission périodique de données de température et d'humidité à la Cerntrale à l'aide d'une tâche de minuterie.
 * Après dix transmissions, le capteur est retiré de la Centrale.
 */

import java.rmi.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.net.*;

public class Capteur
{
    private String codeUnique;                      // Code identifiant le capteur
    private Double latitude;                        // Latitude du capteur
    private Double longitude;                       // Longitude du capteur
    private static Random random = new Random();    // Génération aléatoire de coordonnées

    // Constructeur de la classe
    public Capteur(String codeUnique, Double latitude, Double longitude)
    {
        this.codeUnique = codeUnique;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter sur le code identifiant le capteur
    public String getCodeUnique()
    {
        return codeUnique;
    }

    // Getter sur le code identifiant le capteur
    public String getCoordonneesGPS()
    {
        return latitude + ", " + longitude;
    }

    // Génère un entier compris entre min et max (pour les coordonnées)
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

    public static void main(String args[])
    {
        try
        {
            // Récupération de la référence de l'objet distant centrale
            Centrale centrale = (Centrale) Naming.lookup("rmi://localhost/Centrale");

            // Génération aléatoire des attributs du capteur
            String ID = generateCodeUnique();
            Double lat = generateRandomCoordinate();
            Double lon = generateRandomCoordinate();
            
            // Ajout du capteur à la centrale
            centrale.ajouterCapteur(ID, lat, lon);

            // // Création d'une tâche de minuterie pour simuler la transmission de données à intervalle réguliers
            Timer timer = new Timer();
            
            // Limiter le nombre d'exécutions de la transmission des données (afin de retirer le capteur à la fin)
            final int[] executionCount = {0};

            TimerTask task = new TimerTask()
            {
                public void run()
                {
                    // Génération de valeurs aléatoires pour la température et l'humidité
                    int temperature = generateRandomValue(20, 30);
                    int humidite = generateRandomValue(40, 60);
                    
                    try {
                        // Transmission des données à la centrale
                        centrale.enregistrerMesures(ID, temperature, humidite);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    executionCount[0]++;
                    if(executionCount[0] >= 10)
                    {
                        // // Arrêt de la tâche après dix exécutions et retrait du capteur de la centrale
                        timer.cancel();
                        try
                        {
                            centrale.retirerCapteur(ID);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            // Planification de la tâche pour être exécutée toutes les 5 secondes (5000 millisecondes)
            timer.schedule(task, 0, 5000);
        }
        catch(RemoteException re)
        {
            re.printStackTrace();
        }
        catch(NotBoundException nbe)
        {
            nbe.printStackTrace();
        }
        catch(MalformedURLException mfe)
        {
            mfe.printStackTrace();
        }
    }
}
