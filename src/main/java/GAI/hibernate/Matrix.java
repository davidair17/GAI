package GAI.hibernate;

class Matrix extends Thread
{
    private final int[][] M1;
    private final int[][] M2;
    private final int[][] result;
    private final int firstIndex;
    private final int lastIndex;
    private final int Len;
    
    public Matrix(final int[][] M1,
                  final int[][] M2,
                  final int[][] result,
                  final int firstIndex,
                  final int lastIndex)
    {
        this.M1  = M1;
        this.M2 = M2;
        this.result = result;
        this.firstIndex   = firstIndex;
        this.lastIndex    = lastIndex;

        Len = M2.length;
    }

    private void multiplier(final int row, final int col)
    {
        int sum = 0;
        for (int i = 0; i < Len; ++i)
            sum += M1[row][i] * M2[i][col];
        result[row][col] = sum;
    }

    public void run()
    {
        System.out.println(getName() + " started.");

        final int colCount = M2[0].length;  // Число столбцов результирующей матрицы.
        for (int index = firstIndex; index < lastIndex; ++index)
            multiplier(index / colCount, index % colCount);

        System.out.println(getName() + " finished.");
    }
}