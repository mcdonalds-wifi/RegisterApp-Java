package edu.uark.registerapp.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.controllers.enums.ViewNames;

@Controller
@RequestMapping(value = "/")
public class SignInRouteController extends BaseRouteController {
	// TODO: Route for initial page load
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView start(
		@RequestParam final Map<String, String> queryParameters,
		final HttpServletRequest request
	) {
		
		//if employee exists serve up sign in view/doc
		//use functionality of task 5 ?
		final Optional<ActiveUserEntity> activeUserEntity = this.getCurrentUser(request);
		//check if there is not an active user and error if true
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
		}
		
		//if there is an active user 
		ModelAndView modelAndView =
			this.setErrorMessageFromQueryString(
				new ModelAndView(ViewNames.SIGN_IN.getViewName()),
				queryParameters);
		
		return modelAndView;
	}
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView performSignIn(
		// TODO: Define an object that will represent the sign in request and add it as a parameter here
		HttpServletRequest request
	) {

		// TODO: Use the credentials provided in the request body
		//  and the "id" property of the (HttpServletRequest)request.getSession() variable
		//  to sign in the user

		return new ModelAndView(
			REDIRECT_PREPEND.concat(
				ViewNames.MAIN_MENU.getRoute()));
	}
}
