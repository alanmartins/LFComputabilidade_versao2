import java.util.ArrayList;


public class AFD {
	
	private EstadoAFD inicial;
	private ArrayList<EstadoAFD> estados = new ArrayList<EstadoAFD>();
	private ArrayList<EstadoAFD> finais = new ArrayList<EstadoAFD>();
	private ArrayList<TransicaoAFD> transicoes = new ArrayList<TransicaoAFD>();
	
	
	public AFD(){

	}

	public EstadoAFD getInicial() {
		return inicial;
	}


	public void setInicial(EstadoAFD inicial) {
		this.inicial = inicial;
	}


	public ArrayList<EstadoAFD> getEstados() {
		return estados;
	}


	public void setEstados(ArrayList<EstadoAFD> estados) {
		this.estados = estados;
	}


	public ArrayList<EstadoAFD> getFinais() {
		return finais;
	}


	public void addFinal(EstadoAFD estadoFinal) {
		this.finais.add(estadoFinal);
	}

	public void copiarFinais(ArrayList<EstadoAFD> estadosFinais){
		this.finais.addAll(estadosFinais);
	}

	public ArrayList<TransicaoAFD> getTransicoes() {
		return transicoes;
	}


	public void setTransicoes(TransicaoAFD transicao) {
		this.transicoes.add(transicao);
	}
	
	public void copiarTransicoes(ArrayList<TransicaoAFD> transicoesAFD){
		this.transicoes.addAll(transicoesAFD);
	}
	
	public EstadoAFD funcaoTransicaoAFD(EstadoAFD estadoAtual, Character simb){		
		for(int i=0; i<transicoes.size(); i++){
			if(transicoes.get(i).getAnterior().getId() == estadoAtual.getId() && transicoes.get(i).getSimbolo().equals(simb)){
				return transicoes.get(i).getSeguinte();
			}
		}
		return null;
	}
	
}
