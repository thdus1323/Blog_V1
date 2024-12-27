package shop.mtcoding.blog.love;

import lombok.Data;

//TODO: 좋아요&취소하기
public class LoveRequest {
    @Data
    public static class SaveDTO {
        private Integer boardId;
    }
}