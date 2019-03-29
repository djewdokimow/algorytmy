package edu.jewdokimow.pathfinder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inFileName = "/Users/dawidjewdokimow/Downloads/in4.txt";
        String outFileName = "/Users/dawidjewdokimow/Desktop/out.txt";
        PathFinder parser = new PathFinder();

        try (BufferedReader br = new BufferedReader(new FileReader(inFileName))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                parser.parseLine(sCurrentLine);
            }
            long startMs = System.currentTimeMillis();
            List<String> lines = new ArrayList<>();
            lines.addAll(parser.findShortestPathWithDijkstra());
            lines.addAll(parser.findTheShortestPath());
            long stopMs = System.currentTimeMillis();
            System.out.println(stopMs - startMs + "ms");

            saveToFile(outFileName, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(String outFileName, List<String> lines) throws IOException {
        File file = new File(outFileName);
        FileWriter fileWriter = new FileWriter(file);
        for (String line : lines) {
            fileWriter.write(line + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
