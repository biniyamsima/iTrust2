# iTrust2


*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)

##Introduction
This project involves the integration of billing functionality into the base iTrust2 Medical Records system. Currently, the iTrust2 system does not maintain a method for patients to be billed for office visits, which means that separate software must be used to maintain a patient's billing information and history. We will be implementing logic into the existing iTrust2 system to allow patients to be billed for office visits, for patients to view and pay their bills, and for billing staff members to view bills in the system. These requirements detail how we will be implementing this functionality.

##Glossary
Health Care Personnel (HCP): A health care professional who works with medical records.
Patient: Upon their first visit to a practice using the iTrust2 medical records system, a patient is assigned a medical identification (MID) name and password. Then, this person's electronic records are accessible via the iTrust2 Medical Records system.
Billing Staff Member: A Staff Member in the system that is not an HCP, rather, they will only have access to billing information and functionality.
Current Procedural Terminology Code (CPT Code): A unique five digit code that is issued to a patients. The code indicates the type of service the patient received and the length of time of the service. CPTs are maintained by the Billing Staff Members and are assigned by HCPs. CPTs are viewable on bills.
Administrator: The administrator assigns medical identification numbers and passwords to HCPs.
