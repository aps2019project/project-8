package menus;

import model.AI;
import model.Account;

public class Multiplayer extends Menu {
    private static final String[] selectingUserCommands = {
            "Select user [user name]",
    };

    private static final String[] nonSelectingUserCommands = {
            "Start multiplayer game [mode] [number of flags]"
    };

    private static final String[] gameModes = {
            "Kill opponent hero",
            "Hold the flag",
            "Collect the flags"
    };

    private static Account secondAccount = null;

    public static void help(boolean selectingUser) {
        if (selectingUser) {
            Account.getAccounts().forEach(o -> {
                if (o != account)
                    view.showAccount(o);
            });
            view.showHelp(selectingUserCommands);
        } else {
            view.showHelp(gameModes);
            view.showHelp(nonSelectingUserCommands);
        }
    }

    public static boolean selectUser(String name) {
        secondAccount = Account.getAccount(name);
        if (secondAccount == null || secondAccount == getAccount()) {
            view.showNoSuchAccountError();
            return false;
        }
        if (secondAccount.getMainDeck() == null) {
            view.showSecondPlayerHasNoMainDeckError();
            return false;
        }
        if (!secondAccount.getMainDeck().isValid()) {
            view.showSecondPlayerInvalidMainDeckError();
            return false;
        }
        view.alertSecondAccountSelection();
        return true;
    }

    public static Account getSecondAccount() {
        return secondAccount;
    }
}
