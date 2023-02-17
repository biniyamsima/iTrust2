package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Repository for interacting with CPTCode model. Method implementations generated
 * by Spring
 *
 * @author bmmorgan
 * @author bmtaylo7
 *
 */
public interface CPTCodeRepository extends JpaRepository<CPTCode, Long> {
	
	 /**
     * Check if a CPTCode exists with the given code
     * 
     * @param code
     *            Code to search by
     * @return If the cpt code exists
     */
    public boolean existsByCode ( String code );

    /**
     * Finds a CPTCode with the provided code
     * 
     * @param code
     *            Code to check
     * @return CPTCode, if found
     */
    public CPTCode findByCode ( String code );
    
}
