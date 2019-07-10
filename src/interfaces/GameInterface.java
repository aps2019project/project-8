package interfaces;

import com.gilecode.yagson.com.google.gson.JsonObject;
import menus.Menu;
import model.AccountUser;
import model.Game;
import model.GameType;
import view.CommandLineView;

import java.util.HashMap;
import java.util.Map;

public class GameInterface {

    private HashMap<AccountUser, Game> games;

    public GameInterface() {
        games = new HashMap<>();
    }

    public String checkAccount(AccountUser accountUser) {
        if (accountUser.getData().getMainDeck() == null) {
            return "you don't have a main deck";
        }
        if (!accountUser.getData().getMainDeck().isValid()) {
            return "you don't have a valid main deck";
        }
        return "ok";
    }

    private String checkGameParameters(int mode, int numberOfFlags) {
        if (mode < 1 || mode > 3) {
            return "no such game mode";
        }
        if (mode == 3 && numberOfFlags < 1) {
            return "invalid parameters";
        }
        if (mode != 3 && numberOfFlags != -1) {
            return "invalid parameters";
        }
        return "ok";
    }

    public Game startGame(AccountUser a, AccountUser b, int mode, int numberOfFlags) {

        // guaranteed that a and b have a valid main deck

//        String checkA = checkAccount(a);
//        String checkB = checkAccount(b);
//
//        if (!checkA.equals("you have a valid main deck to play") || !checkB.equals("you have a valid main deck to play"))
//            return "one account doesn't have a valid main deck";
//

//        if (!checkGameParameters(mode, numberOfFlags).equals("ok"))
//            return "invalid parameters";



        Game game = new Game(a, b, GameType.get(mode), numberOfFlags);
        game.initiateGame();
        games.put(a, game);
        games.put(b, game);
        game.setView(new CommandLineView());
//        System.err.println("game created successfully for " + a.getName() + " " + b.getName());
//        hasAI = false;
//        GraveyardMenu.setGame(game);
        return game;
    }

    public String sendCommand(AccountUser accountUser, String command) {
        Game game = games.get(accountUser);
        if (game == null)
            return "you are not in a game";
        ((CommandLineView) game.getView()).clean();
//        boolean reverse = false;
//        if (!game.accounts[game.turn % 2].equals(accountUser))
//            reverse = true;
//        if (reverse)
//            game.turn++;
//            game.swap();
        game.parse(command, accountUser);
//        if (reverse)
//            game.turn--;
//            game.swap();
        return ((CommandLineView) game.getView()).getMessages();
    }

    public String inGame(AccountUser accountUser) {
//        System.err.println("asking game query for " + accountUser.getName() + " " + games.get(accountUser));

        return games.get(accountUser) != null ? "yes" : "no";
    }


    public void getGameInfo(AccountUser accountUser, JsonObject jsonObject) {
        Game game = games.get(accountUser);
        jsonObject.addProperty("player0", game.players[0].getName());
        jsonObject.addProperty("player1", game.players[1].getName());
        jsonObject.addProperty("currentPlayer", game.getCurrentPlayer().getName());
    }

    public Game getGame(AccountUser accountUser) {
        return games.get(accountUser);
    }

    public void diconnect(AccountUser accountUser) {
        Game game = games.get(accountUser);
        if (game == null)
            return;
        for (int i = 0; i < 2; i++)
            games.remove(game.accounts[i]);
    }

    public String showGame(String input) {
        for (HashMap.Entry<AccountUser, Game> entry : games.entrySet()) {
            Game game = entry.getValue();
            if (((String) (game.accounts[0].getName() + " " +  game.accounts[1].getName())).equals(input)) {
                ((CommandLineView) game.getView()).clean();
                game.parse("shengdebao", new AccountUser("***", "***"));
                return ((CommandLineView) game.getView()).getMessages();
            }
        }
        return "no valid input";
    }

    public String[] getGames() {
        String[] ans = new String[games.size()];
        int i = 0;
        for (HashMap.Entry<AccountUser, Game> entry : games.entrySet()) {
            Game game = entry.getValue();
            ans[i] = game.accounts[0].getName() + " " + game.accounts[1].getName();
        }
        return ans;
    }

}
