package view;

import model.Account;
import java.util.ArrayList;

public interface View {
    void showAccountCreationError();

    void showNoSuchAccountError();

    void showIncorrectPasswordError();

    void showLeaderboard(ArrayList<Account> accounts);

    void showInvalidCommandError();

    void showHelp(String[] commands);

    void promptPassword();

    void alertAccountCreation();

    void alertLogin();
}
