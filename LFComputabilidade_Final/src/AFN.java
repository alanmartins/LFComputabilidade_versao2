import java.util.ArrayList;


public class AFN {
	
	
	private Estado inicialAFN;
	private ArrayList<Estado> estados = new ArrayList<Estado>();
	private ArrayList<Estado> finais = new ArrayList<Estado>();
	private ArrayList<Transicao> transicoes = new ArrayList<Transicao>();
	public ArrayList<Estado> auxDestino = new ArrayList<Estado>();
	
	public AFN() {
		
	}


	public void copiarEstados(ArrayList<Estado> estados){
		this.estados.addAll(estados);
	}
	
	public void copiarFinais(ArrayList<Estado> finais){
		this.finais.addAll(finais);
	}
	
	
	public void copiarTransicoes(ArrayList<Transicao> transicoes){
		this.transicoes.addAll(transicoes);
	}
	
	public Estado getInicial() {
		return this.inicialAFN;
	}


	public void setInicial(Estado inicial) {
		this.inicialAFN = inicial;
	}


	public ArrayList<Estado> getEstados() {
		return estados;
	}


	public void setEstado(Estado estado) {
		this.estados.add(estado);
	}


	public ArrayList<Estado> getFinais() {
		return finais;
	}


	public void setFinal(Estado fin) {
		this.finais.add(fin);
	}


	public ArrayList<Transicao> getTransicoes() {
		return transicoes;
	}


	public void setTransicao(Transicao transicao) {
		this.transicoes.add(transicao);
	}
	
	// Retorna a lista de estados destino, tendo q como origem e símbolo "simbolo"
	public ArrayList<Estado> funcaoTransicao(Estado q, Character simbolo){
		auxDestino.clear();
		for(int j=0; j < transicoes.size(); j++){
			if(transicoes.get(j).getAnterior().getTag() == q.getTag() && transicoes.get(j).getSimbolos().contains(simbolo)){
				auxDestino.add(transicoes.get(j).getSeguinte());
			}
		}
		return auxDestino;
	}
	
}
