package fr.umontpellier.iut.autorite.commands;

import fr.umontpellier.iut.commandesUtility.Command;

public class StartBidCommand extends Command {
    @Override
    public void init() {
        commandName = "lancer";
        description = "lancer l'enchère.";
        syntax = "lancer";
    }
}
