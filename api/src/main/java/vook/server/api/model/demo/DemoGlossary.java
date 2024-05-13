package vook.server.api.model.demo;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.model.BaseEntity;

import java.util.UUID;

/**
 * 데모 용어집
 */
@Getter
@Entity
@Table(name = "demo_glossary")
public class DemoGlossary extends BaseEntity {

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

    public static DemoGlossary forCreateOf(
            String name
    ) {
        DemoGlossary result = new DemoGlossary();
        result.uid = UUID.randomUUID().toString();
        result.name = name;
        return result;
    }
}
