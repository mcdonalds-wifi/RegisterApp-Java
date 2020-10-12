package edu.uark.registerapp.commands.employees;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.commands.exceptions.UnauthorizedException;
import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

public class EmployeeSignInCommand implements ResultCommandInterface<Employee>
{
    private EmployeeSignIn employeeSignIn;
    private String sessionKey;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    public void setEmployeeSignIn(EmployeeSignIn e)
    {
        employeeSignIn = e;
    }

    public void setSessionKey(String s)
    {
        sessionKey = s;
    }

    public EmployeeSignIn getEmployeeSignIn()
    {
        return employeeSignIn;
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public Employee execute()
    {
        validate();
        return new Employee();
    }

    private void validate()
    {
        try
        {
            Integer.parseInt(employeeSignIn.getEmployeeId());
        }
        catch (NumberFormatException e)
        {
            throw new UnprocessableEntityException("ID");
        }

        if (StringUtils.isBlank(employeeSignIn.getPassword()))
        {
            throw new UnprocessableEntityException("Password");
        }
    }

    @Transactional
    private EmployeeEntity query()
    {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findByEmployeeId(Integer.parseInt(employeeSignIn.getEmployeeId()));
        
        if (!employeeEntity.isPresent() 
        || !Arrays.equals(employeeEntity.get().getPassword(), EmployeeHelper.hashPassword(employeeSignIn.getPassword())))
        {
            throw new UnauthorizedException();
        }

        Optional<ActiveUserEntity> activeUserEntity = activeUserRepository.findByEmployeeId(employeeEntity.get().getId());

        if (activeUserEntity.isPresent())
        {
            activeUserRepository.save(activeUserEntity.get().setSessionKey(sessionKey));
        }
        else
        {
            activeUserRepository.save((new ActiveUserEntity())
            .setSessionKey(sessionKey).setEmployeeId(employeeEntity.get().getId())
            .setClassification(employeeEntity.get().getClassification())
			.setName(employeeEntity.get().getFirstName().concat(" ").concat(employeeEntity.get().getLastName())));
        }

        return employeeEntity.get();
    }
}
