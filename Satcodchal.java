import java.util.*;

public class Satcodchal{
   
   public static void main(String[] args){
      Scanner in = new Scanner(System.in);
      int N = in.nextInt();
      int K = in.nextInt();

      int[] A = new int[N];
      HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();

      for (int i = 0;i < N-1 ;i++){
         A[i] = in.nextInt();
         int check = K - A[i];

         if(map.get(check)!= null)
         {
            int index = map.get(check);
            System.out.println("At positions "+index+" and "+i+" our numbers "+A[i]+" and "+check +" are summing up to "+K);
            System.exit(0);
         }
         map.put(A[i],i);
      }
      System.out.println("something wrong");
   }
}
