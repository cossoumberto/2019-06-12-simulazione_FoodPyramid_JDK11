package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {

	private FoodDAO dao;
	private Map<Integer, Condiment> idMapCondiments;
	private Graph<Condiment, DefaultWeightedEdge> grafo;
	List<Condiment> bestDieta;
	Double bestCalorie;
	
	public Model() {
		dao = new FoodDAO();
		idMapCondiments = new HashMap<>();
		dao.listAllCondiment(idMapCondiments);
	}
	
	public void creaGrafo(double calories) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getCondimentMaxCalories(calories, idMapCondiments));
		for(Coppia c : dao.getCoppieCondiments(calories, idMapCondiments))
			Graphs.addEdge(grafo, c.getC1(), c.getC2(), c.getPeso());
	}
	
	public Graph<Condiment, DefaultWeightedEdge> getGrafo(){
		return grafo;
	}

	public Integer getNumCibiCondiment(Condiment c) {
		return dao.getNumCibiCondiment(c);
	}
	
	public void archiDieta() {
		List<DefaultWeightedEdge> list = new ArrayList<>();
		for(Condiment c1 : bestDieta)
			for(Condiment c2 : bestDieta)
				if(!c1.equals(c2))
					if(Graphs.neighborListOf(grafo, c1).size()>0 && Graphs.neighborListOf(grafo, c1).contains(c2)
							&& list.contains(grafo.getEdge(c1, c2)))
						list.add(grafo.getEdge(c1, c2));
		System.out.println(list);
	}
	
	public List<Condiment> dieta(Condiment c){
		bestDieta = new ArrayList<>();
		bestCalorie = 0.0;
		List<Condiment> parziale = new ArrayList<>();
		parziale.add(c);
		cerca(parziale, 0, c.getCondiment_calories());
		return bestDieta;
	}

	private void cerca(List<Condiment> parziale, int i, Double calories) {
		System.out.println(parziale);
		if(parziale.size()>=grafo.vertexSet().size()-1) {
			if(calories>bestCalorie) {
				bestCalorie = calories;
				bestDieta = new ArrayList<>(parziale);
			}
		return;
		} else {
			for(Condiment c : grafo.vertexSet())
				if(!parziale.contains(c)) {
					Condiment vicino = null;
					int presenti = 0;
					if(Graphs.neighborListOf(grafo,c)!=null) {
						for(Condiment v : Graphs.neighborListOf(grafo, c)) {
							if(parziale.contains(v))
								presenti++;
							if(presenti==1)
								vicino = v;
						}
					}
					if(presenti==0)
						parziale.add(c);
					else if(presenti==1) {
						int presenti1 = 0;
						for(Condiment v : Graphs.neighborListOf(grafo, vicino))
							if(parziale.contains(v))
								presenti1++;
						if(presenti1==0)
							parziale.add(c);
					}
					cerca(parziale, i+1, calories+c.getCondiment_calories());
				}
			parziale.remove(parziale.size()-1);
		}
	}
}
