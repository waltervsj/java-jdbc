package model.dao;

import java.util.List;

import entity.Department;
import entity.Seller;

public interface SellerDao {
	int insert(Seller seller);
	int update(Seller seller);
	int deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department department);
}
