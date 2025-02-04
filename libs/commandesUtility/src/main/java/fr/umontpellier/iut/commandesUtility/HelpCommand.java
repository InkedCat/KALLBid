package fr.umontpellier.iut.commandesUtility;

import fr.umontpellier.iut.commandesUtility.Command;

public class HelpCommand extends Command {
    @Override
    public void init() {
        commandName = "help";
        description = "afficher les commandes et leur syntaxe.";
        syntax = "help";
    }
}
