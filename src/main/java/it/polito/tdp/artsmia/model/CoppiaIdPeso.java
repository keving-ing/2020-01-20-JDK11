package it.polito.tdp.artsmia.model;

public class CoppiaIdPeso implements Comparable<CoppiaIdPeso>{
	
	private Artist a1;
	private Artist a2;
	private int peso;
	
	public CoppiaIdPeso(Artist a1, Artist a2, int peso) {
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}

	public Artist getA1() {
		return a1;
	}

	public Artist getA2() {
		return a2;
	}

	public int getPeso() {
		return peso;
	}

	@Override
	public int compareTo(CoppiaIdPeso o) {
		return -(this.getPeso() - o.getPeso());
	}

	@Override
	public String toString() {
		return a1.getName() + " - " + a2.getName() + " (Esposizioni comuni= " + peso + ")\n";
	}

	
	
	
	
	
	
	

}
