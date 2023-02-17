package edu.ncsu.csc.iTrust2.forms;

import java.util.List;

import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.BillPayment;
import edu.ncsu.csc.iTrust2.models.enums.BillStatus;


/**
 * A form for REST API communication. Contains fields for updating Bill
 * objects.
 *
 * @author mtdunni2
 */
public class BillForm {

	///** ID of the Bill */
    //private Long   id;

    /** Name of the Drug */
    private BillStatus status;

    /** List of payments made towards the bill */
    private List<BillPayment> payments;

    /**
     * Empty constructor for filling in fields without a Drug object.
     */
    public BillForm () {
    }

    /**
     * Constructs a new form with information from the given drug.
     *
     * @param drug
     *            the drug object
     */
    public BillForm ( final Bill bill ) {
        setStatus( bill.getStatus() );
        setPayments( bill.getPayments() );
    }

    /**
     * Returns the drug's NDC
     *
     * @return the NDC
     */
    public BillStatus getStatus () {
        return status;
    }

    /**
     * The list of payments entered into the BillForm
     *
     * @return the payment list
     */
    public List<BillPayment> getPayments () {
        return payments;
    }


    /**
     * Sets the payment status of the BillForm
     *
     * @param the bill's status
     *            
     */
    public void setStatus ( final BillStatus status ) {
        this.status = status;
    }

    /**
     * Sets the drug name.
     *
     * @param name
     *            the name of the drug
     */
    public void setPayments ( final List<BillPayment> payments ) {
        this.payments = payments;
    }

}
