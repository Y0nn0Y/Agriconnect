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

/**
 * Cette classe représente l'implémentation d'une centrale météorologique utilisant RMI (Remote Method Invocation).
 * Elle utilise la bibliothèque RMI pour permettre la communication avec les capteurs météorologiques distants.
 *
 * @author Enzo Soulan
 * @author Yon Beaurain
 * @version 2.0
 * @since 2024-03-01
 */
public class  CentraleImpl extends UnicastRemoteObject implements Centrale
{
    // Collection HashMap stockant les capteurs associés à leurs codes uniques.
    private static HashMap<String, Capteur> capteurs;
    
    /**
     * Constructeur par défaut de la classe CentraleImpl.
     *
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
    public CentraleImpl() throws RemoteException { 
        super();
        capteurs = new HashMap<String, Capteur>();
    }
    
    /**
     * Ajoute un nouveau capteur à la centrale avec l'intervalle spécifié.
     *
     * @param intervalle Intervalle de mesure du capteur.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     * @throws MalformedURLException En cas d'URL mal formée lors de la communication avec le capteur.
     * @throws NotBoundException Lorsque le capteur distant n'est pas lié correctement.
     */
    public synchronized void ajouterCapteur(int intervalle) throws RemoteException, MalformedURLException, NotBoundException {
        Capteur capteur = new Capteur();
        
        capteurs.put(capteur.getCodeUnique(), capteur);
        System.out.println("\nCapteur " + capteur.getCodeUnique() + " de coordonnées " + capteurs.get(capteur.getCodeUnique()).getCoordonneesGPS() + " et d'intervalle " + intervalle + " ajouté.");
        try {
            capteur.demarrer(intervalle);
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }        
    }

    /**
     * Retire le capteur associé au code unique spécifié.
     *
     * @param codeUnique Code unique du capteur à retirer.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
    public synchronized void retirerCapteur(String codeUnique) throws RemoteException {
        capteurs.get(codeUnique).arreter(codeUnique);
        capteurs.remove(codeUnique);
        System.out.println("\nCapteur " + codeUnique + " retiré.");

    }

    /**
     * Liste tous les codes uniques des capteurs enregistrés dans la centrale.
     *
     * @return Liste des codes uniques des capteurs.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
    public String listerCapteurs() throws RemoteException {
        StringBuilder liste = new StringBuilder();
        liste.append("\nListe des capteurs :");
    
        for (String codeUnique : capteurs.keySet()) {
            liste.append("\n- Capteur ").append(codeUnique);
        }

        String message = liste.toString();
        return message;
    }

    /**
     * Enregistre les mesures de température et d'humidité pour un capteur spécifié.
     *
     * @param codeUnique Code unique du capteur.
     * @param temperature Température enregistrée.
     * @param humidite Humidité enregistrée.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
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

    /**
     * Récupère la dernière mesure enregistrée pour un capteur spécifié.
     *
     * @param codeUnique Code unique du capteur.
     * @return Message contenant la dernière mesure ou une notification d'absence de mesures.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
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
                message += "\nDernière mesure pour le capteur " + codeUnique + " :\n" + derniereMesure;
            } else {
                message += "\nAucune mesure enregistrée pour le capteur " + codeUnique + ".";
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return message;
    }

    /**
     * Calcule et retourne les moyennes de température et d'humidité ainsi que les tendances sur une période spécifiée.
     *
     * @param codeUnique Code unique du capteur.
     * @param periode     Période de calcul des moyennes et tendances ("heure" ou "jour").
     * @return Message contenant les moyennes, tendances et informations sur la période spécifiée.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
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
                } else if ("journée".equalsIgnoreCase(periode) && time.isAfter(now.minusDays(1))) {
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

                return String.format("\nMoyennes sur la dernière %s :\n- Température : %.2f\n- Humidité : %.2f\n\nTendances sur la dernière %s :\n- Température : %s\n- Humidité : %s",
                        periode, moyenneTemperature, moyenneHumidite, periode, tendanceTemperature, tendanceHumidite);
            } else {
                return "\nAucune mesure disponible pour le capteur " + codeUnique + " dans la période spécifiée.";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "\nLe fichier est introuvable.";
        }
    }


    /**
     * Détermine la tendance des valeurs fournies (à la hausse, à la baisse ou stable).
     *
     * @param valeurs Liste des valeurs pour lesquelles la tendance doit être déterminée.
     * @return Chaîne indiquant la tendance des valeurs.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
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
            return "\nInsuffisant de données pour déterminer la tendance.";
        }
    }

    /**
     * Modifie l'intervalle de mesure d'un capteur spécifié.
     *
     * @param codeUnique Code unique du capteur.
     * @param nouvelIntervalle Nouvel intervalle de mesure.
     * @throws MalformedURLException En cas d'URL mal formée lors de la communication avec le capteur.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     * @throws NotBoundException Lorsque le capteur distant n'est pas lié correctement.
     */
    public void modifierIntervalleCapteur(String codeUnique, int nouvelIntervalle) throws MalformedURLException, RemoteException, NotBoundException {
        Capteur capteur = capteurs.get(codeUnique);
        capteur.resetTimer(nouvelIntervalle);
    }

    /**
     * Modifie l'intervalle de mesure de tous les capteurs enregistrés dans la centrale.
     *
     * @param nouvelIntervalle Nouvel intervalle de mesure global.
     * @throws MalformedURLException En cas d'URL mal formée lors de la communication avec le capteur.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     * @throws NotBoundException Lorsque le capteur distant n'est pas lié correctement.
     */
    public void modifierIntervalleGlobal(int nouvelIntervalle) throws MalformedURLException, RemoteException, NotBoundException {
        for (String codeUnique : capteurs.keySet()) {
            Capteur capteur = capteurs.get(codeUnique);
            capteur.resetTimer(nouvelIntervalle);
        }
    }

    /**
     * Méthode principale exécutée lors du lancement de l'application.
     * Créé une centrale et un registre sur le port 1099, puis relie la centrale au registre.
     *
     * @param args Arguments de la ligne de commande.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     * @throws AlreadyBoundException Lorsque la centrale est déjà liée au registre RMI.
     */
    public static void main(String args[]) throws RemoteException, AlreadyBoundException {   
        try {
            CentraleImpl centrale = new CentraleImpl();
            Registry registre = LocateRegistry.createRegistry(1099);
            registre.bind("Centrale", centrale);
        } catch(RemoteException re) {
            re.printStackTrace();
        }
    }
}