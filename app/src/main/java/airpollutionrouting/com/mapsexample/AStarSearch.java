package airpollutionrouting.com.mapsexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import airpollutionrouting.com.mapsexample.models.Road;

/**
 * Created by Yiqun on 21/01/2019.
 *
 * Implementation of Astar (Dijkstra) algorithm.
 */

public class AStarSearch {

    // All nodes can be expanded.
    private HashMap<String, Double> frontier = new HashMap<>();
    // Recording the cost for each node.
    private HashMap<String, Double> costSoFar = new HashMap<>();
    // Recording the parent node of current node
    private HashMap<String, String> cameFrom = new HashMap<>();
    // Recording all visited nodes.
    private ArrayList<String> exploredNodes = new ArrayList<>();
    // Store all roads data.
    private ArrayList<Road> allRoads;
    // Recording the father road of current node
    private HashMap<String, Road> cameFromRoad = new HashMap<>();

    public static ArrayList<Road> pathRoadList = new ArrayList<>();

    public AStarSearch(ArrayList<Road> allRoads){
        this.allRoads = allRoads;
    }

    /**
     *
     * Calculating the best combination of nodes having the minimum air pollution value.
     *
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     * @param startRoad
     * @param endRoad
     * @return ArrayList<String> a list of nodes that having the optimized A value.
     */
    public ArrayList<String> findpath(Double startLat,Double startLng,Double endLat,Double endLng,Road startRoad, Road endRoad,Boolean pollution){

        frontier.clear();
        costSoFar.clear();
        cameFrom.clear();
        exploredNodes.clear();

        frontier.put("start", 0.0);
        costSoFar.put("start", 0.0);

        while (frontier.size() > 0) {
            String current = getNextNodeToExpand();
            // reach the goal state
            if (current.equals("end")) return drawPath(current);
            frontier.remove(current);
            exploredNodes.add(current);
            // get all possible successors of current node
            ArrayList<Road> successors = getSuccessors(current,startLat,startLng,endLat,endLng,startRoad,endRoad);

            for (Road road: successors){
                String successor;
                Double successorLat;
                Double successorLng;
                if (road.getFromCrossID().equals(current)){
                    successor = road.getToCrossID();
                    successorLat = road.getToLat();
                    successorLng = road.getToLng();
                }
                else{
                    successor = road.getFromCrossID();
                    successorLat = road.getFromLat();
                    successorLng = road.getFromLng();
                }

                if (exploredNodes.contains(successor)) {
                    continue;
                }

                double cost;
                double heuristic = 0;
                if(pollution){
                    //air pollution routing
                    cost = road.getDistance() * road.getPollutionIndex();
                }
                else{
                    //normal routing
                    cost = road.getDistance();
                    heuristic = Utils.coordinatesToDistance(successorLat,successorLng,endLat,endLng);
                }
                double newCost = costSoFar.get(current) + cost;
                if(costSoFar.containsKey(successor)){
                    if (newCost >= costSoFar.get(successor)){
                        continue;
                    }
                }
                cameFrom.put(successor, current);
                cameFromRoad.put(successor,road);
                costSoFar.put(successor, newCost);

                double priority = newCost + heuristic;
                frontier.put(successor, priority);

            }
        }
        return null;
    }

    /**
     * Find all roads can be reached according to info of current node
     *
     * @param current
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     * @param startRoad
     * @param endRoad
     * @return ArrayList<Road> all possible successor roads
     */
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

    /**
     * Finding the next node to expand.
     * @return String of the next node
     */
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

        pathRoadList.clear();
        for(String node: path){
            if(cameFromRoad.get(node)!=null){
                pathRoadList.add(cameFromRoad.get(node));
            }
        }

        return path;
    }


}
