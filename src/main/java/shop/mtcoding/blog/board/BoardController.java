package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;
    private final HttpSession session;


    @GetMapping({"/", "/board"})
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);
        return "index";
    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        //   값이 null 이면 로그인 페이지로 리다이렉션
        //   값이 null 이 아니면, /board/saveForm 으로 이동
        if(sessionUser == null){
            return "redirect:/login-form";
        }
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        // 3. 모델 위임
        // insert into board_tb(title, content, user_id, created_at) values(?,?,?, now());
        boardRepository.save(requestDTO, sessionUser.getId());

        return "redirect:/";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

        request.setAttribute("board", boardDTO);

        return "board/detail";
    }

    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable int id, HttpServletRequest request){
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        // 2. 권한 없으면 나가
        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);
        if(board.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        // 3. 가방에 담기
        request.setAttribute("board", board);

        return "board/update-form";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        // 2. 권한 체크
        Board board = boardRepository.findById(id);
        if(board.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        // 3. 핵심 로직
        // update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/"+id;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request) {
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) { // 401
            return "redirect:/loginForm";
        }

        // 2. 권한 없으면 나가
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            request.setAttribute("status", 403);
            request.setAttribute("msg", "게시글을 삭제할 권한이 없습니다");
            return "error/40x";
        }

        boardRepository.deleteById(id);

        return "redirect:/";
    }

}
