import java.util.ArrayList;

/**
 * @author lee
 * @create 2021-05-18 15:20
 */
public class Player extends Character{
    private int healthLevel;
    private ArrayList<Artefact> inventory;
    private Location currentLocation;
    private static final int INITIAL_HEALTH_LEVEL = 3;

    public Player(String name, String description, Location startPoint) {
        super(name, description);
        this.inventory = new ArrayList<>();
        this.healthLevel = INITIAL_HEALTH_LEVEL;
        this.currentLocation = startPoint;
    }

    public ArrayList<Artefact> getInventory() {
        return this.inventory;
    }

    public Location getLocation() {
        return this.currentLocation;
    }

    public void setLocation(Location nextLocation) {
        this.currentLocation = nextLocation;
    }

    public void addArtefact(Artefact artefact) {
        this.inventory.add(artefact);
    }

    public int getInventoryByName(String name) {
        if (this.inventory.size() == 0) {
            return -1;
        }
        for (int i=0; i<this.inventory.size(); i++) {
            if (inventory.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public boolean deleteArtefactByName(String name) {
        int indexOfArtefact = getInventoryByName(name);
            if (indexOfArtefact > -1) {
                this.inventory.remove(indexOfArtefact);
                return true;
            }
            else {
                return false;
            }
    }

    public boolean isDead() {
        if(this.healthLevel > 0) {
            return false;
        }
        return true;
    }

    public String playerDead(Location startPoint) {

        for (Artefact artefact : inventory) {
            currentLocation.getArtefacts().add(artefact);
        }
        inventory = new ArrayList<Artefact>();
        this.currentLocation = startPoint;
        this.setHealth(INITIAL_HEALTH_LEVEL);
        return "You were dead, you resurrect at the " + startPoint.getName();
    }

    public int getHealth() {
        return this.healthLevel;
    }

    public void setHealth(int health) {
        this.healthLevel = health;
    }
}
