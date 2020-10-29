package experis.noticeboard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import experis.noticeboard.models.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Optional<UserAccount> findByUserName(String userName);
}
