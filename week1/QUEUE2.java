import java.util.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class QUEUE2
{
    static class Reader
    {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader()
        {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException
        {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException
        {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1)
            {
                if (c == '\n')
                    break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException
        {
            int ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do
            {
                ret = ret * 10 + c - '0';
            }  while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        public long nextLong() throws IOException
        {
            long ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');
            if (neg)
                return -ret;
            return ret;
        }

        public double nextDouble() throws IOException
        {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');

            if (c == '.')
            {
                while ((c = read()) >= '0' && c <= '9')
                {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException
        {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException
        {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException
        {
            if (din == null)
                return;
            din.close();
        }
   }

   public static void main(String[] args) throws IOException
   {
      Reader s = new Reader();
      int T = s.nextInt();

      while(T > 0)
      {
         int N = s.nextInt();
         int M = s.nextInt();
         int K = s.nextInt();
         int L = s.nextInt();
         int M_smallest = M;
         int t_smallest = 1;
         int M_current = M;

         int[] A = new int[N];
         for(int i = 0;i < N;i++)
            A[i] = s.nextInt();

         Arrays.sort(A);

         for(int i = 0;i < N;i++)
         {
            //System.out.println("currently at the begining of "+A[i]);
            if(A[i] > K)
               break;

            int number_of_people_out;

            number_of_people_out =  A[i]/L;

            //System.out.println("\tpeople that went out till now "+number_of_people_out);
            if(M - number_of_people_out + i <= M_smallest)
            {
               //System.out.println("\t"+(M - number_of_people_out + i)+" <= "+M_smallest);
               M_smallest = M - number_of_people_out + i;
               t_smallest = A[i];
            }

            M_current = M - number_of_people_out + i;
            //System.out.println("\tM_smallest = "+M_smallest+" t_smallest = "+t_smallest+" M_current = "+M_current);
         }

         if(K > A[A.length - 1] && t_smallest == A[A.length - 1])
         {
            //System.out.println("\there we are at 176 with M_smallest = "+M_smallest+" t_smallest = "+t_smallest+" M_current = "+M_current);
            M_current++;
            int M_smallest_old = M_smallest;
            M_smallest = M - (K/L) + A.length;

            if(M_smallest < M_smallest_old)
            {

               t_smallest = K;
               int quotient = t_smallest/L;
               int remainder = t_smallest%L;
               int L_new = t_smallest + (L - remainder);

               //System.out.println("\there we are at 176 with M_smallest = "+M_smallest+" t_smallest = "+t_smallest+" M_current = "+M_current);

               //System.out.println("quotient = "+quotient+" remainder = "+remainder+" L_new = "+L_new);
               System.out.println((M_smallest * L +(L_new - t_smallest)));
            }
            else if(M_smallest < 0)
            {
               System.out.println("0");
            }
            else
            {
               int quotient = t_smallest/L;
               int remainder = t_smallest%L;
               int L_new = t_smallest + (L - remainder);

               //System.out.println("\there we are at 176 with M_smallest = "+M_smallest_old+" t_smallest = "+t_smallest+" M_current = "+M_current);

               //System.out.println("quotient = "+quotient+" remainder = "+remainder+" L_new = "+L_new);
               System.out.println((M_smallest_old * L +(L_new - t_smallest)));
            }


         }
         else
         {
            //System.out.println("here it is ");
            int quotient = t_smallest/L;
            int remainder = t_smallest%L;
            int L_new = t_smallest + (L - remainder); //..L_new is a multiple of L just greater than t_smallest

            //System.out.println("quotient = "+quotient+" remainder = "+remainder+" L_new = "+L_new);
            System.out.println((M_smallest * L +(L_new - t_smallest)));
         }

         T--;
      }
   }
}
/*
3 2 10 3
6 8 7
4

*/
