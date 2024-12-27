package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Table(name = "love_tb", uniqueConstraints = {
        @UniqueConstraint(
                name = "love_uk",
                columnNames = {"board_id", "user_id"} //board_id와 user_id가 유일해야 한다 = 동일 유저가 같은 게시판에는 좋아요 한 번만 가능!
        )})
@Data
@Entity
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 전략
    private Integer id;
    private Integer boardId;
    private Integer userId;
    private Timestamp createdAt;
}