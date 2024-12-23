package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Table(name = "love_tb")
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
