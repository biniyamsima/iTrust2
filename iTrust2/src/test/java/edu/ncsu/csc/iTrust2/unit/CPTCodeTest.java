package edu.ncsu.csc.iTrust2.unit;

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
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CPTCodeTest {
	 @Autowired
	 private CPTCodeService CPTCodeService;
	 
	 @Test
	 @Transactional
	 public void testCPTCodeForm() {
		 // Tests that we can create a CPT Code off of a CPT Code form.
		 final CPTCodeForm form = new CPTCodeForm();
		 form.setId(1L);
		 form.setCode("92203");
		 form.setCost(100.00);
		 form.setDescription("30-45 minutes");
		 
		 CPTCode code1 = new CPTCode();
		 code1.setId(1L);
		 code1.setCode("92203");
		 code1.setCost(100.00);
		 code1.setDescription("30-45 minutes");
		 
		 CPTCode code2 = new CPTCode(form);
		 
		 assertEquals(code1.getCode(), code2.getCode());
		 assertEquals(code1.getCost(), code2.getCost(), 10); // Delta
		 assertEquals(code1.getDescription(), code2.getDescription());
		// assertEquals(code1.getId(), code2.getId());
		 
	 }
	 
	 @Test
	 @Transactional
	 public void testCPTCodePersist() {
		 // Tests that CPT Codes are able to be saved and retrieved from the database
		 final CPTCodeForm form = new CPTCodeForm();
		 form.setCode("92203");
		 form.setCost(100.00);
		 form.setDescription("30-45 minutes");
		 
		 final CPTCodeForm form2 = new CPTCodeForm();
		 form2.setCode("92212");
		 form2.setCost(50.00);
		 form2.setDescription("20-39 minutes");
		 
		 CPTCode code1 = new CPTCode(form);
		 CPTCode code2 = new CPTCode(form2);
		 CPTCodeService.save(code1);
		 CPTCodeService.save(code2);
		 
		 List<CPTCode> dbList = CPTCodeService.findAll();
		 assertEquals(2, dbList.size());
		 
		 assertEquals(code1.getCode(), dbList.get(0).getCode());
		 assertEquals(code1.getCost(), dbList.get(0).getCost(), 10); // Delta
		 assertEquals(code1.getDescription(), dbList.get(0).getDescription());
		 
		 assertEquals(code2.getCode(), dbList.get(1).getCode());
		 assertEquals(code2.getCost(), dbList.get(1).getCost(), 10); // Delta
		 assertEquals(code2.getDescription(), dbList.get(1).getDescription());
		 
	 }
	 
	 @Test
	 @Transactional
	 public void testInvalidCPTCode() {
		 CPTCode code1 = new CPTCode();
		 // Test with too long of a code
		 code1.setCode("922444");
		 code1.setCost(50.00);
		 code1.setDescription("30-40 minutes");
		// List<CPTCode> dbList = new ArrayList<>();
		 try {
			 CPTCodeService.save(code1);
			 Assert.assertNull("A CPT Code was created with too long code", code1.getCode());

		 } catch (ConstraintViolationException cvee) {

		 }
		 
		 code1.setCode("abcd");
		 try {
			 CPTCodeService.save(code1);
			 Assert.assertNull("A CPT Code was created with invalid code", code1.getCode());

		 } catch (ConstraintViolationException cvee) {

		 }
		 
		 code1.setCode("92201");
		 code1.setDescription("");
		 try {
			 CPTCodeService.save(code1);
			 Assert.assertNull("A CPT Code was created with an empty description", code1.getCode());
		 } catch (ConstraintViolationException cvee) {

		 }
		 
		 code1.setDescription("30-40 minutes");
		 code1.setCost(-10.00);
		 try {
			 CPTCodeService.save(code1);
			 Assert.assertNull("A CPT Code was created with a negative cost", code1.getCode());
		 } catch (ConstraintViolationException cvee) {

		 }

		 
	 }
	 

}
