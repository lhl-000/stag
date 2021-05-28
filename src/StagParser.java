import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author lee
 * @create 2021-05-18 19:14
 */
public class StagParser {
    private String  entitiesFile;
    private String  actionsFile;

    public StagParser(String entitiesFile, String actionsFile) {
        this.entitiesFile = entitiesFile;
        this.actionsFile = actionsFile;
    }

    public ArrayList<Location> parseEntities() {
        ArrayList<Location> locations = new ArrayList<>();
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entitiesFile);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            for (Graph graph : subGraphs) {
                processGraph(graph, locations);
            }
        } catch (FileNotFoundException | ParseException e) {
            System.out.println(e);
        }
        return locations;
    }

    public  HashMap<String, ArrayList<Action>> parseActions() {
        HashMap<String, ArrayList<Action>> actionsMap = new HashMap<>();
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(actionsFile)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray actions = (JSONArray) jsonObject.get("actions");
            for (Object actionObj : actions) {
                JSONObject actionJsonObject = (JSONObject) actionObj;
                ArrayList<String> triggers = new ArrayList<String>((JSONArray) actionJsonObject.get("triggers"));
                ArrayList<String> subjects = new ArrayList<String>((JSONArray) actionJsonObject.get("subjects"));
                ArrayList<String> consumed = new ArrayList<String>((JSONArray) actionJsonObject.get("consumed"));
                ArrayList<String> produced = new ArrayList<String>((JSONArray) actionJsonObject.get("produced"));
                String narration = (String) actionJsonObject.get("narration");
                Action action = new Action(triggers, subjects, consumed, produced, narration);
                for (String trigger : triggers) {
                    if (actionsMap.containsKey(trigger)) {
                        actionsMap.get(trigger).add(action);
                    } else {
                        ArrayList<Action> actionsArrayList = new ArrayList<>();
                        actionsArrayList.add(action);
                        actionsMap.put(trigger, actionsArrayList);
                    }
                }
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            System.err.println("Failed to read actionsFile or parse JSON");
        }
        return actionsMap;
    }

    private void processGraph(Graph g, ArrayList<Location> locations) {
        ArrayList<Graph> locationGraphs = g.getSubgraphs();
        ArrayList<Edge> edges = g.getEdges();
        for (int i = 0; i < locationGraphs.size(); i++) {
            Graph cluster = locationGraphs.get(i);
            ArrayList<Node> clusterNodes = cluster.getNodes(false);
            Node clusterNode = clusterNodes.get(0);
            Location location = new Location(clusterNode.getId().getId(), clusterNode.getAttribute("description"));
            locations.add(location);

            ArrayList<Graph> subGraphs = cluster.getSubgraphs();
            for (Graph minGraph : subGraphs) {
                processEntities(minGraph, location);
            }
        }
        for (Edge edge : edges) {
           int indexOfLocation = getLocationsByName(locations, edge.getSource().getNode().getId().getId());
            int indexOfNextLocation = getLocationsByName(locations, edge.getTarget().getNode().getId().getId());
           if (indexOfLocation == -1 || indexOfNextLocation == -1) {
               System.err.println("Find an invalid location" + edge.getSource().getNode().getId().getId());
           }
            locations.get(indexOfLocation).getPaths().add(locations.get(indexOfNextLocation));
        }
    }

    private int getLocationsByName(ArrayList<Location> locations,String name) {
        for (int i=0; i<locations.size(); i++) {
            if (locations.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }


    private void processEntities(Graph minGraph, Location location) {
        ArrayList<Node> entitiesNodes = minGraph.getNodes(false);
        for (Node entityNode : entitiesNodes) {
            String entityName = entityNode.getId().getId();
            String entityDescription = entityNode.getAttribute("description");
            switch (minGraph.getId().getId()) {
                case "artefacts":
                    location.getArtefacts().add(new Artefact(entityName, entityDescription));
                    break;
                case "furniture":
                    location.getFurniture().add(new Furniture(entityName, entityDescription));
                    break;
                case "characters":
                    location.getCharacters().add(new Character(entityName, entityDescription));
                    break;
                default:
                    System.err.println("Find an invalid entity " + entityNode.getId().getId());
            }
        }
    }

}
