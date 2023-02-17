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

## Implemented Functionalities
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

## UC 2

### 2.1 Preconditions
The iTrust2 system contains at least one billing staff member. The iTrust2 user has authenticated themselves into iTrust2. 

### 2.2 Main Flow
The Billing User navigates to the CPT codes page, where a list of all the CPT codes and their related relevant information are displayed. From there, they have the ability to add[S1][E2][E3], edit[S2][E2], or remove[S3] any CPT code in the system.

### 2.3 Sub-flows
 * [S1] The Billing User selects the option to Add a CPT code to the system. This brings up input fields for the CPT code, description, and associated item price [E2, E3].
 * [S2]  The Billing User selects the option to edit a CPT code already in the system. This lets them select a CPT code and edit its code, description, or associated item price [E2]. 
 * [S3] The Billing User selects the option to delete a CPT code from the system. This allows them to select a CPT code to delete and to confirm to remove the CPT code from the system[E3]
### 2.4 Alternative Flows
 * [E1] If there are no currently stored CPT codes, the system prompts the Billing User that there are currently no CPT codes in the database. 
 * [E2] If the Billing User provides an invalid input, such as a non-integer value less than or equal to zero for the price or a code that is not 5 numerical characters an when adding or editing a CPT code, the code is not saved and an error message is displayed.
 * [E3] If the Billing User attempts to add a CPT code that already exists in the system, a duplicate error message is displayed and the code is not saved. 

### 2.5 Logging
| Transaction Code | Verbose Description | Logged In MID | Secondary MID | Transaction Type | Patient Viewable |
|------------------|---------------------|---------------|---------------|------------------|------------------|
| 2000 | Billing User Adds CPT code | Billing | None | Create | No |
| 2001 | Billing User Edits CPT Code | Billing | None | Edit | No |
| 2002 | Billing User Deletes CPT Code | Billing | None | Remove | No |

### 2.6 Data Format
|Field	               |Format
|----------------------|------------------------------------------------------------|
|Code                  |5 number code                                               |
|Description           |Short string description of the code, up to 50 characters   |
|Assoc. Item Price     |Price of the item the code is associated with               |

### 2.7 Acceptance Scenarios
Scenario 1 - Billing User adds a CPT code to system\
Billing User Mike opens the CPT page and chooses the option to add a CPT code. He fills in a valid 5 digit code, not already in the database. He then enters the description then associated item and price. He chooses the option to submit this code and it is added to the system. It then appears in the listing on the CPT codes page.

Scenario 2 - Billing User edits a CPT code in the system\
Billing User Mike opens the CPT page and chooses the option to edit a CPT code. He may fill in another valid 5 digit code, not already in the database. He then may enter a new description and then update the associated item and price. He chooses the option to submit this code and it is updated in the system. The changes then appear in the listing on the CPT codes page.


Scenario 3 - Billing User attempts to add an invalid CPT code from system\
Billing User Mike opens the CPT page and chooses the option to add a CPT code. He fills in a valid 5 digit code, not already in the database. He then enters the description then associated item. Finally he enters a negative non-integer value for the price. He chooses the option to submit this code. An error message appears, informing him that all prices must be integers greater than 0. The code is not added to the system. It doesn't appear in the listing on the CPT codes page.

## UC 3 Add CPT Codes to Office Visit

### 3.1 Preconditions
The iTrust2 system contains at least one HCP, patient, and billing staff member. The HCP is in the process of documenting an office visit for the patient. The iTrust2 user has authenticated themselves into iTrust2. 

### 3.2 Main Flow
During an office visit, an HCP should enter at least one CPT code appropriate for the office visit [S1][S2][E1]. At least once of the CPT codes must be from the list of office visit time ranges as listed in the Maintain CPT Codes description [(UC2)](https://github.ncsu.edu/engr-csc326-spring2022/csc326-TP-204-4/wiki/UC-2:-Maintain-CPT-Codes). Other CPT codes can be added as appropriate and if they are available in the iTrust2 system (e.g., a CPT code for administering a vaccine). The HCP will select the CPT code from a drop-down menu.

### 3.3 Sub-flows
 * [S1] The HCP adds one CPT code from the list of available CPT codes before saving the office visit.
 * [S2] The HCP adds multiple CPT codes from the list of available codes before saving the visit.

### 3.4 Alternative Flows
 * [E1] The HCP attempts to submit an office visit that does not have any assigned CPT codes.

### 3.5 Logging
| Transaction Code | Verbose Description | Logged In MID | Secondary MID | Transaction Type | Patient Viewable |
|------------------|---------------------|---------------|---------------|------------------|------------------|
| 701 | Office Visit created for patient | HCP | None | Edit | Yes |

### 3.6 Data Format
*The data created for this Use Case is that of an Office Visit. The HCP is adding CPT Codes to the OfficeVisits.*

### 3.7 Acceptance Scenarios
Scenario 1 - HCP adds CPT code to office visit\
HCP Sarah Heckwoman authenticates into iTrust2. Sarah creates an office visit for a patient that she has just visited with. The appointment lasted 30 minutes, so she adds the default CPT code for the time range 30-44 minutes. She finishes entering the rest of information for the office visit and submits it. A success message appears that reads: "Office Visit created successfully" and the office visit is saved to the database.

Scenario 2 - HCP adds multiple CPT codes to office visit\
HCP Sarah Heckwoman authenticates into iTrust2. Sarah creates an office visit for a patient that she has just visited with. The appointment lasted 45 minutes and the patient received an X-Ray during their visit. Sarah adds the default CPT code for the time range 45-59 minutes and another CPT code for X rays. She finishes entering the rest of information for the office visit and submits it. A success message appears that reads: "Office Visit created successfully" and the office visit is saved to the database.

Scenario 3 - HCP does not add CPT code to office visit\
HCP Sarah Heckwoman authenticates into iTrust2. Sarah creates an office visit for a patient that she has just visited with. The appointment lasted 20 minutes, but she forgets to add the relevant CPT code to the visit information. She finishes entering the rest of information for the office visit and submits it. An error message appears that reads: "An office visit must have at least one CPT code" and the office visit is not saved to the database.

# UC4: View Bills

## 4.1 Precondition
A Patient and Billing User ([UC1](https://github.ncsu.edu/engr-csc326-spring2022/csc326-TP-204-4/wiki/UC-1:-Adding-New-Billing-User)) are registered in the iTrust2 system. A user logs into the system. 

## 4.2 Main Flow
When on the Billing Page, the Billing User can see a list of bills, their status, and totals for all patients [E1]. The Billing user may select a specific bill [S1] and print it [S2] if they like. The list of bills is given in chronological order, with the most recent bills at the top.

When on the Billing Page, the Patient can see a list of all of their bills, amounts, designees, and status [E1]. The patient can select a specific bill [S1] and can chose to print it [S2] if they like.

## 4.3 Sub-flows
* [S1] When a bill is selected, the bill displays the patient name, office visit date, attending HCP, bill status, and an itemized list of CPT codes, descriptions, and costs.
* [S2] When a bill is selected, the user can chose to convert the bill into a PDF and print the bill.

## 4.4 Alternative Flows
* [E1] If there are no current Bills, then the Billing page will contain no information, only a message letting the user know they have no current Bills.

## 4.5 Logging
| Transaction Code | Verbose Description | Logged In MID | Secondary MID | Transaction Type | Patient Viewable |
|------------------|---------------------|---------------|---------------|------------------|------------------|
| 2200 | Patient Billing Page Viewed | Patient | None | View | Yes |
| 2201 | Billing User Billing Page Viewed | Billing User | None | View | No |
| 2210 | Patient Billing Download | Patient | None | View | Yes |
| 2211 | Billing User Billing Download | Billing User | None | View | No |

## 4.6 Data Format
*No new data is entered/created as a part of this Use Case, rather existing records are viewed. For Data Format, see UC3.*

## 4.7 Acceptance Scenarios 
**Scenario 1** - *Patient View Bills and Download*\
Patient Sarah Heckwoman authenticates into iTrust2. Sarah chooses to view her billing page. The system displays all of her current (unpaid) and past (paid) medical bills and the total amounts for each. She decides to select a specific bill. The specified bill shows her all of the information specified in [S1]. Sarah selects the optional "Download Bill" button and a PDF of the bill is downloaded to her device. A message is displayed that the bill has been downloaded successfully.

**Scenario 2** - *Billing User Views and Downloads Bill*\
Billing User, Bobby Billington, is an authenticated Billing user of iTrust2. Bobby chooses the view the billing page. The system displays all of the current (unpaid) and past (paid) medical bills and the total amounts for each. The list is in chronological order with the most recent bills at the top. Bobby is searching for a specific patient, so he goes to the search bar at the top and enters the patient's name. The list now only shows bills related to the searched patient. He selects the specific bill. The specified bill shows him all of the information specified in [S1]. Bobby selects the optional "Download Bill" button and a PDF of the bill is downloaded to his device. A message is displayed that the bill has been downloaded successfully.

**Scenario 3** - *No Bills are available*\
Patient, Zach Groseopen, is an authenticated in iTrust2, but has not yet been billed for his appointment. Zach chooses to view is billing page. Since Zach has not yet been billed, the billing page is contains no bills and is empty. A message appears on the screen letting Zach know that he does not have any current bills.

## 5.1 Preconditions
At least one patient and one billing user ([UC1](https://github.ncsu.edu/engr-csc326-spring2022/csc326-TP-204-4/wiki/UC-1:-Adding-New-Billing-User)) are registered in the iTrust2 system. A billing user is logged into the system and has accessed the main billing page that contains all bills in the system ([UC4](https://github.ncsu.edu/engr-csc326-spring2022/csc326-TP-204-4/wiki/UC-4:-View-Bills)). The billing user then selects one of the bills from this list.

## 5.2 Main Flow
The billing user selects the "Pay Bill" option while on the page for an individual bill. A form appears below the bill information that allows the billing user to record payment methods and the amount paid for each [S1][S2].

## 5.3 Sub-Flows
* [S1] If the bill is unpaid, then the billing user can select any number of payment methods from a list of cash, check, credit card, or insurance. The billing user then enters the amount paid with each payment method in their associated fields [E1]. If the total amount entered across all payment methods is equal to the bill amount, then the bill is automatically marked as paid. Otherwise, the bill is marked as partially paid.
* [S2] If the bill has already been paid in full or has been partially paid, then the form is populated with the existing payment information. The billing user can then edit this information [E1].

## 5.4 Alternative Flows
* [E1] If the total amount entered into the payment method fields is greater than the total bill amount, then the user is unable to submit the form and an error message is displayed beneath the list of payment methods.

## 5.5 Logging
| Transaction Code | Verbose Description | Logged In MID | Secondary MID | Transaction Type | Patient Viewable |
|------------------|---------------------|---------------|---------------|------------------|------------------|
| 2300 | Add Payment Information | Billing User | None | Create | No |
| 2301 | Edit Payment Information | Billing User | None | Edit | No |

## 5.6 Data Format
| Field | Format |
|-------|--------|
| Cash | Decimal amount paid |
| Check | Decimal amount paid |
| Credit Card | Decimal amount paid |
| Insurance | Decimal amount paid |

## 5.7 Acceptance Scenarios
* **Scenario 1** - *Add Payment to Unpaid Bill*\
An authenticated billing user logs into the iTrust2 system, visits the billing page, and then chooses an unpaid bill. They have just received payment information from the associated patient. The user presses the "Pay Bill" option and a form appears that allows them to enter information about the patient's payment methods. The bill is for a total of $75 and the patient had already paid $25 in cash while in the office. The patient then paid the remaining $50 of the bill with their credit card. The billing user selects the "Cash" checkbox and enters "25" into the associated field, then selects the "Credit Card" checkbox and enters "50" into its associated field. The billing user then presses the "Submit" button at the bottom of the form and a success message appears that states: "Payment Added Successfully". The bill page is updated to show a new status of "Paid" with the payment information that was just entered.

* **Scenario 2** - *Add Payment to Partially Paid Bill*\
An authenticated billing user logs into the iTrust2 system, visits the billing page, and then chooses a partially paid bill. The patient's insurance has already covered $100 of the total $125 bill, which had been entered into the system prior. The billing user has just received payment information from the patient regarding the remainder of the bill, which they are paying with a check. The billing user presses the "Pay Bill" option and a form appears that allows them to enter information about the patient's payment methods. The form is populated with the insurance payment information that had been entered previously. The billing user selects the "Check" checkbox and enters "25" into the associated field, then presses the "Submit" button at the bottom of the form. A success message appears that states: "Payment Updated Successfully" and the bill page is updated to show a new status of "Paid" with the payment information that was just entered.

* **Scenario 3** - *Incorrect Total Paid*\
An authenticated billing user logs into the iTrust2 system, visits the billing page, and then chooses an unpaid bill. They have just received payment information from the associated patient. The user presses the "Pay Bill" option and a form appears that allows them to enter information about the patient's payment methods. The bill is for a total of $100 and the patient had already paid $50 in cash while in the office. The patient then paid the remaining $50 of the bill with their credit card. The billing user selects the "Cash" checkbox and enters "50" into the associated field, then selects the "Credit Card" checkbox and accidentally enters "75" into its associated field. The billing user then presses the "Submit" button at the bottom of the form and an error message appears that states: "Amount paid is greater than bill amount". The payment information is not saved.
