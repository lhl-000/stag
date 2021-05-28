import java.lang.reflect.Array;
import java.security.UnresolvedPermission;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author lee
 * @create 2021-05-18 16:47
 */
public abstract  class Command {
    private Player player;

    public Command (Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract String execute ();

}

class InvCMD extends Command {

    public InvCMD(Player player) {
        super(player);
    }

    public String execute () {
        return showInventory();
    }

    public String showInventory() {
        if (getPlayer().getInventory().isEmpty()) {
            return "There is nothing in your inventory";
        } else {
            String artefacts = "";
            for (Artefact artefact : getPlayer().getInventory()) {
                artefacts += artefact.getName() + '\n';
            }
//            for (int i=0; i< getPlayer().getInventory().size(); i++) {
//                artefacts += getPlayer().getInventory().get(i).toString() + '\n';
//            }
            return "The artefacts in your inventory are : " + '\n' + artefacts;
        }
    }
}

class GetCMD extends Command
{
    private String artefact;

    public GetCMD(Player player, String[] commands) throws Exception {
        super(player);
        if (commands.length != 2) {
            throw new Exception();
        }
        this.artefact = commands[1];
    }

    public String execute() {
        return getArtefact();
    }

    public String getArtefact() {

        Location currentLocation = getPlayer().getLocation();
        int indexOfArtefact = currentLocation.getArtefactByName(this.artefact);
        if (indexOfArtefact == -1) {
            return "There is no " + this.artefact + " in your location";
        } else {
            getPlayer().addArtefact(currentLocation.getArtefacts().get(indexOfArtefact));
            currentLocation.removeArtefact(indexOfArtefact);

            return "You picked up a " + this.artefact;
        }
    }
}

class DropCMD extends  Command {
    private String artefact;

    public DropCMD(Player player, String[] commands) throws Exception {
        super(player);
        if (commands.length != 2) {
            throw new Exception();
        }
        this.artefact = commands[1];
    }

    public String execute () {
        return dropArtefact();
    }

    public String dropArtefact() {
        Location currentLocation = getPlayer().getLocation();
        int indexOfArtefact = getPlayer().getInventoryByName(this.artefact);
        if (indexOfArtefact == -1) {
            return "There is no " + artefact + " in your inventory";
        } else {
            currentLocation.getArtefacts().add(getPlayer().getInventory().get(indexOfArtefact));
            getPlayer().getInventory().remove(indexOfArtefact);
            return "You dropped a " + this.artefact;
        }
    }
}

class GotoCMD extends  Command {
    private String location;
    private ArrayList<Location> locations;

    public GotoCMD(Player player, String[] commands, ArrayList<Location> locations) throws Exception {
        super(player);
        if (commands.length != 2) {
            throw new Exception();
        }
        this.location = commands[1];
        this.locations = locations;
    }

    public String execute () {
        return gotoLocation();
    }

    public String gotoLocation() {
        Location currentLocation = getPlayer().getLocation();
        int indexOfNextLocation = currentLocation.getPathByName(this.location);
        if (indexOfNextLocation == -1) {
            return "There is no way to " + this.location;
        }
        else {
            for (Location nextLocation : locations) {
                if (nextLocation.getName().equals(location)) {
                    getPlayer().setLocation(nextLocation);
                    return currentLocation.getPaths().get(indexOfNextLocation).toString();
                }
            }
            return "There is no way to " + this.location;
        }
    }
}

class LookCMD extends  Command {

    public LookCMD(Player player) {
        super(player);
    }

    public String execute () {
        return lookLocation();
    }

    public String lookLocation () {
        Location currentLocation = getPlayer().getLocation();
        return currentLocation.toString();
    }

}
