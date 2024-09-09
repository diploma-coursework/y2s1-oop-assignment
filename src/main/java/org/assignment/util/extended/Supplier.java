package org.assignment.util.extended;

import org.assignment.util.base.Person;

import java.time.LocalDate;
import java.util.ArrayList;

public class Supplier extends Person {
    protected LocalDate contractStartDate;
    protected LocalDate contractEndDate;
    protected final int contractYear = 2;

    // Constructors
    public Supplier() {
        this("", "", "", "", "", null);
    }

    public Supplier(String id, String name, String contactNumber, String address, String email, LocalDate contractStartDate) {
        super(id, name, contactNumber, email, address);
        this.contractStartDate = contractStartDate;
        extendContract();
    }

    // Getters
    public LocalDate getContractStartDate() { return contractStartDate; }
    public LocalDate getContractEndDate()   { return contractEndDate;   }
    public int getContractYear()            { return contractYear;      }

    // Setter
    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    // Extend Contract
    public void extendContract() {
        this.contractEndDate = this.contractStartDate.plusYears(contractYear);
    }

    // Contract Renewal
    // Only allow contract renewal within 2 - 6 months before the current contract ends
    public int renewContract () {
        LocalDate today = LocalDate.now();
        LocalDate renewPeriodStart = this.contractEndDate.minusMonths(6);
        LocalDate renewPeriodEnd = this.contractEndDate.minusMonths(6);

        if (today.isBefore(renewPeriodStart))
            return -1;  // Not reach renew period yet
        else if (today.isAfter(renewPeriodEnd))
            return 1;   // Over renew period, unable to renew
        else {
            extendContract();
            return 0;   // Success renew contract
        }
    }

    // Check Contract Availability
    public boolean checkContractAvailability() {
        LocalDate today = LocalDate.now();
        return (!today.isBefore(this.contractStartDate) && !today.isAfter(this.contractEndDate));
    }

    public ArrayList<Item> getSupplyingItems() {
        return new ArrayList<>();
    }
}
