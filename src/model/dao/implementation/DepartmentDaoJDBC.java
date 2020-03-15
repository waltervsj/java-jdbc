package model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import entity.Department;
import model.dao.DepartmentDao;

public class DepartmentDaoJDBC implements DepartmentDao {

	private static final String TABLE = "department";
	private Connection dbConnection;
	
	public DepartmentDaoJDBC(Connection connection) {
		this.dbConnection = connection;
	}
	
	
	public int insert(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = dbConnection.prepareStatement(
					"INSERT INTO " + TABLE
					+ " (name)"
					+ " VALUES (?)"
			, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, department.getName());
			
			if (preparedStatement.executeUpdate() > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				while(resultSet.next()) {
					department.setId(resultSet.getInt(1));
				}
			}
			
			return department.getId();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultset(resultSet);
		}
	}

	public int update(Department department) {
		PreparedStatement preparedStatement = null;
		
		try {
			String sqlCommand = 
				"UPDATE " + TABLE + " SET"
				+ " Name = ?"
				+ " WHERE Id = ?";
			
			preparedStatement = dbConnection.prepareStatement(sqlCommand);
			
			preparedStatement.setString(1, department.getName());
			preparedStatement.setInt(2, department.getId());
			
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

	public Department findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(
					"SELECT * "
					+ "FROM " + TABLE
					+ " WHERE id = ?"
			);
			statement.setInt(1, id);
			
			resultSet = statement.executeQuery();
			if(resultSet.next())
				return new Department(resultSet.getInt("id"), resultSet.getString("name"));
			
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultset(resultSet);
		}
	}

	public List<Department> findAll() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(
					"SELECT * "
					+ "FROM " + TABLE
					+ " ORDER BY name"
			);
			
			List<Department> departments = new ArrayList<Department>();
			resultSet = statement.executeQuery();
			while(resultSet.next()) {
				departments.add(new Department(resultSet.getInt("Id"), resultSet.getString("Name")));
			}
			return departments;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultset(resultSet);
		}
	}

}
