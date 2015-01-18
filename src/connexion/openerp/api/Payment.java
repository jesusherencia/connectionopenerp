package connexion.openerp.api;

import java.util.ArrayList;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;

public class Payment extends ConnexionOpenErp {

	private static String journal_bnk = "BNK1"; // Code Journal de BNK1
	
	private static Integer account_cr = 13; // Creditors
	private static Integer account_dr = 8; // Debtors
	
	private static String typeVoucherPurchase = "payment";
	private static String typeVoucherSale = "receipt";
	
	private static String typeVoucherLinePurchase = "dr";
	private static String typeVoucherLineSale = "cr";

	public static Integer creerVoucherFromPurchaseOrder(Integer order_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			// Purchase order
			ObjectAdapter purchaseOrderAd = openERPSession
					.getObjectAdapter("purchase.order");
			// ////
			
			RowCollection listePurchaseOrder = purchaseOrderAd.readObject(
					new Integer[] { order_id }, purchaseOrderAd.getFieldNames() );
			Row rowPurchaseOrder = listePurchaseOrder.get(0);
			
			Integer partner_id = (Integer)((Object [])rowPurchaseOrder.get("partner_id"))[0];
			
			// moveLine pour le voucherLine
			ObjectAdapter moveLineAd = openERPSession
					.getObjectAdapter("account.move.line");
			FilterCollection filters1 = new FilterCollection();
			filters1.add("ref", "=", rowPurchaseOrder.get("name").toString());
			filters1.add("name", "=", rowPurchaseOrder.get("name").toString());
			RowCollection listeMoveLine = moveLineAd.searchAndReadObject(filters1, new String[] { "id", "name"});
			Row rowMoveLine = listeMoveLine.get(0);
			Integer idMoveLine = rowMoveLine.getID();
					
			// Creation de voucher
			Integer voucher_id = Payment.creerVoucher(partner_id, (Double)rowPurchaseOrder.get("amount"), (String)rowPurchaseOrder.get("name"), typeVoucherPurchase);
			
			// add lines to account_voucher_line en utilisant l'invoice du purchase order
			// Invoices
			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			Object[] invoice_ids = (Object[]) rowPurchaseOrder
					.get("invoice_ids");
			
			Payment.creerVoucherLine(voucher_id, invoice_ids, idMoveLine, typeVoucherLinePurchase, account_cr);
			
			// confirmation de voucher
			Payment.confirmerVoucher(voucher_id);
			
			//System.out.println("New Row ID: " + newVoucher.getID());
			return voucher_id;

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}
	
	public static Integer creerVoucherSaleFromInvoice(Integer invoice_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			// Purchase order
			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			// ////
			
			RowCollection listeInvoice = invoiceAd.readObject(
					new Integer[] { invoice_id }, new String[] {"id", "name", "state", "partner_id", "amount_total", "reference"} );
			Row rowInvoice = listeInvoice.get(0);
			
			Integer partner_id = (Integer)((Object [])rowInvoice.get("partner_id"))[0];
			
			// moveLine pour le voucherLine
			ObjectAdapter moveLineAd = openERPSession
					.getObjectAdapter("account.move.line");
			FilterCollection filters1 = new FilterCollection();
			filters1.add("ref", "=", rowInvoice.get("reference").toString());
			//filters1.add("name", "=", "/");
			filters1.add("debit", ">", 0);
			RowCollection listeMoveLine = moveLineAd.searchAndReadObject(filters1, new String[] { "id", "name"});
			Row rowMoveLine = listeMoveLine.get(0);
			Integer idMoveLine = rowMoveLine.getID();
			//Integer idMoveLine = null;
					
			// Creation de voucher
			Integer voucher_id = Payment.creerVoucher(partner_id, (Double)rowInvoice.get("amount_total"), (String)rowInvoice.get("reference"), typeVoucherSale);
			
			// add lines to account_voucher_line en utilisant l'invoice du purchase order
			Payment.creerVoucherLine(voucher_id, new Integer[]{invoice_id}, idMoveLine, typeVoucherLineSale, account_dr);
			
			// confirmation de voucher
			Payment.confirmerVoucher(voucher_id);
			
			//System.out.println("New Row ID: " + newVoucher.getID());
			return voucher_id;

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}
	
	public static Integer creerVoucher(Integer partner_id, Double amount, String reference, String type) {
		try {
			
			// obtenir le journal et account pour paiement en BNK1
			ObjectAdapter journalAd = openERPSession
					.getObjectAdapter("account.journal");
			FilterCollection filters = new FilterCollection();
			filters.add("code", "=", journal_bnk);
			RowCollection listeJournal = journalAd.searchAndReadObject(filters, new String[] { "id", "code", "default_debit_account_id"});
			Row rowJournal = listeJournal.get(0);
			Integer idJournal = rowJournal.getID(); // 8
			
			// ////
			ObjectAdapter voucherAd = openERPSession
					.getObjectAdapter("account.voucher");
			
			Row newVoucher = voucherAd.getNewRow(new String[] {
					"partner_id",
					"journal_id",
					"account_id",
					"amount",
					//"reference",
					"type",
					});
			newVoucher.put("partner_id", partner_id);
			newVoucher.put("journal_id", idJournal); // 8
			newVoucher.put("account_id", (Integer)((Object [])rowJournal.get("default_debit_account_id"))[0]); // Account Cash 25
//			newVoucher.put("amount", rowPurchaseOrder.get("amount")); // il faudra l'obtenir depuis les invoices
//			newVoucher.put("reference", rowPurchaseOrder.get("name")); // reference p.o.
			newVoucher.put("amount", amount); // il faudra l'obtenir depuis les invoices
			//newVoucher.put("reference", reference); // reference p.o., pour le saleOrder est vide
			newVoucher.put("type", type); // payment
			voucherAd.createObject(newVoucher);
			
			System.out.println("New Row ID: " + newVoucher.getID());
			
			return (Integer)newVoucher.getID();
		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}
	
	public static ArrayList<Integer> creerVoucherLine(Integer voucher_id, Object[] invoice_ids, Integer idMoveLine, String type, Integer account_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			// add lines to account_voucher_line en utilisant l'invoice du purchase order
			// Invoices
//			ObjectAdapter invoiceAd = openERPSession
//					.getObjectAdapter("account.invoice");
//			Object[] invoice_ids = (Object[]) rowPurchaseOrder
//					.get("invoice_ids");
			
			ObjectAdapter invoiceAd = openERPSession
					.getObjectAdapter("account.invoice");
			ObjectAdapter voucherLineAd = openERPSession
					.getObjectAdapter("account.voucher.line");
			
			ArrayList<Integer> listaIds = new ArrayList<Integer>();
			
			if (invoice_ids != null && invoice_ids.length>0) {
				System.out.println("Invoices:");
				RowCollection invoices = invoiceAd.readObject(invoice_ids,
						new String[] {"id", "name", "state", "amount_total"}); // si on met tous les champs il done un erreur quand l'invoice est validé
				// on cree de voucherLine par chaque invoice
				for (Row invoice : invoices) {
					System.out.println("  Id: " + invoice.get("id"));
//					System.out.println("  Name: " + invoice.get("name"));
//					System.out.println("  State: " + invoice.get("state"));
					System.out.println("  amount_total: " + invoice.get("amount_total"));
					
					Row newVoucherLine = voucherLineAd.getNewRow(new String[] {
							"voucher_id",
							"move_line_id",
							"account_id",
							"amount",
							"type",
							});
					newVoucherLine.put("voucher_id", voucher_id);
					newVoucherLine.put("move_line_id", idMoveLine); // obtenir depuis account_move_line // required?
					newVoucherLine.put("account_id", account_id);
					newVoucherLine.put("amount", invoice.get("amount_total")); // obtenir depuis l'invoice
					newVoucherLine.put("type", type);
					voucherLineAd.createObject(newVoucherLine);
					listaIds.add(newVoucherLine.getID());
				}
			}
			return listaIds;
		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();
			return null;

		}
	}
	
	public static void confirmerVoucher(Integer voucher_id) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			
			// Voucher
			ObjectAdapter voucherAd = openERPSession
					.getObjectAdapter("account.voucher");
			// ////
			
			RowCollection listeVoucher = voucherAd.readObject(
					new Integer[] { voucher_id }, voucherAd.getFieldNames() );
			Row rowVoucher = listeVoucher.get(0);
			
			voucherAd.executeWorkflow(rowVoucher, "proforma_voucher");
		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			e.printStackTrace();

		}
	}
	
}
