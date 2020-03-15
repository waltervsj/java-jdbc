package model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private static final String TABLE = "seller";
	private Connection dbConnection;
	private Map<Integer, Department> map = new HashMap<>();
	
	public SellerDaoJDBC(Connection connection) {
		this.dbConnection = connection;
	}
	
	public int insert(Seller seller) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = dbConnection.prepareStatement(
					"INSERT INTO " + TABLE
					+ "(name, email, birthdate, basesalary, departmentid)"
					+ "VALUES (?, ?, ?, ?, ?)"
			, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			
			if (preparedStatement.executeUpdate() > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				while(resultSet.next()) {
					seller.setId(resultSet.getInt(1));
				}
			}
			
			return seller.getId();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultset(resultSet);
		}
	}

	public int update(Seller seller) {
		PreparedStatement preparedStatement = null;
		
		try {
			String sqlCommand = 
				"UPDATE " + TABLE + " SET"
				+ " Name = ?"
				+ ", Email = ?"
				+ ", BirthDate = ?" 
				+ ", BaseSalary = ?"
				+ ", DepartmentId = ?"
				+ " WHERE Id = ?";
			
			preparedStatement = dbConnection.prepareStatement(sqlCommand);
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			preparedStatement.setInt(6, seller.getId());
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}

	public int deleteById(Integer id) {
		PreparedStatement preparedStatement = null;	
		try {
			preparedStatement = dbConnection.prepareStatement("DELETE FROM " + TABLE + " WHERE Id = ?");
			preparedStatement.setInt(1, id);
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}

	public Seller findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(
					"SELECT s.*, d.Name as DepName "
					+ "FROM " + TABLE + " s INNER JOIN department d "
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
					+ "FROM " + TABLE + " s "
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
					+ "FROM " + TABLE + " s "
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
