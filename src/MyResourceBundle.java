import java.util.ListResourceBundle;

public class MyResourceBundle extends ListResourceBundle {
	
	private Object[][] contents;
	
	public MyResourceBundle(Observer observer, InformazioniSessione informazioniSessione) {
		Object[][] contents = {
	            { "observer"   , observer },
	            {"informazioniSessione", informazioniSessione},
	    };
		this.contents=contents;
	}

	@Override
	protected Object[][] getContents() {
		return contents;
    }
}
