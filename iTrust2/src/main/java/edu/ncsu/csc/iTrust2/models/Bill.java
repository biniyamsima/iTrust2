package edu.ncsu.csc.iTrust2.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.iTrust2.forms.BillForm;
import edu.ncsu.csc.iTrust2.models.enums.BillStatus;

/**
 * Represent a Bill in the system.
 *
 * @author mtdunni2
 *
 */
@Entity
public class Bill extends DomainObject {

    public Bill () {
    }

    public Bill ( final String patient, final String hcp, final BillStatus status, final LocalDate date,
            final List<CPTCode> codes, final List<BillPayment> payments ) {
        this.patient = patient;
        this.hcp = hcp;
        this.status = status;
        this.date = date;
        this.codes = codes;
        this.payments = payments;
    }

    /**
     * Id of the Bill
     */
    @Id
    @GeneratedValue
    Long                  id;

    /**
     * The patient associated with the Bill
     */
    @Length ( min = 1 )
    String                patient;

    /**
     * Health Care Provider associated with Bill
     */
    @Length ( min = 1 )
    String                hcp;

    /**
     * Status of the Bill's payment
     */
    @Enumerated ( EnumType.STRING )
    BillStatus            status;

    /**
     * Date of office visit associated with Bill
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    LocalDate             date;

    /**
     * List of CPT codes associated with Bill
     */
    @ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<CPTCode> codes;

    /**
     * List of BillPayments made towards Bill
     */
    @OneToMany ( cascade = CascadeType.ALL )
    @JoinColumn ( name = "bill_payment_set", columnDefinition = "varchar(100)" )
    List<BillPayment>     payments;

    /**
     * Updates Bill info with inputs from Bill Form
     *
     * @param status
     *
     * @param payments
     *
     */
    public void updateBill ( final BillForm form ) {
        // setId( form.getId() );
        setStatus( form.getStatus() );
        setPayments( form.getPayments() );
    }

    /**
     * Setter for bill's associated patient
     *
     * @param patient
     *            The new patient
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Setter for HCP (health care provider)
     *
     * @param hcp
     *            The new health care provider
     */
    public void setHCP ( final String hcp ) {
        this.hcp = hcp;
    }

    /**
     * Setter for bill's payment status
     *
     * @param status
     *            The bill status
     */
    public void setStatus ( final BillStatus status ) {
        this.status = status;
    }

    /**
     * Setter for bill's associated office visit date
     *
     * @param date
     *            The office visit date
     */
    public void setDate ( final LocalDate date ) {
        this.date = date;
    }

    /**
     * Setter for bill's set of CPT codes
     *
     * @param codes
     *            The bill's list of codes
     */
    public void setCodes ( final List<CPTCode> codes ) {
        this.codes = codes;
    }

    /**
     * Setter for bill's set of Bill Payments
     *
     * @param codes
     *            The bill's list of codes
     */
    public void setPayments ( final List<BillPayment> payments ) {
        this.payments = payments;
    }

    /**
     * Getter for bill's associated patient
     *
     * @return patient The bill's patient
     */
    public String getPatient () {
        return this.patient;
    }

    /**
     * Getter for HCP (health care provider)
     *
     * @return hcp The new health care provider
     */
    public String getHCP () {
        return this.hcp;
    }

    /**
     * Getter for bill's payment status
     *
     * @return status The bill status
     */
    public BillStatus getStatus () {
        return this.status;
    }

    /**
     * Getter for bill's associated office visit date
     *
     * @return date The office visit date
     */
    public LocalDate getDate () {
        return this.date;
    }

    /**
     * Getter for bill's set of CPT codes
     *
     * @return codes The bill's list of codes
     */
    public List<CPTCode> getCodes () {
        return this.codes;
    }

    /**
     * Getter for bill's set of Bill Payments
     *
     * @return payments The bill's list of bill payments
     */
    public List<BillPayment> getPayments () {
        return this.payments;
    }

    /**
     * Sets the id
     *
     * @param id
     *            the new id
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the id associated with this bill.
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Update the bill's status. Should be called whenever a bill is updated
     * through the API.
     */
    public void updateStatus () {
        double totalAmount = 0.0;
        for ( final CPTCode code : codes ) {
            totalAmount += code.getCost();
        }
        double totalPaid = 0.0;
        for ( final BillPayment payment : payments ) {
            totalPaid += payment.getPayAmount();
        }

        // If total amount matches amount paid, then bill is paid.
        if ( totalPaid == totalAmount ) {
            setStatus( BillStatus.PAID );
        }
        // If more than 60 days have passed and bill is not fully paid, mark as
        // delinquent.
        else if ( ChronoUnit.DAYS.between( date, LocalDate.now() ) >= 60 ) {
            setStatus( BillStatus.DELINQUENT );
        }
        // If there are any payments, the bill is partially paid.
        else if ( totalPaid > 0.0 ) {
            setStatus( BillStatus.PARTIALLY_PAID );
        }
        // Otherwise, bill is unpaid.
        else {
            setStatus( BillStatus.UNPAID );
        }
    }
}
