package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.BillPayment;
import edu.ncsu.csc.iTrust2.repositories.BillPaymentRepository;

/**
 * The BillPayment service class
 *
 * @author Biniyam Sima
 *
 */
@Component
@Transactional
public class BillPaymentService extends Service<BillPayment, Long> {
    /**
     * BillPaymentRepository
     */
    @Autowired
    private BillPaymentRepository repository;

    /**
     * Returns the repository
     */
    @Override
    protected JpaRepository<BillPayment, Long> getRepository () {

        return repository;
    }
}
