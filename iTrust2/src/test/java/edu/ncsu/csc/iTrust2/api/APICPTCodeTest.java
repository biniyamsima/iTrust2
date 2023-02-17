package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
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
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Hospital;
import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.iTrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.HospitalService;
import edu.ncsu.csc.iTrust2.services.OfficeVisitService;
import edu.ncsu.csc.iTrust2.services.UserService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICPTCodeTest {
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CPTCodeService        service;

    @Autowired
    private UserService<User>     userService;

    @Autowired
    private OfficeVisitService    officeVisitService;

    @Autowired
    private HospitalService       hospitalService;

    /**
     * Sets up test
     */
    @Before
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void setup () throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
        userService.deleteAll();

        userService.save( new Personnel( new UserForm( "admin", "admin", Role.ROLE_ADMIN, 1 ) ) );
        userService.save( new Personnel( new UserForm( "hcp", "hcp", Role.ROLE_HCP, 1 ) ) );
        userService.save( new Personnel( new UserForm( "oph", "oph", Role.ROLE_OPH, 1 ) ) );
        userService.save( new Personnel( new UserForm( "billing", "billing", Role.ROLE_BILLING, 1 ) ) );
        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );
        userService.save( patient );
        final Hospital hospital = new Hospital( "iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC" );
        hospitalService.save( hospital );

    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing", roles = { "BILLING" } )
    public void testCodeAPI () throws Exception {
        service.deleteAll();
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "92203" );
        form.setCost( 100.00 );
        form.setDescription( "30-45 minutes" );

        String content = mvc.perform( post( "/api/v1/cptcodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        CPTCodeForm code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        form.setId( code.getId() ); // fill in the id of the code we just
        // created
        assertEquals( form, code );

        // Check that the list is not null
        assertNotNull( mvc.perform( get( "/api/v1/cptcodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString() );

        // Test get by ID
        content = mvc.perform( get( "/api/v1/cptcode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form, code );

        // edit it
        form.setCode( "92204" );
        content = mvc.perform( put( "/api/v1/cptcodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form, code );

        mvc.perform( delete( "/api/v1/cptcode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );


    }

    @Test
    @Transactional
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testCPTwithOfficeVisit () throws Exception {

        // Create two mock CPT Codes and save them
        final CPTCodeForm codeForm1 = new CPTCodeForm();
        codeForm1.setCode( "90001" );
        codeForm1.setCost( 10.00 );
        codeForm1.setDescription( "Test code 1" );

        final CPTCodeForm codeForm2 = new CPTCodeForm();
        codeForm2.setCode( "90002" );
        codeForm2.setCost( 20.00 );
        codeForm2.setDescription( "Test code 2" );

        final CPTCode code1 = new CPTCode( codeForm1 );
        final CPTCode code2 = new CPTCode( codeForm2 );

        service.save( new CPTCode( codeForm1 ) );
        service.save( new CPTCode( codeForm2 ) );

        // Create an office visit with two CPTCodes
        final OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "2048-04-16T09:50:00.000-04:00" ); // 4/16/2048 9:50 AM
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "iTrust Test Hospital 2" );
        form.setHdl( 1 );
        form.setHeight( 1f );
        form.setWeight( 1f );
        form.setLdl( 1 );
        form.setTri( 100 );
        form.setDiastolic( 1 );
        form.setSystolic( 1 );
        form.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        form.setPatientSmokingStatus( PatientSmokingStatus.FORMER );

        final List<CPTCode> list = new ArrayList<CPTCode>();
        list.add( code1 );
        list.add( code2 );

        form.setCPTCodeList( list );

        // Test that office visit builds correctly with CPTCodes
        final OfficeVisit visit = officeVisitService.build( form );
        assertEquals( 2, visit.getCPTCodeList().size() );
        // save office visit
        officeVisitService.save( visit );
        // get the office visit from database and test information is correct.
        final OfficeVisit retrieved = officeVisitService.findAll().get( 0 );

        final List<CPTCode> retrievedList = retrieved.getCPTCodeList();
        assertEquals( retrievedList.size(), 2 );
        for ( int i = 0; i < retrievedList.size(); i++ ) {
            assertEquals( retrievedList.get( i ).getCode(), list.get( i ).getCode() );
            assertEquals( retrievedList.get( i ).getDescription(), list.get( i ).getDescription() );
            assertEquals( retrievedList.get( i ).getCost(), list.get( i ).getCost(), 10 );
        }
    }
}
