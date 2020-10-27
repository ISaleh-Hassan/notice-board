package experis.noticeboard.controllers.viewControllers;

import experis.noticeboard.models.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "not_inlogged.html";
    }
}
