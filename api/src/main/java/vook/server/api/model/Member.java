package vook.server.api.model;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 회원
 */
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 회원 이름
     */
    private String name;

    public static Member forCreateOf(String name) {
        Member result = new Member();
        result.name = name;
        return result;
    }
}
