package ru.codemark.usermanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.codemark.usermanagementapp.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "select u.login, u.name, u.password, r.name \n" +
            "from ((users u left join user_roles ur on u.login = ur.user_id)\n" +
            "left join roles r on r.id = ur.role_id) where u.login = ?", nativeQuery = true)
    UserEntity findByLogin(@Param("login") String login);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_roles ur where ur.user_id = ?; delete from users u where u.login = ?",
            nativeQuery = true)
    void deleteByLogin(String login, String login2);

}
