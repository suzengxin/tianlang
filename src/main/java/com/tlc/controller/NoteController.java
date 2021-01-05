package com.tlc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/note")
public class NoteController {

	/**
	 * 笔记首页
	 * @param model
	 * @return
	 */
	@GetMapping(value = "")
	public String index(Model model) {
		return "note";
	}
	
}
