package edu.ncsu.csc.iTrust2.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.BillStatus;

/**
 * Repository for interacting with Bill model. Method implementations generated
 * by Spring
 *
 * @author mtdunni2
 *
 */
public interface BillRepository extends JpaRepository<Bill, Long> {
	
	/**
     * Checks if a Bill with the provided date exists.
     *
     * @param date
     *            Date to check
     * @return Whether or not a Bill with this date exists.
     */
    public boolean existsByDate ( final LocalDate date );
    
    /**
     * Finds a Bill with the provided date
     * 
     * @param date
     *            Date to check
     * @return Bill, if found
     */
    public Bill findByDate ( final LocalDate date );
    
    /**
     * Checks if a Bill with the provided patient name exists.
     *
     * @param patient
     *            Patient name to check
     * @return Whether or not a Bill with this patient exists.
     */
    public boolean existsByPatient ( final String patient );
    
    /**
     * Find bill for a given patient
     * 
     * @param patient
     *            Patient to search by
     * @return Matching bills
     */
    //public List<Bill> findAllByPatient ( final String patient );
    
    /**
     * Finds a Bill with the provided patient name
     * 
     * @param patient
     *            Patient name to check
     * @return Bill, if found
     */
    public List<Bill> findByPatient ( final String patient );
    
    /**
     * Checks if a Bill with the provided status exists.
     *
     * @param status
     *            Status to check
     * @return Whether or not a Bill with this status exists.
     */
    public boolean existsByStatus ( final BillStatus status );
    
    /**
     * Finds a Bill with the provided bill status
     * 
     * @param status
     *            Status to check
     * @return Bill, if found
     */
    public Bill findByStatus ( final BillStatus status );
    
}
