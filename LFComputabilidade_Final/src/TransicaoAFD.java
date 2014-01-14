
public class TransicaoAFD {

	private EstadoAFD anterior;
	private EstadoAFD seguinte;
	private Character simbolo;

	public TransicaoAFD(EstadoAFD anterior, EstadoAFD seguinte, Character simbolo) {
		this.anterior = anterior;
		this.seguinte = seguinte;
		this.simbolo = simbolo;
	}

	public EstadoAFD getAnterior() {
		return anterior;
	}

	public void setAnterior(EstadoAFD anterior) {
		this.anterior = anterior;
	}

	public EstadoAFD getSeguinte() {
		return seguinte;
	}

	public void setSeguinte(EstadoAFD seguinte) {
		this.seguinte = seguinte;
	}

	public Character getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(Character simbolo) {
		this.simbolo = simbolo;
	}
	
	

}
