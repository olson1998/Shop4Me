package com.shop.application.bussineslogic;

import com.shop.application.entities.userdb.CustomerSession;
import com.shop.application.repositories.userdbdao.CustomerSessionRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        current_page.replace(session_id, page);
    }

    public void overTheSession(String session_id){
        Optional<CustomerSession> session = repository.getCustomerSessionBySession_id(session_id);
        session.ifPresentOrElse(s->{
            Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault()));
            current_page.remove(session_id);
            repository.updateFinishTimeOfSessionWithLocalDateTime(session_id, now);
        }, ()->{
            throw new RuntimeException("Session has not been found...");
        });

    }

    public void registerNewCustomerSession(String session_id){
        repository.registerNewCustomerSession(session_id);
        current_page.put(session_id, 1);
    }

    public String sendNewSessionID(){
        return repository.generateNewSessionID();
    }

    private int readPageFromPathVariable(String page_path_var){
        int page = 1;
        try{
            page = Integer.parseInt(page_path_var);
        }catch (Exception e){
            throw new RuntimeException("couldn't read page from path variable...");
        }
        return page;
    }
}
