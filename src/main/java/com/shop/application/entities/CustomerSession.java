package com.shop.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "session_history")
public class CustomerSession {

    @Id
    @Column(name = "session_id", length = 16)
    private String session_id;

    @Column(name="user", length = 50, nullable = false)
    private String user_name;

    @Column(name="session_start_time", nullable = false)
    private Timestamp session_start_time;

    @Column(name="session_end_time")
    private Timestamp session_end_time;

}
