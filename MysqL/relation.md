```sql
Users ↔ Profiles: One-to-One

Users ↔ Posts: One-to-Many

Posts ↔ Comments: One-to-Many

Posts ↔ Categories: Many-to-One

Posts ↔ Tags: Many-to-Many (via Post_Tags)

Users ↔ Orders: One-to-Many

Orders ↔ Order_Items: One-to-Many

Order_Items ↔ Products: Many-to-One

Orders ↔ Payments: One-to-One

Users ↔ Sessions: One-to-Many
```