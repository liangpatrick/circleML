package Environment;
import org.apache.commons.lang3.builder.*;
import java.util.ArrayList;
import java.util.Random;

public class Graph {
//    Used for each cell in graph
    public static class Node{
//        used during bfs
        public Node prev;
//        cell number; 0-49
        int cell;
//        constructors for Node
        public Node(int cell){
            this.cell = cell;
        }
//      used for bfs
        public Node(int cell, Node prev){
            this.cell = cell;
            this.prev = prev;
        }


//      returns cell of Node
        public int getCell(){
            return cell;
        }


//        Create my own hashfunction and equals function using Apache Commons
        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                    // if deriving: appendSuper(super.hashCode()).
                            append(cell).
//                    append(age).
                    toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node))
                return false;
            if (obj == this)
                return true;

            Node rhs = (Node) obj;
            return new EqualsBuilder().append(cell, rhs.cell).isEquals();
        }

        @Override
        public String toString(){
            return ("Cell: " + cell);
        }

    }

//  wrapper method which generates a graph that satisfies conditions
    public static ArrayList<ArrayList<Node>> buildGraph(){
        return addRandomEdges(buildSkeletonGraph());
    }

//  creates initial undirected graph
    public static ArrayList<ArrayList<Node>> buildSkeletonGraph(){
        ArrayList<ArrayList<Node>> graph = new ArrayList<>();
        for(int x = 0; x < 50; x++){
            ArrayList<Node> temp = new ArrayList<>();
            temp.add(new Node(x));
//            codes the wrap around
            if (x==0){
                temp.add(new Node(x + 1));
                temp.add(new Node(49));
            } else if (x == 49) {
                temp.add(new Node(0));
                temp.add(new Node(x-1));
            } else {
//                creates bidirectional edge
                temp.add(new Node(x + 1));
                temp.add(new Node(x - 1));

            }
            graph.add(temp);
        }
        return graph;

    }

//  checks if the current Node is in the list of neighbors
    static boolean contains(ArrayList<Node> neighbors, Node node){
        for(int x = 0; x < neighbors.size(); x++){
            if (neighbors.get(x).getCell() == node.getCell()){
                return true;
            }
        }
        return false;

    }
//  adds random edges to skeleton graph
    static ArrayList<ArrayList<Node>> addRandomEdges(ArrayList<ArrayList<Node>> skeleton){
//        initializes cells to be added
        ArrayList<Node> full = new ArrayList<>();
        for(ArrayList<Node> arr: skeleton) {
            full.add(arr.get(0));
        }

//        accounts for worst case of max edges
        while(!full.isEmpty()){
//            initializes variables
            int index = new Random().nextInt(full.size());
            Node currNode = full.get(index);
//            used to insert and calculate wrap around
            int value = currNode.getCell();
            int range = new Random().nextInt(5) + 1;
            int sign = new Random().nextInt(2);
//            negative if 1 and positive is 0
            sign = sign == 1 ? -1 : 1;
//          Potential new edge to be added
            int newEdge = value + range * sign;

            Node newNode = null;
            int count = 0;
//            checks if newNode has already been added or is within 5 steps
            while(newNode == null){
                range = new Random().nextInt(5) + 1;
                sign = new Random().nextInt(2);
                sign = sign == 1 ? -1 : 1;
                newEdge = value + range* sign;
//                wraparound
                newEdge = newEdge < 0 ? 50 + newEdge : newEdge;
                newEdge = newEdge > 49 ? newEdge - 50 : newEdge;

                newNode = full.contains(new Node(newEdge)) ? full.get(full.indexOf(new Node(newEdge))) : null;

//              checks if newNode is valid
                if (newNode != null && contains(skeleton.get(currNode.getCell()), newNode)){
                    newNode = null;
                    continue;
                }
                count++;
//                abritray limit to exhaust all possibilities that no possible edge can be added
                if (count > 50){
                    return skeleton;
                }
            }

//          error check
            if(contains(skeleton.get(currNode.getCell()), newNode)){
                return null;
            }
//            updates skeleton and removes from full so that no duplicates can happen
            skeleton.get(currNode.getCell()).add(new Node(newEdge));
            skeleton.get(newNode.getCell()).add(new Node(value));
            full.remove(full.indexOf(currNode));
            full.remove(full.indexOf(newNode));

        }



        return skeleton;
    }

//    used for error checking
    public static void printGraph(ArrayList<ArrayList<Node>> graph){
         for(ArrayList<Node> key: graph) {
             System.out.println(key.toString());
             System.out.println();
         }
    }


}
