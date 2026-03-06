package org.itmo.cli;

import org.itmo.cli.commands.*;
import org.itmo.service.TaskService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleHandler {
    private final TaskService taskService;
    private final Scanner scanner;
    private final Map<String, Command> commands;
    private boolean isRunning;

    public ConsoleHandler() {
        this.taskService = new TaskService();
        this.scanner = new Scanner(System.in);
        this.commands = new HashMap<>();
        this.isRunning = true;

        register(new HelpCommand(taskService, commands.values()));
        register(new TaskAddCommand(taskService));
        register(new TaskListCommand(taskService));
        register(new TaskUpdateCommand(taskService));
        register(new TaskDeleteCommand(taskService));
        register(new CheckAddCommand(taskService));
        register(new CheckToggleCommand(taskService));
    }

    private void register(Command cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public void start() {
        System.out.println("Программа управления задачами запущена.");
        System.out.println("Введите 'help' для получения списка команд.");

        while (isRunning) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] args = input.split("\\s+");
            String commandName = args[0].toLowerCase();

            if (commandName.equals("exit")) {
                isRunning = false;
                System.out.println("Программа завершена.");
                continue; 
            }

            Command command = commands.get(commandName);

            try {
                if (command == null) {
                    throw new IllegalArgumentException("Ошибка: Неизвестная команда '" + commandName + "'");
                }

                command.validateArgs(args);
                command.execute(args, scanner);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
