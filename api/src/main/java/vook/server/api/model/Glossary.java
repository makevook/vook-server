package vook.server.api.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

/**
 * 용어집
 */
@Getter
@Entity
@Table(name = "glossary")
public class Glossary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * UID; 외부 노출용 식별자
     */
    @Column(length = 36, nullable = false, unique = true)
    private String uid;

    /**
     * 용어집 이름
     */
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Glossary forCreateOf(
            String name,
            Member member
    ) {
        Glossary result = new Glossary();
        result.uid = UUID.randomUUID().toString();
        result.name = name;
        result.member = member;
        return result;
    }
}
