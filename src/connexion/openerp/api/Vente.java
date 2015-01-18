package connexion.openerp.api;
import java.util.ArrayList;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;

import connexion.openerp.api.model.Invoice;
import connexion.openerp.api.model.Product;

public class Vente extends ConnexionOpenErp {

	public static Integer sales_pricelist_id = 1; // Public Pricelist, utiliser
	// pour les sales

	public static Integer creerSalesOrder(Integer partner_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter priceListeAd = openERPSession
					.getObjectAdapter("product.pricelist");
			RowCollection listePriceList = priceListeAd.readObject(
					new Integer[] { sales_pricelist_id }, new String[] { "id",
							"name", "currency_id" });
			Row rowPriceList = listePriceList.get(0);
			System.out.println("Id : " + rowPriceList.get("id"));
			System.out.println("Nom : " + rowPriceList.get("name"));
			Object[] priceListIdName = (Object[]) rowPriceList
					.get("currency_id");
			Integer currencyId = (Integer) priceListIdName[0];

			// ////
			ObjectAdapter saleAd = openERPSession
					.getObjectAdapter("sale.order");
			
			Row newSale = saleAd.getNewRow(new String[] { "currency_id",
					"partner_id", "partner_invoice_id", "partner_shipping_id",
					"pricelist_id", });
			newSale.put("currency_id", currencyId);
			newSale.put("partner_id", partner_id);
			newSale.put("partner_invoice_id", partner_id); // on assume que
															// c'est le meme de
															// partner_id
			newSale.put("partner_shipping_id", partner_id);
			newSale.put("pricelist_id", sales_pricelist_id);
			saleAd.createObject(newSale);

			System.out.println("New Row ID: " + newSale.getID());
			
			return (Integer)newSale.getID();

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}
	
	public static Integer creerLigneSalesOrder(Integer order_id, Integer product_id,
			Number product_uom_qty) {
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
			Double productLstPrice = (Double)rowProduct.get("lst_price");

			// ////////
			 ObjectAdapter salelineAd = openERPSession
			 .getObjectAdapter("sale.order.line");
			 Row newSaleline = salelineAd.getNewRow(new String[] {
			 "price_unit", "order_id", "product_id", "product_uom_qty", "name" });
			 newSaleline.put("order_id", order_id);
			 newSaleline.put("product_id", product_id);
			 newSaleline.put("product_uom_qty", product_uom_qty);
			 newSaleline.put("name", productName); // on utilise le nom du produit
			 newSaleline.put("price_unit", productLstPrice); // on utilise la LstPrice du produit
			 salelineAd.createObject(newSaleline);
			 
			 System.out.println("New Row ID: " + newSaleline.getID());
			 return (Integer)newSaleline.getID();

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}
	
	public static void quotationSentSaleOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter saleOrderAd = openERPSession
					.getObjectAdapter("sale.order");
			// ////
			
			RowCollection listeSaleOrder = saleOrderAd.readObject(
					new Integer[] { order_id }, saleOrderAd.getFieldNames() );
			Row rowSaleOrder = listeSaleOrder.get(0);

			System.out.println("SaleOrder State: " + rowSaleOrder.get("state"));
			
			saleOrderAd.executeWorkflow(rowSaleOrder, "quotation_sent");

			System.out.println("SaleOrder Row ID: " + rowSaleOrder.getID());

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	public static void confirmSaleOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter saleOrderAd = openERPSession
					.getObjectAdapter("sale.order");
			// ////
			
			RowCollection listeSaleOrder = saleOrderAd.readObject(
					new Integer[] { order_id }, saleOrderAd.getFieldNames() );
			Row rowSaleOrder = listeSaleOrder.get(0);

			System.out.println("SaleOrder State: " + rowSaleOrder.get("state"));
			
			saleOrderAd.executeWorkflow(rowSaleOrder, "order_confirm");

			System.out.println("SaleOrder Row ID: " + rowSaleOrder.getID());

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	// creer et passer a state draft
	public static void manualInvoiceSaleOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			ObjectAdapter saleOrderAd = openERPSession
					.getObjectAdapter("sale.order");
			// ////
			
			RowCollection listeSaleOrder = saleOrderAd.readObject(
					new Integer[] { order_id }, saleOrderAd.getFieldNames() );
			Row rowSaleOrder = listeSaleOrder.get(0);

			System.out.println("SaleOrder State: " + rowSaleOrder.get("state"));
			
			saleOrderAd.executeWorkflow(rowSaleOrder, "manual_invoice");

			System.out.println("SaleOrder Row ID: " + rowSaleOrder.getID());

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	// valider invoice
	public static void validateInvoiceSaleOrder(Integer invoice_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			// ////
			
			RowCollection listeInvoice = invoiceAd.readObject(
					new Integer[] { invoice_id }, new String[] {"id", "name", "state"} );
			Row rowInvoice = listeInvoice.get(0);

			System.out.println("Invoice State: " + rowInvoice.get("state"));
			
			invoiceAd.executeWorkflow(rowInvoice, "subflow.paid");

			System.out.println("SaleOrder Row ID: " + rowInvoice.getID());

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	public static void validateInvoiceSaleOrder1(Integer invoice_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			ObjectAdapter saleOrderAd = openERPSession
					.getObjectAdapter("sale.order");
			// ////
			
			RowCollection listeSaleOrder = saleOrderAd.readObject(
					new Integer[] { invoice_id }, saleOrderAd.getFieldNames() );
			Row rowSaleOrder = listeSaleOrder.get(0);

			System.out.println("SaleOrder State: " + rowSaleOrder.get("state"));
			
			saleOrderAd.executeWorkflow(rowSaleOrder, "all_lines");

			System.out.println("SaleOrder Row ID: " + rowSaleOrder.getID());

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	public static void getStateSaleOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();

			ObjectAdapter saleOrderAd = openERPSession
					.getObjectAdapter("sale.order");
			// ////
			
			RowCollection listeSaleOrder = saleOrderAd.readObject(
					new Integer[] { order_id }, saleOrderAd.getFieldNames() );
			Row rowSaleOrder = listeSaleOrder.get(0);

			System.out.println("SaleOrder State: " + rowSaleOrder.get("state"));

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
	public static ArrayList<Invoice> listerInvoices(Integer partner_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			// ////
			FilterCollection filters = new FilterCollection();
			filters.add("partner_id", "=", partner_id);
			filters.add("state", "=", "open");
			filters.add("type", "=", "out_invoice");
			RowCollection invoices = invoiceAd.searchAndReadObject(filters, new String[]{"id","name","number","amount_total","state","reference"}
					//invoiceAd.getFieldNames()
					);
			
			ArrayList<Invoice> listeInvoices = new ArrayList<Invoice>();
			
			for (Row row : invoices) {
				System.out.println("Id : " + row.get("id"));
				System.out.println("Nom : " + row.get("number"));
				System.out.println("state : " + row.get("state"));
				System.out.println("reference : " + row.get("reference"));
				System.out.println("amount_total : " + row.get("amount_total"));
				// System.out.println("Active : " + row.get("active"));
				Invoice inv = new Invoice();
				inv.setId((Integer)row.get("id"));
				inv.setName((String)row.get("number"));
				inv.setRefOrder((String)row.get("reference"));
				inv.setTotal((Double)row.get("amount_total"));
//				prod.setPrixVente((Double)row.get("lst_price"));
				listeInvoices.add(inv);
			}
			
			return listeInvoices;

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			return null;

		}
	}
	
}
