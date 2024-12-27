package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor //final 필드 생성자 생성
@Repository
public class BoardRepository {
    private final EntityManager em;

    public Long count(String keyword) {
        Query query = em.createNativeQuery("select count(*) from board_tb where title like ?");
        query.setParameter(1, "%" + keyword + "%");
        return (Long) query.getSingleResult();
    }

    public List<Board> findAll(Integer page, String keyword) {
        Query query = em.createNativeQuery("select * from board_tb where title like ? order by id desc limit ?, 3", Board.class);
        query.setParameter(1, "%" + keyword + "%");
        query.setParameter(2, page * 3);
        return query.getResultList();
    }

    @Transactional
    public void save(BoardRequest.SaveDTO requestDTO, int userId) {
        Query query = em.createNativeQuery("insert into board_tb(title, content, user_id, created_at) values(?,?,?, now())");
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, userId);

        query.executeUpdate();
    }
}
