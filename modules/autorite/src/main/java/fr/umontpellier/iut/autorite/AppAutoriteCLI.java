package fr.umontpellier.iut.autorite;

import fr.umontpellier.iut.autorite.commands.StartBidCommand;
import fr.umontpellier.iut.autorite.commands.StopBidCommand;
import fr.umontpellier.iut.commandesUtility.Command;
import fr.umontpellier.iut.commandesUtility.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppAutoriteCLI implements Runnable {

    Helper helper;

    AppAutorite app;

    public AppAutoriteCLI(AppAutorite app) {
        ArrayList<Command> commands = new ArrayList<>(List.of(new StartBidCommand(), new StopBidCommand()));
        this.helper = new Helper(commands);
        this.app = app;
    }

    @Override
    public void run() {
        helper.displayCommands();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("--> Entrez votre commande : ");

            try {
                routeCommand(scanner.nextLine());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.err.println("Erreur lors de l'exécution de la commande, veuillez réessayer.");
            }
        }
    }

    private void routeCommand(String command) throws IOException {
        switch (command) {
            case "lancer" -> {
                app.startAuction();
                System.out.println("Log (serveur): enchère démarré avec succès");
            }
            case "stop" -> {
                System.out.println("Log (serveur): enchère stoppé avec succès");
                app.stopAuction();
            }
            case "quit" -> {
                System.out.println("Log (serveur): programme terminé avec succès");
                app.cleanRessources();
                System.exit(0);
            }
            case "help" -> helper.displayCommands();
            default -> System.err.println("Commande inconnue, tapez 'help' pour afficher la liste des commandes");
        }
    }
}
