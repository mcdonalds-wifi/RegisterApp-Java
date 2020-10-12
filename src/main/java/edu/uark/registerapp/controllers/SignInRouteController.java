package edu.uark.registerapp.controllers;

import java.util.Map;
import java.lang.String;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.employees.ActiveEmployeeExistsQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.controllers.enums.QueryParameterNames;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.EmployeeSignIn;

@Controller
@RequestMapping(value = "/")
public class SignInRouteController extends BaseRouteController {
	
	//properties of class
	@Autowired
	private EmployeeSignInCommand employeeSignInCommand;

	@Autowired
	private ActiveEmployeeExistsQuery activeEmployeeExistsQuery;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView signIn(
		@RequestParam final Map<String, String> queryParameters
	) {
		try {
			this.activeEmployeeExistsQuery.execute();
		} catch (NotFoundException e) {
			return new ModelAndView(
				REDIRECT_PREPEND.concat(
					ViewNames.EMPLOYEE_DETAIL.getRoute()));
		}

		//create new model and view
		ModelAndView modelAndView =
			this.setErrorMessageFromQueryString(
				new ModelAndView(ViewNames.SIGN_IN.getViewName()),
				queryParameters);
		
		if (queryParameters.containsKey(QueryParameterNames.EMPLOYEE_ID.getValue())) {
			modelAndView.addObject(
				ViewModelNames.EMPLOYEE_ID.getValue(),
				queryParameters.get(QueryParameterNames.EMPLOYEE_ID.getValue()));
		}

		return modelAndView;
		
	}
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView performSignIn(
		
		HttpServletRequest request,
		EmployeeSignIn ESI
		) {

		//  Use the credentials provided in the request body
		//  and the "id" property of the (HttpServletRequest)request.getSession() variable
		//  to sign in the user

		try {
			this.employeeSignInCommand
				.setSessionId(request.getSession().getId())
				.setEmployeeSignIn(ESI)
				.execute();
		} catch (Exception e) {
			ModelAndView modelAndView =
				new ModelAndView(ViewNames.SIGN_IN.getViewName());

			modelAndView.addObject(
				ViewModelNames.ERROR_MESSAGE.getValue(),
				e.getMessage());
			modelAndView.addObject(
				ViewModelNames.EMPLOYEE_ID.getValue(),
				ESI.getEmployeeId());

			return modelAndView;
		}

		return new ModelAndView(
			REDIRECT_PREPEND.concat(
				ViewNames.MAIN_MENU.getRoute()));
	}

	
	
}
