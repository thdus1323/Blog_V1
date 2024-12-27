package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.ApiUtil;

@Controller
@RequiredArgsConstructor // final 붙은 애들에 대한 생성자를 만들어줌
public class UserController {

    //자바는 final 변수는 반드시 초기화가 되어야 한다.
    private final UserRepository userRepository;
    private final HttpSession session;

    @GetMapping("/api/username-same-check")
    public @ResponseBody ApiUtil<?> usernameSameCheck(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) { // 회원가입 해도 된다.
            return new ApiUtil<>(true);
        } else { // 회원가입 하면 안된다.
            return new ApiUtil<>(false);
        }
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        System.out.println("requestDTO = " + requestDTO);
        userRepository.save(requestDTO); // 모델계층인 repository에 위임

        // TODO: delete
//        try {
//        } catch (Exception e) {
//            throw new RuntimeException("아이디가 중복되었어요");
//        }
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        System.out.println("requestDTO = " + requestDTO);
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }
        User user = userRepository.findByUsernameAndPassword(requestDTO);

        session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)

        return "redirect:/";
    }


    @GetMapping("/user/update-form")
    public String updateForm() {
        return "user/update-form";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
