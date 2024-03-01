import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Récupération de la référence de l'objet distant Centrale
            Centrale centrale = (Centrale) Naming.lookup("rmi://localhost/Centrale");

            // Menu interactif
            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Lister les capteurs");
                System.out.println("2. Obtenir la dernière mesure d'un capteur");
                System.out.println("3. Obtenir la moyenne et les tendances pour la dernière heure ou la dernière journée");
                System.out.println("0. Quitter");
                System.out.print("Choisissez une option : ");

                int choix = scanner.nextInt();
                scanner.nextLine();  // Consommer la nouvelle ligne après la saisie du nombre

                switch (choix) {
                    case 1:
                        // Lister les capteurs
                        System.out.println(centrale.listerCapteurs());
                        break;
                    case 2:
                        // Obtenir la dernière mesure d'un capteur
                        System.out.print("Entrez le code du capteur : ");
                        String codeCapteur = scanner.nextLine();
                        System.out.println(centrale.obtenirDerniereMesure(codeCapteur));
                        break;
                    case 3:
                        // Obtenir la moyenne et les tendances pour la dernière heure ou la dernière journée
                        System.out.print("Entrez le code du capteur : ");
                        String codeCapteurMoyenne = scanner.nextLine();
                        System.out.print("Entrez la période (heure/jour) : ");
                        String periode = scanner.nextLine();
                        System.out.println(centrale.obtenirMoyennesTendances(codeCapteurMoyenne, periode));
                        break;
                    case 0:
                        System.out.println("Au revoir !");
                        return; // Sortir de la méthode main plutôt que d'utiliser System.exit
                    default:
                        System.out.println("Option invalide. Veuillez choisir une option valide.");
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        } finally {
            // Fermer le scanner dans le bloc finally pour s'assurer qu'il est fermé, même en cas d'exception
            scanner.close();
        }
    }
}
