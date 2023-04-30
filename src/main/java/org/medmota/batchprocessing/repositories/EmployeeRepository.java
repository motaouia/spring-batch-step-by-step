package org.medmota.batchprocessing.repositories;

import org.medmota.batchprocessing.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}
