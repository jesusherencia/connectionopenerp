package connexion.openerp.api;
import java.util.Date;

import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;

public class Achat extends ConnexionOpenErp {

	public static Integer purchase_pricelist_id = 1; // Public Pricelist, utiliser pour les achats
	public static Integer location_id = 12; // Location par defaut

	/**
	 * Creer une PurchaseOrder
	 * @param partner_id Id de fournisseur
	 * @return
	 */
	public static Integer creerPurchaseOrder(Integer partner_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			ObjectAdapter priceListeAd = openERPSession
					.getObjectAdapter("product.pricelist");
			RowCollection listePriceList = priceListeAd.readObject(
					new Integer[] { purchase_pricelist_id }, new String[] { "id",
							"name", "currency_id" });
			
			Row rowPriceList = listePriceList.get(0);
			Object[] priceListIdName = (Object[]) rowPriceList
					.get("currency_id");
			Integer currencyId = (Integer) priceListIdName[0];
			System.out.println("Currency Id : "+currencyId);
			
			// ////
			ObjectAdapter purchaseOrderAd = openERPSession
					.getObjectAdapter("purchase.order");
			
			Row newPurchase = purchaseOrderAd.getNewRow(new String[] {
					"currency_id", "partner_id", //"partner_invoice_id",
					//"partner_shipping_id",
					"location_id",
					"pricelist_id", });
			newPurchase.put("currency_id", currencyId);
			newPurchase.put("partner_id", partner_id);
			//newPurchase.put("partner_invoice_id", partner_id);
			//newPurchase.put("partner_shipping_id", partner_id);
			newPurchase.put("pricelist_id", purchase_pricelist_id);
			newPurchase.put("location_id", location_id);
			purchaseOrderAd.createObject(newPurchase);
			
			System.out.println("New Row ID: " + newPurchase.getID());
			
			return (Integer)newPurchase.getID();

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * Creer PurchaseOrderLine
	 * @param order_id
	 * @param product_id
	 * @param product_uom_qty
	 * @param price_unit
	 */
	public static void creerPurchaseOrderLine(Integer order_id, Integer product_id,
			Number product_qty, Double price_unit) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			ObjectAdapter productAd = openERPSession
					.getObjectAdapter("product.product");

			RowCollection listeProduct = productAd.readObject(
					new Integer[] { product_id }, productAd.getFieldNames() );
			Row rowProduct = listeProduct.get(0);

			System.out.println(rowProduct.get("id"));
			System.out.println(rowProduct.get("name"));
			System.out.println(rowProduct.get("lst_price"));
			String productName = (String)rowProduct.get("name");
			//Double productLstPrice = (Double)rowProduct.get("lst_price");
			
			// ////
			ObjectAdapter purchaseOrderAd = openERPSession
					.getObjectAdapter("purchase.order");
			
			RowCollection listePurchaseOrder = purchaseOrderAd.readObject(
					new Integer[] { order_id }, purchaseOrderAd.getFieldNames() );
			Row rowPurchaseOrder = listePurchaseOrder.get(0);

			// ////////
			 ObjectAdapter purchaseLineAd = openERPSession
			 .getObjectAdapter("purchase.order.line");
			 Row newPurchaseline = purchaseLineAd.getNewRow(new String[] {
			 "price_unit", "order_id", "product_id", "product_qty", "name", "date_planned" });
			 newPurchaseline.put("order_id", order_id);
			 newPurchaseline.put("date_planned", new Date());
			 //newPurchaseline.put("date_planned", rowPurchaseOrder.get("minimum_planned_date")); // on utilise le date minimun de PurchaseOrder
			 newPurchaseline.put("product_id", product_id);
			 newPurchaseline.put("product_qty", product_qty);
			 newPurchaseline.put("name", productName); // on utilise le nom du produit
			 newPurchaseline.put("price_unit", price_unit);
			 purchaseLineAd.createObject(newPurchaseline);
			 
			 System.out.println("New Row ID: " + newPurchaseline.getID());

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	public static void confirmPurchaseOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter purchaseOrderAd = openERPSession
					.getObjectAdapter("purchase.order");
			// ////
			
			RowCollection listePurchaseOrder = purchaseOrderAd.readObject(
					new Integer[] { order_id }, purchaseOrderAd.getFieldNames() );
			Row rowPurchaseOrder = listePurchaseOrder.get(0);

			System.out.println("PurchaseOrder State: " + rowPurchaseOrder.get("state"));
			
			purchaseOrderAd.executeWorkflow(rowPurchaseOrder, "purchase_confirm");

			System.out.println("PurchaseOrder Row ID: " + rowPurchaseOrder.getID());
			System.out.println("PurchaseOrder State: " + rowPurchaseOrder.get("state"));

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	public static void completeInvoiceFromOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			// ////
			ObjectAdapter purchaseOrderAd = openERPSession
					.getObjectAdapter("purchase.order");
			
			RowCollection listePurchaseOrder = purchaseOrderAd.readObject(
					new Integer[] { order_id }, purchaseOrderAd.getFieldNames() );
			Row rowPurchaseOrder = listePurchaseOrder.get(0);

			System.out.println("PurchaseOrder State: " + rowPurchaseOrder.get("state"));
			System.out.println("PurchaseOrder minimum_planned_date: " + rowPurchaseOrder.get("minimum_planned_date"));
			
			// Invoices
			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			Object[] invoice_ids = (Object[]) rowPurchaseOrder
					.get("invoice_ids");
			if (invoice_ids != null && invoice_ids.length>0) {
				System.out.println("Invoices:");
				RowCollection listeInvoices = invoiceAd.readObject(invoice_ids,
						invoiceAd.getFieldNames());
				for (Row rowInvoice : listeInvoices) {
					System.out.println("  Id: " + rowInvoice.get("id"));
					System.out.println("  Name: " + rowInvoice.get("name"));
					System.out.println("  Name: " + rowInvoice.get("state"));
					invoiceAd.executeWorkflow(rowInvoice, "invoice_open"); // cet action cree automatiquement account_move et account_move_line
					//invoiceAd.executeWorkflow(rowInvoice, "invoice_pay");
				}
			}

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	public static void getPurchaseOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter purchaseOrderAd = openERPSession
					.getObjectAdapter("purchase.order");
			// ////
			
			RowCollection listePurchaseOrder = purchaseOrderAd.readObject(
					new Integer[] { order_id }, purchaseOrderAd.getFieldNames() );
			Row rowPurchaseOrder = listePurchaseOrder.get(0);

			System.out.println("PurchaseOrder State: " + rowPurchaseOrder.get("state"));
			System.out.println("PurchaseOrder minimum_planned_date: " + rowPurchaseOrder.get("minimum_planned_date"));
			
			// Invoices
			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			Object[] invoice_ids = (Object[]) rowPurchaseOrder
					.get("invoice_ids");
			if (invoice_ids != null && invoice_ids.length>0) {
				System.out.println("Invoices:");
				RowCollection invoices = invoiceAd.readObject(invoice_ids,
						new String[] {"id", "name", "state"}); // si on met tous les champs il done un erreur quand l'invoice est validé
				for (Row invoice : invoices) {
					System.out.println("  Id: " + invoice.get("id"));
					System.out.println("  Name: " + invoice.get("name"));
					System.out.println("  State: " + invoice.get("state"));
				}
			}
			
			// Shipping
			ObjectAdapter pickingAd = openERPSession
					.getObjectAdapter("stock.picking");
			Object[] picking_ids = (Object[]) rowPurchaseOrder
					.get("picking_ids");
			if (picking_ids != null) {
				System.out.println("Pickings:");
				RowCollection pickings = pickingAd.readObject(picking_ids,
						pickingAd.getFieldNames());
				for (Row picking : pickings) {
					System.out.println("  Id: " + picking.get("id"));
					System.out.println("  Name: " + picking.get("name"));
					System.out.println("  State: " + picking.get("state"));
				}
			}

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
}
