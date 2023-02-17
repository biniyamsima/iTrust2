# iTrust2


*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)

## Introduction
This project involves the integration of billing functionality into the base iTrust2 Medical Records system. Currently, the iTrust2 system does not maintain a method for patients to be billed for office visits, which means that separate software must be used to maintain a patient's billing information and history. We have implemented logic into the existing iTrust2 system to allow patients to be billed for office visits, for patients to view and pay their bills, and for billing staff members to view bills in the system. These requirements detail how we will be implementing this functionality.

## Glossary
Health Care Personnel (HCP): A health care professional who works with medical records.
Patient: Upon their first visit to a practice using the iTrust2 medical records system, a patient is assigned a medical identification (MID) name and password. Then, this person's electronic records are accessible via the iTrust2 Medical Records system.
Billing Staff Member: A Staff Member in the system that is not an HCP, rather, they will only have access to billing information and functionality.
Current Procedural Terminology Code (CPT Code): A unique five digit code that is issued to a patients. The code indicates the type of service the patient received and the length of time of the service. CPTs are maintained by the Billing Staff Members and are assigned by HCPs. CPTs are viewable on bills.
Administrator: The administrator assigns medical identification numbers and passwords to HCPs.


# UC 1

## 1.1 Preconditions 
The iTrust2 admin has authenticated themselves in the iTrust2 medical record system.
***
## 1.2 Main Flow
The admin selects the option to create a user[S1][E21[E2] or delete[S2] a user. One of the user roles should be a billing staff member.
***
## 1.3 Sub-flows
* [S1]: An Admin enters a user name, a password, a confirmation of the password, the role[s], and if the user is enabled (or active) in the system. The Admin presses the button to add the user [E1][E2]. The user name serves as the User's Medical ID (MID). The possible roles are:

    *  Patient
    *  Health Care Provider (HCP)
    *  Optometrist
    *  Ophthalmologist
    *  Admin
    *  Emergency Responder
    *  Virologist
    *  Vaccinator
    *  Billing Staff Member
* [S2]: An Admin selects a Billing Staff Member from the list of possible users, confirms the delete, and presses the button to delete the user[E3].

***
## 1.4 Alternative Flows
[E1] The password and repeated password must match or an error is displayed <br>
[E2] The system prompts the Admin to correct the format of a required data field because the input of that data field does not match that specified in Section 1.6 <br>
[E3] If the billing staff member cannot be deleted, for example because other records in the system depend on them, the billing staff member is not deleted and an error message is displayed. <br>
***
## 1.5 Logging
***
|Transaction Code     |	Verbose Description    |Logged In MID  |Secondary MID       |Transaction Type       | Patient Viewable|
|---------------------|------------------------|---------------|--------------------|-----------------------|-----------------|
|100	              |   New user created     |   Admin       |Billing staff member|	   Create           |      No         |

## 1.6 Data Format
***
|Field	               |Format
|----------------------|------------------------------------------------------------|
|User Name             |Between 6 and 20 alpha characters and symbols - or _        |
|Password              |Between 6 and 20 characters                                 |
|Repeated password     |Between 6 and 20 characters                                 |

## 1.7 Acceptance Scenarios
***
### Scenario 1 :: Wrong repeated password
Admin Josh attempts to add a billing staff to the system by username and nonmatching passwords. The admin makes the selection for Billing Staff member and clicks add user. Josh sees an error message saying passwords do not match. Newman enters matching passwords and receives a message saying user added successfully.

### Scenario 2 :: Billing staff member cannot be deleted
Admin Josh attempts to delete a billing staff member already in the system that has records in the system that depends on them. The billing staff member is not deleted and an error message saying unsuccessful deletion is displayed.

### Scenario 3 :: Billing staff member is added successfully
Admin Josh adds a billing staff member to the system by username and password. Josh selects the user type to be a billing staff member then Josh clicks add user. No errors are produced and Josh can see the billing staff member listed in the system.
