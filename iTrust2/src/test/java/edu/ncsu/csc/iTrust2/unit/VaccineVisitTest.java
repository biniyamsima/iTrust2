package edu.ncsu.csc.iTrust2.unit;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccineAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccineType;
import edu.ncsu.csc.iTrust2.models.VaccineVisit;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.Status;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccineAppointmentRequestService;
import edu.ncsu.csc.iTrust2.services.VaccineTypeService;
import edu.ncsu.csc.iTrust2.services.VaccineVisitService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class VaccineVisitTest {
	 
	 @Autowired
	 private UserService<User>         userService;
	 
	 @Autowired
	 private VaccineVisitService vaccineService;
	 
	@Autowired
	private CPTCodeService            cptCodeService;
	
	@Autowired
	private VaccineAppointmentRequestService            vaccAppService;
	
	@Autowired
	private VaccineTypeService            vaccTypeService;
	    
    @Test
    @Transactional
    public void testVaccineVisit() {
    	vaccineService.deleteAll();
        Assert.assertEquals( 0, vaccineService.count() );
        
        // Create Mock roles
        final User user1 = new Personnel( new UserForm( "vaccinator", "123456", Role.ROLE_VACCINATOR, 1 ) );
        final Patient alice = new Patient( new UserForm( "AliceThirteen", "123456", Role.ROLE_PATIENT, 1 ) );
        userService.saveAll( List.of( user1, alice ) );
        
        // Creating an appointment request.
        final VaccineAppointmentRequest app = new VaccineAppointmentRequest();
        app.setHcp( user1 );
        final VaccineType vacc = new VaccineType();
        app.setVaccine( vacc );
        app.setPatient(alice);
        app.setStatus(Status.APPROVED);
        app.setComments("None");
        final ZonedDateTime now = ZonedDateTime.now();
        app.setDate(now);
        app.setType(AppointmentType.VACCINATION);

        vaccTypeService.save(vacc);
        vaccAppService.save(app);
        
        // Create the visit
        final VaccineVisit vac = new VaccineVisit();
        vac.setCorrespondingRequest(app);
        vac.setPatient(alice);
        vac.setVaccinator(user1);
        vac.setVaccineType(vacc);
        vac.setDate(now);       
        vac.setFollowUpDate( now.plus( Period.ofWeeks( 5 ) ) );       
        vac.setDose(5);
        
        // Create the CPT Codes
        final CPTCode cptCode1 = new CPTCode();
        cptCode1.setCode( "10175" );
        cptCode1.setCost( 55.0 );
        cptCode1.setDescription( "30-45 minutes" );
        cptCodeService.save( cptCode1 );
        final List<CPTCode> cpts = new ArrayList<CPTCode>();
        cpts.add( cptCode1 );
        vac.setCPTList( cpts );

        // Save and make sure we can get everything.
        vaccineService.save(vac);
        
        Assert.assertEquals( 1, vaccineService.count() );

        VaccineVisit retrieved = vaccineService.findAll().get( 0 );
        
        Assert.assertEquals(retrieved.getCPTList().size(), 1);

    	
    }

}
