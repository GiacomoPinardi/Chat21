import java.util.ListResourceBundle;

import client.InformazioniSessione;
import client.Observer;

public class MyResourceBundle extends ListResourceBundle {
	
	private Observer observer;
	private InformazioniSessione informazioniSessione;
	
	public MyResourceBundle(Observer observer, InformazioniSessione informazioniSessione) {
		this.observer=observer;
		this.informazioniSessione=informazioniSessione;
	}
	
	@Override
	protected Object[][] getContents() {
		Object[][] contents = {
	            { "observer"   , this.observer },
	            {"informazioniSessione", this.informazioniSessione},
	    };
		return contents;
    }
}
