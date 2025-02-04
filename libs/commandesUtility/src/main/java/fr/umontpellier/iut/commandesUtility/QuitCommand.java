package fr.umontpellier.iut.commandesUtility;

import fr.umontpellier.iut.commandesUtility.Command;

public class QuitCommand extends Command {

    @Override
    public void init() {
        commandName = "quit";
        description = "quitter le programme.";
        syntax = "quit";
    }
}
