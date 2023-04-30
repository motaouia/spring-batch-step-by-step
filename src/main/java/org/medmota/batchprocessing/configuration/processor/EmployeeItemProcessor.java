package org.medmota.batchprocessing.configuration.processor;

import org.medmota.batchprocessing.dtos.EmployeeDto;
import org.medmota.batchprocessing.models.Employee;
import org.medmota.batchprocessing.repositories.CompanyRepository;
import org.medmota.batchprocessing.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeItemProcessor implements ItemProcessor<EmployeeDto, Employee> {

	private static final Logger log = LoggerFactory.getLogger(EmployeeItemProcessor.class);

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee process(EmployeeDto employeeDto) throws Exception {

		if (validateCompany(employeeDto.getCompanyId())) {
			Employee employee = employeeDto.employeeDtoToEmployee();
			log.info("save Employee to databse ...");
			employeeRepository.save(employee);
			log.info("Done saving employee!!!");
			return employee;
		}

		log.debug("Not a valid company, can't save the employee");
		return null;
	}

	private boolean validateCompany(Integer companyId) {
		return companyRepository.findById(companyId).isPresent();
	}

}
