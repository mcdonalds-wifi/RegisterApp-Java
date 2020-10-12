package edu.uark.registerapp.controllers;

import java.util.Optional;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.products.ProductQuery;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.Product;

@Controller
@RequestMapping(value = "/productDetail")
public class ProductDetailRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView start(@RequestParam final Map<String, String> query_params, final HttpServletRequest req)
	{

		final Optional<ActiveUserEntity> activeUserEntity = this.getCurrentUser(req);
		
		//detect presence and elevated status
		bool act_user_present = activeUserEntity.isPresent();
		bool act_user_elevated = this.isElevatedUser(activeUserEntity.get());		

		if(!act_user_present) { return this.buildInvalidSessionResponse();}
		else if(!act_user_elevated) {return this.buildNoPermissionsResponse(ViewNames.PRODUCT_LISTING.getRoute());}
		
		final ModelAndView mod_view = this.setErrorMessageFromQueryString(new ModelAndView(ViewNames.PRODUCT_DETAIL.getViewName()), query_params);
		mod_view.addObject(ViewModelNames.IS_ELEVATED_USER.GETValue(), true);
		mod_view.addObject(ViewModelNames.PRODUCT.getValue(),(new Product()).setLookupCode(StringUtils.EMPTY).setCount(0));
	
		return mod_view;
/*
		return (new ModelAndView(ViewNames.PRODUCT_DETAIL.getViewName()))
			.addObject(ViewModelNames.PRODUCT.getValue(), (new Product()).setLookupCode(StringUtils.EMPTY).setCount(0)); */
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ModelAndView startWithProduct(@PathVariable final UUID productId, @RequestParam final Map<string, String> query_params, final HttpServletRequest req) {

		final ModelAndView modelAndView = new ModelAndView(ViewNames.PRODUCT_DETAIL.getViewName());
		final Optional<ActiveUserEntity> activeUserEntity = this.getCurrentUser(req);

		//check if current user is present
		bool present = activeUserEntity.isPresent();
		int x = 0;
		if (!present){x = 1;}
		if (x = 1) {return this.buildInvalidSessionResponse();}

		final ModelAndView modelAndView = this.setErrorMessageFromQueryString(new ModelAndView(ViewNames.PRODUCT_DETAIL.getViewName()), query_params);

		modelAndView.addObject(ViewModelNames.IS_ELEVATED_USER.getValue(), EmployeeClassification.isElevatedUser(activeUserEntity.get().getClassification()));

		try {
			modelAndView.addObject(
				ViewModelNames.PRODUCT.getValue(),
				this.productQuery.setProductId(productId).execute());
		} catch (final Exception e) {
			modelAndView.addObject(
				ViewModelNames.ERROR_MESSAGE.getValue(),
				e.getMessage());
			modelAndView.addObject(
				ViewModelNames.PRODUCT.getValue(),
				(new Product())
					.setCount(0)
					.setLookupCode(StringUtils.EMPTY));
		}

		return modelAndView;
	}

	// Properties
	@Autowired
	private ProductQuery productQuery;
}
