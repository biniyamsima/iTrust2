package edu.ncsu.csc.iTrust2.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.BillForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.BillStatus;
import edu.ncsu.csc.iTrust2.repositories.BillRepository;

/**
 * Service class for interacting with Bill model, performing CRUD tasks with
 * database.
 *
 * @author mtdunni2
 *
 */
@Component
@Transactional
public class BillService extends Service<Bill, Long> {

    /** Repository for CRUD tasks */
    @Autowired
    private BillRepository repository;

    @Override
    protected JpaRepository<Bill, Long> getRepository () {
        return repository;
    }

    
    /**
     * Checks if a Bill with the provided date exists.
     *
     * @param date
     *            Date to check
     * @return Whether or not a Bill with this date exists.
     */
    public boolean existsByDate ( final LocalDate date ) {
        return repository.existsByDate( date );
    }
    
    /**
     * Finds a Bill with the provided date
     * 
     * @param date
     *            Date to check
     * @return Bill, if found
     */
    public Bill findByDate ( final LocalDate date ) {
        return repository.findByDate( date );
    }
    
    /**
     * Checks if a Bill with the provided patient name exists.
     *
     * @param patient
     *            Patient name to check
     * @return Whether or not a Bill with this patient exists.
     */
    public boolean existsByPatient ( final String patient ) {
        return repository.existsByPatient( patient );
    }
    
    /**
     * Find the list of bills associated with this patient
     * @param patient
     * 				The patient user
     * @return A list of bills
     */
    public List<Bill> findByPatient( final String patient ){
    	return repository.findByPatient( patient );
    }
    

    
    /**
     * Checks if a Bill with the provided status exists.
     *
     * @param status
     *            Status to check
     * @return Whether or not a Bill with this status exists.
     */
    public boolean existsByDate ( final BillStatus status ) {
        return repository.existsByStatus( status );
    }
    
    /**
     * Finds a Bill with the provided bill status
     * 
     * @param status
     *            Status to check
     * @return Bill, if found
     */
    public Bill findByDate ( final BillStatus status ) {
        return repository.findByStatus( status );
    }
    
}
