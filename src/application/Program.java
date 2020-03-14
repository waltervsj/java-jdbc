package application;

import java.util.List;

import entity.Department;
import entity.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {
	public static void main(String args[]) {
		SellerDao sellerDao = DaoFactory.createSellerDao();
		Seller seller = sellerDao.findById(1);
		System.out.println(seller);
		
		System.out.println("------- Find by departmentId -------");
		Department department = new Department(1);
		
		List<Seller> sellers = sellerDao.findByDepartment(department);
		
		for(Seller sel: sellers) {
			System.out.println(sel);
		}
	}
}
