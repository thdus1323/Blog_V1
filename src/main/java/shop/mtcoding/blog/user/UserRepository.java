package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository //ioc에 new하는 방법
public class UserRepository {

    //db에 접근하는 매니저객체
    //스프링이 만들어서 ioc에 넣어둠 => di해서 꺼내 쓰면 된다.
    private EntityManager em;

    //생성자
    public UserRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional // db에 write 할때는 필수
    public void save(UserRequest.JoinDTO requestDTO){
        Query query = em.createNativeQuery("insert into user_tb(username, password, email, created_at) values (?,?,?, now())");
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getEmail());
        query.executeUpdate();
    }


}
