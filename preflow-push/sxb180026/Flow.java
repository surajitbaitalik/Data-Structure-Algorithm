package sxb180026;

//Starter code for max flow

import sxb180026.Graph;
import sxb180026.Graph.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Flow extends Graph.GraphAlgorithm<Flow.FlowVertex> {

	private HashMap<Edge, Integer> capacity;
	private HashMap<Edge, Integer> flow = new HashMap<>();
	Queue<Vertex> vertexList = new LinkedList<Vertex>();
	private Vertex source;
	private Vertex sink;

	public static class FlowVertex implements Factory {
		int height;
		int excess;

		public FlowVertex(Vertex u) {
			height = 0;
			excess = 0;
		}

		public FlowVertex make(Vertex u) {
			return new FlowVertex(u);
		}
	}

	private int getExcess(Vertex u) {
		return get(u).excess;
	}

	private int getHeight(Vertex u) {
		return get(u).height;
	}

	private void setExcess(Vertex u, int excess) {
		get(u).excess = excess;
	}

	private void setHeight(Vertex u, int height) {
		get(u).height = height;
	}

	public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {

		super(g, new FlowVertex(null));
		this.capacity = capacity;
		this.source = s;
		this.sink = t;
	}

	// Return max flow found. Use either relabel to front or FIFO.
	public int preflowPush() {

		initialize(vertexList);
		while (!vertexList.isEmpty()) {
			Vertex u = vertexList.remove();
			discharge(u);
			if (getExcess(u) > 0) {
				relabel(u);
			}
		}
		return getExcess(sink);
	}

	private void initialize(Queue<Vertex> vertexList) {

		for (Vertex u : g) {
			for (Edge e : g.outEdges(u)) {
				flow.put(e, 0);
			}
		}

		for (Vertex u : g) {
			setExcess(u, 0);
			setHeight(u, getInitialHeight(u));
		}

		for (Edge e : g.outEdges(source)) {
			flow.put(e, capacity.get(e));
			setExcess(source, getExcess(source) - capacity.get(e));
			Vertex u = e.otherEnd(source);
			setExcess(u, getExcess(u) - capacity.get(e));
			vertexList.add(u);
		}

	}

	private int getInitialHeight(Vertex u) {
		if (u == source)
			return g.n;
		else if (u == sink)
			return 0;
		else {
			int height = 0;
			Stack<Vertex> vertexStack = new Stack<>();
			vertexStack.add(u);
			List<Vertex> seenVertexList = new ArrayList<>();
			seenVertexList.add(u);
			while (vertexStack.peek() != sink) {
				Vertex current = vertexStack.pop();
				for (Edge e : g.outEdges(current)) {
					Vertex v = e.otherEnd(current);
					if (!seenVertexList.contains(v)) {
						vertexStack.add(v);
						height += 1;
					}
				}
			}
			return height;
		}

	}

	private void discharge(Vertex u) {
		for (Edge e : g.getEdgeArray()) {
			if ((e.fromVertex() == u) || (e.toVertex() == u)) {
				Vertex v = e.otherEnd(u);
				// admissable edge
				if (getHeight(u) == (getHeight(v) + 1)) {
					int r = capacity.get(e) - flow.get(e);
					int delta = Math.min(getExcess(u), r);
					if (e.fromVertex() == u) {
						flow.put(e, flow.get(e) + delta);
					} else {
						flow.put(e, flow.get(e) - delta);
					}
					setExcess(u, (getExcess(u) - delta));
					setExcess(v, (getExcess(v) - delta));
					if (!vertexList.contains(v)) {
						vertexList.add(v);
					}
					if (getExcess(u) == 0)
						return;
				}
			}

		}

	}

	private void relabel(Vertex u) {

	}

	// flow going through edge e
	public int flow(Edge e) {
		return 0;
	}

	// capacity of edge e
	public int capacity(Edge e) {
		return 0;
	}

	/*
	 * After maxflow has been computed, this method can be called to get the
	 * "S"-side of the min-cut found by the algorithm
	 */
	public Set<Vertex> minCutS() {
		return null;
	}

	/*
	 * After maxflow has been computed, this method can be called to get the
	 * "T"-side of the min-cut found by the algorithm
	 */
	public Set<Vertex> minCutT() {
		return null;
	}
}
