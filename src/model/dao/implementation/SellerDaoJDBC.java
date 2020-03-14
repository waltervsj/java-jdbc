package model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import entity.Department;
import entity.Seller;
import model.dao.SellerDao;

public class SellerDaoJDBC implements SellerDao {

	private Connection dbConnection;
	
	public SellerDaoJDBC(Connection connection) {
		this.dbConnection = connection;
	}
	
	public void insert(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	public void update(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	public Seller findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(
					"SELECT s.*, d.Name as DepName "
					+ "FROM seller s INNER JOIN department d "
					+ "ON s.DepartmentId = d.Id "
					+ "WHERE s.id = ?"
			);
			statement.setInt(1, id);
			
			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				Department department = new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
				department.setName(resultSet.getString("DepName"));
				
				Seller seller = new Seller();
				seller.setId(resultSet.getInt("Id"));
				seller.setName(resultSet.getString("Name"));
				seller.setEmail(resultSet.getString("Email"));
				seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
				seller.setBirthDate(resultSet.getDate("BirthDate"));
				seller.setDepartment(department);
				
				return seller;
			}
			
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultset(resultSet);
		}
	}

	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
