/*
 * Cette interface RMI définit les méthodes de la centrale qui peuvent être appelées de manière distante.
 */

import java.rmi.*;
import java.util.List;

public interface Centrale extends Remote
{
    // Ajoute un capteur avec un code unique, une latitude et une longitude
    public void ajouterCapteur(String codeUnique, Double latitude, Double longitude) throws RemoteException;
    
    // Retire un capteur en utilisant son code unique
    public void retirerCapteur(String codeUnique) throws RemoteException;

    // Liste les capteurs enregistrés auprès de la centrale
    public String listerCapteurs() throws RemoteException;

    // Transmet des données (température et humidité) à partir d'un capteur identifié par son code unique
    public void enregistrerMesures(String codeUnique, int temperature, int humidite) throws RemoteException;

    // Obtient la dernière mesure enregistrée dans le fichier
    public String obtenirDerniereMesure(String codeUnique) throws RemoteException;

    // Calculer la moyenne des températures et humidités pour la dernière heure ou la dernière journée
    public String obtenirMoyennesTendances(String codeUnique, String periode) throws RemoteException;

    // Déterminer la tendance (hausse, baisse, stable) d'une liste de mesures
    public String determinerTendance(List<Integer> mesures) throws RemoteException;
    
}