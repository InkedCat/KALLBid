package fr.umontpellier.iut.autorite.commands;

import fr.umontpellier.iut.commandesUtility.Command;

public class StopBidCommand extends Command {
    @Override
    public void init() {
        commandName = "stop";
        description = "stopper l'enchère.";
        syntax = "stop";
    }
}
