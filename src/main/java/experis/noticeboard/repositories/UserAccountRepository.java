package experis.noticeboard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import experis.noticeboard.models.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
}
