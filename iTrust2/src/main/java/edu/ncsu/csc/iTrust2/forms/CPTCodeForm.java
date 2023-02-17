package edu.ncsu.csc.iTrust2.forms;

import java.io.Serializable;
import java.util.Objects;

import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * A form for REST API communication. Contains fields for constructing CPTCode
 * objects.
 *
 * @author bmmorgan
 * @author bmtaylo7
 */
public class CPTCodeForm implements Serializable {

    private static final long serialVersionUID = 3L;

    /** id of the cpt code */
    private Long              id;

    /**
     * The code for the CPTCode
     */
    String                    code;

    /**
     * Description of the CPTCode
     */
    String                    description;

    /**
     * Cost associated with the CPTCode
     */
    double                    cost;

    /**
     * Status of the CPTCode
     */
    boolean                   active;

    /**
     * Empty constructor for filling in fields without a CPTCode object.
     */
    public CPTCodeForm () {
    }

    /**
     * Constructs a new form with information from the given CPTCode.
     *
     * @param cptcode
     *            The given CPT code
     */
    public CPTCodeForm ( final CPTCode cptcode ) {
      //  setId( cptcode.getId() );
        setCode( cptcode.getCode() );
        setDescription( cptcode.getDescription() );
        setCost( cptcode.getCost() );
       // setActive( true );
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
     * Getter for the id
     *
     * @return id
     */
    public Long getId () {
        return this.id;
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

    @Override
    public int hashCode () {
        return Objects.hash( code, cost, description, id );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final CPTCodeForm other = (CPTCodeForm) obj;
        return Objects.equals( code, other.code )
                && Double.doubleToLongBits( cost ) == Double.doubleToLongBits( other.cost )
                && Objects.equals( description, other.description );
    }

}
