package GAI.hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    Main() {
    }

    private static void inputMatrix(final int[][] matrix, String Filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(Filename));
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
    }

    private static void printMatrix(final FileWriter fileWriter,
                                    final int[][] matrix) throws IOException
    {
        boolean hasNegative = false;
        int     maxValue    = 0;

        for (final int[] row : matrix) {
            for (final int element : row) {
                int temp = element;
                if (element < 0) {
                    hasNegative = true;
                    temp = -temp;
                }
                if (temp > maxValue)
                    maxValue = temp;
            }
        }

        int len = Integer.toString(maxValue).length() + 1;
        if (hasNegative)
            ++len;

        final String formatString = "%" + len + "d";

        for (final int[] row : matrix) {
            for (final int element : row)
                fileWriter.write(String.format(formatString, element));

            fileWriter.write("\n");
        }
    }

    private static void print(final int[][] matrix1,
                              final int[][] matrix2,
                              final int[][] resultMatrix)
    {
        try (final FileWriter fileWriter = new FileWriter("Matrix.txt", false)) {
            fileWriter.write("First ut:\n");
            printMatrix(fileWriter, matrix1);

            fileWriter.write("\nSecond ut:\n");
            printMatrix(fileWriter, matrix2);

            fileWriter.write("\nResult:\n");
            printMatrix(fileWriter, resultMatrix);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[][] Multiplier(final int[][] matrix1,
                                      final int[][] matrix2,
                                      int threadCount)
    {
        assert threadCount > 0;

        final int rowCount = matrix1.length;
        final int colCount = matrix2[0].length;
        final int[][] result = new int[rowCount][colCount];

        final int cellsForThread = (rowCount * colCount) / threadCount;
        int firstIndex = 0;
        final Matrix[] Threads = new Matrix[threadCount];


        for (int Index = threadCount - 1; Index >= 0; --Index) {
            int lastIndex = firstIndex + cellsForThread;
            if (Index == 0) {
                lastIndex = rowCount * colCount;
            }
            Threads[Index] = new Matrix(matrix1, matrix2, result, firstIndex, lastIndex);
            Threads[Index].start();
            firstIndex = lastIndex;
        }

        try {
            for (final Matrix multiplierThread : Threads)
                multiplierThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
    static Scanner scanner;

    static {
        try {
            scanner = new Scanner(new File("mat1.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Scanner scanner2;

    static {
        try {
            scanner2 = new Scanner(new File("mat2.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static int R1  = scanner.nextInt();
    static int C1  = scanner.nextInt();
    static int R2 = scanner2.nextInt();
    static int C2 = scanner2.nextInt();


    public static void main(String[] args) throws FileNotFoundException {
        final int[][] matrix1  = new int[R1][C1];
        final int[][] matrix2 = new int[R2][C2];

        inputMatrix(matrix1, "mat1.txt");
        inputMatrix(matrix2, "mat2.txt");


        if (C1 == R2){
            final int[][] result = Multiplier(matrix1, matrix2, Runtime.getRuntime().availableProcessors());
            print(matrix1, matrix2, result);
        }
        else System.out.println("Перемножение невозможно");

    }
}