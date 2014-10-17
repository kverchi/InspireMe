package com.kverchi.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kverchi.service.CountryService;
import com.kverchi.service.ImageService;
import com.kverchi.service.SightService;
import com.kverchi.domain.AjaxValidation;
import com.kverchi.domain.Country;
import com.kverchi.domain.CountrySight;
import com.kverchi.domain.UserDetailsAdapter;

@Controller
@RequestMapping("sights")
@SessionAttributes("country_code") 
public class SightController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields(new String[] {
				"title", "description", "img_url"});
	}
	
	private final static String IMG_PATH="E:/Java/sts-3.6.1.RELEASE/workspace/InspireMe/src/main/webapp/countryImg/countries_sights";
	private final static String P_COUNTRY = "redirect:/main/sights/country";
	private static final String P_ERROR = "error";
	
	@Autowired private SightService sightsService;
	@Autowired private CountryService countryService; 
	@Autowired private ImageService imageService;
	
	@RequestMapping("country") 
	public String country(@RequestParam("country_code") String code, Model model, Principal principal,
			HttpServletResponse response)  {
		List<CountrySight>  sights = new ArrayList<CountrySight>();
		Country result = null;
		final UserDetailsAdapter currentUser = (UserDetailsAdapter) ((Authentication) principal).getPrincipal();
		int userId = currentUser.getId();
		sights = sightsService.getAllSights(code, userId);
		result = countryService.findCountry(code);	
		
		if (result != null) {
			model.addAttribute( "country", result);
			model.addAttribute( "country_sigths", sights);
			model.addAttribute( "country_code", code);
			model.addAttribute("sight", new AddSightForm());
			return "country";
		}
		else
			//TODO error page and denied page - it's not the same
			return P_ERROR;
	}
	
	@RequestMapping("removeSight")
	public String singUp(@RequestParam("sightId") int sightId,
			HttpServletRequest request) {
		//String countryCode = request.getSession().getAttribute("country_code").toString();
		CountrySight sight = sightsService.getSight(sightId);
		File delFile = new File(IMG_PATH+sight.getImg_url());
		delFile.delete();
		sightsService.removeSight(sight);
				
		return (P_COUNTRY);
	}
	@RequestMapping(value = "addSight", method = RequestMethod.POST)
	public @ResponseBody AjaxValidation addNewSight(@ModelAttribute("sight") @Valid AddSightForm form, BindingResult result,
			MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException
	{
		final UserDetailsAdapter currentUser = (UserDetailsAdapter) ((Authentication) principal).getPrincipal();
		
		AjaxValidation res = new AjaxValidation();
		ValidationUtils.rejectIfEmpty(result, "title", "Title can not be empty.");
		ValidationUtils.rejectIfEmpty(result, "description", "Description not be empty");
		//ValidationUtils.rejectIfEmpty(result, "img_url", "Choose image");
		String countryCode = request.getSession().getAttribute("country_code").toString();
		MultipartFile imgFile = form.getImg_url();
		String title = form.getTitle();
		String description = form.getDescription();
		
		Set<String> allowedImageExtensions = new HashSet<String>();
		allowedImageExtensions.add("png");
		allowedImageExtensions.add("jpg");
		allowedImageExtensions.add("JPG");
		allowedImageExtensions.add("jpeg");
		allowedImageExtensions.add("JPEG");
		allowedImageExtensions.add("gif");
		
		//imageService.setCustomImgSize(200, 200);
		imageService.setImgSizeToDefault();
		boolean saveImgResult = imageService.saveImg(imgFile, IMG_PATH, allowedImageExtensions);
		if(!result.hasErrors() && saveImgResult) {
			CountrySight sight = new CountrySight(); 		
	 		
	 		sight.setDescription(description);
	 		sight.setSight_label(title);
	 		sight.setImg_url(imgFile.getOriginalFilename());
	 		sight.setCountry_code(countryCode);
	 		sight.setUserId(currentUser.getId());
	 		sightsService.addSight(sight);	
				res.setStatus("SUCCESS");
				res.setResult("/main/sights/country?country_code=" + countryCode);
		}
		else {
			res.setStatus("ERROR");
			result.reject("vse ploho", null);
			res.setResult(result.getAllErrors());
		}
		return res;
	}
	
		
}
