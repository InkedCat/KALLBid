package fr.umontpellier.iut.commandesUtility;

import java.util.Objects;

public abstract class  Command {
    protected String commandName;
    protected String description;
    protected String syntax;

    public Command(){
        init();
    }

    public abstract void init();
    public boolean isCommandValid(String cmd){
        return Objects.equals(cmd,commandName);
    }

    @Override
    public String toString() {
        return commandName + " : " + description + "\n" + "syntaxe : " + syntax + "\n";
    }
}
