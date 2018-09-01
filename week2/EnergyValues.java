import java.io.IOException;
import java.util.*;


class Equation {
    Equation(double a[][], double b[]) {
        this.a = a;
        this.b = b;
    }

    double a[][];
    double b[];
}

class Position {
    Position(int column, int raw) {
        this.column = column;
        this.raw = raw;
    }

    public int column;
    public int raw;
}

class EnergyValues {
    static Equation ReadEquation() throws IOException 
    {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double a[][] = new double[size][size];
        double b[] = new double[size];

        for (int raw = 0; raw < size; ++raw) 
        {
            for (int column = 0; column < size; ++column)
                a[raw][column] = scanner.nextInt();
            b[raw] = scanner.nextInt();
        }
        return new Equation(a, b);
    }

    static Position SelectPivotElement(double a[][],int x,int y) 
    {
        for(int i = y;i < a.length;i++)  //in ith column search all rows
        {
        	for(int j = x ;j < a.length;j++)
        	{
        		if(a[j][i] != 0)
        		{
        			return new Position(i,j);
        		}
        	}
        }
        return null;
    }

    /* there are possibilities that our matrix must not be arranged in proper manner like
     * --       --                                                 --       --
       | 0 0 0 3 |                                                 | 4 1 2 3 |
       | 0 0 5 6 |	 need to swap lines to make it like this       | 7 0 8 9 |
       | 7 0 8 9 |  ------------------------------------------>    | 0 0 5 6 |
       | 4 1 2 3 |												   | 0 0 0 3 |		
       --       --	                                               --       --
    */   

    static void SwapLines(double a[][], double b[], boolean used_raws[], Position pivot_element) 
    {
        int size = a.length;
        
        /*swapping two rows depending upon pivot in this loop
         *eg: if pivot is (0,1) then it will swap row 0 and row 1 because pivot is at (0,1) for input like
         * --       --
           | 0 3 4 5 |
           | 3 2 4 7 |   here first and second rows are needed to be swapped because row 0 must be completely full
           | ..blah..|
         * --	    --
         * similarly if pivot is (3,2) the it will swap row 3 with row 2
         */

        for (int column = 0; column < size; ++column) 
        {
            double tmpa = a[pivot_element.column][column];
            a[pivot_element.column][column] = a[pivot_element.raw][column];
            a[pivot_element.raw][column] = tmpa;
        }

        //swapping rhs of two rows
        double tmpb = b[pivot_element.column];
        b[pivot_element.column] = b[pivot_element.raw];
        b[pivot_element.raw] = tmpb;

        pivot_element.raw = pivot_element.column;
    }

    static void ProcessPivotElement(double a[][], double b[], Position pivot_element) 
    {
        for(int row = pivot_element.raw + 1; row < a.length; row++)
        {
        	double ratio = a[row][pivot_element.column] / a[pivot_element.raw][pivot_element.column];
        	for(int col = pivot_element.column; col < a.length; col++)
        	{
        		a[row][col] = a[row][col] - (a[pivot_element.raw][col] * ratio); 
        	}
        	b[row] = b[row] - (b[pivot_element.raw] * ratio); 
        }
    }

    static double[] SolveEquation(Equation equation) 
    {
        double a[][] = equation.a;
        double b[] = equation.b;
        int size = a.length;
        boolean[] used_columns = new boolean[size];
        boolean[] used_raws = new boolean[size];
        Position pivot_element = new Position(-1,-1);
      
        for (int step = 0; step < size; ++step) 
        {
            pivot_element = SelectPivotElement(a,pivot_element.raw+1, pivot_element.column+1);
            //System.out.println("pivot element is at ("+pivot_element.raw+" , "+pivot_element.column+")");
            
            SwapLines(a, b, used_raws, pivot_element);
            //System.out.println("after swapping its pivot_element is at ("+pivot_element.raw+" , "+pivot_element.column+")");
            //print(a,b);
            ProcessPivotElement(a, b, pivot_element);
        }

        return BackTracking(a,b);
    }

    static double[] BackTracking(double[][] a,double[] b)
    {
    	double[] ans = new double[a.length];
    	for(int row = a.length - 1; row >= 0; row--)
    	{
    		for(int col = a.length - 1; col >= row; col--)
    		{
    			b[row] = b[row] - (a[row][col] * ans[col]);
    		}
    		ans[row] = b[row]/a[row][row];
    	} 
    	return ans;
    }

    static void print(double[][] a,double[] b)
    {
    	for(int i = 0;i < a.length;i++)
    	{
    		for(int j = 0;j < a[0].length;j++)
    		{
    			System.out.print(a[i][j]+" ");
    		} 
    		System.out.print("|"+b[i]+" ");
    		System.out.println();
    	}
    }

    static void PrintColumn(double column[]) {
        int size = column.length;
        for (int raw = 0; raw < size; ++raw)
            System.out.printf("%.20f\n", column[raw]);
    }

    public static void main(String[] args) throws IOException {
        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
