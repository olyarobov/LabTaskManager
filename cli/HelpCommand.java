package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.service.TaskService;
import java.util.Collection;
import java.util.Scanner;

public class HelpCommand extends Command {
    private final Collection<Command> allCommands;

    public HelpCommand(TaskService taskService, Collection<Command> allCommands) {
        super(taskService, "help", "Вывести список всех доступных команд", 1);
        this.allCommands = allCommands;
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.println("=== Список доступных команд ===");
      
        for (Command cmd : allCommands) {
            System.out.println(cmd.getName() + " \t - " + cmd.getDescription());
        }
    }
}
