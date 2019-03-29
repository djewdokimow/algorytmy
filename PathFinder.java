package edu.jewdokimow.pathfinder;

import javafx.util.Pair;

import java.util.*;

public class PathFinder {

    private HashSet<Pair<Integer, Integer>>[] incidency;
    private int[] distances;
    private int[] previous;
    private boolean[] visited;

    private int citiesCount = -1;
    private int connectionsCount = -1;

    private int start;
    private int destination;

    private void init() {
        incidency = new HashSet[citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            incidency[i] = new HashSet<>();
        }
    }

    private void resetMutatingArrays(int start) {
        distances = new int[citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            distances[i] = i == start-1 ? 0 : Integer.MAX_VALUE;
        }
        previous = new int[citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            previous[i] = -1;
        }
        visited = new boolean[citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            visited[i] = false;
        }
    }

    void parseLine(String line) {
        String[] numbers = line.split(" ");
        switch (numbers.length) {
            case 2:
                if (citiesCount == -1) {
                    citiesCount = Integer.parseInt(numbers[0]);
                    init();
                    connectionsCount = Integer.parseInt(numbers[1]);
                } else {
                    start = Integer.parseInt(numbers[0]);
                    destination = Integer.parseInt(numbers[1]);
                }
                break;
            case 3:
                int firstCity = Integer.parseInt(numbers[0]);
                int secondCity = Integer.parseInt(numbers[1]);
                int distance = Integer.parseInt(numbers[2]);
                markOnCollisionMark(firstCity, secondCity, distance);
                markOnCollisionMark(secondCity, firstCity, distance);
                break;
        }
    }

    private void markOnCollisionMark(int firstCity, int secondCity, int distance) {
        addCityToCollisionInfo(firstCity, secondCity, distance);
        addCityToCollisionInfo(secondCity, firstCity, distance);
    }

    private void addCityToCollisionInfo(int firstCity, int secondCity, int distance) {
        incidency[firstCity - 1].add(new Pair<>(secondCity, distance));
    }

    // BFS
    List<String> findTheShortestPath() {
        resetMutatingArrays(start);
        LinkedList<Integer> queue = new LinkedList<>();
        queue.push(start);
        visited[start - 1] = true;
        while (!queue.isEmpty()) {
            int current = queue.peekFirst();
            int index = current - 1;
            HashSet<Pair<Integer, Integer>> currentDistances = incidency[index];
            for (Pair<Integer, Integer> distancePair : currentDistances) {
                int city = distancePair.getKey();
                if (!visited[city - 1]) {
                    queue.addLast(city);
                    int distance = distancePair.getValue();
                    distances[city - 1] = distances[index] + distance;
                    previous[city - 1] = current;
                    visited[city - 1] = true;
                }
            }
            queue.removeFirst();
        }
        List<String> toSaveFile = new ArrayList<>();
        List<String> path = findPathFromStartToDestination();
        toSaveFile.add(path.size() - 2 + " " + distances[destination - 1]);
        toSaveFile.add(String.join(" ", path));
        return toSaveFile;
    }

    // Dijkstra
    List<String> findShortestPathWithDijkstra() {
        resetMutatingArrays(start);
        PriorityQueue<Integer> queue =
                new PriorityQueue<>(citiesCount, Comparator.comparingLong(o -> distances[o - 1]));
        queue.offer(start);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int index = current - 1;
            if (!visited[index]) {
                visited[index] = true;
                HashSet<Pair<Integer, Integer>> currentDistances = incidency[index];
                for (Pair<Integer, Integer> distancePair : currentDistances) {
                    int city = distancePair.getKey();
                    int distance = distancePair.getValue();
                    if (!visited[city - 1] && distances[index] + distance < distances[city - 1]) {
                        distances[city - 1] = distances[index] + distance;
                        previous[city - 1] = current;
                        queue.offer(city);
                    }
                }
            }
        }
        List<String> toSaveFile = new ArrayList<>();
        toSaveFile.add(String.valueOf(distances[destination - 1]));
        toSaveFile.add(String.join(" ", findPathFromStartToDestination()));
        return toSaveFile;
    }

    private List<String> findPathFromStartToDestination() {
        List<String> path = new ArrayList<>();
        int current = destination;
        path.add(String.valueOf(current));
        while (current != start) {
            current = previous[current - 1];
            path.add(String.valueOf(current));
        }
        Collections.reverse(path);
        return path;
    }
}
