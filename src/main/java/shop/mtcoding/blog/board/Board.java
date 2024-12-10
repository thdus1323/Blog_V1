package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name="board_tb")
@Entity
public class Board {
    @Id // pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private int id;

    @Column(length = 30)
    private String title;
    private String content;
    private int userId; // user : board = 1 : n
    private LocalDateTime createdAt;
}
