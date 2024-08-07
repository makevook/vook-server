# social_user

## Description

소셜 사용자

<details>
<summary><strong>Table Definition</strong></summary>

```sql
CREATE TABLE `social_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `provider` varchar(255) DEFAULT NULL COMMENT '제공자',
  `provider_user_id` varchar(255) DEFAULT NULL COMMENT '제공자 사용자 ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '사용자 ID',
  `created_at` datetime(6) DEFAULT NULL COMMENT '생성일시',
  PRIMARY KEY (`id`),
  KEY `fk_social_user_users` (`user_id`),
  CONSTRAINT `fk_social_user_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='소셜 사용자'
```

</details>

## Columns

| Name | Type | Default | Nullable | Extra Definition | Children | Parents | Comment |
| ---- | ---- | ------- | -------- | ---------------- | -------- | ------- | ------- |
| id | bigint(20) |  | false | auto_increment |  |  | ID |
| provider | varchar(255) | NULL | true |  |  |  | 제공자 |
| provider_user_id | varchar(255) | NULL | true |  |  |  | 제공자 사용자 ID |
| user_id | bigint(20) | NULL | true |  |  | [users](users.md) | 사용자 ID |
| created_at | datetime(6) | NULL | true |  |  |  | 생성일시 |

## Constraints

| Name | Type | Definition |
| ---- | ---- | ---------- |
| fk_social_user_users | FOREIGN KEY | FOREIGN KEY (user_id) REFERENCES users (id) |
| PRIMARY | PRIMARY KEY | PRIMARY KEY (id) |

## Indexes

| Name | Definition |
| ---- | ---------- |
| fk_social_user_users | KEY fk_social_user_users (user_id) USING BTREE |
| PRIMARY | PRIMARY KEY (id) USING BTREE |

## Relations

```mermaid
erDiagram

"social_user" }o--o| "users" : "FOREIGN KEY (user_id) REFERENCES users (id)"

"social_user" {
  bigint_20_ id PK
  varchar_255_ provider
  varchar_255_ provider_user_id
  bigint_20_ user_id FK
  datetime_6_ created_at
}
"users" {
  bigint_20_ id PK
  varchar_255_ uid
  varchar_255_ email
  varchar_30_ status
  datetime_6_ registered_at
  bit_1_ onboarding_completed
  datetime_6_ onboarding_completed_at
  datetime_6_ withdrawn_at
  datetime_6_ last_updated_at
}
```

---

> Generated by [tbls](https://github.com/k1LoW/tbls)
