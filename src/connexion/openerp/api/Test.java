package connexion.openerp.api;

public class Test {
	
	public static void main(String[]args){
		ConnexionOpenErp.host = "vps70274.ovh.net";
		ConnexionOpenErp.port = 80;
		ConnexionOpenErp.db = "test1";
		ConnexionOpenErp.user = "admin";
		ConnexionOpenErp.pass = "admin";
		
//		System.out.println("Méthode consultation les produits");
//		openerptest.listeProduits();
//		
		System.out.println("Liste des customers");
		ConnexionOpenErp.listerCustomers();
//		
//		System.out.println("Liste des fournisseurs");
//		openerptest.listerFournisseurs();;
		
//		System.out.println("Creer customer");
//		openerptest.createCustomer();
		
//		System.out.println("Méthode quantité d'un produit");
//		openerptest.consultationStock("Chaise");
		
		/* Achat */
		
		System.out.println("Créer une Purchase Order");
		Achat.creerPurchaseOrder(7);
		
		/* Vente */
		
//		System.out.println("Créer une Sales Order");
//		Integer salesOrderId = Vente.creerSalesOrder(6);
//		
//		System.out.println("Créer une lignne de une commande");
//		Vente.creerLigneSalesOrder(salesOrderId, 3, 4);
//		
//		System.out.println("quotation sent sale order");
//		Vente.quotationSentSaleOrder(salesOrderId);
//		
//		System.out.println("Confirm salse order");
//		Vente.confirmSaleOrder(salesOrderId);
//		
//		System.out.println("Manual invoice salse order");
//		Vente.manualInvoiceSaleOrder(salesOrderId);
//		
//		System.out.println("State sale order");
//		Vente.getStateSaleOrder(salesOrderId);
	}
	
}