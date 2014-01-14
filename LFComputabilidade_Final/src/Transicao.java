import java.util.ArrayList;


public class Transicao {
	private Estado anterior;
	private Estado seguinte;
	private ArrayList<Character> simbolos; 
	
	public Transicao(Estado anterior, Estado seguinte){
		this.anterior = anterior;
		this.seguinte = seguinte;
		simbolos = new ArrayList<Character>();
	}
	
	

	public Estado getAnterior() {
		return anterior;
	}

	public void setAnterior(Estado anterior) {
		this.anterior = anterior;
	}

	public Estado getSeguinte() {
		return seguinte;
	}

	public void setSeguinte(Estado seguinte) {
		this.seguinte = seguinte;
	}

	public ArrayList<Character> getSimbolos() {
		return simbolos;
	}
	
	public void setSimbolo(Character simb){
		this.simbolos.add(simb);
	}

}
