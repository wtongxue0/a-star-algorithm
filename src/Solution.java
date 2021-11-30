import java.util.*;

/**
 * @program: A*
 * @description:
 * @author: wtongxue
 * @create: 2021-11-28 13:50
 **/
public class Solution {

    /*
        启发函数：f(n) = g(n) + P(n)
        g(n):从初始结点 s 到结点 n 路径的耗散值
        P(n):每一个数码与其目标位置间的距离的总和
     */

    /**
     * 初始状态
     */
    int[][] matrix = {{5, 1, 2, 4}, {9, 6, 3, 8}, {13, 15, 10, 11}, {14, 0, 7, 12}};

    /**
     * 目标状态
     */
    int[][] result = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};

    /**
     * OPEN表
     */
    ArrayList<Node> open = new ArrayList<>();

    /**
     * CLOSED表
     */
    ArrayList<Node> closed = new ArrayList<>();

    public Solution() {
        int row=0;
        int col=0;
        //查找初始状态中空位数码0的坐标
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix[0].length;j++){
                if (matrix[i][j]==0){
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        //构造初始状态结点加入OPEN表
        Node initStateNode = new Node(row, col, matrix);
        open.add(initStateNode);
    }

    /**
     * 以矩阵形式输出结点
     *
     * @param matrix
     */
    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%-6d", matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * 复制矩阵
     *
     * @param matrix
     * @return
     */
    private int[][] cloneMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix[0].length; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        return newMatrix;
    }

    /**
     * 计算f(n)
     *
     * @return
     */
    private int calculate(Node node) {
        int pN = 0;
        //计算P(n)
        int[][] matrix = node.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0 && matrix[i][j] != (4 * i) + j + 1) {
                    //目标坐标行数
                    int targetRow = matrix[i][j] / 4;
                    //目标坐标列数
                    int targetCol = matrix[i][j] - (targetRow * 4) - 1;

                    if (targetCol==-1){
                        targetRow-=1;
                        targetCol = 3;
                    }

                    pN += Math.abs(targetRow - i) + Math.abs(targetCol - j);
                }
            }
        }
        return node.getG() + pN;
    }

    /**
     * 更新OPEN表
     *
     * @return
     */
    public void updateOpen(Node node) {

        if (node.getRow()>0){
            //上移
            addNode(node,node.getRow()-1,node.getCol());
        }

        if (node.getRow()<result.length-1){
            //下移
            addNode(node,node.getRow()+1,node.getCol());
        }

        if (node.getCol()>0){
            //左移
            addNode(node,node.getRow(),node.getCol()-1);
        }

        if (node.getCol()<result[0].length-1){
            //右移
            addNode(node,node.getRow(),node.getCol()+1);
        }

    }

    /**
     * 判断结点状态，将符合要求的结点加入OPEN表
     *
     * @param parentNode
     * @param row
     * @param col
     */
    public void addNode(Node parentNode,int row,int col){
        Node node = new Node(row,col);
        //复制矩阵，更新复制后的矩阵为移动后的状态
        int[][] nodeMatrix = cloneMatrix(parentNode.getMatrix());
        int tmp = nodeMatrix[node.getRow()][node.getCol()];
        nodeMatrix[node.getRow()][node.getCol()] = nodeMatrix[parentNode.getRow()][parentNode.getCol()];
        nodeMatrix[parentNode.getRow()][parentNode.getCol()] = tmp;
        node.setMatrix(nodeMatrix);
        //设置结点字段值
        node.setParent(parentNode);
        node.setF(calculate(node));
        node.setG(node.getG()+1);

        if (isIn(closed,node)){
            //如果在CLOSED表中，代表以及拓展过该结点，不进行操作
            return;
        }

        if (!isIn(open,node)) {
            //如果不在OPEN表，加入OPEN表
            open.add(node);
        }

    }

    /**
     * 判断是否到达目标状态
     *
     * @param node
     * @return
     */
    public boolean isTarget(Node node) {
        int[][] nodeMatrix = node.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                if (result[i][j] != nodeMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否在open或closed表中
     *
     * @param table
     * @param node
     * @return
     */
    public boolean isIn(ArrayList<Node> table,Node node){
        for (Node tableNode : table){
            if (isSameMatrix(tableNode.getMatrix(),node.getMatrix())){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个状态的数码矩阵是否相同
     *
     * @param matrix1
     * @param matrix2
     * @return
     */
    public boolean isSameMatrix(int[][]matrix1,int[][]matrix2){
        for (int i=0;i<matrix2.length;i++){
            for (int j=0;j<matrix2[0].length;j++){
                if (matrix1[i][j]!=matrix2[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();

        while (!solution.open.isEmpty()){
            //将OPEN表按照f(n)递增排序
            Collections.sort(solution.open, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.getF() - o2.getF();
                }
            });

            //选择f(n)最小的结点
            Node firstNode = solution.open.get(0);

            if (solution.isTarget(firstNode)){
                //达到目标状态
                Stack<Node> stack = new Stack<>();
                Node node = firstNode;
                while (node!=null){
                    //回溯结果
                    stack.push(node);
                    node = node.getParent();
                }
                //输出结果
                int step = 0;
                while (!stack.isEmpty()){
                    System.out.println("step:"+step++);
                    solution.printMatrix(stack.pop().getMatrix());
                }
                break;
            }else{
                //将结点n移出open表,加入closed表
                solution.closed.add(firstNode);
                solution.open.remove(0);
                //遍历结点n的邻近结点
                solution.updateOpen(firstNode);
            }
        }
    }

}

