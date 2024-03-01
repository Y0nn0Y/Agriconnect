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
                System.out.println("╔═══════════════════════════════╗");
                System.out.println("║          AgriConnect          ║");
                System.out.println("╚═══════════════════════════════╝");
                System.out.println("\n1. Lister les capteurs");
                System.out.println("2. Obtenir la dernière mesure d'un capteur");
                System.out.println("3. Obtenir la moyenne et les tendances pour la dernière heure ou la dernière journée");
                System.out.println("4. Ajouter un capteur");
                System.out.println("5. Retirer un capteur");
                System.out.println("6. Modifier l'intervalle de mesures d'un ou plusieurs capteurs");
                System.out.println("0. Quitter");
                System.out.print("\nChoisissez une option : ");

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
                        String codeUnique = scanner.nextLine();
                        System.out.println(centrale.obtenirDerniereMesure(codeUnique));
                        break;
                    case 3:
                        // Obtenir la moyenne et les tendances pour la dernière heure ou la dernière journée
                        System.out.print("Entrez le code du capteur : ");
                        codeUnique = scanner.nextLine();
                        System.out.print("Entrez la période (heure/jour) : ");
                        String periode = scanner.nextLine();
                        System.out.println(centrale.obtenirMoyennesTendances(codeUnique, periode));
                        break;
                    case 4:
                        // Ajouter un capteur
                        System.out.print("Entrez l'intervalle de mesure du capteur (ms) : ");
                        int intervalle = scanner.nextInt();
                        centrale.ajouterCapteur(intervalle);
                        break;
                    case 5:
                        // Retirer un capteur
                        System.out.print("Entrez le code du capteur : ");
                        codeUnique = scanner.nextLine();
                        centrale.retirerCapteur(codeUnique);
                        break;
                    case 6:
                        // Modifier intervalle de mesure d'un ou plusieurs capteurs
                        System.out.println("1. Définir l'intervalle global\n2. Définir l'intervalle d'un capteur\n");
                        System.out.print("Choisissez une option (global/capteur) : ");
                        String typeIntervalle = scanner.nextLine();
                        switch (typeIntervalle) {
                            case "global":
                                System.out.println("Entrez l'intervalle global : ");
                                intervalle = scanner.nextInt();
                                centrale.modifierIntervalleGlobal(intervalle);
                                break;
                            case "capteur":
                                System.out.println("Entrez le code du capteur : ");
                                codeUnique = scanner.nextLine();
                                System.out.println("Entrez l'intervalle du capteur : ");
                                intervalle = scanner.nextInt();
                                centrale.modifierIntervalleCapteur(codeUnique, intervalle);
                                break;
                        }
                        break;
                    case 0:
                        System.out.println("Au revoir !");
                        return;
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
