package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@ToString
public class EmployeePair {

    private final long employee1_ID;
    private final long employee2_ID;

    /***
     * Create a normalized employee pair.
     * This ensures that [emp1, emp2] == [emp2, emp1].
     *
     * @param emp1 employeeID of first employee
     * @param emp2 employeeID of second employee
     */
    public EmployeePair(long emp1, long emp2) {
        if (emp1 < emp2) {
            this.employee1_ID = emp1;
            this.employee2_ID = emp2;
        } else {
            this.employee1_ID = emp2;
            this.employee2_ID = emp1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final EmployeePair that = (EmployeePair) o;
        return employee1_ID == that.employee1_ID && employee2_ID == that.employee2_ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee1_ID, employee2_ID);
    }
}
