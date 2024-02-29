/*
 * Cette interface RMI définit les méthodes qui peuvent être appelées de manière distante.
 */

import java.rmi.*;

public interface Centrale extends Remote
{
    // Ajoute un capteur avec un code unique, une latitude et une longitude
    public void ajouterCapteur(String codeUnique, Double latitude, Double longitude) throws RemoteException;
    
    // Retire un capteur en utilisant son code unique
    public void retirerCapteur(String codeUnique) throws RemoteException;

    // Liste les capteurs enregistrés auprès de la Centrale
    public String listerCapteurs() throws RemoteException;

    // Enregistre les dernières mesures du capteur dans un fichier 
    public void enregistrerDansFichier(String codeUnique, String message) throws RemoteException;

    // Transmet des données (température et humidité) à partir d'un capteur identifié par son code unique
    public void transmettreDonnees(String codeUnique, int temperature, int humidite) throws RemoteException;
    
}