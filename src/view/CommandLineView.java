package view;

import model.Account;
import java.util.ArrayList;
import java.util.Collections;

public class CommandLineView implements View {
    @Override
    public void showAccountCreationError() {
        System.out.println("An account with this name already exists.");
    }

    @Override
    public void showIncorrectPasswordError() {
        System.out.println("Incorrect password entered.");
    }

    @Override
    public void showNoSuchAccountError() {
        System.out.println("No account with this name exists.");
    }

    @Override
    public void showLeaderboard(ArrayList<Account> accounts) {
        Collections.sort(accounts);
        for (int i = 0; i < accounts.size(); i++)
            System.out.println((i + 1) + accounts.get(i).toString());
    }

    @Override
    public void showInvalidCommandError() {
        System.out.println("Invalid command entered.");
    }

    @Override
    public void showHelp(String[] commands) {
        for (String command : commands)
            System.out.println(command);
    }

    @Override
    public void promptPassword() {
        System.out.println("Please enter the password.");
    }

    @Override
    public void alertAccountCreation() {
        System.out.println("model.Account successfully created.");
    }

    @Override
    public void alertLogin() {
        System.out.println("Successfully logged into account.");
    }
}
