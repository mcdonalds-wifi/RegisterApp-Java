package edu.uark.registerapp.models.api;

import org.apache.commons.lang3.StringUtils;

import edu.uark.registerapp.models.enums.EmployeeClassification;

public class EmployeeType {

    //classif is employee type
    private int classif;
    
    public int getClassification() {return this.classif;}
    
	public EmployeeType setClassification(final int classif) {
		this.classif = classif;
		return this;
	}

    //dspL is label for display
    private String dspL;
    
	public String getDisplayLabel() {return this.dspL;}

	public EmployeeType setDisplayLabel(final String dspL) {
		this.dspL = dspL;
		return this;
	}

	public static EmployeeType[] allEmployeeTypes() {
		final EmployeeClassification[] employeeClassifications = EmployeeClassification.values();
		final EmployeeType[] employeeTypes = new EmployeeType[employeeClassifications.length];

        int z = 0;
        while(z < employeeClassifications.length){
            employeeTypes[z] = new EmployeeType(employeeClassifications[z]);
            z++;
        }
		return employeeTypes;
	}

	public EmployeeType() {
		this(-1, StringUtils.EMPTY);
	}

	public EmployeeType(final EmployeeClassification employeeClassification) {
		this(
			employeeClassification.getClassification(),
			employeeClassification.getDisplayLabel());
	}

	public EmployeeType(final int classif, final String dspL) {
		this.dspL = dspL;
		this.classif = classif;
	}
}