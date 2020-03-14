package model.dao;

import java.util.List;

import entity.Seller;

public interface SellerDao {
	void insert(Seller seller);
	void update(Seller seller);
	void deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
}