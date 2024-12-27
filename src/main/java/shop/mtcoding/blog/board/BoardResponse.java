package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;


public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Boolean boardOwner;

        public void isBoardOwner(User sessionUser) {
            if (sessionUser == null) boardOwner = false;
            else boardOwner = sessionUser.getId() == userId;
        }
    }
}
