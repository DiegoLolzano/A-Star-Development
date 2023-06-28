import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Node extends JButton implements ActionListener{

    Node parent;
    int col;
    int row;
    int gCost;
    int hCost;
    int fCost;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;

    /*G Cost: Distance between the current node
    * and the start node.
    * H Cost: Distance from the current node
    * to the goal node. 
    * F Cost: The total cost (G Cost + H Cost) of the node.
    */

    public Node(int col, int row){
        this.col = col;
        this.row = row;

        setBackground(Color.white);
        setForeground(Color.black);

        addActionListener(this);
    }

    public void setAsStart(){
        setBackground(Color.blue);
        setForeground(Color.white);
        setText("Start");
        start = true;
    }

    public void setAsGoal(){
        setBackground(Color.yellow);
        setForeground(Color.black);
        setText("Goal");
        goal = true;
    }

    public void setAsSolid(){
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }

    public void setAsOpen(){
        open = true;
    }

    public void setAsChecked(){
        if(start == false && goal == false){
            setBackground(Color.orange);
            setForeground(Color.black);
        }

        checked = true;
    }

    public void setAsPath(){
        setBackground(Color.green);
        setForeground(Color.black);
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isGoal() {
        return goal;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setBackground(Color.orange);
    }
    
}
