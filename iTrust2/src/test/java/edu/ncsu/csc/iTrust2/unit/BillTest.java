package edu.ncsu.csc.iTrust2.unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.BillForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.BillPayment;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.enums.BillStatus;
import edu.ncsu.csc.iTrust2.models.enums.PaymentMethod;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class BillTest {
	 @Autowired
	 private BillService BillService;
	 
	 @Test
	 @Transactional
	 public void testBillForm() {
		 // Tests that we can update a bill using a bill form.
		 
		 List<CPTCode> codes = new ArrayList<CPTCode>();
		 CPTCode c1 = new CPTCode();
		 CPTCode c2 = new CPTCode();
		 c1.setCode("11111");
		 c2.setCode("22222");
		 c1.setCost(100.0);
		 c2.setCost(50.0);
		 c1.setDescription("DESCRIPTION");
		 c2.setDescription("description");
		 codes.add(c1);
		 codes.add(c2);
		 
		 List<BillPayment> payments = new ArrayList<BillPayment>();
		// BillPayment bp1 = new BillPayment(c1, 50.0, PaymentMethod.CREDIT_CARD, LocalDate.of(2022, 6, 1));
		// BillPayment bp2 = new BillPayment(c2, 25.0, PaymentMethod.CASH, LocalDate.of(2022, 6, 1));
		// payments.add(bp1);
		// payments.add(bp2);
		 
		 final BillForm form = new BillForm();
		 form.setPayments(payments);
		 form.setStatus(BillStatus.PARTIALLY_PAID);
		 
		 
		 Bill bill1 = new Bill();
		 Bill bill2 = new Bill();
		 bill1.setId(1L);
		 bill2.setId(1L);
		 bill1.setPatient("Rick");
		 bill2.setPatient("Rick");
		 bill1.setHCP("Doug");
		 bill2.setHCP("Doug");
		 bill1.setStatus(BillStatus.PARTIALLY_PAID);
		 bill2.setStatus(BillStatus.UNPAID);
		 bill1.setDate(LocalDate.of(2022, 5, 26));
		 bill2.setDate(LocalDate.of(2022, 5, 26));
		 bill1.setCodes(codes);
		 bill2.setCodes(codes);
		 bill1.setPayments(payments);
		 bill2.setPayments(new ArrayList<BillPayment>());
		 
		 bill2.updateBill(form);
		 
		 assertEquals(bill1.getPatient(), bill2.getPatient());
		 assertEquals(bill1.getHCP(), bill2.getHCP());
		 assertEquals(bill1.getStatus(), bill2.getStatus());
		 assertEquals(bill1.getDate(), bill2.getDate());
		 assertEquals(bill1.getCodes(), bill2.getCodes());
		 assertEquals(bill1.getPayments(), bill2.getPayments());
		 assertEquals(bill1.getId(), bill2.getId());
		 
	 }
	 
	 @Test
	 @Transactional
	 public void testBillPersist() {
		 // Tests that Bills are able to be saved and retrieved from the database
		 List<CPTCode> codes = new ArrayList<CPTCode>();
		 CPTCode c1 = new CPTCode();
		 CPTCode c2 = new CPTCode();
		 c1.setCode("11111");
		 c2.setCode("22222");
		 c1.setCost(100.0);
		 c2.setCost(50.0);
		 c1.setDescription("DESCRIPTION");
		 c2.setDescription("description");
		 codes.add(c1);
		 codes.add(c2);
		 
		 List<BillPayment> payments = new ArrayList<BillPayment>();
		// BillPayment bp1 = new BillPayment(c1, 50.0, PaymentMethod.CREDIT_CARD, LocalDate.of(2022, 6, 1));
		// BillPayment bp2 = new BillPayment(c2, 25.0, PaymentMethod.CASH, LocalDate.of(2022, 6, 1));
		// payments.add(bp1);
		// payments.add(bp2);
		 
		 final Bill bill1 = new Bill("Rick", "Doug", BillStatus.PARTIALLY_PAID, LocalDate.of(2022, 5, 6), codes, payments);
		 final Bill bill2 = new Bill("John", "Mack", BillStatus.PARTIALLY_PAID, LocalDate.of(2022, 5, 6), codes, payments);
		 BillService.save(bill1);
		 BillService.save(bill2);
		 
		 List<Bill> dbList = BillService.findAll();
		 assertEquals(2, dbList.size());
		 
		 assertEquals(bill1.getPatient(), dbList.get(0).getPatient());
		 assertEquals(bill1.getHCP(), dbList.get(0).getHCP()); 
		 assertEquals(bill1.getStatus(), dbList.get(0).getStatus());
		 assertEquals(bill1.getDate(), dbList.get(0).getDate());
		 assertEquals(bill1.getCodes(), dbList.get(0).getCodes());
		 assertEquals(bill1.getPayments(), dbList.get(0).getPayments());
		 
		 assertEquals(bill2.getPatient(), dbList.get(1).getPatient());
		 assertEquals(bill2.getHCP(), dbList.get(1).getHCP()); 
		 assertEquals(bill2.getStatus(), dbList.get(1).getStatus());
		 assertEquals(bill2.getDate(), dbList.get(1).getDate());
		 assertEquals(bill2.getCodes(), dbList.get(1).getCodes());
		 assertEquals(bill2.getPayments(), dbList.get(1).getPayments());
		 
	 }
	 
	 @Test
	 @Transactional
	 public void testInvalidBill() {
		 List<CPTCode> codes = new ArrayList<CPTCode>();
		 CPTCode c1 = new CPTCode();
		 CPTCode c2 = new CPTCode();
		 c1.setCode("11111");
		 c2.setCode("22222");
		 c1.setCost(100.0);
		 c2.setCost(50.0);
		 c1.setDescription("DESCRIPTION");
		 c2.setDescription("description");
		 codes.add(c1);
		 codes.add(c2);
		 
		 List<BillPayment> payments = new ArrayList<BillPayment>();
		// BillPayment bp1 = new BillPayment(c1, 50.0, PaymentMethod.CREDIT_CARD, LocalDate.of(2022, 6, 1));
		// BillPayment bp2 = new BillPayment(c2, 25.0, PaymentMethod.CASH, LocalDate.of(2022, 6, 1));
		// payments.add(bp1);
		// payments.add(bp2);
		 
		 Bill bill1 = new Bill("", "", BillStatus.UNPAID, null, codes, payments);
		 Bill bill2 = new Bill("John", "Mack", BillStatus.PARTIALLY_PAID, LocalDate.of(2022, 5, 6), codes, payments);
		
		 // Test with empty patient name, empty HCP name, invalid status, invalid date, no codes
		 	
		// Test with empty patient name
		 try {
			 BillService.save(bill1);
			 assertEquals("Bill was created with an empty patient name", bill1.getPatient(), "");
		 } catch (ConstraintViolationException cvee) {

		 }
		 
		 bill1.setPatient("Rick");
		 
		// Test with empty HCP name
		 try {
			 BillService.save(bill1);
			 assertEquals("Bill was created with an empty health care provider name", bill1.getHCP(), "");
		 } catch (ConstraintViolationException cvee) {

		 }
		 
		 bill1.setPatient("Mack");
		 
		// Test with invalid date
		 try {
			 BillService.save(bill1);
			 Assert.assertNull("A Bill was created with a null date", bill1.getDate());
		 } catch (ConstraintViolationException cvee) {
			 
		 }

		// Test with no codes
		 try {
			 BillService.save(bill1);
			 assertEquals("A Bill was created with no CPT Codes", bill1.getCodes().size(), 0);
		 } catch (ConstraintViolationException cvee) {
			 
		 }

		 
	 }
	 

}