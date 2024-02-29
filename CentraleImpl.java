/*
 * Cette classe est conçue pour être utilisée dans un système de surveillance avec des capteurs qui enregistrent des mesures de température et d'humidité.
 * Elle agit comme un serveur RMI central qui gère ces capteurs.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class  CentraleImpl extends UnicastRemoteObject implements Centrale
{
    // Collection pour stocker des capteurs associés à un code unique
    private static HashMap<String, Capteur> capteurs;
    
    // Constructeur de la classe
    public CentraleImpl() throws RemoteException
    { 
        super();
        capteurs = new HashMap<String, Capteur>();
    }
    
    // Ajoute un capteur à la collection synchronisée
    public synchronized void ajouterCapteur(String codeUnique, Double latitude, Double longitude) throws RemoteException
    {
        capteurs.put(codeUnique, new Capteur(codeUnique, latitude, longitude));
        System.out.println("Capteur " + codeUnique + " de coordonnées " + capteurs.get(codeUnique).getCoordonneesGPS() + " ajouté.");
    }

    // Retirer un capteur de la collection synchronisée
    public synchronized void retirerCapteur(String codeUnique) throws RemoteException
    {
        capteurs.remove(codeUnique);
        System.out.println("Capteur " + codeUnique + " retiré.");
    }

    // Lister les capteurs de la collection
    public String listerCapteurs() throws RemoteException {
        StringBuilder list = new StringBuilder();
        list.append("Liste des capteurs :\n");
    
        for (String codeUnique : capteurs.keySet()) {
            list.append("- Capteur ").append(codeUnique).append("\n");
        }
    
        list.append("\n");
        String result = list.toString();
        System.out.println(result); // Vous pouvez aussi imprimer si nécessaire
    
        return result;
    }

    public void enregistrerDansFichier(String codeUnique, String message) throws RemoteException
    {
        String nomFichier = codeUnique + "_mesures.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.write(message);
            writer.newLine();
            System.out.println("Mesures enregistrées dans le fichier: " + nomFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Transmettre la température et l'humidité depuis un capteur
    public void transmettreDonnees(String codeUnique, int temperature, int humidite) throws RemoteException
    {
        String message = "Le capteur " + codeUnique + " a enregistré de nouvelles mesures :\n- Température = " + temperature + " ;\n- Humidité = " + humidite + " ;";
        System.out.println(message);
        enregistrerDansFichier(codeUnique, message);
    }
}