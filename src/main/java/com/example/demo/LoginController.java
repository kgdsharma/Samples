package com.example.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import javax.el.ELManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.NameID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Element;

@Controller
public class LoginController {
	
	@RequestMapping("/")
	public String login(HttpServletRequest req, HttpServletResponse res, @RequestParam("url") String url) {
		
		Cookie[] reqCookies= req.getCookies();
		
		
		
		
		
		for(Cookie cookie:reqCookies) {
			System.out.println(cookie.getName());
			System.out.println(cookie.getName());
		}
		
		System.out.println("URL -->>"+url);

		
		String userId= getUserInfo();
		
		Cookie cookie = new Cookie("RACFID", userId);
		cookie.setMaxAge(-1);
		res.addCookie(cookie);
		
		try {
			res.addCookie(cookie);
			res.sendRedirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "login Success";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse res, @RequestParam("url") String url) {
		
		Cookie[] reqCookies= req.getCookies();	
		req.getSession().invalidate();
		
		if(reqCookies!=null) {
		for(Cookie cookie:reqCookies) {
			
			if(cookie.getName().equalsIgnoreCase("JSESSIONID")) {				
				cookie.setMaxAge(0);
				res.addCookie(cookie);					
			}			
		}
		}
		
		System.out.println("URL -->>"+url);

		
		try {	
			
			res.sendRedirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "login Success";
	}
	
	public String getUserInfo() {
		
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        NameID nameId = (NameID) authentication.getPrincipal();
	        
	        System.out.println("Name Id ---> "+nameId.getValue());
	        

	        // there be dragons
	        Assertion assertion = (Assertion) nameId.getParent().getParent();
	        assertion.getAttributeStatements().forEach(attributeStatement -> {
	            attributeStatement.getAttributes().forEach(attribute -> {
	                String key = attribute.getName();
	                System.out.println("Key -->> "+key);
	              
	                
	                // cheat a little to not deal with an array/list value
	                attribute.getAttributeValues().forEach(element-> {
	                	Element elm= element.getDOM();	                	
	                	System.out.println("Element -->  "+elm);
	                });;
	               
	            });
	        });
			return nameId.getValue();

		
	}
	

}
