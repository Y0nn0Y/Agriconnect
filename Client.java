import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String args[])
    {
        try
        {
            // Récupération de la référence de l'objet distant Centrale
            Centrale centrale = (Centrale) Naming.lookup("rmi://localhost/Centrale");
            
            // Lister les capteurs
            System.out.println(centrale.listerCapteurs());
            
            // Récupération des dernières informations d'un capteur
            
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
