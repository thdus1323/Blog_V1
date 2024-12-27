package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.user.User;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;
    private final HttpSession session;
//    private final ReplyRepository replyRepository;
//    private final LoveRepository loveRepository;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "") String keyword) {
        // isEmpty -> null, 공백
        // isBlank -> null, 공백, 화이트 스페이스
        if (keyword.isBlank()) {
            List<Board> boardList = boardRepository.findAll(page, keyword);
            // 전체 페이지 개수
            int count = boardRepository.count(keyword).intValue();
            int namerge = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;
            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", "");
        } else {
            List<Board> boardList = boardRepository.findAll(page, keyword);
            // 전체 페이지 개수
            int count = boardRepository.count(keyword).intValue();
            int namerge = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;
            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", keyword);
        }

        //에러 -> 자바스크립트응답
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        //   값이 null 이면 로그인 페이지로 리다이렉션
        //   값이 null 이 아니면, /board/saveForm 으로 이동
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
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
        //sessionUser에 따라 (수정,삭제)권한 주려고
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

//        List<BoardResponse.ReplyDTO> replyDTOList = replyRepository.findByBoardId(id, sessionUser);

        request.setAttribute("board", boardDTO);
//        request.setAttribute("replyList", replyDTOList);

        //주인이 아니면, 특정게시물의 좋아요 갯수를 가져와
//        if (sessionUser == null) {
//            LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id);
//            request.setAttribute("love", loveDetailDTO);
//            //주인이면, 특정 유저가 특정 게시물에 대해 좋아요 눌렀는지, 좋아요 갯수 계산
//        } else {
//            LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id, sessionUser.getId());
//            request.setAttribute("love", loveDetailDTO);
//        }

        return "board/detail";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 권한 없으면 나가
        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }

        // 3. 가방에 담기
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 권한 체크
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }

        // 3. 핵심 로직
        // update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/" + id;
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
