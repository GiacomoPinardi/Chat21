import java.util.ListResourceBundle;

public class MyResourceBundleGruppo extends ListResourceBundle{

	private Object[][] contents;
	
	public MyResourceBundleGruppo(Observer observer, InformazioniSessione informazioniSessione, String nomeGruppo) {
		Object[][] contents = {
	            { "observer"   , observer },
	            {"informazioniSessione", informazioniSessione},
	            {"nomeGruppo", nomeGruppo}
	    };
		this.contents=contents;
	}

	@Override
	protected Object[][] getContents() {
		return contents;
	}

}
