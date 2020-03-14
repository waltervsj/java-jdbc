package entity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;

public class Seller implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String email;
	private String birthDate;
	private Double baseSalary;
	private Department department;
	
	public static final String TABLE = "seller";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthDate() {
		return dateFormat.format(birthDate);
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Double getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(Double baseSalary) {
		this.baseSalary = baseSalary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Seller() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seller other = (Seller) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seller [id=" + id + ", name=" + name + ", email=" + email + ", birthDate=" + birthDate + ", baseSalary="
				+ baseSalary + ", department=" + department + "]";
	}

	public int create() {
		Connection dbConnection = DB.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = dbConnection.prepareStatement(
					"INSERT INTO " + TABLE
					+ "(name, email, birthdate, basesalary, departmentid)"
					+ "VALUES (?, ?, ?, ?, ?)"
			, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, this.name);
			preparedStatement.setString(2, this.email);
			preparedStatement.setDate(3, new java.sql.Date(dateFormat.parse(this.birthDate).getTime()));
			preparedStatement.setDouble(4, this.baseSalary);
			preparedStatement.setInt(5, this.department.getId());
			
			if (preparedStatement.executeUpdate() > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				while(resultSet.next()) {
					this.id = resultSet.getInt(1);
				}
				DB.closeResultset(resultSet);
			}
			
			return this.id;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new RuntimeException("Error to convert date");
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}
	
	public int update() {
		Connection dbConnetion = DB.getConnection();
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
			
			preparedStatement = dbConnetion.prepareStatement(sqlCommand);
			
			preparedStatement.setString(1, this.name);
			preparedStatement.setString(2, this.email);
			preparedStatement.setDate(3, new java.sql.Date(dateFormat.parse(this.birthDate).getTime()));
			preparedStatement.setDouble(4, this.baseSalary);
			preparedStatement.setInt(5, this.department.getId());
			preparedStatement.setInt(6, this.id);
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new RuntimeException("Error to convert date");
		} finally {
			DB.closeStatement(preparedStatement);
		}
		
	}
	
	public boolean delete() {
		Connection dbConnetion = DB.getConnection();
		PreparedStatement preparedStatement = null;
		
		try {
		
			preparedStatement = dbConnetion.prepareStatement("DELETE FROM " + TABLE + " WHERE Id = ?");

			preparedStatement.setInt(1, this.id);
			
			return preparedStatement.execute();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}
	
	public static List<String> list() {
		Connection dbConnection = DB.getConnection();
		List<String> sellers = new ArrayList<>();
		Statement statement = null;
		
		try {
			statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from " + TABLE);
			System.out.println("Has rows: " + resultSet.first());
			resultSet.beforeFirst();
			
			while (resultSet.next()) {
				sellers.add(resultSet.getString("name"));
			}
			
			return sellers;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
		}
	}
}
