package shop.mtcoding.blog;

import org.junit.jupiter.api.Test;
import shop.mtcoding.blog.board.BoardResponse;
import shop.mtcoding.blog.user.User;

public class BooleanTest {

    @Test
    public void bool_test(){
        // 1. 테스트용 객체 생성
        BoardResponse.DetailDTO boardDTO = new BoardResponse.DetailDTO();
        boardDTO.setUserId(1); // 게시글 작성자 ID 설정

        // 2. 테스트용 User 객체 생성
        User sessionUser = new User();
        sessionUser.setId(1); // 로그인한 사용자 ID 설정

        // 3. 메서드 호출 및 결과 출력
        System.out.println(boardDTO.isBoardOwner(sessionUser)); // 파라미터 전달
    }
}
