package application;

import java.util.Scanner;

import db.DB;
import entity.Department;
import entity.Seller;

public class Program {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		
		Seller seller = new Seller();
		
		System.out.print("Name: ");
		seller.setName(scanner.nextLine());
		
		System.out.print("Email: ");
		seller.setEmail(scanner.nextLine());
		
		System.out.print("Base salary: ");
		seller.setBaseSalary(scanner.nextDouble());
		
		scanner.nextLine();
		
		System.out.print("Birth date (dd/MM/yyyy): ");
		seller.setBirthDate(scanner.nextLine());
		
		System.out.print("Department number: ");
		seller.setDepartment(new Department(scanner.nextInt()));
		
		int returnId = seller.create();
		
		if (returnId != 0)
			System.out.println("User created with id: " + returnId);
		else
			System.out.println("User not created");
		
		for(String name : Seller.list()) {
			System.out.println(name);
		}
				
		DB.closeConnection();
		scanner.close();
	}
}
