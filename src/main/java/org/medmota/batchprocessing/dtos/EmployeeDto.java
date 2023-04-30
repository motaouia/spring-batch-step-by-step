package org.medmota.batchprocessing.dtos;

import java.io.Serializable;

import org.medmota.batchprocessing.models.Company;
import org.medmota.batchprocessing.models.Employee;
import org.medmota.batchprocessing.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CompanyRepository companyRepository;

	private Integer employeeId;
	private Integer companyId;
	private String firstName;
	private String lastName;
	private String email;
	private String jobTitle;

	public EmployeeDto(Integer employeeId, Integer companyId, String firstName, String lastName, String email,
			String jobTitle) {
		super();
		this.employeeId = employeeId;
		this.companyId = companyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.jobTitle = jobTitle;
	}

	public Employee employeeDtoToEmployee() {

		Employee employee = new Employee();
		employee.setEmployeeId(this.getEmployeeId());
		employee.setFirstName(this.firstName);
		employee.setLastName(this.lastName);
		employee.setEmail(this.email);
		Company company = companyRepository.findById(this.companyId).get();
		employee.setCompany(company);
		employee.setJobTitle(this.jobTitle);
		return employee;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
