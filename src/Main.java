public class Main{
    public static void main(String[] args) {
        int[][] data = {{1, 4, 6, 3},
                        {9, 7, 10, 9},
                        {4, 5, 11, 7},
                        {8, 7, 8, 5}};

        HungarianMethod h = new HungarianMethod(data);
        int[] res = h.getPerfectMatch();
        System.out.println("Вихідні дані: ");
        for (int[] row : data) {
            for (int col : row)
                System.out.printf(" %-4d", col);

            System.out.println();
        }
        System.out.println("Оптимальні значення: ");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++)
                if(res[i] == j)
                    System.out.printf("[%d]  ", data[i][j]);
                else
                    System.out.printf(" %-4d", data[i][j]);
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < res.length; i++)
            System.out.print(i + "->" + res[i] + "; ");
        System.out.println();
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if(res[i] == j)
                    sum += data[i][j];
            }
        }
        System.out.println("\nЦільова функція: ");
        System.out.println("f(x) = " + sum);

    }
}
