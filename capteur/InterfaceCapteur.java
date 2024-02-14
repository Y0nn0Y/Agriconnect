package capteur;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCapteur extends Remote {
    void remonterTemperature(float temperature) throws RemoteException;
    void remonterHumidite(float humidite) throws RemoteException;
    void declarerAjout() throws RemoteException;
    void declarerRetrait() throws RemoteException;
}
