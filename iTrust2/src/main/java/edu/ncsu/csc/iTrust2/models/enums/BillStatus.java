package edu.ncsu.csc.iTrust2.models.enums;

public enum BillStatus {
	
	/**
     * Bill is full unpaid
     */
	UNPAID ( 1 ),
	/**
     * Bill is fully paid
     */
	PAID ( 2 ),
	/**
     * Bill is partially paid
     */
	PARTIALLY_PAID ( 3 ),
	/**
     * Bill is unpaid 60 days or more after visit
     */
	DELINQUENT ( 4 );
	
	/**
     * Code of the bill status
     */
    private int code;

    /**
     * Create a BillStatus from the numerical code.
     *
     * @param code
     *            Code of the Status
     */
    private BillStatus ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numerical Code of the BillStatus
     *
     * @return Code of the Status
     */
    public int getCode () {
        return code;
    }
}
