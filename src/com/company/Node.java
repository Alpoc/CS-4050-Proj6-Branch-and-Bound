package com.company;

import java.util.ArrayList;
import java.util.List;

public class Node{
    public int nodeNum;     // Node Number
    public List<Integer> items;     // Items in the node
    public int level;       // level that the node is on
    public int profit = 0;      // total profit
    public int weight = 0;      // total weight
    public int bound;       // bound that will be used to determine how good it is
    public int relations[] = new int[3]; // 0 parent, 1 left child, 2 right child
    public List<Integer> cantUse;

    public void Node(int num, List item, int p, int w, int b, List cantUseNums){
        nodeNum = num;
        items = item;
        profit = p;
        weight = w;
        bound = b;
        cantUse = cantUseNums;

    }

//    private int getArrayItem(int index) {
//        return items.get(index);
//    }
//    public int getWeight() {
//        return weight;
//    }
//
//    public int getProfit() {
//        return profit;
//    }
}