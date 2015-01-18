package connexion.openerp.api.model;

public class Product {
	public Integer id;
	public String name;
	public Double qtyAvailable;
	public Double prixVente;

	public Product() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getQtyAvailable() {
		return qtyAvailable;
	}

	public void setQtyAvailable(Double qtyAvailable) {
		this.qtyAvailable = qtyAvailable;
	}

	public Double getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Double prixVente) {
		this.prixVente = prixVente;
	}
}
