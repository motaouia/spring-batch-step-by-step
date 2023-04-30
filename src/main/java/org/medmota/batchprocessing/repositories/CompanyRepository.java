package org.medmota.batchprocessing.repositories;

import org.medmota.batchprocessing.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
