package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Drug;
import edu.ncsu.csc.iTrust2.repositories.CPTCodeRepository;

/**
 * Service class for interacting with CPTCode model, performing CRUD tasks with
 * database.
 *
 * @author bmmorgan
 * @author bmtaylo7
 *
 */
@Component
@Transactional
public class CPTCodeService extends Service<CPTCode, Long> {

    /** Repository for CRUD tasks */
    @Autowired
    private CPTCodeRepository repository;

    @Override
    protected JpaRepository<CPTCode, Long> getRepository () {
        return repository;
    }

    /**
     * Builds CPT Code based off the give serialized code form
     * @param cf
     * 			The CPT Code form 
     * @return the CPT Code
     */
    public CPTCode build ( final CPTCodeForm cf ) {
        final CPTCode cc = new CPTCode();

        cc.setCode( cf.getCode() );
        cc.setCost( cf.getCost() );
        cc.setDescription( cf.getDescription() );
        cc.setId( cf.getId() );

        return cc;
    }

    /**
     * Checks if a CPTCode with the provided code exists.
     *
     * @param code
     *            Code to check
     * @return Whether or not a CPT code with this code exists.
     */
    public boolean existsByCode ( final String code ) {
        return repository.existsByCode( code );
    }
    
    /**
     * Finds a CPTCode with the provided code
     * 
     * @param code
     *            Code to check
     * @return CPTCode, if found
     */
    public CPTCode findByCode ( final String code ) {
        return repository.findByCode( code );
    }
    
   // public List<CPTCode> findCodes( final String code ){
   // 	return repository.find
   // }
}
