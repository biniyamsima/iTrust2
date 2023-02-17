package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.BillForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.BillPayment;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.BillStatus;
import edu.ncsu.csc.iTrust2.models.enums.PaymentMethod;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.AppointmentRequestService;
import edu.ncsu.csc.iTrust2.services.BasicHealthMetricsService;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.HospitalService;
import edu.ncsu.csc.iTrust2.services.OfficeVisitService;
import edu.ncsu.csc.iTrust2.services.UserService;

/**
 * Test for the API functionality for interacting with bills
 *
 * @author bmmorgan
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIBillTest {

    private MockMvc                   mvc;

    @Autowired
    private WebApplicationContext     context;

    @Autowired
    private OfficeVisitService        officeVisitService;

    @Autowired
    private UserService<User>         userService;

    @Autowired
    private AppointmentRequestService appointmentRequestService;

    @Autowired
    private HospitalService           hospitalService;

    @Autowired
    private BasicHealthMetricsService bhmService;

    @Autowired
    private CPTCodeService            cptService;

    @Autowired
    private BillService               billService;

    @Test
    @Transactional
    @WithMockUser ( username = "billing", roles = { "BILLING" } )
    public void testBills () throws Exception {

        // Create mock users, CPT codes, and a bill and save them to the
        // database.
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );
        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );
        userService.saveAll( List.of( patient, hcp ) ); // antti

        final CPTCode cpt = new CPTCode();
        cpt.setCode( "99503" );
        cpt.setCost( 75.00 );
        cpt.setDescription( "35-45 minutes" );
        cptService.save( cpt );

        final Bill b = new Bill();
        b.setHCP( hcp.getUsername() );
        b.setPatient( patient.getUsername() );
        final LocalDate date = LocalDate.of( 2021, 6, 15 );
        b.setDate( date );

        final List<CPTCode> cptList = new ArrayList<>();
        cptList.add( cpt );
        b.setCodes( cptList );

        b.setStatus( BillStatus.UNPAID );

        billService.save( b );
        assertEquals( billService.count(), 1 );

        // Test getting all of the bills
        mvc.perform( get( "/api/v1/bill" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );

        final Long id = billService.findAll().get( 0 ).getId();

        // Testing getting a specific bill
        mvc.perform( get( "/api/v1/bill/" + b.getId() ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );

        // Filling out a bill form and mock payment to test updating bill
        final BillForm bf = new BillForm();
        final BillPayment bp = new BillPayment();
        bp.setAmount( 25.00 );
        // bp.setCode(cpt);
        bp.setMethod( PaymentMethod.CREDIT_CARD );
        bp.setDate( date );
        final List<BillPayment> bpList = new ArrayList<>();
        bpList.add( bp );

        bf.setPayments( bpList );
        bf.setStatus( BillStatus.PARTIALLY_PAID );

        // Test updating the bill
        mvc.perform( put( "/api/v1/bill/" + id ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( bf ) ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );

        final List<Bill> retrieved = billService.findAll();
        // Make sure updated values are correct.
        assertEquals( retrieved.size(), 1 );
        assertEquals( retrieved.get( 0 ).getStatus(), BillStatus.DELINQUENT );
        // assertEquals(
        // retrieved.get(0).getPayments().get(0).getCode().getCode(),
        // cpt.getCode());

        // Test getting bills given a patient
        mvc.perform( get( "/api/v1/bill/patients/" + patient.getUsername() ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testBillsPatient () throws Exception {

        // Create mock users, CPT codes, and a bill and save them to the
        // database.
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );
        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );
        userService.saveAll( List.of( patient, hcp ) ); // antti

        final CPTCode cpt = new CPTCode();
        cpt.setCode( "99503" );
        cpt.setCost( 75.00 );
        cpt.setDescription( "35-45 minutes" );
        cptService.save( cpt );

        final Bill b = new Bill();
        b.setHCP( hcp.getUsername() );
        b.setPatient( patient.getUsername() );
        final LocalDate date = LocalDate.of( 2021, 6, 15 );
        b.setDate( date );

        final List<CPTCode> cptList = new ArrayList<>();
        cptList.add( cpt );
        b.setCodes( cptList );

        b.setStatus( BillStatus.UNPAID );

        billService.save( b );
        assertEquals( billService.count(), 1 );

        // Testing getting bills as a patient
        mvc.perform( get( "/api/v1/bill/mybills" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );

        final Long id = billService.findByPatient( patient.getUsername() ).get( 0 ).getId();

        mvc.perform( get( "/api/v1/bill/mybills/" + id ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );

    }

}
