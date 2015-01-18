package connexion.openerp.api;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;

import connexion.openerp.api.model.Customer;
import connexion.openerp.api.model.Product;

public class ConnexionOpenErp {

	public static String host;
	public static Integer port;
	public static String db;
	public static String user;
	public static String pass;

	protected static Session openERPSession;
	
	public static Object getParameter(String parameter) {
		try{
			Properties prop = new Properties();
			String propFileName = "connexion.properties";
			FileReader in = new FileReader(propFileName);
			prop.load(in);
			return prop.getProperty(parameter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void initSession() {
		try{
//			host = (String)ConnexionOpenErp.getParameter("host");
//			port = Integer.valueOf(ConnexionOpenErp.getParameter("port").toString());
//			db = (String)ConnexionOpenErp.getParameter("db");
//			user = (String)ConnexionOpenErp.getParameter("user");
//			pass = (String)ConnexionOpenErp.getParameter("pass");
			
			openERPSession = new Session(host, port, db, user,
					pass);
			
			openERPSession.startSession();
		} catch (Exception e) {
			e.printStackTrace();;

		}
	}

	public static ArrayList<Customer> listerCustomers() {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			//openERPSession.startSession();
			ObjectAdapter partnerAd = openERPSession
					.getObjectAdapter("res.partner");
			// ////
			FilterCollection filters = new FilterCollection();
			filters.add("customer", "=", true);
			RowCollection partners = partnerAd
					.searchAndReadObject(filters, new String[] { "id", "name",
							"email", "phone", "city", "id" });
			
			ArrayList<Customer> listeCustomers = new ArrayList<Customer>();
			
			for (Row row : partners) {
				System.out.println("Id : " + row.get("id"));
				System.out.println("Name:" + row.get("name"));
				// System.out.println("Email:" + row.get("email"));
				// System.out.println("Phone:" + row.get("phone"));
				// System.out.println("City:" + row.get("city"));
				// System.out.println("ID customers:" + row.get("id"));
				
				Customer c = new Customer();
				c.setId((Integer)row.get("id"));
				c.setName((String)row.get("name"));
				listeCustomers.add(c);

			}
			
			return listeCustomers;

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			return null;

		}
	}

	public static void listerSuppliers() {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			ObjectAdapter partnerAd = openERPSession
					.getObjectAdapter("res.partner");
			// ////
			FilterCollection filters = new FilterCollection();
			filters.add("supplier", "=", true);
			RowCollection partners = partnerAd
					.searchAndReadObject(filters, new String[] { "id", "name",
							"email", "phone", "city", "id" });
			for (Row row : partners) {
				System.out.println("Id : " + row.get("id"));
				System.out.println("Name:" + row.get("name"));
				// System.out.println("Email:" + row.get("email"));
				// System.out.println("Phone:" + row.get("phone"));
				// System.out.println("City:" + row.get("city"));
				// System.out.println("ID customers:" + row.get("id"));

			}

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());

		}
	}
	
	public static ArrayList<Product> listerProducts() {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			ObjectAdapter productAd = openERPSession
					.getObjectAdapter("product.product");
			// ////
			FilterCollection filters = new FilterCollection();
			RowCollection products = productAd.searchAndReadObject(filters,
					productAd.getFieldNames());
			
			ArrayList<Product> listeProducts = new ArrayList<Product>();
			
			for (Row row : products) {
				System.out.println("Id : " + row.get("id"));
				System.out.println("Nom : " + row.get("name"));
				System.out.println("Price : " + row.get("price"));
				System.out.println("lst_price : " + row.get("lst_price"));
				System.out.println("price_margin : " + row.get("price_margin"));
				System.out.println("price_extra : " + row.get("price_extra"));
				System.out.println("pricelist_id : " + row.get("pricelist_id"));
				System.out.println("Qty available : "
						+ row.get("qty_available"));
				// System.out.println("Active : " + row.get("active"));
				Product prod = new Product();
				prod.setId((Integer)row.get("id"));
				prod.setName((String)row.get("name"));
				prod.setQtyAvailable((Double)row.get("qty_available"));
				prod.setPrixVente((Double)row.get("lst_price"));
				listeProducts.add(prod);
			}
			
			return listeProducts;

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());
			return null;

		}
	}

	public static void createCustomer() {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			ObjectAdapter partnerAd = openERPSession
					.getObjectAdapter("res.partner");

			// ///////Créer un contact

			Row newPartner = partnerAd
					.getNewRow(new String[] { "name", "ref" });
			newPartner.put("name", "New Customer");
			newPartner.put("ref", "Reference Number1");
			partnerAd.createObject(newPartner);

			System.out.println("New Row ID: " + newPartner.getID());
			// ///Fin créer contact

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());

		}
	}	

	public static void consultationStock(String prod) {
		try {
			// startSession logs into the server and keeps the userid of the
			// logged in user
			initSession();
			ObjectAdapter consulterStock = openERPSession
					.getObjectAdapter("product.product");
			// ////
			FilterCollection filters = new FilterCollection();
			filters.add("name", "=", prod);
			RowCollection stocks = consulterStock.searchAndReadObject(filters,
					new String[] { "name", "qty_available" });
			for (Row row : stocks) {
				System.out.println("Nom : " + row.get("name"));
				System.out.println("Quantite : " + row.get("qty_available"));

			}

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n"
					+ e.getMessage());

		}
	}
	
}
