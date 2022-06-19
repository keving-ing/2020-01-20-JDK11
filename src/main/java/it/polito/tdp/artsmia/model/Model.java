package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	private List<Artist> percorsoMigliore;
	
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
	
	public Map<Integer, Artist> getIdMapArtisti()
	{
		return dao.getIdMapArtists();
	}
	
	public List<Artist> calcolaPercorso(Artist sorg)
	{
		percorsoMigliore = new LinkedList<Artist>();
		List<Artist> parziale = new LinkedList<>();
		parziale.add(sorg);
		Artist primoVicino = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1)).get(0);
		int peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(sorg, primoVicino));
		cercaRicorsiva(parziale, peso);
		return percorsoMigliore;
	}

	private void cercaRicorsiva(List<Artist> parziale, int peso) {
		
		Artist ultimo = parziale.get(parziale.size() - 1);
		//condizione di terminazione)
		if(parziale.size()>percorsoMigliore.size()) //la strada piú lunga é la migliore
		{
			percorsoMigliore = new LinkedList<>(parziale);
		}
		
		for(Artist v:Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) //scorro sui vicini dell'ultimo nodo sulla lista
		{
			if(!parziale.contains(v) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, v)) == peso)
			{
				parziale.add(v);
				cercaRicorsiva(parziale, peso);
				parziale.remove(parziale.size()-1);
			}
		}
				
	}
	
	
	
	

}
