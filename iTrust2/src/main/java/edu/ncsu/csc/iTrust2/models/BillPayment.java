package edu.ncsu.csc.iTrust2.models;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.iTrust2.models.enums.PaymentMethod;

/**
 * The bill payment class that will be associated with bills
 *
 * @author Biniyam Sima
 *
 */
@Entity
public class BillPayment extends DomainObject {
    /**
     * The id of the bill payment
     */
    @Id
    @GeneratedValue
    private Long          id;
    /**
     * The pay amount of the bill payment
     */
    @Min ( 0 )
    private double        payAmount;
    /**
     * The enum type of the payMethod
     */
    @Enumerated ( EnumType.STRING )
    private PaymentMethod payMethod;
    /**
     * The pay date
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate     payDate;
    /**
     * date to sql
     */

    // Date localToSql;

    /**
     * Empty constructor
     */
    public BillPayment () {

    }

    /**
     * The constructor of BillPayment
     *
     * @param code
     *            the cpt code for the payment
     * @param payAmount
     *            the amount paid for the payment
     * @param payMethod
     *            the method of payment
     */
    public BillPayment ( final double payAmount, final PaymentMethod payMethod, final LocalDate payDate ) {
        this.payAmount = payAmount;

        this.payMethod = payMethod;

        this.payDate = payDate;

        // this.localToSql = Date.valueOf( payDate );
    }

    /**
     * Returns the payment amount for the bill payment
     *
     * @return pay amount
     */
    public double getPayAmount () {
        return payAmount;
    }

    /**
     * Returns the payment method of the bill payment
     *
     * @return payment method
     */
    public PaymentMethod getPayMethod () {
        return payMethod;
    }

    /**
     * Returns the id of the bill payment
     */
    @Override
    public Long getId () {
        // TODO Auto-generated method stub
        return id;
    }

    /**
     * Returns the payDate
     *
     * @return payDate
     */
    public LocalDate getPayDate () {
        return payDate;
    }

    public void setMethod ( final PaymentMethod pay ) {
        this.payMethod = pay;
    }

    public void setDate ( final LocalDate payDate ) {
        this.payDate = payDate;
    }

    public void setAmount ( final double amount ) {
        this.payAmount = amount;
    }
}
