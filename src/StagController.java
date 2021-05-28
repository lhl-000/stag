import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author lee
 * @create 2021-05-18 17:07
 */
public class StagController {
    private ArrayList<Location> locations;
    private HashMap<String, ArrayList<Action>> actions;
    private ArrayList<Player> players;
    private Location startPoint;
    private Player currentPlayer;

    public StagController(StagParser stagParser)
    {
        this.locations = stagParser.parseEntities();
        this.actions = stagParser.parseActions();
        this.startPoint = locations.get(0);
        this.players = new ArrayList<>();
    }

    public void setCurrentPlayer(String name)
    {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                this.currentPlayer = player;
                return;
            }
        }
        Player newPlayer = new Player(name, "No. " + this.players.size() + 1 + " player", this.startPoint);
        this.players.add(newPlayer);
        this.currentPlayer = newPlayer;
    }

    public void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {
        String message = in.readLine();
        String playerName = message.substring(0, message.indexOf(':'));
        String[] commands = message.substring(playerName.length() + 2).split("\\s+");
        setCurrentPlayer(playerName);
        if (commands.length == 0) {
            out.write("Invalid commands");
        }
        try {
            out.write(passCommand(commands));
        } catch (Exception e) {
            e.printStackTrace();
            out.write("Invalid commands");
        }
    }

    public String passCommand(String[] commands) throws Exception {
        String result = "";
        if (actions.containsKey(commands[0])) {
             result = generateAction(commands);
            if (this.currentPlayer.isDead()) {
                result += this.currentPlayer.playerDead(this.startPoint);
            }
        } else {
            Command command = generateCommand(commands);
            result = command.execute();
        }
        return result;
    }

    public Command generateCommand(String[] commands) throws Exception
    {
        switch (commands[0].toLowerCase()) {
            case "inv":
            case "inventory":
                return new InvCMD(this.currentPlayer);
            case "get":
                return new GetCMD(this.currentPlayer, commands);
            case "drop":
                return new DropCMD(this.currentPlayer, commands);
            case "goto":
                return new GotoCMD(this.currentPlayer, commands, locations);
            case "look":
                return new LookCMD(this.currentPlayer);
            default:
                throw new Exception();
        }
    }

    public String generateAction(String[] commands) {
        String trigger = commands[0];
        ArrayList<Action> triggerActions = this.actions.get(trigger);
        if (triggerActions.size() == 1) {
            return triggerActions.get(0).execute(this.currentPlayer, locations);
        } else {
            if (commands.length < 2) {
                return "invalid action :  "+ trigger;
            }
            for (Action action : triggerActions) {
                for (String subject : action.getSubjects()) {
                    if (subject.equalsIgnoreCase(commands[1])) {
                        return action.execute(this.currentPlayer, locations);
                    }
                }
            }
        }
        return "Invalid trigger action";
    }

}
