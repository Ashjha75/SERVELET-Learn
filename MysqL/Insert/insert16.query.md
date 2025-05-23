
```sql
-- 30 sample INSERTs for the `audits` table
INSERT INTO audits (table_name, operation, old_data_json, new_data_json, changed_at) VALUES
('users',    'INSERT', NULL,                                                          '{"id":1,"username":"alice_w","email":"alice.williams@example.com"}',      '2025-05-01 08:23:45'),
('users',    'INSERT', NULL,                                                          '{"id":2,"username":"bob_smith","email":"bob.smith@example.com"}',          '2025-05-02 09:15:10'),
('users',    'INSERT', NULL,                                                          '{"id":3,"username":"charlie89","email":"charlie.johnson@example.com"}',   '2025-05-03 10:05:30'),
('profiles', 'INSERT', NULL,                                                          '{"profile_id":1,"user_id":1,"first_name":"Alice"}',                         '2025-05-03 11:12:00'),
('profiles', 'INSERT', NULL,                                                          '{"profile_id":2,"user_id":2,"first_name":"Bob"}',                           '2025-05-04 12:20:15'),
('posts',    'INSERT', NULL,                                                          '{"post_id":1,"user_id":1,"title":"Hiking the Alps"}',                       '2025-05-05 13:30:45'),
('posts',    'UPDATE', '{"post_id":1,"metadata_json":{"views":120}}',                 '{"post_id":1,"metadata_json":{"views":150}}',                               '2025-05-06 14:40:25'),
('comments', 'INSERT', NULL,                                                          '{"comment_id":1,"post_id":1,"user_id":2}',                                  '2025-05-06 15:50:05'),
('comments', 'DELETE', '{"comment_id":1,"comment_text":"Amazing photos"}',            NULL,                                                                        '2025-05-07 08:05:35'),
('categories','INSERT', NULL,                                                         '{"category_id":1,"name":"Travel"}',                                         '2025-05-07 09:15:20'),
('tags',     'INSERT', NULL,                                                         '{"tag_id":1,"name":"travel"}',                                              '2025-05-07 10:25:55'),
('post_tags','INSERT', NULL,                                                         '{"post_id":1,"tag_id":1}',                                                  '2025-05-07 11:35:40'),
('orders',   'INSERT', NULL,                                                         '{"order_id":1,"user_id":1,"status":"completed"}',                           '2025-05-08 12:45:10'),
('orders',   'UPDATE', '{"order_id":1,"status":"pending"}',                          '{"order_id":1,"status":"completed"}',                                       '2025-05-08 13:55:30'),
('order_items','INSERT',NULL,                                                        '{"order_item_id":1,"order_id":1,"product_id":1,"quantity":2}',              '2025-05-08 15:05:05'),
('payments', 'INSERT', NULL,                                                         '{"payment_id":1,"order_id":1,"amount":259.98}',                             '2025-05-08 16:15:20'),
('sales',    'INSERT', NULL,                                                         '{"sale_id":1,"order_id":1,"sale_amount":259.98}',                           '2025-05-08 17:25:45'),
('logs',     'INSERT', NULL,                                                         '{"log_id":1,"user_id":1,"action":"User logged in"}',                        '2025-05-09 08:35:55'),
('logs',     'UPDATE', '{"log_id":1,"action":"User logged in"}',                     '{"log_id":1,"action":"User signed in"}',                                     '2025-05-09 09:45:15'),
('sessions', 'INSERT', NULL,                                                         '{"session_id":"sess_001","user_id":1}',                                     '2025-05-09 10:55:30'),
('sessions', 'UPDATE', '{"session_id":"sess_001","logout_time":NULL}',               '{"session_id":"sess_001","logout_time":"2025-05-09 12:00:00"}',              '2025-05-09 12:05:00'),
('users',    'UPDATE', '{"id":4,"status":"active"}',                                 '{"id":4,"status":"inactive"}',                                               '2025-05-10 08:15:45'),
('profiles', 'UPDATE', '{"profile_id":2,"bio":NULL}',                                '{"profile_id":2,"bio":"Avid reader and coffee enthusiast."}',              '2025-05-10 09:25:30'),
('products', 'INSERT', NULL,                                                         '{"product_id":1,"name":"Alpine Hiking Boots"}',                              '2025-05-10 10:35:20'),
('products', 'UPDATE', '{"product_id":1,"stock":50}',                                '{"product_id":1,"stock":45}',                                                '2025-05-11 11:45:05'),
('categories','UPDATE', '{"category_id":1,"description":NULL}',                      '{"category_id":1,"description":"Articles about travel"}',                   '2025-05-11 12:55:40'),
('tags',     'UPDATE', '{"tag_id":1,"name":"travel"}',                               '{"tag_id":1,"name":"travelogue"}',                                           '2025-05-11 14:05:25'),
('audits',   'INSERT', NULL,                                                         '{"table_name":"audits","operation":"INSERT","new_data_json":"..."}',       '2025-05-11 15:15:55');
```
