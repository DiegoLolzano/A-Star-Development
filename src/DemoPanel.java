import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class DemoPanel extends JPanel{

    //Screen Settings
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();


    boolean goalReached = false;
    int step = 0;
    int distanceTolerance = 3; //Changes the random Start and Goal distance tolerance

    public DemoPanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        int col = 0;
        int row = 0;

        while(col < maxCol && row < maxRow){
           node[col][row]  = new Node(col, row);
           this.add(node[col][row]);

           col++;

           if(col == maxCol){
            col = 0;
            row++;
           }
        }

        setRandomStartAndGoal();
        //setStartNode(3, 6);
        //setGoalNode(11, 3);

        setSolidNode(3, 5);
        setSolidNode(4, 5);
        setSolidNode(5, 5);
        setSolidNode(6, 5);
        setSolidNode(7, 5);
        setSolidNode(7, 2);
        setSolidNode(8, 3);
        setSolidNode(8, 4);
        setSolidNode(8, 5);
        setSolidNode(10, 3);
        setSolidNode(10, 4);
        setSolidNode(10, 5);
        setSolidNode(10, 6);
        setSolidNode(10, 7);
        setSolidNode(12, 4);
        setSolidNode(12, 5);

        setCostOnNodes();
    }

    private void setStartNode(int col, int row){
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    private void setGoalNode(int col, int row){
        node[col][row].setAsGoal();
        goalNode = node[col][row];
        currentNode = startNode;
    }

    private void setSolidNode(int col, int row){
        node[col][row].setAsSolid();
    }

    private void setCostOnNodes(){
        int col = 0;
        int row = 0;

        while(col < maxCol && row < maxRow){

            getCost(node[col][row]);
            col++;

            if(col == maxCol){
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node){
        //G Cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //H Cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //F Cost
        node.fCost = node.gCost + node.hCost;

        if(node != startNode && node != goalNode){
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }

    public void manualSearch(){
        if(goalReached == false && step < 300){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            openList.remove(currentNode);

            if(row -1 >= 0){
                //Open up node
                openNode(node[col][row - 1]);
            }
            
            if(col -1 >= 0){
                //Open left node
                openNode(node[col - 1][row]);
            }
            
            if(row +1 < maxRow){
                //Open down node
                openNode(node[col][row + 1]);
            }
            
            if(col +1 < maxCol){
                //Open right node
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++){
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if(openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode){
                goalReached = true;
            }
        }
    }

    public void autoSearch(){
        while(goalReached == false){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            openList.remove(currentNode);

            if(row -1 >= 0){
                //Open up node
                openNode(node[col][row - 1]);
            }
            
            if(col -1 >= 0){
                //Open left node
                openNode(node[col - 1][row]);
            }
            
            if(row +1 < maxRow){
                //Open down node
                openNode(node[col][row + 1]);
            }
            
            if(col +1 < maxCol){
                //Open right node
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++){
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if(openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode){
                goalReached = true;
                trackThePath();
            }
        }

         step++;
    }

    private void openNode(Node node){
        //Change to == false in case of not working
        if(!node.open && !node.checked && !node.solid){
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath(){
        Node current = goalNode;

        while(current != startNode){
            current = current.parent;

            if(current != startNode){
                current.setAsPath();
            }
        }
    }

    private void setRandomStartAndGoal(){
        Random random = new Random();
        boolean validStartNode = false;
        boolean validGoalNode = false;

        while(!validStartNode || !validGoalNode){
             int startCol = random.nextInt(maxCol);
             int startRow = random.nextInt(maxRow);

             if(!node[startCol][startRow].isSolid() && !node[startCol][startRow].isGoal()){
                setStartNode(startCol, startRow);
                validStartNode = true;
             }

             int goalCol = random.nextInt(maxCol);
             int goalRow = random.nextInt(maxRow);
             if (!node[goalCol][goalRow].isSolid() && !node[goalCol][goalRow].isStart() && (Math.abs(goalCol - startCol) + Math.abs(goalRow - startRow) >= distanceTolerance)) {
                setGoalNode(goalCol, goalRow);
                validGoalNode = true;
             }
        }   
    }    
}
