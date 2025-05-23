# MySQL Database Schema Creation

## Database Configuration
All tables use:
- InnoDB engine for ACID compliance and transaction support
- utf8mb4 charset for full Unicode support including emojis
- Foreign key constraints for referential integrity
- Appropriate indexing for performance

## Users Table
This table stores core user account information.
```sql
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `id`: Auto-incrementing primary key
- `username`: Unique user identifier (50 chars max)
- `password_hash`: Stores hashed passwords (255 chars for modern hash algorithms)
- `email`: Unique email address
- `created_at`: Account creation timestamp
- `status`: User account status (active/inactive/suspended)

## Profiles Table
Stores additional user information separate from authentication data.
```sql
CREATE TABLE profiles (
    profile_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    birth_date DATE,
    bio TEXT,
    PRIMARY KEY (profile_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `profile_id`: Primary key for profile
- `user_id`: Foreign key linking to users table
- `first_name`, `last_name`: Optional personal information
- `birth_date`: User's date of birth
- `bio`: Extended user description
- ON DELETE CASCADE: When a user is deleted, their profile is automatically deleted

## Posts Table
Manages user-created content/posts.
```sql
CREATE TABLE posts (
    post_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    content TEXT,
    metadata_json JSON,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id),
    INDEX idx_posts_user (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `post_id`: Unique identifier for posts
- `user_id`: Links post to its author
- `title`: Post title (150 chars max)
- `content`: Main post content
- `metadata_json`: Flexible JSON field for additional post metadata
- `created_at`: Post creation time
- `updated_at`: Automatically updates when post is modified
- Index on user_id for faster user-based queries

## Comments Table
Stores user comments on posts.
```sql
CREATE TABLE comments (
    comment_id INT NOT NULL AUTO_INCREMENT,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id),
    INDEX idx_comments_post (post_id),
    INDEX idx_comments_user (user_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `comment_id`: Unique identifier for comments
- `post_id`: Links comment to specific post
- `user_id`: Links comment to its author
- `comment_text`: The comment content
- `created_at`: Comment timestamp
- Indexes on both post_id and user_id for better query performance
- Comments are deleted with their parent post (CASCADE)
- User deletion sets comment's user_id to NULL (SET NULL)

## Categories Table
Manages post categories for organization.
```sql
CREATE TABLE categories (
    category_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `category_id`: Unique identifier for categories
- `name`: Unique category name
- `description`: Optional category description

## Post Categories Junction Table
Links posts to categories (many-to-many relationship).
```sql
CREATE TABLE post_categories (
    post_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (post_id, category_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- Composite primary key of post_id and category_id
- Enables many-to-many relationship between posts and categories
- Cascading deletes in both directions

## Tags Table
Stores tags for flexible content categorization.
```sql
CREATE TABLE tags (
    tag_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `tag_id`: Unique identifier for tags
- `name`: Unique tag name

## Post Tags Junction Table
Links posts to tags (many-to-many relationship).
```sql
CREATE TABLE post_tags (
    post_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- Composite primary key of post_id and tag_id
- Enables many-to-many relationship between posts and tags
- Cascading deletes in both directions

## Products Table
Stores product information for e-commerce functionality.
```sql
CREATE TABLE products (
    product_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    attributes_json JSON,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `product_id`: Unique identifier for products
- `name`: Product name
- `price`: Product price with 2 decimal places
- `stock`: Current inventory level
- `attributes_json`: Flexible JSON field for product attributes
- `created_at`: Product creation timestamp

## Orders Table
Manages customer orders.
```sql
CREATE TABLE orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'pending',
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `order_id`: Unique identifier for orders
- `user_id`: Links order to customer
- `order_date`: When order was placed
- `status`: Order status (pending/completed/cancelled)
- User deletion sets order's user_id to NULL

## Order Items Table
Stores individual items within orders.
```sql
CREATE TABLE order_items (
    order_item_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `order_item_id`: Unique identifier for order items
- `order_id`: Links item to specific order
- `product_id`: Links item to product
- `quantity`: Number of items ordered
- `unit_price`: Price at time of purchase
- RESTRICT on product deletion prevents orphaned orders

## Payments Table
Records payment transactions for orders.
```sql
CREATE TABLE payments (
    payment_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(50) NOT NULL,
    payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (payment_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `payment_id`: Unique identifier for payments
- `order_id`: Links payment to order
- `amount`: Payment amount
- `method`: Payment method used
- `payment_date`: When payment was made
- Payments are deleted with their parent order

## Sales Table
Tracks sales records.
```sql
CREATE TABLE sales (
    sale_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    sale_amount DECIMAL(10,2) NOT NULL,
    sale_date DATE NOT NULL,
    PRIMARY KEY (sale_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `sale_id`: Unique identifier for sales records
- `order_id`: Links sale to order
- `sale_amount`: Total sale amount
- `sale_date`: Date of sale
- Sales records are deleted with their parent order

## Logs Table
Tracks user actions for auditing.
```sql
CREATE TABLE logs (
    log_id INT NOT NULL AUTO_INCREMENT,
    user_id INT,
    action VARCHAR(100) NOT NULL,
    log_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (log_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `log_id`: Unique identifier for log entries
- `user_id`: Links log to user (optional)
- `action`: Description of action performed
- `log_time`: When action occurred
- User deletion sets log's user_id to NULL

## Sessions Table
Manages user login sessions.
```sql
CREATE TABLE sessions (
    session_id VARCHAR(128) NOT NULL,
    user_id INT NOT NULL,
    ip_address VARCHAR(45),
    login_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_time DATETIME,
    PRIMARY KEY (session_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `session_id`: Unique session identifier (128 chars for security)
- `user_id`: Links session to user
- `ip_address`: User's IP address (IPv4 or IPv6)
- `login_time`: Session start time
- `logout_time`: Session end time (NULL if session is active)
- Sessions are deleted when user is deleted

## Audits Table
Records changes to important tables for data tracking.
```sql
CREATE TABLE audits (
    audit_id INT NOT NULL AUTO_INCREMENT,
    table_name VARCHAR(50) NOT NULL,
    operation VARCHAR(20) NOT NULL,
    old_data_json JSON,
    new_data_json JSON,
    changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (audit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- `audit_id`: Unique identifier for audit records
- `table_name`: Name of table being audited
- `operation`: Type of change (INSERT/UPDATE/DELETE)
- `old_data_json`: Previous state of record in JSON format
- `new_data_json`: New state of record in JSON format
- `changed_at`: Timestamp of the change

## Schema Overview
This database schema supports:
1. User management with profiles
2. Content management (posts, comments)
3. Content organization (categories, tags)
4. E-commerce functionality (products, orders, payments)
5. Sales tracking
6. Security (sessions, logs)
7. Audit trail
8. Data integrity through foreign keys
9. Performance optimization through indexes
10. Flexible data storage using JSON fields

