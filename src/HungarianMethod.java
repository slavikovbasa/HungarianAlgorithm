import java.util.Arrays;
import java.util.LinkedList;

class HungarianMethod {
    //Source array with all costs
    private int[][] source;
    /*markedRows[i] - contains boolean value that indicates if i-th row is marked
    markedCols[j] - contains boolean value that indicates if j-th column is marked*/
    private boolean[] markedRows, markedCols;
    /*chosenInRow[i] - contains best value column number for i-th row
    chosenInCol[j] - contains best value row number for i-th column*/
    private int[] chosenInRow, chosenInCol;
    private int rows, cols;

    HungarianMethod(int[][] source){
        rows = source.length;
        cols = source[0].length;
        this.source = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            this.source[i] = Arrays.copyOf(source[i], cols);

        chosenInRow = new int[rows];
        Arrays.fill(chosenInRow, -1);
        chosenInCol = new int[cols];
        Arrays.fill(chosenInCol, -1);
        markedRows = new boolean[rows];
        Arrays.fill(markedRows, false);
        markedCols = new boolean[cols];
        Arrays.fill(markedCols, false);
    }

    //Get best solution
    int[] getPerfectMatch(){
        reduce();
        genInitialMatch();

        int unmatched = getUnmatchedRow();
        while(unmatched >= 0){
            Arrays.fill(markedRows, false);
            Arrays.fill(markedCols, false);

            LinkedList<Cell> path = findAugmentedPath(unmatched, new LinkedList<>());
            if(!path.isEmpty()){
                for (Cell point : path)
                    if (path.indexOf(point) % 2 == 0){
                        chosenInRow[point.row] = point.col;
                        chosenInCol[point.col] = point.row;
                    }
            } else {
                for (int i = 0; i < rows; i++)
                    if(chosenInRow[i] == -1) markRowsAndCols(i);

                int minValue = Integer.MAX_VALUE;
                for (int i = 0; i < rows; i++)
                    for (int j = 0; j < cols; j++)
                        if (markedRows[i] && !markedCols[j] && source[i][j] < minValue)
                            minValue = source[i][j];

                for (int i = 0; i < rows; i++)
                    if (markedRows[i])
                        for (int j = 0; j < cols; j++)
                            source[i][j] -= minValue;

                for (int j = 0; j < cols; j++)
                    if (markedCols[j])
                        for (int i = 0; i < rows; i++)
                            source[i][j] += minValue;
            }
            unmatched = getUnmatchedRow();
        }
        return chosenInRow;
    }

    //Subtract min values from rows and columns
    private void reduce(){
        int min;
        //Subtract min value from rows
        for (int i = 0; i < rows; i++) {
            min = source[i][0];
            for (int j = 1; j < cols; j++)
                if(source[i][j] < min) min = source[i][j];

            for (int j = 0; j < cols; j++)
                source[i][j] -= min;
        }

        //Subtract min value from cols
        for (int j = 0; j < cols; j++) {
            min = source[0][j];
            for (int i = 1; i < rows; i++)
                if(source[i][j] < min) min = source[i][j];

            for (int i = 0; i < rows; i++)
                source[i][j] -= min;

        }
    }

    //Get initial match, matching first possible minimal values
    private void genInitialMatch(){
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if(source[i][j] == 0 && chosenInRow[i] == -1 && chosenInCol[j] == -1) {
                    chosenInRow[i] = j;
                    chosenInCol[j] = i;
                    break;
                }
    }

    //Get row index that is not matched yet
    private int getUnmatchedRow(){
        for (int i = 0; i < rows; i++)
            if(chosenInRow[i] < 0) return i;

        return -1;
    }

    //Recursive method that returns list of cells in the augmented path if it exists, else empty list
    private LinkedList<Cell> findAugmentedPath(int row, LinkedList<Cell> head){
        LinkedList<Cell> resultPoints = new LinkedList<>();
        for (int j = 0; j < cols; j++) {
            if(j != chosenInRow[row] && source[row][j] == 0) {
                int chosenRowForJ = chosenInCol[j];

                if(head.contains(new Cell(chosenRowForJ, j)))
                    continue;

                resultPoints.add(new Cell(row, j));
                if(chosenRowForJ < 0)
                    return resultPoints;

                resultPoints.add(new Cell(chosenRowForJ, j));
                head.addAll(resultPoints);

                LinkedList<Cell> tail = findAugmentedPath(chosenRowForJ, head);
                if(tail.isEmpty()) {
                    resultPoints.remove(resultPoints.getLast());
                    resultPoints.remove(resultPoints.getLast());
                    continue;
                }
                resultPoints.addAll(tail);
                return resultPoints;
            }
        }
        return resultPoints;
    }

    //Recursive method that writes values to markedRows and markedCols
    private void markRowsAndCols(int row){
        if(row < 0)
            return;

        markedRows[row] = true;
        for (int j = 0; j < cols; j++)
            if(j != chosenInRow[row] && source[row][j] == 0) {
                markedCols[j] = true;
                markRowsAndCols(chosenInCol[j]);
            }
    }

    private class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object that) {
            if(!(that instanceof Cell))
                return false;
            return (this.row == ((Cell)that).row) && (this.col == ((Cell)that).col);
        }
    }
}
