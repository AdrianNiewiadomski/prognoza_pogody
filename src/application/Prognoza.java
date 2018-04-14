package application;




public class Prognoza {

    private String miasto;
    private String panstwo;
    private String json;
    
    
    //---------------------------------------- Konstruktory --------------------------------------------
    /**
     * Konstruktor bezparametrowy.
     */
    public Prognoza() {
        this(null, null);
    }

    /**
     * Konstruktor dwuparametrowy.
     * 
     * @param miasto
     * @param panstwo
     */
    public Prognoza(String miasto, String panstwo) {
        this.miasto = miasto;
        this.panstwo = panstwo;

        // Some initial dummy data, just for convenient testing.
        this.json = "";
        }
    
    /**
     * Konstruktor trójparametrowy.
     * 
     * @param miasto
     * @param panstwo
     * @param json
     */
    public Prognoza(String miasto, String panstwo, String json) {
        this.miasto = miasto;
        this.panstwo = panstwo;  
        this.json = json;
        }
    
    
    //---------------------------------------- Setery i getery --------------------------------------------


    public String podajMiasto() {
        return this.miasto;
    }

    public void ustawMiasto(String miasto) {
        this.miasto=miasto;
    }

    public String podajPanstwo() {
        return this.panstwo;
    }

    public void ustawPpanstwo(String panstwo) {
        this.panstwo=panstwo;
    }

    public String podajJson() {
        return this.json;
    }

    public void ustawJson(String json) {
        this.json=json;
    }

    /*public String getOsoba() {
		String temp=wlasciwoscImie.get()+" "+wlasciwoscNazwisko.get()+" "+wslasciwoscNumerTelefonu.get();
		return temp;
	}*/
}