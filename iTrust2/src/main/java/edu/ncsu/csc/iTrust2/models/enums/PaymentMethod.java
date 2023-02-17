package edu.ncsu.csc.iTrust2.models.enums;

public enum PaymentMethod {
    /**
     * Credit card payment method
     */
    CREDIT_CARD ( "1" ),
    /**
     * Cash payment method
     */
    CASH ( "2" ),
    /**
     * Check payment method
     */
    CHECK ( "3" ),
    /**
     * Insurance payment method
     */
    INSURANCE ( "4" );

    /**
     * The current payment method
     */
    private String method;

    private PaymentMethod ( final String method ) {

        this.method = method;

    }

    /**
     * Gets the current payment method
     *
     * @return payment method
     */
    public String getMethod () {
        return method;
    }

    /**
     * Converts the PaymentMethod to a String
     */
    @Override
    public String toString () {
        return getMethod();
    }

    /**
     * Find the matching payment method enum type for the given string
     *
     * @param typeStr
     *            the string to find the enum type for
     * @return enum payment type
     */
    public static PaymentMethod parse ( final String typeStr ) {
        for ( final PaymentMethod method : values() ) {
            if ( method.getMethod().equals( typeStr ) ) {
                return method;
            }
        }
        return null;
    }

}

