package domain;

public class Operazione implements Info {

	private static final long serialVersionUID = 1L;

	private String parametro1;
	private String parametro2;
	
	public Operazione () {
		
	}
	
	public Operazione (String parametro1, String parametro2) {
		this.parametro1 = parametro1;
		this.parametro2 = parametro2;
	}
	
	public String getParametro1() {
		return parametro1;
	}
	
	public void setParametro1(String parametro1) {
		this.parametro1 = parametro1;
	}
	
	public String getParametro2() {
		return parametro2;
	}
	
	public void setParametro2(String parametro2) {
		this.parametro2 = parametro2;
	}
	
}
