import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    /**
     * 上次移动前数码0的坐标行数
     */
    int lastRow = -1;

    /**
     * 上次移动前数码0的坐标列数
     */
    int lastCol = -1;

    /**
     * 当前数码0的坐标行数
     */
    int row = 3;

    /**
     * 当前数码0的坐标列数
     */
    int col = 1;

    /**
     * g(n)
     */
    int gN = 0;

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
    private int calculate(int[][] matrix) {
        int pN = 0;
        //计算P(n)
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0 && matrix[i][j] != (4 * i) + j + 1) {
                    //目标坐标行数
                    int targetRow = matrix[i][j] / 4;
                    //目标坐标列数
                    int targetCol = matrix[i][j] - (targetRow * 4) - 1;
                    pN += Math.abs(targetRow - i) + Math.abs(targetCol - j);
                }
            }
        }
        return gN + pN;
    }

    /**
     * 生成新的可能结点
     *
     * @return
     */
    public ArrayList<Node> getNewNodes() {
        ArrayList<Node> newNodes = new ArrayList<>();

        if (row > 0 && row - 1 != lastRow) {
            //上移
            newNodes.add(new Node(row - 1, col));
        }
        if (row < result.length - 1 && row + 1 != lastRow) {
            //下移
            newNodes.add(new Node(row + 1, col));
        }
        if (col > 0 && col - 1 != lastCol) {
            //左移
            newNodes.add(new Node(row, col - 1));
        }
        if (col < result[0].length - 1 && col + 1 != lastCol) {
            //右移
            newNodes.add(new Node(row, col + 1));
        }

        for (Node node : newNodes) {
            int[][] nodeMatrix = cloneMatrix(matrix);
            int tmp = nodeMatrix[row][col];
            nodeMatrix[row][col] = nodeMatrix[node.getRow()][node.getCol()];
            nodeMatrix[node.getRow()][node.getCol()] = tmp;
            node.setMatrix(nodeMatrix);
            node.setF(calculate(node.getMatrix()));
        }

        //按照f(n)递增排序
        Collections.sort(newNodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.getF() - o2.getF();
            }
        });

        return newNodes;
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

    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println("初始状态：");
        solution.printMatrix(solution.matrix);
        System.out.println("--------------------");

        //将初始状态加入CLOSED表
        Node initStateNode = new Node(3, 1, solution.matrix);
        solution.closed.add(initStateNode);

        while (true) {
            //增加深度
            solution.gN++;
            System.out.println("STEP:" + solution.gN);

            //获取可能结点
            ArrayList<Node> stepOpen = solution.getNewNodes();

            Node bestNode = stepOpen.get(0);
            solution.printMatrix(bestNode.getMatrix());
            if (solution.isTarget(bestNode)) {
                //达到了目标状态
                break;
            }else {
                //更新状态
                solution.lastRow = solution.row;
                solution.lastCol = solution.col;
                solution.matrix = bestNode.getMatrix();
                solution.row = bestNode.getRow();
                solution.col = bestNode.getCol();

                //更新OPEN和CLOSED
                stepOpen.remove(0);
                solution.open.addAll(stepOpen);
                solution.closed.add(bestNode);
            }
        }
    }

}
