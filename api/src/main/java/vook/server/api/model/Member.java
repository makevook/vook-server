package vook.server.api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public static Member forCreateOf(String name) {
        Member result = new Member();
        result.name = name;
        return result;
    }
}
