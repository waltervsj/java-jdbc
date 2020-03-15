package model.dao;

import java.util.List;

import entity.Department;

public interface DepartmentDao {
	int insert(Department department);
	int update(Department department);
	int deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
}
