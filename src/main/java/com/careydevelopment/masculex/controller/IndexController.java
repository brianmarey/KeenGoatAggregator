package com.careydevelopment.masculex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.careydevelopment.masculex.jpa.entity.Context;
import com.careydevelopment.masculex.jpa.repository.ContextRepository;

@Controller
public class IndexController {
	
	@Autowired
	ContextRepository contextRepository;

    @RequestMapping(value = "/{contextName}", method=RequestMethod.GET)
    public String index(@PathVariable String contextName, Model model) {
    	
    	if (contextName.equals("saltwater")) {
    		return contextName;
    	}
    	
    	Context context = contextRepository.fetchByName(contextName);
    	
    	if (context == null) {
    		return "404";
    	} else {
        	model.addAttribute("webContext", context);        	
            return "index";
    	}    	
    }
	
}
