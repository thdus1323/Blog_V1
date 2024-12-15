package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardResponse {

    @Data
    public static class DetailDTO{
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Boolean boardOwner;
        public boolean isBoardOwner(User sessionUser){
            if (sessionUser == null) {
                return false; // sessionUser가 없으면 false 반환
            }
            return sessionUser.getId() == userId; // ID 비교 결과 반환
            //참이면 true, 거짓이면 false
        }
    }

    @Data
    public static class ReplyDTO {
        private Integer id;
        private Integer userId;
        private String comment;
        private String username;

        public ReplyDTO(Object[] ob) {
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.comment = (String) ob[2];
            this.username = (String) ob[3];
        }
    }

    
}
