package com.company;

import java.util.ArrayList;

public class Node{
    public int nodeNum;     // Node Number
    public ArrayList items;     // Items in the node
    public int level;       // level that the node is on
    public int profit = 0;      // total profit
    public int weight = 0;      // total weight
    public int bound;       // bound that will be used to determine how good it is
    public int relations[] = new int[3]; // 0 parent, 1 left child, 2 right child

    public void Node(int num, ArrayList item, int p, int w, int b){
        nodeNum = num;
        items = item;
        profit = p;
        weight = w;
        bound = b;

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