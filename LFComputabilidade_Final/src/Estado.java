import java.util.ArrayList;




public class Estado {
	private Integer tipo; // (0)inicial, (1)final
	private String tag;
	private Integer id;
	private ArrayList<Estado> closure_Empty = new ArrayList<Estado>();
	
	public Estado(Integer tipo){
		this.tipo = tipo;
		this.id = ExpressaoRegular.numEstadosAFN;
		ExpressaoRegular.numEstadosAFN = ExpressaoRegular.numEstadosAFN + 1;
		this.tag = "q".concat(id.toString());
	}

	
	public ArrayList<Estado> getClosure_Empty() {
		return closure_Empty;
	}

	public void setClosure_Empty(ArrayList<Estado> closure_Empty) {
		this.closure_Empty = closure_Empty;
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
