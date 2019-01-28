package whiteelephant.com.mapsexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AStarSearch {


    private HashMap<String, Double> frontier = new HashMap<>();
    private HashMap<String, Double> costSoFar = new HashMap<>();
    private HashMap<String, String> cameFrom = new HashMap<>();
    private ArrayList<String> exploredNodes = new ArrayList<>();

    private ArrayList<Road> allRoads;

    public AStarSearch(ArrayList<Road> allRoads){
        this.allRoads = allRoads;
    }

    public ArrayList<String> findpath(Double startLat,Double startLng,Double endLat,Double endLng,Road startRoad, Road endRoad){

        frontier.clear();
        costSoFar.clear();
        cameFrom.clear();
        exploredNodes.clear();

        frontier.put("start", 0.0);
        costSoFar.put("start", 0.0);

        while (frontier.size() > 0) {
            String current = getNextNodeToExpand();
            if (current.equals("end")) return drawPath(current);
            frontier.remove(current);
            exploredNodes.add(current);
            ArrayList<Road> successors = getSuccessors(current,startLat,startLng,endLat,endLng,startRoad,endRoad);

            for (Road road: successors){
                String successor;
                if (road.getFromCrossID().equals(current)){
                    successor = road.getToCrossID();
                }
                else{
                    successor = road.getFromCrossID();
                }

                if (exploredNodes.contains(successor)) {
                    continue;
                }


                double cost = road.getDistance() * road.getPollutionIndex();
                double newCost = costSoFar.get(current) + cost;

                if(costSoFar.containsKey(successor)){
                    if (newCost >= costSoFar.get(successor)){
                        continue;
                    }
                }

                cameFrom.put(successor, current);
                costSoFar.put(successor, newCost);

                double heuristic = 0; //Dijkstra
                double priority = newCost + heuristic;
                frontier.put(successor, priority);

            }
        }
        return null;
    }

    private ArrayList<Road> getSuccessors(String current,Double startLat,Double startLng,Double endLat,Double endLng,Road startRoad, Road endRoad) {
        ArrayList<Road> successors = new ArrayList<>();
        if(current.equals("start")){
            successors.add(new Road(current, startRoad.getFromCrossID(), startLat, startLng, startRoad.getFromLat(), startRoad.getFromLng(), startRoad.getPollutionIndex()));
            successors.add(new Road(current, startRoad.getToCrossID(), startLat, startLng, startRoad.getToLat(), startRoad.getToLng(), startRoad.getPollutionIndex()));
        }
        if(current.equals(endRoad.getFromCrossID())){
            successors.add(new Road(current, "end",endRoad.getFromLat(), endRoad.getFromLng(), endLat, endLng, endRoad.getPollutionIndex()));
        }
        if(current.equals(endRoad.getToCrossID())){
            successors.add(new Road(current, "end",endRoad.getToLat(), endRoad.getToLng(), endLat, endLng, endRoad.getPollutionIndex()));
        }

        for(Road road: allRoads) {
            if (road.getFromCrossID().equals(current)||road.getToCrossID().equals(current)){
                successors.add(road);
            }
        }

        return successors;
    }

    private String getNextNodeToExpand() {
        double min=Double.MAX_VALUE;
        String key = null;
        for(Map.Entry entry: frontier.entrySet()){
            if((double)entry.getValue() < min){
                min = (double) entry.getValue();
                key = (String) entry.getKey();
            }
        }
        return key;
    }

    private ArrayList<String> drawPath(String current) {
        ArrayList<String> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

}
