package experis.noticeboard.controllers.viewControllers;

import experis.noticeboard.models.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("user", new UserAccount());
        model.addAttribute("id", 1);
        model.addAttribute("name", "Saleh Hassan");
        return "home";
    }

}
