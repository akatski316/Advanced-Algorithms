import java.util.*;

public class Test
{
	public static int INFINITY = 100000;
	public static class Vertex implements Comparable<Vertex>
    {
    	int index;
    	int distance;
    	int previous;
    	int indexOfBackEdge;

    	public Vertex(int i)
    	{
    		index = i;
    		distance = INFINITY;
    		previous = -1;
    		indexOfBackEdge = -1;
    	}

    	@Override
    	public int compareTo(Vertex o)
    	{
    		if(this.distance > o.distance)
    			return 1;
    		else if(this.distance < o.distance) 
    			return -1;
            else 
            	return 0;
    	}
    }

	public static void main(String[] args) 
	{
		Vertex vertices[] = new Vertex[5];
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		for(int i = 0;i < vertices.length;i++)
    	{
    		vertices[i] = new Vertex(i);
    		queue.add(vertices[i]);
    	}
    	vertices[0].distance = 0;
    	vertices[2].distance = 1;
    	vertices[1].distance = 2;


    	while(!queue.isEmpty())
    		System.out.print((queue.poll().index + 1) + " ");
		
		
	}
}