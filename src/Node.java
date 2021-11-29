/**
 * @program: A*
 * @description:
 * @author: wtongxue
 * @create: 2021-11-29 12:35
 **/
public class Node {

    /**
     * 0数码坐标行
     */
    private int row;

    /**
     * 0数码坐标列
     */
    private int col;

    /**
     * f(n)
     */
    private int f;

    /**
     * 结点状态矩阵
     */
    private int[][] matrix;

    public Node(){}

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Node(int row, int col, int[][] matrix) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

}
