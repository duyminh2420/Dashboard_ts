-- Create Users table
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  phone VARCHAR(20),
  img TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  verified BOOLEAN DEFAULT FALSE,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Products table
CREATE TABLE IF NOT EXISTS products (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  color VARCHAR(50),
  producer VARCHAR(100),
  price VARCHAR(20) NOT NULL,
  img TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  in_stock BOOLEAN DEFAULT TRUE,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Activities table
CREATE TABLE IF NOT EXISTS activities (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  text TEXT NOT NULL,
  time VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_products_title ON products(title);
CREATE INDEX idx_products_in_stock ON products(in_stock);
CREATE INDEX idx_activities_user_id ON activities(user_id);
CREATE INDEX idx_activities_created_at ON activities(created_at);

-- Insert sample data
INSERT INTO users (first_name, last_name, email, phone, img, created_at, verified) VALUES
('Eula', 'Hubbard', 'kewez@gmail.com', '123 456 789', 'https://images.pexels.com/photos/8405873/pexels-photo-8405873.jpeg?auto=compress&cs=tinysrgb&w=1600&lazy=load', '2023-02-01', true),
('Stella', 'Manning', 'comhuhmit@gmail.com', '123 456 789', 'https://images.pexels.com/photos/1181519/pexels-photo-1181519.jpeg?auto=compress&cs=tinysrgb&w=1600', '2023-02-01', true),
('Mary', 'Greer', 'ujudokon@hottmail.com', '123 456 789', 'https://images.pexels.com/photos/1587009/pexels-photo-1587009.jpeg?auto=compress&cs=tinysrgb&w=1600', '2023-02-01', true);

INSERT INTO products (title, color, producer, price, img, created_at, in_stock) VALUES
('Playstation 5 Digital Edition', 'white', 'Sony', '$250.99', 'https://store.sony.com.au/on/demandware.static/-/Sites-sony-master-catalog/default/dw1b537bbb/images/PLAYSTATION5W/PLAYSTATION5W.png', '2023-02-01', true),
('Dell Laptop KR211822', 'black', 'Dell', '$499.99', 'https://www.pngmart.com/files/6/Dell-Laptop-PNG-Image.png', '2023-02-01', true),
('Samsung TV 4K SmartTV', 'gray', 'Samsung', '$999.49', 'http://images.samsung.com/is/image/samsung/uk-led-tv-hg40ed670ck-hg40ed670ckxxu-001-front', '2023-02-01', true);

INSERT INTO activities (user_id, text, time) VALUES
(1, 'purchased Playstation 5 Digital Edition', '3 day ago'),
(1, 'added 3 items into their wishlist', '1 week ago'),
(1, 'purchased Sony Bravia KD-32w800', '2 weeks ago'),
(2, 'purchased iPhone 13 Pro Max', '1 day ago'),
(2, 'added 2 items into their wishlist', '3 weeks ago'),
(3, 'purchased Samsung Galaxy S22', '2 days ago');
