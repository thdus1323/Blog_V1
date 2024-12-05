package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor // final 붙은 애들에 대한 생성자를 만들어줌
public class UserController {

    //자바는 final 변수는 반드시 초기화가 되어야 한다.
    private final UserRepository userRepository;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO){
        System.out.println("requestDTO = " + requestDTO);

        userRepository.save(requestDTO); // 모델계층인 repository에 위임
        return "redirect:/login-form";}

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO){
        System.out.println("requestDTO = " + requestDTO);
        if (requestDTO.getUsername().length() < 3){
            return "error/400";
        }
        User user = userRepository.findByUsernameAndPassword(requestDTO);

        if(user == null){ // 조회 안됨 (401)
            return "error/401";
        }else{ // 조회 됐음 (인증됨)
            session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)
        }

        return "redirect:/";
    }


    @GetMapping("/user/update-form")
    public String updateForm() {
        return "user/update-form";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
