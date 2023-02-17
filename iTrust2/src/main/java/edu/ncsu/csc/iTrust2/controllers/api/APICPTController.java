package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.OfficeVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints that deal with CPTCodes. Exposes functionality to
 * add, edit, fetch, and delete codes.
 *
 * @author bmmorgan
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICPTController extends APIController {

    /** CPT Code service */
    @Autowired
    private CPTCodeService     service;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil         loggerUtil;

    /** OfficeVisit service */
    @Autowired
    private OfficeVisitService officeVisitService;

    /**
     * Adds new CPT Code to the system. User must be logged in as a billing user
     * first.
     *
     * @param form
     *            Form to create the CPT Code
     * @return the create cpt code
     */
    @PreAuthorize ( "hasRole('ROLE_BILLING')" )
    @PostMapping ( BASE_PATH + "/cptcodes" )
    public ResponseEntity addCPTCode ( @RequestBody final CPTCodeForm form ) {

        try {
            // Build the code based on the form
            final CPTCode cpt = new CPTCode( form );
            // Get list of the saved CPTCodes
            final List<CPTCode> saved = service.findAll();
            // for every code see if it matches the new code to be added.
            for ( final CPTCode c : saved ) {
                if ( c.getCode().equals( cpt.getCode() ) ) {
                    // If we have a match make sure that code is inactive
                    if ( c.getActive() ) {
                        loggerUtil.log( TransactionType.CPT_CREATE, LoggerUtil.currentUser(),
                                "Conflict: CPT code with code " + cpt.getCode() + " already exists" );
                        return new ResponseEntity(
                                errorResponse( "CPT code with code " + cpt.getCode() + " already exists" ),
                                HttpStatus.CONFLICT );
                    }
                }
            }

            cpt.setActive( true );
            service.save( cpt );
            loggerUtil.log( TransactionType.CPT_CREATE, LoggerUtil.currentUser(),
                    "CPT code " + cpt.getCode() + " created" );
            return new ResponseEntity( cpt, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.CPT_CREATE, LoggerUtil.currentUser(), "Failed to create CPT code" );
            return new ResponseEntity( errorResponse( "Could not add CPT code: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );

        }
    }

    /**
     * Edits a CPT code in the system. The id stored in the form must match an
     * existing code, and changes to NDCs cannot conflict with existing NDCs.
     * Requires billinig permissions.
     *
     * @param form
     *            the edited cpt code form
     * @return the edited cpt code or an error message
     */
    @SuppressWarnings ( "unused" )
    @PreAuthorize ( "hasRole('ROLE_BILLING')" )
    @PutMapping ( BASE_PATH + "/cptcodes" )
    public ResponseEntity editCPTCode ( @RequestBody final CPTCodeForm form ) {
        try {
            // Build the code based on the form
            final CPTCode cpt = new CPTCode( form );
            // Get list of the saved CPTCodes
            final List<CPTCode> saved = service.findAll();
            // for every code see if it matches the new code to be added.
            for ( final CPTCode c : saved ) {
                if ( c.getCode().equals( cpt.getCode() ) ) {
                    // If we have a match make sure that code is inactive
                    if ( c.getActive() ) {
                        loggerUtil.log( TransactionType.CPT_CREATE, LoggerUtil.currentUser(),
                                "Conflict: CPT code with code " + cpt.getCode() + " already exists" );
                        return new ResponseEntity(
                                errorResponse( "CPT code with code " + cpt.getCode() + " already exists" ),
                                HttpStatus.CONFLICT );
                    }
                }
            }

            // Find the code we are overwriting and mark it inactive
            final CPTCode savedCode = service.findById( form.getId() );
            cpt.setActive( true );
            savedCode.setActive( false );
            service.save(
                    savedCode ); /*
                                  * Overwrite existing CPT code, making it false
                                  */
            service.save( cpt ); /* Save the new code */

            loggerUtil.log( TransactionType.CPT_EDIT, LoggerUtil.currentUser(),
                    "CPT code with id " + cpt.getId() + " edited" );
            return new ResponseEntity( cpt, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.CPT_EDIT, LoggerUtil.currentUser(), "Failed to edit CPT code" );
            return new ResponseEntity( errorResponse( "Could not update CPT code: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Remove the CPT code with the id matching the given id. Requires billing
     * permissions.
     *
     * @param id
     *            the id of the code to delete
     * @return the id of the deleted code
     */
    @PreAuthorize ( "hasRole('ROLE_BILLING')" )
    @DeleteMapping ( BASE_PATH + "/cptcode/{id}" )
    public ResponseEntity removeCPTCode ( @PathVariable final String id ) {
        try {
            final CPTCode cpt = service.findById( Long.parseLong( id ) );
            if ( cpt == null ) {
                loggerUtil.log( TransactionType.CPT_REMOVE, LoggerUtil.currentUser(),
                        "Could not find CPT code with id " + id );
                return new ResponseEntity( errorResponse( "No CPT code found with id " + id ), HttpStatus.NOT_FOUND );
            }

            cpt.setActive( false );
            service.save( cpt );
            loggerUtil.log( TransactionType.CPT_REMOVE, LoggerUtil.currentUser(),
                    "Deleted CPT code with id " + cpt.getId() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.CPT_REMOVE, LoggerUtil.currentUser(), "Failed to delete CPT code" );
            return new ResponseEntity( errorResponse( "Could not delete CPT code: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Gets a list of all the CPT codes in the system.
     *
     * @return a list of CPT codes
     */
    @GetMapping ( BASE_PATH + "/cptcodes" )
    public List<CPTCode> getCPTCodes () {
        loggerUtil.log( TransactionType.CPT_VIEW, LoggerUtil.currentUser(), "Fetched list of CPT codes" );
        List<CPTCode> all = service.findAll();

        // Generate all default CPT codes if none exist in the system.
        if ( all.isEmpty() ) {
            all = generateDefaultCPT();
            loggerUtil.log( TransactionType.CPT_DEFAULT, LoggerUtil.currentUser(),
                    "List of default CPT codes generated" );
        }

        // Only display active CPT codes.
        final List<CPTCode> active = new ArrayList<CPTCode>();
        for ( final CPTCode code : all ) {
            if ( code.getActive() ) {
                active.add( code );
            }
        }
        return active;
    }

    /**
     * Get a specific CPT code by id
     *
     * @param id
     *            ID of the code to find
     * @return the CPT code if found
     */
    @GetMapping ( BASE_PATH + "/cptcode/{id}" )
    public ResponseEntity getCPTCode ( @PathVariable ( "id" ) final String id ) {
        try {
            final CPTCode code = service.findById( Long.parseLong( id ) );
            if ( code == null ) {
                return new ResponseEntity( errorResponse( "No code with id " + id ), HttpStatus.NOT_FOUND );
            }
            loggerUtil.log( TransactionType.CPT_VIEW, LoggerUtil.currentUser(), "Fetched CPT code with id " + id );
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not retrieve CPT Code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Generates the list of default CPT codes. Should only be called on the
     * first API call to getCPTCodes().
     */
    private List<CPTCode> generateDefaultCPT () {
        final List<CPTCode> codes = new ArrayList<CPTCode>();

        final CPTCode code1 = new CPTCode();
        code1.setCode( "99202" );
        code1.setDescription( "15-29 minutes" );
        code1.setCost( 75.00 );
        code1.setActive( true );
        service.save( code1 );
        codes.add( code1 );

        final CPTCode code2 = new CPTCode();
        code2.setCode( "99203" );
        code2.setDescription( "30-44 minutes" );
        code2.setCost( 150.00 );
        code2.setActive( true );
        service.save( code2 );
        codes.add( code2 );

        final CPTCode code3 = new CPTCode();
        code3.setCode( "99204" );
        code3.setDescription( "45-59 minutes" );
        code3.setCost( 200.00 );
        code3.setActive( true );
        service.save( code3 );
        codes.add( code3 );

        final CPTCode code4 = new CPTCode();
        code4.setCode( "99205" );
        code4.setDescription( "60-74 minutes" );
        code4.setCost( 250.00 );
        code4.setActive( true );
        service.save( code4 );
        codes.add( code4 );

        final CPTCode code5 = new CPTCode();
        code5.setCode( "99212" );
        code5.setDescription( "10-19 minutes" );
        code5.setCost( 50.00 );
        code5.setActive( true );
        service.save( code5 );
        codes.add( code5 );

        final CPTCode code6 = new CPTCode();
        code6.setCode( "99213" );
        code6.setDescription( "20-29 minutes" );
        code6.setCost( 100.00 );
        code6.setActive( true );
        service.save( code6 );
        codes.add( code6 );

        final CPTCode code7 = new CPTCode();
        code7.setCode( "99214" );
        code7.setDescription( "30-39 minutes" );
        code7.setCost( 125.00 );
        code7.setActive( true );
        service.save( code7 );
        codes.add( code7 );

        final CPTCode code8 = new CPTCode();
        code8.setCode( "99215" );
        code8.setDescription( "40-54 minutes" );
        code8.setCost( 175.00 );
        code8.setActive( true );
        service.save( code8 );
        codes.add( code8 );

        return codes;
    }
}
