package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.BillForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * API controller for interacting with the Bill model. Provides standard CRUD
 * routes as appropriate for different user types
 *
 * @author bmmorgan
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIBillController extends APIController {

    /** Billing service */
    @Autowired
    private BillService       billService;

    /** User service */
    @Autowired
    private UserService<User> userService;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil        loggerUtil;

    /**
     * Method for returning all of the bills saved in the system. This can only
     * be done by the billing user.
     *
     * @return A list of all of the bills in the system.
     */
    @GetMapping ( BASE_PATH + "/bill" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING')" )
    public List<Bill> getBills () {
        loggerUtil.log( TransactionType.BILLING_VIEW_BILLS, LoggerUtil.currentUser() );
        return billService.findAll();
    }

    /**
     * Retrieves a list of all Bills in the database for the current patient
     *
     * @return list of Bills
     */
    @GetMapping ( BASE_PATH + "/bill/mybills" )
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    public List<Bill> getMyBills () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.PATIENT_VIEW_BILLS, self );
        return billService.findByPatient( self.getUsername() );
    }

    /**
     * Get mapping for getting a single bill for the current patient
     *
     * @param id
     *            The id of the desired bill
     * @return The bill
     */
    @GetMapping ( BASE_PATH + "/bill/mybills/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    public ResponseEntity getMyBill ( @PathVariable final Long id ) {
        loggerUtil.log( TransactionType.PATIENT_VIEW_BILLS, LoggerUtil.currentUser() );
        if ( !billService.existsById( id ) ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( billService.findById( id ), HttpStatus.OK );
    }

    /**
     * Retrieves a specific Bill in the database, with the given ID
     *
     * @param id
     *            ID of the bill to retrieve
     * @return A single Bill
     */
    @GetMapping ( BASE_PATH + "/bill/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING')" )
    public ResponseEntity getBill ( @PathVariable final Long id ) {
        // final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.BILLING_VIEW_BILLS, LoggerUtil.currentUser() );
        if ( !billService.existsById( id ) ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( billService.findById( id ), HttpStatus.OK );
    }

    /**
     * Get a list of bills given a specific patient
     *
     * @param patient
     *            The patient to get the bills for
     * @return A list of bills
     */
    @GetMapping ( BASE_PATH + "/bill/patients/{patient}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING')" )
    public List<Bill> getBillByPatient ( @PathVariable final String patient ) {
        loggerUtil.log( TransactionType.BILLING_VIEW_BILLS, LoggerUtil.currentUser() );
        // if( !userService.existsByName(patient.getUsername())) {
        // return
        // }
        return billService.findByPatient( patient );

    }

    /**
     * Update a bill with the given id.
     *
     * @param id
     *            ID of the bill to update
     * @param billForm
     *            The bill to be validated and saved
     * @return response
     */
    @PutMapping ( BASE_PATH + "/bill/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING')" )
    public ResponseEntity updateBill ( @PathVariable final Long id, @RequestBody final BillForm billForm ) {
        try {
            // Make sure bill with the id exists.
            if ( !billService.existsById( id ) ) {
                return new ResponseEntity( errorResponse( "Bill with the id " + id + " doesn't exist" ),
                        HttpStatus.NOT_FOUND );
            }

            // Get the bill and update it with the billform.
            final Bill updated = billService.findById( id );
            updated.updateBill( billForm );
            updated.updateStatus();
            billService.save( updated );

            loggerUtil.log( TransactionType.BILLING_EDIT_PAYMENT, LoggerUtil.currentUser(), updated.getPatient() );
            return new ResponseEntity( updated, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Bill provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Delete a given bill
     *
     * @param id
     *            The bill to be deleted
     * @return response
     */
    @DeleteMapping ( BASE_PATH + "/bill/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING')" )
    public ResponseEntity removeBill ( @PathVariable final Long id ) {

        try {
            final Bill retrieved = billService.findById( id );

            if ( retrieved == null ) { // change to remove
                loggerUtil.log( TransactionType.BILLING_VIEW_BILLS, LoggerUtil.currentUser(),
                        "Could not find Bill with id " + id );
                return new ResponseEntity( errorResponse( "No Bill found with id " + id ), HttpStatus.NOT_FOUND );
            }
            billService.delete( retrieved ); // change to remove
            loggerUtil.log( TransactionType.BILLING_VIEW_BILLS, LoggerUtil.currentUser(),
                    "Deleted Bill code with id " + retrieved.getId() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) { // chnage to remove
            loggerUtil.log( TransactionType.CPT_REMOVE, LoggerUtil.currentUser(), "Failed to delete CPT code" );
            return new ResponseEntity( errorResponse( "Could not delete CPT code: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
}
