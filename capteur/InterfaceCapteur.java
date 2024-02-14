package capteur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import centrale.InterfaceCentrale;

public interface InterfaceCapteur extends Remote {

    String getCode() throws RemoteException;;

    double getLatitude() throws RemoteException;;

    double getLongitude() throws RemoteException;;

    float getTemperature() throws RemoteException;;
    
    float getHumidite() throws RemoteException;;


    void remonterTemperature(InterfaceCentrale centrale) throws RemoteException;

    void remonterHumidite(InterfaceCentrale centrale) throws RemoteException;

    void declarerAjout(InterfaceCentrale centrale) throws RemoteException;

    void declarerRetrait(InterfaceCentrale centrale) throws RemoteException;


    float genererTemperature() throws RemoteException;;

    float genererHumidite() throws RemoteException;;
}
