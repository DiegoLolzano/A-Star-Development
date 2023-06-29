import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class DemoPanel extends JPanel{

    //Screen Settings
    final int maxCol = 25;
    final int maxRow = 16;
    final int nodeSize = 60;
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

        //setRandomStartAndGoal();
        setStartNode(11, 4);
        setGoalNode(7, 4);


        //CUBE NODES
        setCubeNode(24, 13);
        setCubeNode(24, 10);
        setCubeNode(24, 7);

        setCubeNode(23, 13);
        setCubeNode(23, 10);
        setCubeNode(23, 7);

        //CONE NODES
        setConeNode(24, 15);
        setConeNode(24, 14);
        setConeNode(24, 12);
        setConeNode(24, 11);
        setConeNode(24, 9);
        setConeNode(24, 8);
        setConeNode(24, 6);
        setConeNode(24, 5);

        setConeNode(23, 15);
        setConeNode(23, 14);
        setConeNode(23, 12);
        setConeNode(23, 11);
        setConeNode(23, 9);
        setConeNode(23, 8);
        setConeNode(23, 6);
        setConeNode(23, 5);

        //HYBRID NODES
        setHybridNode(22, 15);
        setHybridNode(22, 14);
        setHybridNode(22, 13);
        setHybridNode(22, 12);
        setHybridNode(22, 11);
        setHybridNode(22, 10);
        setHybridNode(22, 9);
        setHybridNode(22, 8);
        setHybridNode(22, 7);
        setHybridNode(22, 6);
        setHybridNode(22, 5);

        //WALLS AND BARRIERS
        //LOADING ZONE AND HUMAND PLAYER STATIONSs
        setSolidNode(24, 0);
        setSolidNode(24, 1);
        setSolidNode(24, 2);
        setSolidNode(24, 3);

        //RED BARRIER
        setSolidNode(24, 4);
        setSolidNode(23, 4);
        setSolidNode(22, 4);
        setSolidNode(21, 4);
        setSolidNode(20, 4);
        setSolidNode(19, 4);
        setSolidNode(18, 4);
        setSolidNode(17, 4);

        //RED CHARGE STATION
        setSolidNode(17, 8);
        setSolidNode(16, 8);
        setSolidNode(15, 8);
        setSolidNode(14, 8);

        setSolidNode(17, 9);
        setSolidNode(16, 9);
        setSolidNode(15, 9);
        setSolidNode(14, 9);

        setSolidNode(17, 10);
        setSolidNode(16, 10);
        setSolidNode(15, 10);
        setSolidNode(14, 10);

        setSolidNode(17, 11);
        setSolidNode(16, 11);
        setSolidNode(15, 11);
        setSolidNode(14, 11);

        setSolidNode(17, 12);
        setSolidNode(16, 12);
        setSolidNode(15, 12);
        setSolidNode(14, 12);

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

    private void setConeNode(int col, int row){
        node[col][row].setAsConeNode();
    }

    private void setCubeNode(int col, int row){
        node[col][row].setAsCubeNode();
    }

    private void setHybridNode(int col, int row){
        node[col][row].setAsHybridNode();
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
