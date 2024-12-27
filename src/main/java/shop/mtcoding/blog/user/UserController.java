package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.ApiUtil;

@Controller
@RequiredArgsConstructor
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

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        System.out.println("requestDTO = " + requestDTO);
        userRepository.save(requestDTO); // 모델계층인 repository에 위임
        return "redirect:/loginForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

}
