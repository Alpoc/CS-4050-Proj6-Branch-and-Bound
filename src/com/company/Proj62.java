package com.company;

import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;
import static sun.management.Agent.error;

public class Proj62 {
    static int items[][];
    static int capacity;   // capacity of the knapsack
    static ArrayList<Node> availableNodes = new ArrayList<>();

    public static void main(String[] args) {
        fillList();

        System.out.println("Capacity of knapsack is " + capacity +
                "\nItems are:");
        for (int i = 1; i < items[2].length; i++) {
            System.out.println(i + ": " + items[0][i] + " " + items[1][i] +
                    " " + items[2][i]);
        }
        GenerateRootNode();

    }

    public static void BranchAndBound(Node parent) {



//        if (parent.level == (items[2].length - 1) && parent.nodeNum == FindBest()) {
        if (parent.level == (items[2].length) && parent == FindBest()) {
            PrintNodes(parent, "Winner");
        }
//        else if (parent.weight == capacity && parent.nodeNum == FindBest()){
        else if (parent.weight == capacity && parent == FindBest()){
            PrintNodes(parent, "Winner");
        }
        else {
            PrintNodes(parent, "Exploring");
            GenerateChildren(parent);
//            BranchAndBound(availableNodes.get(FindBest()));
            BranchAndBound(FindBest());
        }
    }


    public static void GenerateRootNode() {
        if (availableNodes.size() == 0) {
            List<Integer> empty = new ArrayList<>();
            //ArrayList empty = new ArrayList<Integer>();   // need to initialize items in the object to an empty array
            Node node = new Node();
            node.nodeNum = 1;
            node.level = 0;
            node.items = empty;         // Initializing so we can compare in GetBound
            node = GetBound(node, false);
            availableNodes.add(node);
            BranchAndBound(node);
        } else {
            error("Tree not empty");
        }

    }


    public static void GenerateChildren(Node parent) {

        // Left Child
        Node node = new Node();

        node.items = new ArrayList<Integer>(parent.items);
        node.level = parent.level + 1;
        node.profit = parent.profit;
        node.weight = parent.weight;
        node.nodeNum = parent.nodeNum * 2;
        node = GetBound(node, true);
        availableNodes.add(node);
        PrintNodes(node, "    Left");

        // Right Child
        Node nodeR = new Node();

        nodeR.items = new ArrayList<Integer>(parent.items);
        nodeR.level = parent.level + 1;
        nodeR.profit = parent.profit;
        nodeR.weight = parent.weight;

        nodeR.nodeNum = parent.nodeNum * 2 + 1;
        nodeR.items.add(node.level);
//            System.out.println("(GC) rights item size" + node.items.size());
        nodeR.profit += items[0][nodeR.level];
        nodeR.weight += items[1][nodeR.level];
//        nodeR.bound += items[1][nodeR.level];
        if (nodeR.weight > capacity){
            nodeR.profit = -1;
            nodeR.bound = -1;
            PrintNodes(nodeR, "    Right");
            System.out.println("Pruned because to heavy");
        }
        else {
            nodeR = GetBound(nodeR, false);
            availableNodes.add(nodeR);
            PrintNodes(nodeR, "    Right");
        }
        availableNodes.remove(parent);
        System.out.println("");


        }



    public static Node GetBound(Node node, Boolean left) {
        int i = 0;
        int PLoad = node.weight;      // Plausible load
        int load = node.weight;
        int cantUse = -1;
//        if (left) {
//            cantUse = node.level;
//        }

        while (PLoad <= capacity) {

            if (node.items.size() > 0 && (i) < node.items.size()) {
                //cantUse = Integer.valueOf((node.items.get(i - 1)).toString());

                cantUse = node.items.get(i);
                node.bound += items[1][i];
//                cantUse = int(node.items.get(i - 1));
            }
            else if (i == node.level && left) {
                cantUse = node.level;
            }

            if (i != cantUse) {

                PLoad += items[1][i];

                // Add profit to the bound if its weight still under cap
                if (PLoad <= capacity) {
                    load += items[1][i];
//                    System.out.println("Node Bound in GB: " + node.bound);
                }
                if (PLoad >= capacity) {
                    break;
                }

            }
            i++;
        }
        if (load != capacity) {
            int remainingLoad = capacity - load;
            node.bound += remainingLoad * items[2][i];
//            System.out.println("remaining bound: " + node.bound);
        }
        return node;
    }

//    public static int FindBest() {
//        int best = -1;
//        for (int i = 0; i < availableNodes.size(); i++) {
//                if (best == -1) {
//                    best = i;
//                }
//                else if (availableNodes.get(best).bound < availableNodes.get(i).bound) {
//                    best = i;
//                }
//            }
//            return best;
//
//        }

    public static Node FindBest() {
        Node best = null;
        for (int i = 0; i < availableNodes.size(); i++) {
            if (best == null) {
                best = availableNodes.get(i);
            }
            else if (best.bound < availableNodes.get(i).bound) {
                best = availableNodes.get(i);
            }
        }
        return best;

    }


    public static void fillList() {
        System.out.print("Please enter the name of the file you would like processed: ");
        Scanner keyboard = new Scanner(System.in);
        String fileName = keyboard.nextLine();
        int numItems;
        int profit;
        int weight;
        int profPerKilogram;    // It's just no unit weight but kilo for fun.


        try {
            File file =
                    new File(fileName);

            Scanner fileScanner =
                    new Scanner(file);
            capacity = fileScanner.nextInt();
            numItems = fileScanner.nextInt();
            items = new int[3][numItems];   //Prof want's the items to be labeled starting with one,
            // im just going to leave the 0th index blank for ease of use
            int j = 0;
            while (fileScanner.hasNext()) {
                profit = fileScanner.nextInt();
                weight = fileScanner.nextInt();
                profPerKilogram = profit / weight;
                items[0][j] = profit;
                items[1][j] = weight;
                items[2][j] = profPerKilogram;
                j++;
            }

            fileScanner.close();

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
//            catch(IOException ex) {
//                System.out.println(
//                        "Error reading file '"
//                                + fileName + "'");
//            }


    }

    public static void PrintNodes(Node node, String who){
        String itemString = "[";
        if (node.items.size() != 0) {
            itemString += node.items.get(0);
        }

//        for (int i = 0; i < node.items.length; i++) {
//        itemString += node.items[i] + " ,";
        for (int i = 0; i < node.items.size(); i++) {
            itemString += ", " +node.items.get(i);
        }
        itemString += "]";
        System.out.println(who + " <Node: " + node.nodeNum +
                ":   items: " + itemString +
                " level: " + node.level +
                " profit: " + node.profit +
                " weight: " + node.weight +
                " bound: " + node.bound + " >");
    }
}


