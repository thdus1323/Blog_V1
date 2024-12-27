package shop.mtcoding.blog.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog.board.BoardResponse;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;

    public List<BoardResponse.ReplyDTO> findByBoardId(int boardId, User sessionUser) {
        String q = """
                select rt.id, rt.user_id, rt.comment, ut.username from reply_tb rt inner join user_tb ut on rt.user_id = ut.id where rt.board_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, boardId);

        List<Object[]> rows = query.getResultList();

        return rows.stream().map(row -> new BoardResponse.ReplyDTO(row, sessionUser)).toList();
    }
}
