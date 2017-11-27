package com.company;

import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;
import static sun.management.Agent.error;

public class Proj6 {
    static int items[][];
    static int capacity;   // capacity of the knapsack
    static TreeMap<Integer, Node> treeMap = new TreeMap<Integer, Node>();

    public static void main(String[] args) {
        fillList();

        System.out.println("Capacity of knapsack is " + capacity +
                "\nItems are:");
        for (int i = 1; i < items[2].length; i++) {
            System.out.println(i + ": " + items[0][i] + " " + items[1][i] +
                    " " + items[2][i]);
        }
        GenerateRootNode();
        BranchAndBound(treeMap.get(1));
        //GenerateChildren(treeMap.get(1).nodeNum, );

    }

    public static void BranchAndBound(Node parent) {
        PrintNodes(parent, "Exploring");

        int winner = -1;
        //System.out.println("Parent level: " + parent.level);
//        System.out.println("(BnB) items length: " + items[2].length);
//        System.out.print("(BnB) parent nodeNum: ");   System.out.println(parent.nodeNum);
        if (parent.level == items[2].length && parent.nodeNum == FindBest()) {
            winner = parent.nodeNum;
        }
        else if (parent.weight == capacity && parent.nodeNum == FindBest()){
            winner = parent.nodeNum;
        }
        else {
            GenerateChildren(parent.nodeNum);
            BranchAndBound(treeMap.get(FindBest()));
        }


        System.out.println("Best Node: " + winner + " Items: " + treeMap.get(winner) +
                " level: " + treeMap.get(winner).level +
                " profit: " + treeMap.get(winner).profit +
                " weight: " + treeMap.get(winner).weight +
                " bound: " + treeMap.get(winner).bound);
    }


    public static void GenerateRootNode() {
        if (treeMap.size() == 0) {
            ArrayList empty = new ArrayList<Integer>();   // need to initialize items in the object to an empty array
            Node node = new Node();
            node.nodeNum = 1;
            node.level = 0;
            node.items = empty;         // Initializing so we can compare in GetBound
            node = GetBound(node, false);
            treeMap.put(1, node);
        } else {
            error("Tree not empty");
        }
    }


    public static void GenerateChildren(int parent) {


        // Left Child
        if (treeMap.get(parent).relations[1] == 0) {
            Node node = new Node();

            node.items = treeMap.get(parent).items;
            node.level = treeMap.get(parent).level + 1;
            node.profit = treeMap.get(parent).profit;
            node.weight = treeMap.get(parent).weight;
            node.relations[0] = parent;


            node.nodeNum = parent + 1;
            treeMap.get(parent).relations[1] = node.nodeNum;
            node.level = treeMap.get(parent).level + 1;
            node = GetBound(node, true);
            treeMap.put(treeMap.size() + 1, node);
            PrintNodes(node, "    Left");
        }
        // Right Child
        if (treeMap.get(parent).relations[2] == 0) {
            Node node = new Node();

            node.items = treeMap.get(parent).items;
            node.level = treeMap.get(parent).level + 1;
            node.profit = treeMap.get(parent).profit;
            node.weight = treeMap.get(parent).weight;
            node.relations[0] = parent;

            node.nodeNum = parent + 2;
            treeMap.get(parent).relations[2] = node.nodeNum;
            System.out.println();
//            node.items[node.items.size() - 1] = node.level;
            node.items.add(node.level);
//            System.out.println("(GC) rights item size" + node.items.size());
            node.profit += items[0][node.level];
            node.weight += items[1][node.level];
            if (node.weight > capacity){
                node.profit = -1;
                node.bound = -1;
            }
            else {
                node = GetBound(node, false);
            }
            treeMap.put(treeMap.size() + 1, node);
            PrintNodes(node, "    Right");
            System.out.println();

        }
    }


    public static Node GetBound(Node node, Boolean left) {
        int i = 1;
        int PLoad = node.weight;      // Plausible load
        int load = node.weight;
        int cantUse = -1;
//        if (left) {
//            cantUse = node.level;
//        }
        while (PLoad <= capacity) {

//            System.out.println("(GetBound) items size: " + node.items.size());
            if (node.items.size() != 0 && i - 1 < node.items.size()) {
                System.out.println("I am the contains!!!   " + Integer.valueOf((node.items.get(i - 1)).toString()));
                cantUse = Integer.valueOf((node.items.get(i - 1)).toString());
//                cantUse = int(node.items.get(i - 1));
//                System.out.println("YOU CANT USE ME: " + cantUse);
            }
            else if (i == node.level && left) {
                cantUse = node.level;
            }

            if (i != cantUse) {

                PLoad += items[1][i];

                // Add profit to the bound if its weight still under cap
                if (PLoad <= capacity) {
                    load += items[1][i];
                    node.bound += items[0][i];
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
        }
        return node;
    }

    public static int FindBest() {
        int best = -1;
//        System.out.println("(FindBest) treeMap size: " + treeMap.size());
        for (int i = 1; i <= treeMap.size(); i++) {
            if (treeMap.get(i).relations[1] == 0 && treeMap.get(i).relations[2] == 0) {
                if (best == -1) {
                    best = i;
                }
                else if (treeMap.get(best).bound < treeMap.get(i).bound) {
//                    System.out.println("(FindBest) is node " + treeMap.get(best).nodeNum + ": " + treeMap.get(best).bound +
//                            " < " + treeMap.get(i).nodeNum + ": " + treeMap.get(i).bound);
                    best = i;
                }
            }

        }

//        System.out.print("(FindBest) Best node: ");    System.out.println(best);
//        System.out.print("(FindBest) best nodes bound: ");  System.out.println(treeMap.get(best).bound);

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
            items = new int[3][numItems + 1];   //Prof want's the items to be labeled starting with one,
            // im just going to leave the 0th index blank for ease of use
            int j = 1;
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
        for (int i = 1; i < node.items.size(); i++) {
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


