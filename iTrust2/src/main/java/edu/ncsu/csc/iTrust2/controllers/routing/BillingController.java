package edu.ncsu.csc.iTrust2.controllers.routing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.iTrust2.models.enums.Role;

@Controller
public class BillingController {

    /**
     * Returns the Landing screen for the Billing user
     *
     * @param model
     *            Data from the front end
     * @return The page to display
     */
    @RequestMapping ( value = "billing/index" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING')" )
    public String index ( final Model model ) {
        return Role.ROLE_BILLING.getLanding();
    }

    /**
     * Creates the form page for the manage cpt codes page
     *
     * @param model
     *            data for the front end
     * @return page to show to the user
     */
    @GetMapping ( value = "/billing/manageCPTCodes" )
    @PreAuthorize ( "hasRole('ROLE_BILLING')" )
    public String manageCPTCodes ( final Model model ) {
        return "/billing/manageCPTCodes";
    }

    /**
     * Creates the form page for the view bills page for billing user
     *
     * @param model
     *            data for the front end
     * @return page to show to the user
     */
    @GetMapping ( value = "/billing/viewBills" )
    @PreAuthorize ( "hasRole('ROLE_BILLING')" )
    public String viewBills ( final Model model ) {
        return "/billing/viewBills";
    }

    @GetMapping ( value = "/patient/viewPatientBills" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public String viewPatientBills ( final Model model ) {
        return "/patient/viewPatientBills";
    }

}
