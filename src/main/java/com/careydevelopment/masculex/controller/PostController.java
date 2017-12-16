package com.careydevelopment.masculex.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.careydevelopment.masculex.jpa.entity.Content;
import com.careydevelopment.masculex.jpa.entity.Context;
import com.careydevelopment.masculex.jpa.entity.Post;
import com.careydevelopment.masculex.jpa.repository.ContentRepository;
import com.careydevelopment.masculex.jpa.repository.ContextRepository;
import com.careydevelopment.masculex.jpa.repository.PostRepository;

@Controller
public class PostController {

	@Autowired
	ContextRepository contextRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	PostRepository postRepository;
	
    @RequestMapping(value = "/{contextName}/post/{slug}", method=RequestMethod.GET)
    public String post(@PathVariable String contextName, @PathVariable String slug, Model model,
    	HttpServletRequest request) {
    	Context context = contextRepository.fetchByName(contextName);
    	
    	if (context == null) {
    		return "404";
    	} else {
        	model.addAttribute("webContext", context);        	
        	
        	Post post = postRepository.fetchBySlug(slug);
        	
        	if (post == null) {
        		return "404";
        	} else {
        		String url = request.getRequestURI();
        		String ampUrl = getAmpUrl(url, context);
        		
        		model.addAttribute("post", post);
        		model.addAttribute("ampUrl", ampUrl);
        		
                return "post";	
        	}        	
    	}      
    }
        
    
    private String getAmpUrl(String url, Context context) {
    	int secondSlash = url.indexOf("/", 2);
    	String ampUrl = url.substring(secondSlash, url.length());
    	ampUrl = context.getHomeUrl() + ampUrl + "/amp";
    	
    	return ampUrl;
    }
    
    
    @RequestMapping(value = "/{contextName}/post/{slug}/amp", method=RequestMethod.GET)
    public String postAmp(@PathVariable String contextName, @PathVariable String slug, Model model,
    	HttpServletRequest request) {
    	Context context = contextRepository.fetchByName(contextName);
    	
    	if (context == null) {
    		return "404";
    	} else {
        	model.addAttribute("webContext", context);        	
        	
        	Post post = postRepository.fetchBySlug(slug);
        	
        	if (post == null) {
        		return "404";
        	} else {
            	String url = request.getRequestURI();
            	String noAmpUrl = getNoAmpUrl(url, context);
            	            	
            	
            	model.addAttribute("noAmpUrl",noAmpUrl);
        		model.addAttribute("post", post);
        		
                return "postAmp";	
        	}        	
    	}      
    }
    
    
    private String getNoAmpUrl(String url, Context context) {
    	int secondSlash = url.indexOf("/", 2);
    	String noAmpUrl = url.substring(secondSlash, url.length());
    	
    	System.err.println("No amp url is " + noAmpUrl);
    	
    	int suffix = noAmpUrl.lastIndexOf("/amp");
    	if (suffix > -1) {
    		noAmpUrl = noAmpUrl.substring(0, suffix);
    	}

    	noAmpUrl = context.getHomeUrl() + noAmpUrl;
    	
    	return noAmpUrl;
    }
    
    
    @RequestMapping(value = "/{contextName}/content/{category}/{slug}", method=RequestMethod.GET)
    public String content(@PathVariable String contextName, @PathVariable String slug, 
    	@PathVariable String category, Model model, HttpServletRequest request) {
    	
    	Content meta = contentRepository.fetchBySlug(slug);
    	if (meta == null) {
    		return "404";
    	}
    	
    	Context context = contextRepository.fetchByName(contextName);
    	
    	if (context == null) {
    		return "404";
    	} else {
        	model.addAttribute("webContext", context);        	

        	String content = getContent(contextName, category, slug);
        	
        	if (content == null || content.length() < 5) {
        		return "404";
        	} else {
            	model.addAttribute("content", content);            	
            	model.addAttribute("meta", meta);
            	            	        		
                return "authBlog";	
        	}        	
    	}      
    }

    
    private String getContent(String contextName, String category, String slug) {
    	String path = "/var/www/html/" + contextName + "/" + category + "/" + slug + ".html";
    	
    	StringBuilder sb = new StringBuilder();
    	
		try (Stream<String> stream = Files.lines(Paths.get(path))) {
	        stream.forEach(sb::append);
		} catch (IOException ie) {
			ie.printStackTrace();
		}

		return sb.toString();
    }
}
