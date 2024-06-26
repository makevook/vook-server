# demo_term_synonym

## Description

<details>
<summary><strong>Table Definition</strong></summary>

```sql
CREATE TABLE `demo_term_synonym` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `synonym` varchar(100) NOT NULL,
  `demo_term_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKu0pc6dmrckuupfj3y7n9vxs0` (`demo_term_id`),
  CONSTRAINT `FKu0pc6dmrckuupfj3y7n9vxs0` FOREIGN KEY (`demo_term_id`) REFERENCES `demo_term` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=[Redacted by tbls] DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
```

</details>

## Columns

| Name | Type | Default | Nullable | Extra Definition | Children | Parents | Comment |
| ---- | ---- | ------- | -------- | ---------------- | -------- | ------- | ------- |
| id | bigint(20) |  | false | auto_increment |  |  |  |
| synonym | varchar(100) |  | false |  |  |  |  |
| demo_term_id | bigint(20) |  | false |  |  | [demo_term](demo_term.md) |  |

## Constraints

| Name | Type | Definition |
| ---- | ---- | ---------- |
| FKu0pc6dmrckuupfj3y7n9vxs0 | FOREIGN KEY | FOREIGN KEY (demo_term_id) REFERENCES demo_term (id) |
| PRIMARY | PRIMARY KEY | PRIMARY KEY (id) |

## Indexes

| Name | Definition |
| ---- | ---------- |
| FKu0pc6dmrckuupfj3y7n9vxs0 | KEY FKu0pc6dmrckuupfj3y7n9vxs0 (demo_term_id) USING BTREE |
| PRIMARY | PRIMARY KEY (id) USING BTREE |

## Relations

```mermaid
erDiagram

"demo_term_synonym" }o--|| "demo_term" : "FOREIGN KEY (demo_term_id) REFERENCES demo_term (id)"

"demo_term_synonym" {
  bigint_20_ id PK
  varchar_100_ synonym
  bigint_20_ demo_term_id FK
}
"demo_term" {
  bigint_20_ id PK
  datetime_6_ created_at
  datetime_6_ updated_at
  varchar_2000_ meaning
  varchar_100_ term
}
```

---

> Generated by [tbls](https://github.com/k1LoW/tbls)
