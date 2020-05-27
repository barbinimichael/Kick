package com.Kick.Kick;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KickController {

  @RequestMapping(value = "/")
  public String index() {
    // index.html
    return "index";
  }
}
