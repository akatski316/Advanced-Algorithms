import java.io.*;
import java.util.*;

public class Evacuation 
{
    private static FastScanner in;
    public static final int INFINITY = 200000001;

    public static void main(String[] args) throws IOException 
    {
        in = new FastScanner();
        FlowGraph graph = readGraph();

        //printGraph(graph);
        System.out.println(maxFlow(graph, 0, graph.size() - 1));
    }

    private static int maxFlow(FlowGraph Graph, int from, int to) 
    {
        int flow = 0;
        int temp;
        /* your code goes here */
        while(true)
        {	
			ArrayList<Integer> Path = getShortestPathin(Graph,from,to);		
			temp = Path.get(Path.size() - 1);
			Path.remove(Path.size() - 1);
			
			if(Path.size() == 0)
				return flow;

			for(int i : Path)
				Graph.addFlow(i,temp);

			flow += temp;
		}	        
    }

    public static ArrayList<Integer> getShortestPathin(FlowGraph Graph,int s,int t)
    {
    	Vertex vertices[] = new Vertex[Graph.graph.length];
    	PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(); 
    	
    	for(int i = 0;i < vertices.length;i++)
    	{
    		vertices[i] = new Vertex(i);
    		queue.add(vertices[i]);
    	}

    	vertices[s].distance = 0;
    	ArrayList<Integer> toReturn = new ArrayList<Integer>();

    	while(!queue.isEmpty())
    	{   		
    		Vertex node = queue.poll();

    		for (int i = 0;i < Graph.graph[node.index].size();i++) 
    		{
    			if(Graph.edges.get(Graph.graph[node.index].get(i)).capacity - Graph.edges.get(Graph.graph[node.index].get(i)).flow > 0 || (Graph.edges.get(Graph.graph[node.index].get(i)).capacity == 0 && Graph.edges.get(Graph.graph[node.index].get(i) - 1).capacity == Graph.edges.get(Graph.graph[node.index].get(i) - 1).flow))
    			{
    				if(vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to].distance > node.distance + 1 && Graph.edges.get(Graph.graph[node.index].get(i)).to != s)
    				{
    					vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to].distance = node.distance + 1;
    					vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to].previous = node.index;
    					vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to].indexOfBackEdge = Graph.graph[node.index].get(i);

    					if(queue.contains(vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to]))
    						queue.remove(vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to]);

    					queue.add(vertices[Graph.edges.get(Graph.graph[node.index].get(i)).to]);
    				}
    			}
    		}
    	}

    	Vertex temp = vertices[t];
    	Vertex mintemp = temp;
    	int minflow = INFINITY;

    	while(temp.previous != -1)
    	{	
    		toReturn.add(temp.indexOfBackEdge); ///////...bugged
    		if(Graph.edges.get(temp.indexOfBackEdge).capacity - Graph.edges.get(temp.indexOfBackEdge).flow < minflow && Graph.edges.get(temp.indexOfBackEdge).capacity != 0)
    		{
    			minflow = Graph.edges.get(temp.indexOfBackEdge).capacity - Graph.edges.get(temp.indexOfBackEdge).flow;
    		}
       		temp = vertices[temp.previous];
    	}

    	toReturn.add(minflow);
    	return toReturn;
    }

    static FlowGraph readGraph() throws IOException 
    {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count);

        for (int i = 0; i < edge_count; ++i) 
        {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
            if(from != to)
            	graph.addEdge(from, to, capacity);
        }
        return graph;
    }

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

    static class Edge 
    {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) 
        {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

     /*This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. 
     */

    static class FlowGraph 
    {
        /* List of all - forward and backward - edges */
        public List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        public List<Integer>[] graph;

        public FlowGraph(int n) 
        {
            this.graph = (ArrayList<Integer>[])new ArrayList[n];
            
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();

            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int capacity) 
        {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. 
            */

            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() 
        {
            return graph.length;
        }

        public List<Integer> getIds(int from) 
        {
            return graph[from];
        }

        public Edge getEdge(int id) 
        {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) 
        {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! 
            */

            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    static class FastScanner 
    {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException 
        {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) 
            {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}

/*
5 7
1 2 2
2 5 5
1 3 6
3 4 2
4 5 1
3 2 3
2 4 1

4 5
1 2 10000
1 3 10000
2 3 1
3 4 10000
2 4 10000
*/

