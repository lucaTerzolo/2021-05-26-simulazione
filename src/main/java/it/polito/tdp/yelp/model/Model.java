package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business,DefaultWeightedEdge> grafo;
	private Map<String,Business> idMap;
	
	public Model() {
		dao=new YelpDao();
		idMap=new HashMap<>();
		
		dao.getAllBusiness(idMap);
	}
	
	public List<String> getAllCitta(){
		return dao.getAllCity();
	}
	
	public void creaGrafo(int year, String city) {
		
		grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//System.out.println(dao.getAllVertici(idMap, year, city));
		Graphs.addAllVertices(this.grafo, dao.getAllVertici(this.idMap, year, city));

		System.out.println(dao.getArchi(idMap, year, city));

		for(Adiacenza a:dao.getArchi(idMap, year, city))
			Graphs.addEdge(this.grafo, a.getB1(), a.getB2(), a.getPeso()); 
	}
	
	public int vertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int archi() {
		return this.grafo.edgeSet().size();
	}
	
	public Business localeMigliore() {
		double max=0.0;
		Business migliore=null;
		for(Business b:this.grafo.vertexSet()) {
			double somma=0;
			for(DefaultWeightedEdge e:this.grafo.edgesOf(b)) {
				if(this.grafo.getEdgeSource(e).equals(b))
					somma-=this.grafo.getEdgeWeight(e);
				else if (this.grafo.getEdgeTarget(e).equals(b))
					somma+=this.grafo.getEdgeWeight(e);
			}
			if(somma>max) {
				max=somma;
				migliore=b;
			}
		}
		return migliore;
	}
}
