package application;

import java.util.List;

import entity.Department;
import entity.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {
	public static void main(String args[]) {
		SellerDao sellerDao = DaoFactory.createSellerDao();
		/*Seller seller = sellerDao.findById(1);
		System.out.println(seller);
		
		System.out.println("\n------- Find by departmentId -------");
		Department department = new Department(1);
		
		List<Seller> sellersFromDepartment = sellerDao.findByDepartment(department);
		
		for(Seller sel: sellersFromDepartment) {
			System.out.println(sel);
		}*/
		
		System.out.println("\n------- Find all -------");
		
		List<Seller> allSellers = sellerDao.findAll();
		
		for(Seller sel: allSellers) {
			System.out.println(sel);
		}
		
		System.out.println("\n------- Find by departmentId -------");
		Department department = new Department(1);
		
		List<Seller> sellersFromDepartment = sellerDao.findByDepartment(department);
		
		for(Seller sel: sellersFromDepartment) {
			System.out.println(sel);
		}
	}
}
