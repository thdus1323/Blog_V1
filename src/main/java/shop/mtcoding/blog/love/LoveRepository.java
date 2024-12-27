package shop.mtcoding.blog.love;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class LoveRepository {

    private final EntityManager em;

    //특정게시물의 좋아요 갯수세기
    public LoveResponse.DetailDTO findLove(int boardId) {
        String q = """
                SELECT count(*) loveCount
                FROM love_tb
                WHERE board_id = ?;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, boardId);
        // 한건만 받을때는 바로 받기
        Long loveCount = (Long) query.getSingleResult();
        Integer id = 0;
        Boolean isLove = false;
        System.out.println("id : " + id);
        System.out.println("isLove : " + isLove);
        System.out.println("loveCount : " + loveCount);
        //해당 데이터를 묶어 detailDTO객체반환
        LoveResponse.DetailDTO responseDTO = new LoveResponse.DetailDTO(
                id, isLove, loveCount
        );
        return responseDTO;
    }


    //특정 유저가 특정 게시물에 대해 좋아요를 눌렀는지 확인,
    //& 게시물의 총 좋아요 갯수를 계산
    public LoveResponse.DetailDTO findLove(int boardId, int sessionUserId) {
        String q = """
                SELECT
                    id,
                    CASE
                        WHEN user_id IS NULL THEN FALSE
                        ELSE TRUE
                    END AS isLove,
                    (SELECT COUNT(*) FROM love_tb WHERE board_id = ?) AS loveCount
                FROM
                    love_tb
                WHERE
                    board_id = ? AND user_id = ?;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, boardId);
        query.setParameter(2, boardId);
        query.setParameter(3, sessionUserId);
        Integer id = null;
        Boolean isLove = null;
        Long loveCount = null;
        try {
            Object[] row = (Object[]) query.getSingleResult();
            id = (Integer) row[0];
            isLove = (Boolean) row[1];
            loveCount = (Long) row[2];
        } catch (Exception e) {
            id = 0;
            isLove = false;
            loveCount = 0L;
        }

        System.out.println("id : " + id);
        System.out.println("isLove : " + isLove);
        System.out.println("loveCount : " + loveCount);

        LoveResponse.DetailDTO responseDTO = new LoveResponse.DetailDTO(
                id, isLove, loveCount
        );
        return responseDTO;
    }

    //TODO: 좋아요&취소하기
    public Love findById(int id) {
        Query query = em.createNativeQuery("select * from love_tb where id = ?", Love.class);
        query.setParameter(1, id);
        Love love = (Love) query.getSingleResult();
        return love;
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from love_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }

    //TODO: 좋아요&취소하기
    //좋아요 삽입 & 조회
    @Transactional
    public int save(LoveRequest.SaveDTO requestDTO, int sessionUserId) {
        Query query = em.createNativeQuery("insert into love_tb(board_id, user_id, created_at) values(?,?, now())");
        query.setParameter(1, requestDTO.getBoardId());
        query.setParameter(2, sessionUserId);
        query.executeUpdate();
        //love_tb에서 가장 최근에 추가된 행의  ID 가져오는 쿼리
        Query q = em.createNativeQuery("select max(id) from love_tb");
        Integer loveId = (Integer) q.getSingleResult();
        return loveId;
    }
}
