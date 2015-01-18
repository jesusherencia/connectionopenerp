import connexion.openerp.api.ConnexionOpenErp;
import connexion.openerp.api.Payment;
import connexion.openerp.api.Vente;



public class Test {
	
	public static void main(String[]args){
		
		ConnexionOpenErp.host = "vps70274.ovh.net";
		ConnexionOpenErp.port = 80;
		ConnexionOpenErp.db = "demo";
		ConnexionOpenErp.user = "admin";
		ConnexionOpenErp.pass = "admin";
		
//		System.out.println("Méthode consultation les produits");
//		ConnexionOpenErp.listerProducts();
//		
//		System.out.println("Liste des customers");
//		ConnexionOpenErp.listerCustomers();
//		
//		System.out.println("Liste des fournisseurs");
//		ConnexionOpenErp.listerSuppliers();
		
//		System.out.println("Creer customer");
//		openerptest.createCustomer();
		
//		System.out.println("Méthode quantité d'un produit");
//		openerptest.consultationStock("Chaise");
		
		/* Achat */
		
		Integer supplierId = 7;
		Integer purchaseOrderId = 5;
		
//		System.out.println("Créer une Purchase Order");
//		purchaseOrderId = Achat.creerPurchaseOrder(7);
//		
//		System.out.println("Créer une lignne de une commande");
//		Achat.creerPurchaseOrderLine(purchaseOrderId, 2, 4, 8d);
//		
//		System.out.println("Confirm salse order");
//		Achat.confirmPurchaseOrder(purchaseOrderId);
		
//		System.out.println("Complete Invoice from Purchase order");
//		Achat.completeInvoiceFromOrder(purchaseOrderId);
//		
//		System.out.println("Purchase order");
//		Achat.getPurchaseOrder(purchaseOrderId);
		
		/* Vente */
		
		Integer customerId = 6;
		Integer salesOrderId = 5;
		Integer productId = 2;
		
//		System.out.println("Créer une Sales Order");
//		salesOrderId = Vente.creerSalesOrder(customerId);
//		
//		System.out.println("Créer une lignne de une commande");
//		Vente.creerLigneSalesOrder(salesOrderId, productId, 2);
//		
//		System.out.println("quotation sent sale order");
//		Vente.quotationSentSaleOrder(salesOrderId);
//		
//		System.out.println("Confirm salse order");
//		Vente.confirmSaleOrder(salesOrderId);
//		
//		System.out.println("Manual invoice salse order");
//		Vente.manualInvoiceSaleOrder(salesOrderId);
		
//		System.out.println("Manual invoice salse order");
//		Vente.validateInvoiceSaleOrder(4);
		
//		System.out.println("State sale order");
//		Vente.getStateSaleOrder(salesOrderId);
		
//		Vente.listerInvoices(7);
		
		/* Payment */
		
//		System.out.println("Paiement");
//		Payment.creerVoucherFromPurchaseOrder(6);
		
//		System.out.println("Paiement");
//		Payment.creerVoucherSaleFromInvoice(4);
		
		Vente.listerInvoices(7);
		
	}	
	
}