package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private List<String> ruoli;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private List<Artist> vertici;  //Artisti del ruolo selezionato
	private List<CoppiaIdPeso> archi;
	
	public Model()
	{
		dao = new ArtsmiaDAO();
		ruoli = dao.getRuoli();
	}

	public List<String> getRuoli() {
		return ruoli;
	}
	
	public String creaGrafo(String ruolo)
	{
		grafo = new SimpleWeightedGraph<Artist, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		vertici = dao.getVertici(ruolo);
		Graphs.addAllVertices(grafo, vertici);
		
		archi = dao.getArchi(ruolo);
		for(CoppiaIdPeso arco:archi)
		{
			Graphs.addEdgeWithVertices(grafo, arco.getA1(), arco.getA2(), arco.getPeso());
		}
		
		
		return "#Vertici: " + grafo.vertexSet().size() + " #Archi: " + grafo.edgeSet().size();
		
		
	}
	
	public List<CoppiaIdPeso> getConnessi()
	{
		Collections.sort(archi);
		return archi;
	}
	
	
	
	

}
