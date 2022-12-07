package Agents;

import Environment.Graph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Vstar {




    public static void main(String[] args) throws Exception
    {
        ArrayList<ArrayList<Integer>> namesList = new ArrayList<>();

        try {
                FileInputStream fis = new FileInputStream("states_train");
                ObjectInputStream ois = new ObjectInputStream(fis);

                namesList = (ArrayList) ois.readObject();

                ois.close();
                fis.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        //Verify list m
        for (ArrayList<Integer> name: namesList) {
            System.out.println(name);
        }

        ArrayList<ArrayList<Graph.Node>> maze = new ArrayList<>();

        try
        {
            FileInputStream fis = new FileInputStream("maze");
            ObjectInputStream ois = new ObjectInputStream(fis);

            maze = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        //Verify list m
        for (ArrayList<Graph.Node> employee : maze) {
            System.out.println(employee);
        }
    }

}
