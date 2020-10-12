package edu.uark.registerapp.commands.employees;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

public class EmployeeQuery implements ResultCommandInterface<Employee> {
    //properties
    private UUID employeeId;
    public UUID getEmployeeId() {
		return this.employeeId;
	}
     //set	
     public EmployeeQuery setEmployeeId(final UUID employeeId) {
		this.employeeId = employeeId;
		return this;
	}

    //functionality
    @Override
	public FindEmployee execute() {
        //using .findById()
		final Optional<EmployeeEntity> employeeEntity =
			this.employeeRepository.findById(this.employeeId);

        //if found    
		if (employeeEntity.isPresent()) {
			return new Employee(employeeEntity.get());
		} else {
			throw new NotFoundException("Employee");
		}
	}

    //map to repo
    @Autowired
    private EmployeeRepository employeeRepository;
    
    //set


}
