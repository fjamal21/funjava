package com.algorithm.datastructures;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortestPairAlgorithm {

    private static final Properties prop = new Properties();
    private static final String configFileName = "config1.properties";
    //    private static final String configFileName = "config2.properties";
    private static final Pattern pattern = Pattern.compile("\\((.*?),(.*?)=(\\d+)\\)");

    private static String fullNetwork;
    private static String startNode;
    private static String destinationNode;

    public static void main(String[] args) {
        try {
            readConfig(args);
            List<GraphAlgorithm.Edge> graph = new ArrayList<GraphAlgorithm.Edge>();
            List<String> networkInfo = Arrays.asList(fullNetwork.split(";"));
            for (String info : networkInfo) {
                Matcher m = pattern.matcher(info);
                if (m.matches()) {
                    GraphAlgorithm.Edge edge = new GraphAlgorithm.Edge(m.group(1), m.group(2), Integer.parseInt(m
                            .group(3)));
                    graph.add(edge);
                } else {
                    System.err.println("input string passed= " + info + ", which is not in the proper format.");
                    System.exit(1);
                }
            }

            GraphAlgorithm g = new GraphAlgorithm(graph);
            g.dijkstra(startNode);
            g.restorePath(destinationNode);
            g.revertEdges(destinationNode);
            g.assignPotentials();
            g.dijkstra(startNode);
            g.restorePath(destinationNode);

            System.out.println("Shortest Pair path is: ");
            g.printPaths(startNode, destinationNode);
        } catch (FileNotFoundException ex) {
            System.out.println("error = " + ex);
        } catch (IOException ex) {
            System.out.println("error = " + ex);
        } catch (Exception ex) {
            System.out.println("error = " + ex);
        }
    }

    /**
     * Below method is used to read the file.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void readConfig(String[] args) throws FileNotFoundException, IOException {
        if (args != null && args.length != 0) {
            prop.load(new FileInputStream(args[0]));
        } else {
            prop.load(ShortestPairAlgorithm.class.getClassLoader().getResourceAsStream(configFileName));
        }

        fullNetwork = prop.getProperty("FullNetwork").trim();
        startNode = prop.getProperty("StartNode").trim();
        destinationNode = prop.getProperty("DestinationNode").trim();
    }
}


class GraphAlgorithm {
    private final Map<String, Vertex> graph;

    public static class Edge implements Comparable<Edge> {
        public final String v1, v2;
        public final int dist;

        public Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }

        public int compareTo(Edge other) {
            if (v1.equals(other.v1))
                return v2.compareTo(other.v2);
            return v1.compareTo(other.v1);
        }
    }

    private TreeSet<Edge> answer = new TreeSet<Edge>(); // stores all the edges in the answer

    public static class Vertex implements Comparable<Vertex> {
        public final String name;
        public int potential = 0;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Integer> neighbours = new HashMap<Vertex, Integer>();

        public Vertex(String name) {
            this.name = name;
        }

        public int compareTo(Vertex other) {
            if (dist == other.dist)
                return name.compareTo(other.name);
            return Integer.valueOf(dist).compareTo(Integer.valueOf(other.dist));
        }
    }

    public GraphAlgorithm(List<Edge> edges) {
        graph = new HashMap<String, Vertex>(edges.size());

        for (Edge e : edges) {
            if (!graph.containsKey(e.v1))
                graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2))
                graph.put(e.v2, new Vertex(e.v2));
        }

        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist);
        }
    }

    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<Vertex>();

        // setting-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /**
     * dijkstra's algorithm using a binary heap.
     * 
     * @param q
     */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst();
            if (u.dist == Integer.MAX_VALUE)
                break;

            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey();

                final int alternateDist = u.dist + a.getValue() + u.potential - v.potential;
                if (alternateDist < v.dist) {
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    public void revertEdges(String endName) {
        Vertex v = graph.get(endName);
        while (v.previous != null && v.previous != v) {
            Vertex w = v.previous;
            int weight = v.neighbours.get(w);
            v.neighbours.remove(w);
            w.neighbours.remove(v);

            v.neighbours.put(w, -weight);

            v = w;
        }
    }

    public void assignPotentials() {
        for (Vertex v : graph.values()) {
            v.potential = v.dist;
        }
    }

    public void restorePath(String endName) {
        Vertex v = graph.get(endName);
        while (v.previous != null && v.previous != v) {
            String from = v.previous.name;
            String to = v.name;
            if (answer.contains(new Edge(to, from, 0))) {
                answer.remove(new Edge(to, from, 0));
            } else {
                answer.add(new Edge(from, to, 0));
            }
            v = v.previous;
        }
    }

    public void printOnePath(String startName, String endName) {
        Vertex from = graph.get(startName);
        Vertex to = graph.get(endName);
        Vertex cur = from;
        do {
            System.out.printf("%s -> ", cur.name);

            Edge e = answer.ceiling(new Edge(cur.name, "", 0));
            answer.remove(e);

            cur = graph.get(e.v2);
        } while (cur != to);
        System.out.println(to.name);
    }

    public void printPaths(String startName, String endName) {
        printOnePath(startName, endName);
        printOnePath(startName, endName);
    }
}
