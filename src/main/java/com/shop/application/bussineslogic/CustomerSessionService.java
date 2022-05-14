package com.shop.application.bussineslogic;

import com.shop.application.entities.userdb.CustomerSession;
import com.shop.application.repositories.userdbdao.CustomerSessionRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2

@Getter
@AllArgsConstructor

@Service
public class CustomerSessionService {

    private final List<CustomerSession> active_sessions;
    private final HashMap<String, Integer> current_page;
    private final CustomerSessionRepo repository;

    public int getCurrentPage(String session_id){
        return current_page.get(session_id);
    }

    public void changeCurrentPage(String session_id, String page_path_var){
        int page = readPageFromPathVariable(page_path_var);
        log.info("Session: " + session_id +
                " user changed page from" +
                current_page.get(session_id) +
                "to "
                + page
        );
        current_page.replace(session_id, page);
    }

    public void overTheSession(String session_id){
        Optional<CustomerSession> session = repository.getCustomerSessionBySession_id(session_id);
        ZoneId zone = ZoneId.systemDefault();
        session.ifPresentOrElse(s->{
            Timestamp now = Timestamp.valueOf(LocalDateTime.now(zone));
            current_page.remove(session_id);
            repository.updateFinishTimeOfSessionWithLocalDateTime(session_id, now);
            log.info("Session " + session_id +" was over at " + now + " zone: " + zone);
        }, ()->{
            log.error("No session " + session_id + " was found...");
        });

    }

    public void registerNewCustomerSession(String session_id){
        repository.registerNewCustomerSession(session_id);
        current_page.put(session_id, 1);
        log.info("Registered new session: " + session_id);
    }

    public String sendNewSessionID(){
        String session_id = repository.generateNewSessionID();
        log.info("Generated new session id: " + session_id);
        return session_id;
    }

    private int readPageFromPathVariable(String page_path_var){
        int page = 1;
        try{
            page = Integer.parseInt(page_path_var);
            log.info("Read '" + page_path_var + "' from path var");
        }catch (Exception e){
            log.error("Couldn't read number from path var, provided: " +page_path_var);
        }
        return page;
    }
}
