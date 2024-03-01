/*
 * Cette classe est conçue pour être utilisée dans un système de surveillance avec des capteurs qui enregistrent des mesures de température et d'humidité.
 * Elle agit comme un serveur RMI central qui gère ces capteurs.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class  CentraleImpl extends UnicastRemoteObject implements Centrale
{
    private static HashMap<String, Capteur> capteurs;       // Collection pour stocker des capteurs associés à un code unique
    
    // Constructeur de la classe
    public CentraleImpl() throws RemoteException { 
        super();
        capteurs = new HashMap<String, Capteur>();
    }
    
    // Ajoute un capteur à la collection synchronisée
    public synchronized void ajouterCapteur(int intervalle) throws RemoteException, MalformedURLException, NotBoundException {
        Capteur capteur = new Capteur();
        
        capteurs.put(capteur.getCodeUnique(), capteur);
        System.out.println("Capteur " + capteur.getCodeUnique() + " de coordonnées " + capteurs.get(capteur.getCodeUnique()).getCoordonneesGPS() + " et d'intervalle " + intervalle + " ajouté.");
        try {
            capteur.demarrer(intervalle);
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }        
    }

    // Retirer un capteur de la collection synchronisée
    public synchronized void retirerCapteur(String codeUnique) throws RemoteException {
        capteurs.get(codeUnique).arreter(codeUnique);
        capteurs.remove(codeUnique);
        System.out.println("Capteur " + codeUnique + " retiré.");

    }

    // Lister les capteurs de la collection
    public String listerCapteurs() throws RemoteException {
        StringBuilder liste = new StringBuilder();
        liste.append("Liste des capteurs :\n");
    
        for (String codeUnique : capteurs.keySet()) {
            liste.append("- Capteur ").append(codeUnique).append("\n");
        }

        String message = liste.toString();
        return message;
    }

    // Enregistre et affiche la température et l'humidité depuis un capteur ainsi que l'heure du relevé
    public void enregistrerMesures(String codeUnique, int temperature, int humidite) throws RemoteException {
        LocalDateTime heureActuelle = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String mesure = heureActuelle.format(formatter) + " " + temperature + " " + humidite;
        String nomFichier = codeUnique + "_mesures.txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.write(mesure);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        System.out.println("\nLe capteur " + codeUnique + " a enregistré de nouvelles mesures :\n- Température = " + temperature + " ;\n- Humidité = " + humidite + " ;");
    }

    // Obtient la dernière mesure enregistrée dans le fichier
    public String obtenirDerniereMesure(String codeUnique) throws RemoteException {
        String nomFichier = codeUnique + "_mesures.txt";
        String message = "";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            String derniereMesure = null;
    
            // Lire le fichier ligne par ligne
            while ((ligne = reader.readLine()) != null) {
                derniereMesure = ligne;
            }
    
            if (derniereMesure != null) {
                message += "Dernière mesure pour le capteur " + codeUnique + " :\n" + derniereMesure;
            } else {
                message += "Aucune mesure enregistrée pour le capteur " + codeUnique + ".";
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return message;
    }

    // Obtient la moyenne des températures et humidités sur la dernière heure ou dernière journée pour un capteur
    public String obtenirMoyennesTendances(String codeUnique, String periode) throws RemoteException {
        String nomFichier = codeUnique + "_mesures.txt";
        LocalDateTime now = LocalDateTime.now();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            List<Integer> temperatures = new ArrayList<>();
            List<Integer> humidites = new ArrayList<>();

            while ((ligne = reader.readLine()) != null) {
                String[] elements = ligne.split(" ");
                LocalDateTime time = LocalDateTime.parse(elements[0] + " " + elements[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                int temperature = Integer.parseInt(elements[2]);
                int humidite = Integer.parseInt(elements[3]);

                if ("heure".equalsIgnoreCase(periode) && time.isAfter(now.minusHours(1))) {
                    temperatures.add(temperature);
                    humidites.add(humidite);
                } else if ("jour".equalsIgnoreCase(periode) && time.isAfter(now.minusDays(1))) {
                    temperatures.add(temperature);
                    humidites.add(humidite);
                }
            }

            if (!temperatures.isEmpty() && !humidites.isEmpty()) {
                // Calcul de la moyenne
                double moyenneTemperature = temperatures.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
                double moyenneHumidite = humidites.stream().mapToDouble(Integer::doubleValue).average().orElse(0);

                // Déterminer les tendances (à la hausse, à la baisse, stable)
                String tendanceTemperature = determinerTendance(temperatures);
                String tendanceHumidite = determinerTendance(humidites);

                return String.format("Moyenne sur la dernière %s :\n- Température : %.2f\n- Humidité : %.2f\nTendances :\n- Température : %s\n- Humidité : %s",
                        periode, moyenneTemperature, moyenneHumidite, tendanceTemperature, tendanceHumidite);
            } else {
                return "Aucune mesure disponible pour le capteur " + codeUnique + " dans la période spécifiée.";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier.";
        }
    }


    // Déterminer la tendance (hausse, baisse, stable) d'une liste de mesures
    public String determinerTendance(List<Integer> valeurs) throws RemoteException {
        int taille = valeurs.size();
        if (taille >= 2) {
            int dernier = valeurs.get(taille - 1);
            int avantDernier = valeurs.get(taille - 2);

            if (dernier > avantDernier) {
                return "À la hausse";
            } else if (dernier < avantDernier) {
                return "À la baisse";
            } else {
                return "Stable";
            }
        } else {
            return "Insuffisant de données pour déterminer la tendance.";
        }
    }

    public void modifierIntervalleCapteur(String codeUnique, int nouvelIntervalle) throws MalformedURLException, RemoteException, NotBoundException {
        Capteur capteur = capteurs.get(codeUnique);
        capteur.resetTimer(nouvelIntervalle);
    }

    public void modifierIntervalleGlobal(int nouvelIntervalle) throws MalformedURLException, RemoteException, NotBoundException {
        for (String codeUnique : capteurs.keySet()) {
            Capteur capteur = capteurs.get(codeUnique);
            capteur.resetTimer(nouvelIntervalle);
        }
    }

    public static void main(String args[]) throws RemoteException, AlreadyBoundException {   
        try {
            // Création d'une centrale
            CentraleImpl centrale = new CentraleImpl();

            // Création d'un registre RMI sur le port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Liaison de la centrale au registre RMI avec le nom "Centrale"
            registry.bind("Centrale", centrale);
        } catch(RemoteException re) {
            re.printStackTrace();
        }
    }
}