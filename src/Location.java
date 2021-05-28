import java.util.*;

/**
 * @author lee
 * @create 2021-05-18 15:22
 */
public class Location extends Entity{
    private ArrayList<Location> paths;
    private ArrayList<Character> characters;
    private ArrayList<Artefact> artefacts;
    private ArrayList<Furniture> furniture;


    public Location(String name, String description) {
        super(name, description);
        paths = new ArrayList<>();
        artefacts = new ArrayList<>();
        characters = new ArrayList<>();
        furniture = new ArrayList<>();
    }

    public ArrayList<Location> getPaths() {
        return this.paths;
    }

    public ArrayList<Character> getCharacters() {
        return this.characters;
    }

    public ArrayList<Artefact> getArtefacts() {
        return this.artefacts;
    }

    public int getArtefactByName(String name) {
        if (this.artefacts.size()==0) {
            return -1;
        }
        for (int i=0; i<this.artefacts.size(); i++) {
            if (artefacts.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public int getFurnitureByName(String name) {
        if (this.furniture.size()==0) {
            return -1;
        }
        for (int i=0; i<this.furniture.size(); i++) {
            if (furniture.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public int getCharacterByName(String name) {
        if (this.characters.size()==0) {
            return -1;
        }
        for (int i=0; i<this.characters.size(); i++) {
            if (characters.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }


    public int getPathByName(String name) {
        if (this.paths.size()==0) {
            return -1;
        }
        for (int i=0; i<this.paths.size(); i++) {
            if (paths.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are in ").append(this.getName()).append(this.getDescription()).append(". You can see:\n");
        for (Artefact artefact : artefacts) {
            sb.append(artefact.getDescription()).append("\n");
        }
        for (Furniture f : furniture) {
            sb.append(f.getDescription()).append("\n");
        }
        for (Character character : characters) {
            sb.append(character.getDescription()).append("\n");
        }
        sb.append("You can access from here:\n");
        for (Location path : paths) {
            sb.append(path.getName()).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Furniture> getFurniture() {
        return this.furniture;
    }

    public void removeArtefact(int index) {
        artefacts.remove(index);
    }

    public boolean deleteArtefact(String name) {
        int indexOfArtefact =  getArtefactByName(name);
        if (indexOfArtefact > -1) {
            removeArtefact(indexOfArtefact);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteFurniture(String name) {
        int indexOfFurniture =  getFurnitureByName(name);
        System.out.println(indexOfFurniture);
        if (indexOfFurniture > -1) {
            getFurniture().remove(indexOfFurniture);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteCharacter(String name) {
        int indexOfCharacter =  getFurnitureByName(name);
        if (indexOfCharacter> -1) {
            getCharacters().remove(indexOfCharacter);
            return true;
        }
        else {
            return false;
        }
    }

    public void addPath(Location newPath) {
        getPaths().add(newPath);
    }
}
