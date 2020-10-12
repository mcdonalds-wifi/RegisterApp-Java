package edu.uark.registerapp.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.employees.ActiveEmployeeExistsQuery;
import edu.uark.registerapp.commands.employees.EmployeeQuery;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.api.EmployeeType;
import edu.uark.registerapp.models.entities.ActiveUserEntity;

@Controller
@RequestMapping(value = "/employeeDetail")
public class EmployeeDetailRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView start(@RequestParam final Map<String, String> queryParameters, final HttpServletRequest request)
	{

		// TODO: Logic to determine if the user associated with the current session
		//  is able to create an employee
		final boolean active_user_status = this.activeUserExists();

		if(active_user_status)
			{ 
				//get current user method in BaseRouteController.java
				final Optional<ActiveUserEntity> activeUserEntity = this.getCurrentUser(request);
				bool present = activeUserEntity.isPresent();
				bool elevate_status = this.isElevatedUser(activeUserEntity.get());
				if (!present) 
				{
						return this.buildInvalidSessionResponse();
					//	return this.setRedirectUrl("/mainMenu");
				}	
				else if (!elevate_status) {return this.buildNoPermissionsResponse();}
			}

		return this.buildStartResponse(!activeUserExists, queryParameters);
	//	return new ModelAndView(ViewModelNames.EMPLOYEE_TYPES.getValue());
	}

	@RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
	public ModelAndView startWithEmployee(@PathVariable final UUID employeeId, @RequestParam final Map<String, String> queryParameters, final HttpServletRequest request) 
	{

		final Optional<ActiveUserEntity> activeUserEntity = this.getCurrentUser(request);

		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
		} else if (!this.isElevatedUser(activeUserEntity.get())) {
			return this.buildNoPermissionsResponse();
		}

		// TODO: Query the employee details using the request route parameter
		// TODO: Serve up the page

		//return new ModelAndView(ViewModelNames.EMPLOYEE_TYPES.getValue());
		return this.buildStartResponse(employeeId, queryParameters);
	}


	

	// Helper methods
	private boolean activeUserExists() {
		try
		{
			//call method from activeEmployeeExistsQuery.java, if successful, return true, active user exists
			this.activeEmployeeExistsQuery.execute();
			return true;
		} catch (final NotFoundException e) {
			//no active user exists
			return false;
		}
	}

	private ModelAndView buildStartResponse(final boolean init_Employee, final Map<String, String> queryParams)
	{ 
		return this.buildStartResponse(init_Employee, (new UUID(0, 0)), queryParams);
	}

	private ModelAndView buildStartResponse(final UUID employeeId, final Map<String, String> queryParameters)
	{
		return this.buildStartResponse(false, employeeId, queryParameters);
	}

	private ModelAndView buildStartResponse( final boolean isInitialEmployee, final UUID employeeId, final Map<String, String> queryParameters)
	{
		ModelAndView modelAndView = this.setErrorMessageFromQueryString(new ModelAndView(ViewNames.EMPLOYEE_DETAIL.getViewName()), queryParameters);

		if (employeeId.equals(new UUID(0, 0)))
		{
			modelAndView.addObject(ViewModelNames.EMPLOYEE.getValue(), (new Employee()).setIsInitialEmployee(isInitialEmployee));

		} else {
			try {
				modelAndView.addObject(
					ViewModelNames.EMPLOYEE.getValue(),
					this.employeeQuery
						.setEmployeeId(employeeId)
						.execute()
						.setIsInitialEmployee(isInitialEmployee));
		} catch (final Exception e) 
		{
				modelAndView.addObject(ViewModelNames.ERROR_MESSAGE.getValue(),e.getMessage());
				modelAndView.addObject(ViewModelNames.EMPLOYEE.getValue(),(new Employee()).setIsInitialEmployee(isInitialEmployee));
			}
		}

		modelAndView.addObject(ViewModelNames.EMPLOYEE_TYPES.getValue(),EmployeeType.allEmployeeTypes());

		return modelAndView;
		
	}

	@Autowired
	private EmployeeQuery employeeQuery;
	
	@Autowired
	private ActiveEmployeeExistsQuery activeEmployeeExistsQuery;

	

	


}
