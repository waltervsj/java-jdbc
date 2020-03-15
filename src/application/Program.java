package application;

import java.util.List;

import entity.Department;
import entity.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {
	public static void main(String args[]) {
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("<document HTML>"
				+ "<body>");
		
		if (1 > 2) {
			System.out.println("Error");
		} else {
			System.out.println("	<h1>Hello world!</h1>");
		}
			System.out.println("</body>");
	}
}
