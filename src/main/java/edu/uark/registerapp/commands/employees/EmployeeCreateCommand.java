package edu.uark.registerapp.commands.employees;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.enums.EmployeeClassification;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

public class EmployeeCreateCommand implements ResultCommandInterface<Employee> {
    //properties, object
    private Employee apiEmployee;
    private boolean isInitialEmployee;

    //getters
	public Employee getEmployee() {
		return this.apiEmployee;
    }
    
    public boolean getIsInitialEmployee() {
		return this.isInitialEmployee;
    }
    
    //setters
	public EmployeeCreateCommand setEmployee(final Employee apiEmployee) {
		this.apiEmployee = apiEmployee;
		return this;
	}

	public EmployeeCreateCommand setIsInitialEmployee(
		final boolean isInitialEmployee
	) {
		this.isInitialEmployee = isInitialEmployee;
		return this;
    }
    	public EmployeeCreateCommand setApiEmployee(final Employee apiEmployee) {
		this.apiEmployee = apiEmployee;
		return this;
	}
    private void validateProperties() {
        //not blank
		if (StringUtils.isBlank(this.apiEmployee.getFirstName())) {
			throw new UnprocessableEntityException("first name");
		}
		if (StringUtils.isBlank(this.apiEmployee.getLastName())) {
			throw new UnprocessableEntityException("last name");
		}
		if (StringUtils.isBlank(this.apiEmployee.getPassword())) {
			throw new UnprocessableEntityException("password");
		}

		if (!this.isInitialEmployee
			&& (EmployeeClassification.map(this.apiEmployee.getClassification()) == EmployeeClassification.NOT_DEFINED)) {

			throw new UnprocessableEntityException("classification");
		}
    }
    
    @Override
	public Employee execute() {
		this.validateProperties();
        
        //defualt general manager,is initial
		if (this.isInitialEmployee) {
			this.apiEmployee.setClassification(
				EmployeeClassification.GENERAL_MANAGER.getClassification());
		}
		final EmployeeEntity employeeEntity =
			this.employeeRepository.save(new EmployeeEntity(this.apiEmployee));

		this.apiEmployee.setId(employeeEntity.getId());
		this.apiEmployee.setPassword(StringUtils.EMPTY);
		this.apiEmployee.setCreatedOn(employeeEntity.getCreatedOn());
		this.apiEmployee.setEmployeeId(
			EmployeeHelper.padEmployeeId(
				employeeEntity.getEmployeeId()));

		return this.apiEmployee;
	}

	@Autowired
	private EmployeeRepository employeeRepository;

	public EmployeeCreateCommand() {
		this.isInitialEmployee = false;
    }
}
