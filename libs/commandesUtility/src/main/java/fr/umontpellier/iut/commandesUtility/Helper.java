package fr.umontpellier.iut.commandesUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class Helper {
    private final ArrayList<Command> commands = new ArrayList<>(Arrays.asList(
            new HelpCommand(),
            new QuitCommand()
    ));

    public Helper(ArrayList<Command> commands){
        this.commands.addAll(commands);
    }

    public void displayCommands(){
        for(Command c:commands){
            System.out.println(c);
        }
    }
}
