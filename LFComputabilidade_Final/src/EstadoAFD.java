import java.util.ArrayList;


public class EstadoAFD {

	private ArrayList<Estado> estados;
	private Integer tipo; // (0)inicial, (1)final
	private String tag;
	private Integer id;
	
	public EstadoAFD(Integer tipo) {
		this.tipo = tipo;
		this.id = ExpressaoRegular.numEstadosAFD;
		ExpressaoRegular.numEstadosAFD = ExpressaoRegular.numEstadosAFD + 1;
		this.tag = "Q".concat(id.toString());
		estados = new ArrayList<Estado>();
	}

	public ArrayList<Estado> getEstados() {
		return estados;
	}

	public void addEstado(Estado estado){
		this.estados.add(estado);
	}
	
	public void setEstados(ArrayList<Estado> estados) {
		this.estados = estados;
	}

	
	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
