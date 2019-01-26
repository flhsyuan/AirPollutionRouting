package whiteelephant.com.mapsexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AStarSearch {

    private ArrayList<Road> allRoads = new ArrayList<>();

    private HashMap<Integer, Double> frontier = new HashMap<>();
    private HashMap<Integer, Double> costSoFar = new HashMap<>();
    private HashMap<Integer, Integer> cameFrom = new HashMap<>();
    private ArrayList<Integer> exploredNodes = new ArrayList<>();

    public ArrayList<Integer> findpath(int start, int goal){

        frontier.clear();
        costSoFar.clear();
        cameFrom.clear();
        exploredNodes.clear();

        frontier.put(start, 0.0);
        costSoFar.put(start, 0.0);

        while (frontier.size() > 0) {
            int current = getNextNodeToExpand();
            if (current == goal) return drawPath(current);
            frontier.remove(current);
            exploredNodes.add(current);
            ArrayList<Road> successors = getSuccessors(current);

            for (Road road: successors){
                int successor;
                if (road.getCrossId1() == current){
                    successor = road.getCrossId2();
                }
                else{
                    successor = road.getCrossId1();
                }

                if (exploredNodes.contains(successor)) {
                    continue;
                }

                double cost = road.getDistance() * road.getAvgPollution();
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

    private ArrayList<Road> getSuccessors(int currentId) {

        ArrayList<Road> successors = new ArrayList<>();
        for(Road road: allRoads) {
            if (road.getCrossId1() == currentId||road.getCrossId2() == currentId){
                successors.add(road);
            }
        }

        return successors;
    }

    private int getNextNodeToExpand() {
        int min=Integer.MAX_VALUE;
        Integer key = null;
        for(Map.Entry entry: frontier.entrySet()){
            if((int)entry.getValue() < min){
                min = (int) entry.getValue();
                key = (Integer) entry.getKey();
            }
        }
        return key;
    }

    private ArrayList<Integer> drawPath(int current) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

}
