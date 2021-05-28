import java.util.ArrayList;


/**
 * @author lee
 * @create 2021-05-18 16:29
 */
public class Action {
    private ArrayList<String> triggers;
    private ArrayList<String> subjects;
    private ArrayList<String> consumed;
    private ArrayList<String> produced;
    private String narration;

    public Action(ArrayList<String> triggers, ArrayList<String> subjects, ArrayList<String> consumed, ArrayList<String> produced, String narration ){
     this.triggers = triggers;
     this.subjects = subjects;
     this.consumed = consumed;
     this.produced = produced;
     this.narration = narration;
    }

    public ArrayList<String> getSubjects() {
        return this.subjects;
    }
//
//    public ArrayList<String> getTriggers() {
//        return this.triggers;
//    }
//
//
//    public ArrayList<String> getConsumed() {
//        return this.consumed;
//    }
//
//    public ArrayList<String> getProduced() {
//        return this.produced;
//    }


    public boolean subject(Player player ,Location currentLocation) {
        for (String subject : this.subjects) {
            if (player.getInventoryByName(subject) == -1
                    && currentLocation.getArtefactByName(subject) == -1
                    && currentLocation.getFurnitureByName(subject) == -1
                    && currentLocation.getCharacterByName(subject) == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean consume(Player player, Location currentLocation) {
        for (String consumed : consumed) {
            if (player.getInventoryByName(consumed) != -1) {
                player.deleteArtefactByName(consumed);
                return true;
            }
            else if (consumed.toLowerCase().equals("health")) {
                player.setHealth(player.getHealth()-1);
                return true;
            }
            else if (currentLocation.getCharacterByName(consumed) > -1) {
                currentLocation.deleteCharacter(consumed);
            }
            else {
                currentLocation.deleteFurniture(consumed);
                return true;
            }
        }
        return false;
    }

    public void produce(Player player, Location currentLocation, ArrayList<Location> locations) {
        Location newLocation;
        for (String produced : produced) {
            if (produced.toLowerCase().equals("health")) {
                player.setHealth(player.getHealth() + 1);
            }
            else if ((newLocation = isProductionALocation(produced, locations)) != null) {
                currentLocation.addPath(newLocation);
            }
            else {
                Artefact artefact = new Artefact(produced, "");
                player.addArtefact(artefact);
            }
        }
    }

    public Location isProductionALocation(String name,ArrayList<Location> locations) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }

    public String execute(Player player, ArrayList<Location> locations) {
        Location currentLocation = player.getLocation();
        if (!subject(player,currentLocation)) {
            return "Action failed, no subjects";
        }
        if (!consume(player,currentLocation)) {
            return "Action failed, can not consumed";
        }
        produce(player,currentLocation, locations);
        return this.narration + "\n" + "Your health level is " + player.getHealth();
    }
}
