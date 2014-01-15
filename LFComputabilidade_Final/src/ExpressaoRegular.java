import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class ExpressaoRegular {
	public ArrayList<Character> reservado = new ArrayList<Character>(Arrays.asList('+', '*', '.', 'E', 'V', '(', ')'));
	public ArrayList<Character> operadores = new ArrayList<Character>(Arrays.asList('+', '*', '.'));
	public ArrayList<Character> uniao = new ArrayList<Character>(Arrays.asList('+'));
	public ArrayList<Character> concatenacao = new ArrayList<Character>(Arrays.asList('.'));
	public ArrayList<Character> kleene = new ArrayList<Character>(Arrays.asList('*'));
	public ArrayList<Character> nulo = new ArrayList<Character>();
	
	public ArrayList<Character> alfabeto = new ArrayList<Character>();
	public static String arqEntrada, arqSaida;
	public String exprRegular, palavra;
	public ArrayList<String> cadeias;
	public Estado a, b;
	public Transicao t1, t2;
	public AFN aux = new AFN();
	public int tamanho;
	public static int numEstadosAFN = 0; // Número de estados do autômato não determinístico, incrementado a cada novo estado criado
	public static int numEstadosAFD = 0; // Número de estados do autômato determinístico, incrementado a cada novo estado criado
	public StringBuilder resultados = new StringBuilder();

	// Variáveis de construção do Autômato Finito Não-Determinístico - AFN
	public char simb, op;

	public LinkedBlockingQueue<AFN> subautomatos = new LinkedBlockingQueue<AFN>();
	//public Stack<Character> operadores = new Stack<Character>();
	public AFN automato = new AFN();
	public AFN auxOP = new AFN();

	// Variáveis de construção do Autômato Finito Determinístico - AFD
	public ArrayList<Estado> fechoVazio = new ArrayList<Estado>();
	public ArrayList<Estado> auxFecho = new ArrayList<Estado>();
	public ArrayList<Estado> fechoEstadoInicial = new ArrayList<Estado>();
	
	public LinkedBlockingQueue<Estado> fila = new LinkedBlockingQueue<Estado>();
	public LinkedBlockingQueue<EstadoAFD> filaAFD = new LinkedBlockingQueue<EstadoAFD>();
	
	public ArrayList<Estado> novoEstado = new ArrayList<Estado>();
	public ArrayList<Estado> temp = new ArrayList<Estado>();
	
	public AFD afd = new AFD();
	public EstadoAFD estadoAFD;
	public EstadoAFD auxAFD;
	public EstadoAFD jaContido;
	public EstadoAFD estadoAtual;
	public TransicaoAFD transicaoAFD;
	public int t; // contador

	
	public ExpressaoRegular() throws InterruptedException {
		Entrada();
		extrairAlfabeto();
		nulo.add(null);
		exprRegular = exprRegular.trim(); // Eliminação de todos os espaços em branco
		automato = ER_to_AFN(exprRegular); 
		afd = AFNtoAFD(automato);
		Saida();
	}
	
	public LinkedList<AFN> listaOperandos = new LinkedList<AFN>();
	public LinkedList<Character> listaOperadores = new LinkedList<Character>();
	public LinkedList<AFN> listaOperands = new LinkedList<AFN>();
	public LinkedList<Character> listaOperadors = new LinkedList<Character>();
	
	public LinkedBlockingQueue<AFN> automatos = new LinkedBlockingQueue<AFN>();
	public Stack<Character> operators = new Stack<Character>();
	public AFN automatoAux = new AFN();
	public Character ch;
	
	
	
	public AFN solverSimples(String er) throws InterruptedException{

		listaOperandos.clear();
		listaOperadores.clear();
		// Faz o reconhecimento primário da expressão regular
		for(int i=0; i< er.length(); i++){
			if(alfabeto.contains(er.charAt(i))){
				listaOperandos.add(listaOperandos.size(), Unitario(er.charAt(i)));
			}else{
				if(er.charAt(i) == 'E'){
					listaOperandos.add(listaOperandos.size(), E());
				}else{
					if(er.charAt(i) == 'V'){
						listaOperandos.add(listaOperandos.size(), V());
					}else{
						// Se o caracter é um operador, adiciona ele na lista de operadores
						if(operadores.contains(er.charAt(i))){
							listaOperadores.add(listaOperadores.size(), er.charAt(i));
						}
					}
				}
			}
		}
		
		// Agora faz a resolução, por ordem de precedência dos operadores
		
		//Primeiro por operações Estrela (Fecho de Kleene)
		if(listaOperadores.contains('*')){
			int p = 0;
			for(int i=0; i<listaOperadores.size(); i++){
				if(listaOperadores.get(i) == '*'){
					listaOperandos.add(p, fechoKleene(listaOperandos.remove(p)));				
				}else{
					p = p + 1;
				}
			}
			listaOperadores.removeAll(kleene);
		}
				
		//Segundo resolve operações de concatenação
		if(listaOperadores.contains('.')){
			int p = 0;
			for(int i=0; i<listaOperadores.size(); i++){
				if(listaOperadores.get(i) == '.'){
					listaOperandos.add(p+1, Concat(listaOperandos.get(p), listaOperandos.remove(p+1)));
					listaOperandos.set(p, null);
				}
				p = p + 1;
			}
			listaOperandos.removeAll(nulo);
			listaOperadores.removeAll(concatenacao);
		}

		//Terceiro, resolve operações de união
		if(listaOperadores.contains('+')){
			int p = 0;
			for(int i=0; i<listaOperadores.size(); i++){
				if(listaOperadores.get(i) == '+'){
					listaOperandos.add(p+1, Uniao(listaOperandos.get(p), listaOperandos.remove(p+1)));
				}
				p = p + 1;
			}
			listaOperandos.removeAll(nulo);
			listaOperadores.removeAll(uniao);
		}

		
		return listaOperandos.removeFirst();
	}
		
	
	

	
	
	// 0.(0+1)*.1.(0+1)*  // 0.1*0+1.1*
	
	public AFN ER_to_AFN(String er) throws InterruptedException {
		
		
		String temp = "";
		// Faz o reconhecimento primário da expressão regular
		for(int i=0; i< er.length(); i++){
			if(alfabeto.contains(er.charAt(i))){
				listaOperands.add(listaOperands.size(), Unitario(er.charAt(i)));
			}else{
				if(er.charAt(i) == 'E'){
					listaOperands.add(listaOperands.size(), E());
				}else{
					if(er.charAt(i) == 'V'){
						listaOperands.add(listaOperands.size(), V());
					}else{
						// Se o caracter é um operador, adiciona ele na lista de operadores
						if(operadores.contains(er.charAt(i))){
							listaOperadors.add(listaOperadors.size(), er.charAt(i));
						}else{
							if(er.charAt(i)=='('){
								i = i + 1;
								while(er.charAt(i) != ')'){									
									temp = temp.concat(String.valueOf(er.charAt(i)));
									i = i + 1;
								}	
								listaOperands.add(listaOperands.size(), solverSimples(temp));
								temp = "";
							}
						}
					}
				}
			}
		}
		
		// Agora faz a resolução, por ordem de precedência dos operadores
		
		//Primeiro por operações Estrela (Fecho de Kleene)
		if(listaOperadors.contains('*')){
			int p = 0;
			for(int i=0; i<listaOperadors.size(); i++){
				if(listaOperadors.get(i) == '*'){
					
					listaOperands.add(p, fechoKleene(listaOperands.remove(p)));				
				}else{
					p = p + 1;
				}
			}
			listaOperadors.removeAll(kleene);
		}
	
		//Segundo resolve operações de concatenação
		if(listaOperadors.contains('.')){
			int p = 0;
			for(int i=0; i<listaOperadors.size(); i++){
				if(listaOperadors.get(i) == '.'){
					listaOperands.add(p+1, Concat(listaOperands.get(p), listaOperands.remove(p+1)));
					listaOperands.set(p, null);
				}
				p = p + 1;
			}
			listaOperands.removeAll(nulo);
			listaOperadors.removeAll(concatenacao);
		}

		//Terceiro, resolve operações de união
		if(listaOperadors.contains('+')){
			int p = 0;
			for(int i=0; i<listaOperadors.size(); i++){
				if(listaOperadors.get(i) == '+'){
					listaOperands.add(p+1, Uniao(listaOperands.get(p), listaOperands.remove(p+1)));
				}
				p = p + 1;
			}
			listaOperands.removeAll(nulo);
			listaOperadors.removeAll(uniao);
		}

		
		return listaOperands.removeFirst();
	}
	


	public boolean reconheceLinguagem(AFD afd, String cadeia){
		estadoAtual = afd.getInicial();
		if(!afd.getTransicoes().isEmpty()){
			for(int i=0; i<cadeia.length(); i++){
				if(!afd.funcaoTransicaoAFD(estadoAtual, cadeia.charAt(i)).equals(null)){
					estadoAtual = afd.funcaoTransicaoAFD(estadoAtual, cadeia.charAt(i));
				}
			}
		}
		if(estadoAtual.getTipo() == 1){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	public ArrayList<Estado> fechoVazio(AFN afn, Estado q) throws InterruptedException {
			
		fechoVazio = new ArrayList<Estado>();
		fila.put(q);
				
		while (!fila.isEmpty()) {
			auxFecho.clear();// = new ArrayList<Estado>();
			auxFecho = afn.funcaoTransicao(fila.peek(), 'E'); // Todas as transições que têm origem no estado avaliado
			fechoVazio.add(fila.poll());
			
			//Para todas transições que saem do estado avaliado, se possue transição vazia e o Estado destino ainda não foi avaliado, ele é adicionado na fila 
			for (int d = 0; d < auxFecho.size(); d++) {
				if (!fechoVazio.contains(auxFecho.get(d))) {
					fila.put(auxFecho.get(d));
				}
			}
		}
		return fechoVazio;
	}

	
	public void defineFechosVazios(AFN afn) throws InterruptedException {	
		// Estado inicial
		afn.getInicial().setClosure_Empty(fechoVazio(afn, afn.getInicial()));
		
		//Estados finais
		for(int i=0; i<afn.getFinais().size(); i++){
			afn.getFinais().get(i).setClosure_Empty(fechoVazio(afn, afn.getFinais().get(i)));
		}
		
		//Estados Intermediários
		for(int j=0; j<afn.getEstados().size(); j++){
			afn.getEstados().get(j).setClosure_Empty(fechoVazio(afn, afn.getEstados().get(j)));
		}
	}
	
	
	public boolean isFinal(AFN afn, ArrayList<Estado> estados) {
		t = 0;
		while (t < estados.size()) {
			if (afn.getFinais().contains(estados.get(t))) {
				return true;			
			}
			t = t + 1;
		}
		return false;
	}

	
	
	public AFD AFNtoAFD(AFN afn) throws InterruptedException {
		// Define os fechos transitivos vazios de todos os estados do AFN
		defineFechosVazios(automato);
		
		EstadoAFD estadoInicial = new EstadoAFD(0); // Estado inicial do AFD
		
		fechoEstadoInicial = fechoVazio(afn, afn.getInicial());
		estadoInicial.setEstados(fechoEstadoInicial);
		
		if (isFinal(afn, fechoEstadoInicial)) {
			estadoInicial.setTipo(1);
			afd.addFinal(estadoInicial);
		}		
		afd.setInicial(estadoInicial);
		
		filaAFD.put(afd.getInicial());

		while (! filaAFD.isEmpty()) {	

			auxAFD = filaAFD.poll();
		
			for (int a = 0; a < alfabeto.size(); a++) {

				novoEstado = proximoEstado(auxAFD, alfabeto.get(a));	

					jaContido = auxAFD;
					if (!contemEstado(afd, novoEstado)) {
						// Se ainda não existe um estadoAFD igual, cria um novo estadoAFD 					
						estadoAFD = new EstadoAFD(0);						
						estadoAFD.setEstados(novoEstado);
						
						if (isFinal(afn, novoEstado)) {						
							estadoAFD.setTipo(1);
							afd.addFinal(estadoAFD);							
						}else {
							afd.getEstados().add(estadoAFD);
						}
						
						filaAFD.put(estadoAFD);
						transicaoAFD  = new TransicaoAFD(auxAFD, estadoAFD, alfabeto.get(a));
						afd.setTransicoes(transicaoAFD);					
					} else {
						// Se o próximo estado é igual ao estado avaliado, insere uma transição para o próprio estado avaliado auxAFD
						if(jaContido.equals(auxAFD)){
							afd.setTransicoes(new TransicaoAFD(auxAFD, auxAFD, alfabeto.get(a)));
						}else{
							afd.setTransicoes(new TransicaoAFD(auxAFD, jaContido, alfabeto.get(a)));
						}
					}

			}
			
		}
		return afd;
	}

	
	public ArrayList<Estado> proximoEstado(EstadoAFD Q, Character simb) throws InterruptedException {	
		ArrayList<Estado> proximo = new ArrayList<Estado>();
		
		for(int i=0; i<Q.getEstados().size(); i++){
			temp = automato.funcaoTransicao(Q.getEstados().get(i), simb);
			// Para a função de transição de cada estado de Q, se não contém em "proximo", adiciona
			for(int j=0; j<temp.size(); j++){
				//
				for(int t=0; t<temp.get(j).getClosure_Empty().size(); t++){
					if(!proximo.contains(temp.get(j).getClosure_Empty().get(t))){
						proximo.add(temp.get(j).getClosure_Empty().get(t));
					}
				}
			}
		}
		
		return proximo;
	}
	
	
	public boolean contemEstado(AFD afd, ArrayList<Estado> listaEstadosAFN) {

		// Verifica se o Estado inicial é igual ao próximo estado
		if( afd.getInicial().getEstados().containsAll(listaEstadosAFN) && afd.getInicial().getEstados().size() == listaEstadosAFN.size() ){
			jaContido = afd.getInicial();
			return true;
		}
		
		t = 0;
		// Verifica se o próximo estado já é um dos estados finais
		while (t < afd.getFinais().size()) {
			if (afd.getFinais().get(t).getEstados().containsAll(listaEstadosAFN) && afd.getFinais().get(t).getEstados().size() == listaEstadosAFN.size()) {
				jaContido = afd.getFinais().get(t);
				return true;
			}
			t = t + 1;
		}

		t = 0;
		// Verifica se o próximo estado já é um dos estados do AFD
		while (t < afd.getEstados().size()) {
			if( afd.getEstados().get(t).getEstados().containsAll(listaEstadosAFN) && afd.getEstados().get(t).getEstados().size() == listaEstadosAFN.size()){
				jaContido = afd.getEstados().get(t);
				return true;
			}
			t = t + 1;
		}

		// Caso o estado ainda não exista, então retorna falso
		return false;
	}



	public boolean Precedencia(Character op1, Character op2) {
		if (  (op1 == '.' && op2 == '*') | (op1 == '+' && op2 == '*')
			| (op1 == '+' && op2 == '.') | (op1 == '(' && op2 == '*')
			| (op1 == '(' && op2 == '.') | (op1 == '(' && op2 == '+')
			| (op1 == op2)) 
		{
			return true;
		} else {
			return false;
		}

	}

	public boolean validaER(String er) {
		boolean valida = true;
		if (er.length() == 0 | er.charAt(0) == '+' | er.charAt(0) == '*' | er.charAt(0) == '.') {
			System.err.print("Essa não é uma Expressão Regular.");
			valida = false;
		}
		return valida;
	}

	// Cria um autômato para a palavra vazia
	public AFN E() {
		AFN aux = new AFN();
		a = new Estado(1);
		aux.setInicial(a);
		aux.setFinal(a);
		return aux;
	}

	// Cria um autômato para a palavra vazia
	public AFN V() {
		AFN aux = new AFN();
		a = new Estado(0);
		aux.setInicial(a);
		return aux;
	}

	public AFN Unitario(Character simbolo) {
		AFN autom = new AFN();
		a = new Estado(0); // cria um estado inicial
		b = new Estado(1); // cria um estado final
		t1 = new Transicao(a, b); // cria uma transicao do entre os estados A e
									// B
		t1.setSimbolo(simbolo);

		autom.setInicial(a);
		autom.setFinal(b);
		autom.setTransicao(t1);

		return autom;
	}

	public AFN fechoKleene(AFN autom) {
		aux = autom;
		tamanho = autom.getFinais().size(); // Número de estados finais do autômato passado
		
		if(autom.getInicial().getTipo() == 0){
			aux.setEstado(autom.getInicial()); // O antigo estado inicial, passa a ser um estado intermediário do novo autômato 
		}
		
		a = new Estado(1); // Cria um estado final e inicial para o novo autômato

		// Cria uma transição vazia entre todos os estados finais de AUTOM e o estado inicial do novo autômato
		for (int j = 0; j < tamanho; j++) {
			autom.getFinais().get(j).setTipo(0);
			aux.setEstado(autom.getFinais().get(j));
			t1 = new Transicao(aux.getFinais().get(j), a);
			t1.setSimbolo('E');
			aux.setTransicao(t1);
		}
		aux.getFinais().clear();

		t2 = new Transicao(a, aux.getInicial()); // Cria uma transição vazia do novo estado A para o estado inicial do autômato passado
		t2.setSimbolo('E'); // Seta transição vazia para T
		aux.setTransicao(t2);

		aux.setInicial(a);
		aux.setFinal(a);

		return aux;
	}

	public AFN Uniao(AFN a1, AFN a2) {
		aux = a1;
		a = new Estado(0);

		t1 = new Transicao(a, aux.getInicial());
		t1.setSimbolo('E');
		aux.setTransicao(t1);

		t2 = new Transicao(a, a2.getInicial());
		t2.setSimbolo('E');
		aux.setTransicao(t2);

		aux.setInicial(a);
		aux.copiarEstados(a2.getEstados());
		aux.copiarFinais(a2.getFinais());
		aux.copiarTransicoes(a2.getTransicoes());

		return aux;
	}

	public AFN Concat(AFN a1, AFN a2) {
		aux = new AFN();
		aux = a1;
		tamanho = aux.getFinais().size();

		// Cria uma transição vazia entre todos os estados finais de AUTOM e o estado inicial do novo autômato
		for (int c = 0; c < tamanho; c++) {
			a1.getFinais().get(c).setTipo(0);
			aux.setEstado(a1.getFinais().get(c));
			t1 = new Transicao(aux.getFinais().get(c), a2.getInicial());
			t1.setSimbolo('E');
			aux.setTransicao(t1);
		}
		aux.getFinais().clear();

		if(a2.getInicial().getTipo() == 0){
			aux.setEstado(a2.getInicial());
		}
		aux.copiarEstados(a2.getEstados());
		aux.copiarTransicoes(a2.getTransicoes());
		aux.copiarFinais(a2.getFinais());

		return aux;
	}

	public void extrairAlfabeto() {
		for (int i = 0; i < exprRegular.length(); i++) {
			if (!alfabeto.contains(exprRegular.charAt(i))
					&& !reservado.contains(exprRegular.charAt(i))) {
				alfabeto.add(exprRegular.charAt(i));
			}
		}
	}

	public void Entrada() {

		cadeias = new ArrayList<String>();
		File in = new File(arqEntrada);

		if (in.exists()) {
			try {
				BufferedReader leitor = new BufferedReader(new FileReader(arqEntrada));
				exprRegular = leitor.readLine();

				while ((palavra = leitor.readLine()) != null) {
					cadeias.add(palavra);
				}
				leitor.close();
			} catch (Exception erro) {
				erro.printStackTrace();
			}
		} else {
			System.out.println("Arquivo não existe!");
		}
	}
	
	public void Saida(){
		  try {
			  BufferedWriter escritor = new BufferedWriter(new FileWriter(arqSaida));
			  
			  for(int i=0; i < cadeias.size(); i++){
				  if(reconheceLinguagem(afd, cadeias.get(i))){
					  resultados.append("1");
				  }else{
					  resultados.append("0");
				  }
				  resultados.append("\n");			
			  }
			  escritor.write(resultados.toString());
			  escritor.close();
		  }catch(Exception erro){
			  System.out.println("Erro na saída!");
		  }
	  }

	public static void main(String[] args) throws InterruptedException {
		arqEntrada = args[0]; // arquivo de entrada
		arqSaida = args[1]; // arquivo de saída
		new ExpressaoRegular();
	}

}
