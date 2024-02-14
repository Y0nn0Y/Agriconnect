package centrale;

import capteur.InterfaceCapteur;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCentrale extends Remote {

    void ajouterCapteur(InterfaceCapteur capteur) throws RemoteException;

    void retirerCapteur(InterfaceCapteur capteur) throws RemoteException;

    void enregistrerTemperature(String codeCapteur, float temperature) throws RemoteException;

    void enregistrerHumidite(String codeCapteur, float humidite) throws RemoteException;

}
