package connexion.openerp.api.model;

public class Invoice {
	public Integer id;
	public String name;
	public String refOrder;
	public Double total;

	public Invoice() {
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

	public String getRefOrder() {
		return refOrder;
	}

	public void setRefOrder(String refOrder) {
		this.refOrder = refOrder;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	
}
