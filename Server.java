/*
 * Ce code crée un serveur RMI qui expose l'objet CentraleImpl au registre RMI avec le nom "Centrale".
 */

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server
{
    public static void main(String args[]) throws RemoteException, AlreadyBoundException
    {   
        try
        {
            // Création d'une centrale
            CentraleImpl centrale = new CentraleImpl();

            // Création d'un registre RMI sur le port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Liaison de la centrale au registre RMI avec le nom "Centrale"
            registry.bind("Centrale", centrale);
        }
        catch(RemoteException re) 
        {
            re.printStackTrace();
        }
    }
}
