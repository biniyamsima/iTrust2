package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.models.BillPayment;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.enums.PaymentMethod;
import edu.ncsu.csc.iTrust2.services.BillPaymentService;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class BillPaymentTest {

    @Autowired
    private BillPaymentService paymentService;

    @Autowired
    private CPTCodeService     codeService;

    @Test
    @Transactional
    public void testBillPaymentPojo () {
        final CPTCode code1 = new CPTCode();
        // code1.setId( 1L );
        code1.setCode( "92203" );
        code1.setCost( 100.00 );
        code1.setDescription( "30-45 minutes" );

        codeService.save( code1 );

        final LocalDate d = LocalDate.of( 2022, 5, 5 );

        //final BillPayment payment = new BillPayment( code1, 59.80, PaymentMethod.CHECK, d );

       // assertEquals( payment.getCode().getCode(), code1.getCode() );

      //  assertEquals( (Double) payment.getPayAmount(), (Double) 59.80 );

       // paymentService.save( payment );

        // saving another code
        final CPTCode code2 = new CPTCode();
        // code2.setId( 3L );
        code2.setCode( "92204" );
        code2.setCost( 100.00 );
        code2.setDescription( "30-45 minutes" );

        codeService.save( code2 );

      // final BillPayment payment2 = new BillPayment( code2, 59.80, PaymentMethod.CHECK, d );

       // paymentService.save( payment2 );
    }

}
