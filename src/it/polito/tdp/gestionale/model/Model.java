package it.polito.tdp.gestionale.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


import it.polito.tdp.gestionale.db.DidatticaDAO;

public class Model {

	private List<Corso> corsi;
	private List<Studente> studenti;
	private DidatticaDAO didatticaDAO;
	private SimpleGraph <Nodo, DefaultEdge> grafo;
	private Map <Integer, Studente> mappaStudenti;
	

	public Model() {
		grafo = new SimpleGraph <Nodo, DefaultEdge>(DefaultEdge.class);
		didatticaDAO = new DidatticaDAO();
		mappaStudenti = new HashMap<Integer, Studente>();
	}
	
	
	public void generaGrafo(){

		corsi = this.getTuttiCorsi();
		studenti = this.getTuttiStudenti();
		
		System.out.println("Numero di studenti pari a "+studenti.size()+"\n");
		System.out.println("Numero di corsi pari a "+corsi.size()+"\n");
		
		Graphs.addAllVertices(grafo, studenti);
		Graphs.addAllVertices(grafo, corsi);
		
		System.out.println("Numero vertici del grafo "+grafo.vertexSet().size()+"\n");
		
		
		for (Corso corso : corsi){
			for (Studente studente : corso.getStudenti()){
				grafo.addEdge(corso, studente);
			}
		}
		System.out.println("Numero archi: "+grafo.edgeSet().size()+"\n");
		System.out.println("Grafo CREATO!!"+"\n");
		
	}


	public List<Corso> getTuttiCorsi() {
		if(corsi ==null){
			corsi = didatticaDAO.getTuttiICorsi();
			this.getTuttiStudenti(); //cosi mi assicuro di avere preso già tutti gli studentu
			for (Corso c : corsi){
				didatticaDAO.getStudentiIscrittiAlCorso(c, mappaStudenti);
				
			}
		} 
		return corsi;
	}


	public List<Studente> getTuttiStudenti() {
		if (studenti == null){
			studenti=didatticaDAO.getTuttiStudenti();
			for (Studente studente : studenti){
				mappaStudenti.put(studente.getMatricola(), studente);
			}
		}
		return studenti;
		
	}
	public List <Integer> getStatCorsi(){
		List <Integer> statCorsi = new ArrayList<Integer>();
		
		//inizializzo a zero le statistiche
		for (int i=0; i<corsi.size()+1; i++){
			statCorsi.add(0);
		}
		
		//Aggoingo le statistiche
		for (Studente studente: studenti){
			int ncorsi= Graphs.neighborListOf(grafo, studente).size();
			int counter = statCorsi.get(ncorsi);
			counter++;
			statCorsi.set(ncorsi,counter);
		}
		return statCorsi;
		
	}
	
}
