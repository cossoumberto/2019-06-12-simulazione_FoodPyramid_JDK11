package it.polito.tdp.food.model;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		m.creaGrafo(20);
		System.out.println(m.getGrafo());
	}

}
