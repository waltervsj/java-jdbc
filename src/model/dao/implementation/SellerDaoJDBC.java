package model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import entity.Department;
import entity.Seller;
import model.dao.SellerDao;

public class SellerDaoJDBC implements SellerDao {

	private Connection dbConnection;
	private Map<Integer, Department> map = new HashMap<>();
	
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
				Department department = instanciateDepartment(resultSet); 
				
				return instanciateSeller(resultSet, department);
			}
			
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultset(resultSet);
		}
	}
	
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(
					"SELECT s.*, d.Name as DepName "
					+ "FROM seller s "
					+ "INNER JOIN department d ON s.DepartmentId = d.Id "
					+ "WHERE d.id = ? "
					+ "ORDER BY s.name"
			);
			statement.setInt(1, department.getId());
			
			resultSet = statement.executeQuery();
		
			return returnSellers(resultSet);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultset(resultSet);
		}
	}

	private Seller instanciateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(resultSet.getInt("Id"));
		seller.setName(resultSet.getString("Name"));
		seller.setEmail(resultSet.getString("Email"));
		seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
		seller.setBirthDate(resultSet.getDate("BirthDate"));
		seller.setDepartment(department);
		return seller;
	}

	private Department instanciateDepartment(ResultSet resultSet) throws SQLException {
		return new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
	}

	public List<Seller> findAll() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(
					"SELECT s.*, d.Name as DepName "
					+ "FROM seller s "
					+ "INNER JOIN department d ON s.DepartmentId = d.Id "
					+ "ORDER BY s.name"
			);
			
			resultSet = statement.executeQuery();
			
			return returnSellers(resultSet);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultset(resultSet);
		}
	}

	private List<Seller> returnSellers(ResultSet resultSet) throws SQLException {
		List<Seller> sellers = new ArrayList<Seller>();
		while (resultSet.next()) {
			Department newDepartment = returnOrCreateDepartment(resultSet);
			sellers.add(instanciateSeller(resultSet, newDepartment));
		}
		return sellers;
	}

	private Department returnOrCreateDepartment(ResultSet resultSet) throws SQLException {
		Department department = map.get(resultSet.getInt("DepartmentId"));
		if (department == null) {
			department = instanciateDepartment(resultSet);
			map.put(resultSet.getInt("DepartmentId"), department);
		}
		return department;
	}

}
