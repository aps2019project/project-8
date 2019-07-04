package menus;

import model.AI;
import model.AccountUser;

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

    private static AccountUser secondAccount = null;

    public static void help(boolean selectingUser) {
        if (selectingUser) {
            AccountUser.getAccounts().forEach(o -> {
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
        secondAccount = AccountUser.getAccount(name);
        if (secondAccount == null || secondAccount == getAccount()) {
            view.showNoSuchAccountError();
            return false;
        }
        if (secondAccount.getData().getMainDeck() == null) {
            view.showSecondPlayerHasNoMainDeckError();
            return false;
        }
        if (!secondAccount.getData().getMainDeck().isValid()) {
            view.showSecondPlayerInvalidMainDeckError();
            return false;
        }
        view.alertSecondAccountSelection();
        return true;
    }

    public static AccountUser getSecondAccount() {
        return secondAccount;
    }
}
