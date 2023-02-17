package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;

/**
 * Represent a CPT Code in the system.
 *
 * @author bmmorgan
 * @author bmtaylo7
 *
 */
@Entity
public class CPTCode extends DomainObject {
    /**
     * Id of the CPTCode
     */
    @Id
    @GeneratedValue
    Long    id;

    /**
     * The code for the CPTCode
     */
    @NotEmpty
    @Pattern ( regexp = "^\\d{5}$" )
    String  code;

    /**
     * Description of the CPTCode
     */
    @NotEmpty
    @Length ( max = 1024 )
    String  description;

    /**
     * Cost associated with the CPTCode
     */
    @Min ( 0 )
    double  cost;

    /**
     * Whether or not this is an active CPT code.
     */
    boolean active;

    /**
     * Empty constructor
     */
    public CPTCode () {
    }

    /**
     * Constructor for CPTCode
     *
     * @param code
     *            A unique 5 digit code
     * @param description
     *            A description for the cpt code
     * @param cost
     *            A cost associated with the code.
     */
    public CPTCode ( final CPTCodeForm form ) {
       // setId( form.getId() );
        setCode( form.getCode() );
        setDescription( form.getDescription() );
        setCost( form.getCost() );
        //setActive( form.getActive() );
    }

    /**
     * Setter for code
     *
     * @param code
     *            The new code
     */
    public void setCode ( final String code ) {
        this.code = code;
    }

    /**
     * Setter for description
     *
     * @param description
     *            The new description
     */
    public void setDescription ( final String description ) {
        this.description = description;
    }

    /**
     * Setter for cost
     *
     * @param cost
     *            The new cost
     */
    public void setCost ( final double cost ) {
        this.cost = cost;
    }

    /**
     * Getter for code
     *
     * @return code
     */
    public String getCode () {
        return this.code;
    }

    /**
     * Getter for description
     *
     * @return description
     */
    public String getDescription () {
        return this.description;
    }

    /**
     * Getter for cost
     *
     * @return cost
     */
    public double getCost () {
        return this.cost;
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
     * Returns the id associated with this code.
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the status of the code.
     *
     * @param active
     *            Whether or not the code is active.
     */
    public void setActive ( final boolean active ) {
        this.active = active;
    }

    /**
     * Getter for active status.
     *
     * @return whether or not the code is active.
     */
    public boolean getActive () {
        return this.active;
    }
}
