package edu.uark.registerapp.commands.employees;

import org.springframework.stereotype.Service;
import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ActiveEmployeeExistsQuery implements VoidCommandInterface
{
    @Autowired
    EmployeeRepository employeeRepository;

    public void execute()
    {
        if (!employeeRepository.existsByIsActive(true))
        {
            throw new NotFoundException("Employee");
        }
    }

}
