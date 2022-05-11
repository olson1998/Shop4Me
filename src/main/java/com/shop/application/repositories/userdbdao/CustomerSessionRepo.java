package com.shop.application.repositories.userdbdao;

import com.shop.application.entities.userdb.CustomerSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
public interface CustomerSessionRepo extends JpaRepository<CustomerSession, Long> {

    default Optional<CustomerSession> getCustomerSessionBySession_id(String session_id){
        return findAll().stream()
                .filter(session -> session.getSession_id().equals(session_id))
                .findFirst();
    }

    default void registerNewCustomerSession(String session_id){
        CustomerSession customerSession = new CustomerSession(
                session_id,
                1,
                Timestamp.valueOf(
                        LocalDateTime.now(ZoneId.systemDefault())
                ),
                null
        );
        save(customerSession);
    }

    default String generateNewSessionID(){
        return RandomStringUtils.randomAlphanumeric(15);
    }

    private boolean checkGeneratedSessionID(String session_id){
        AtomicBoolean is = new AtomicBoolean(false);
        findAll().stream()
                .map(CustomerSession::getSession_id)
                .filter(id -> id.equals(session_id))
                .findFirst()
                .ifPresent(id-> is.set(true));

        return is.get();
    }

    @Transactional
    @Modifying
    @Query("update CustomerSession session set session.session_end_time= :now where session.session_id = :session_id")
    void updateFinishTimeOfSessionWithLocalDateTime(String session_id, Timestamp now);
}
