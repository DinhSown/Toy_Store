-- ============================================================
-- Toy Model Store — Seed Data
-- Version: V2 — Initial Data
-- ============================================================

-- ==================== USERS ====================
-- Mật khẩu: Admin@123456  (BCrypt hash)
INSERT INTO users (email, password, full_name, phone, role, is_active)
VALUES (
    'admin@toystore.vn',
    '$2a$10$92mLFcXpDblxU2Ik4mNXOOj0kqNwHYCvVe7nFBpFwkW5sNQpuFd5.',
    'Quản trị viên',
    '0901234567',
    'ROLE_ADMIN',
    TRUE
);

-- Mật khẩu: User@123456  (BCrypt hash)
INSERT INTO users (email, password, full_name, phone, role, is_active)
VALUES (
    'user1@toystore.vn',
    '$2a$10$Kl9y6mBHXvBq6KkX/XJMuexQwPT5YiJqpIgRz4T4J9Z3wK1gGJlRO',
    'Nguyễn Văn Minh',
    '0912345678',
    'ROLE_USER',
    TRUE
),
(
    'user2@toystore.vn',
    '$2a$10$Kl9y6mBHXvBq6KkX/XJMuexQwPT5YiJqpIgRz4T4J9Z3wK1gGJlRO',
    'Trần Thị Lan',
    '0987654321',
    'ROLE_USER',
    TRUE
);

-- ==================== CATEGORIES ====================
-- Danh mục cha
INSERT INTO categories (name, slug, description, image_url, parent_id, is_active) VALUES
('Mô Hình Gundam',       'mo-hinh-gundam',       'Dòng mô hình Gundam chính hãng Bandai từ Nhật Bản',        'https://placehold.co/400x300/1a1a2e/ffffff?text=Gundam',      NULL, TRUE),
('Mô Hình Xe',           'mo-hinh-xe',           'Xe ô tô, xe đua, xe quân sự thu nhỏ các tỷ lệ',            'https://placehold.co/400x300/16213e/ffffff?text=Mo+Hinh+Xe', NULL, TRUE),
('Mô Hình Máy Bay',      'mo-hinh-may-bay',      'Máy bay chiến đấu, dân dụng và tàu vũ trụ thu nhỏ',        'https://placehold.co/400x300/0f3460/ffffff?text=May+Bay',    NULL, TRUE),
('Mô Hình Tàu Chiến',   'mo-hinh-tau-chien',    'Chiến hạm, tàu ngầm và tàu sân bay thu nhỏ tỷ lệ 1/350',  'https://placehold.co/400x300/533483/ffffff?text=Tau+Chien',  NULL, TRUE),
('Nhân Vật & Figure',    'nhan-vat-figure',      'Figure anime, game, siêu anh hùng Marvel & DC',             'https://placehold.co/400x300/e94560/ffffff?text=Figure',     NULL, TRUE);

-- Danh mục con — Gundam (parent_id = 1)
INSERT INTO categories (name, slug, description, image_url, parent_id, is_active) VALUES
('HG (High Grade)',      'hg-high-grade',        'Gundam HG tỷ lệ 1/144, thích hợp người mới bắt đầu',       'https://placehold.co/400x300/1a1a2e/cccccc?text=HG',         1, TRUE),
('MG (Master Grade)',    'mg-master-grade',       'Gundam MG tỷ lệ 1/100, chi tiết cao, khung bên trong',     'https://placehold.co/400x300/1a1a2e/cccccc?text=MG',         1, TRUE),
('RG (Real Grade)',      'rg-real-grade',         'Gundam RG tỷ lệ 1/144, chi tiết cực cao như MG',           'https://placehold.co/400x300/1a1a2e/cccccc?text=RG',         1, TRUE),
('PG (Perfect Grade)',   'pg-perfect-grade',      'Gundam PG tỷ lệ 1/60, đỉnh cao kỹ thuật lắp ráp',         'https://placehold.co/400x300/1a1a2e/cccccc?text=PG',         1, TRUE);

-- Danh mục con — Xe (parent_id = 2)
INSERT INTO categories (name, slug, description, image_url, parent_id, is_active) VALUES
('Xe Đua F1',            'xe-dua-f1',            'Mô hình xe đua Formula 1 tỷ lệ 1/20 và 1/24',             'https://placehold.co/400x300/16213e/cccccc?text=F1',         2, TRUE),
('Xe Quân Sự',           'xe-quan-su',           'Xe tăng, xe bọc thép và phương tiện quân sự thu nhỏ',      'https://placehold.co/400x300/16213e/cccccc?text=Quan+Su',    2, TRUE);

-- ==================== PRODUCTS ====================

-- ─── GUNDAM HG (category_id = 6) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'HG 1/144 RX-78-2 Gundam [BEYOND GLOBAL]',
    'hg-1-144-rx-78-2-gundam-beyond-global',
    'Mô hình HG 1/144 RX-78-2 Gundam phiên bản Beyond Global kỷ niệm 40 năm Gundam. Màu sắc tươi sáng, chi tiết được cải thiện so với bản cũ, bao gồm vũ khí Beam Rifle và Shield. Không cần keo, chỉ cần kìm cắt.',
    290000, NULL, 85, 'Bandai',
    'https://placehold.co/600x600/1a1a2e/ffffff?text=HG+RX-78-2',
    '["https://placehold.co/600x600/1a1a2e/dddddd?text=RX-78-2+Side","https://placehold.co/600x600/1a1a2e/dddddd?text=RX-78-2+Back"]',
    6, TRUE, TRUE
),
(
    'HG 1/144 Wing Gundam Zero EW',
    'hg-1-144-wing-gundam-zero-ew',
    'Mô hình HG 1/144 Wing Gundam Zero từ series Gundam Wing: Endless Waltz. Thiết kế cánh thiên thần đặc trưng, đi kèm Twin Buster Rifle. Phù hợp với fan Gundam Wing.',
    320000, 280000, 60, 'Bandai',
    'https://placehold.co/600x600/0f3460/ffffff?text=HG+Wing+Zero',
    '["https://placehold.co/600x600/0f3460/dddddd?text=Wing+Zero+Side"]',
    6, TRUE, TRUE
),
(
    'HG 1/144 Gundam Barbatos Lupus Rex',
    'hg-1-144-gundam-barbatos-lupus-rex',
    'Mô hình HG 1/144 Gundam Barbatos Lupus Rex từ series Iron-Blooded Orphans Season 2. Thiết kế hung hăng với đuôi scorpion đặc trưng và Dainsleif. Màu đỏ đen nổi bật.',
    350000, NULL, 45, 'Bandai',
    'https://placehold.co/600x600/e94560/ffffff?text=HG+Barbatos',
    '["https://placehold.co/600x600/e94560/dddddd?text=Barbatos+Side","https://placehold.co/600x600/e94560/dddddd?text=Barbatos+Weapon"]',
    6, TRUE, FALSE
),
(
    'HG 1/144 Freedom Gundam',
    'hg-1-144-freedom-gundam',
    'Mô hình HG 1/144 Freedom Gundam từ series Gundam SEED. Thiết kế cánh đặc trưng màu xanh trắng với hệ thống vũ khí METEOR. Một trong những Gundam được yêu thích nhất.',
    300000, NULL, 70, 'Bandai',
    'https://placehold.co/600x600/1a6b8a/ffffff?text=HG+Freedom',
    '["https://placehold.co/600x600/1a6b8a/dddddd?text=Freedom+Side"]',
    6, TRUE, FALSE
),
(
    'HG 1/144 Sazabi Ver. Ka',
    'hg-1-144-sazabi-ver-ka',
    'Phiên bản HG 1/144 của Sazabi thiết kế bởi Hajime Katoki. Màu đỏ đặc trưng của Char, đi kèm beam shot rifle và funnel. Kích thước lớn hơn các HG thông thường.',
    420000, 380000, 30, 'Bandai',
    'https://placehold.co/600x600/c0392b/ffffff?text=HG+Sazabi',
    '["https://placehold.co/600x600/c0392b/dddddd?text=Sazabi+Side","https://placehold.co/600x600/c0392b/dddddd?text=Sazabi+Funnel"]',
    6, TRUE, TRUE
);

-- ─── GUNDAM MG (category_id = 7) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'MG 1/100 Freedom Gundam Ver. 2.0',
    'mg-1-100-freedom-gundam-ver-2',
    'Mô hình MG 1/100 Freedom Gundam Ver. 2.0 với khung nội thất chi tiết, cánh có thể mở rộng hoàn toàn. Hệ thống khớp được cải tiến, cho phép tạo dáng đa dạng. Đây là phiên bản đáng mua nhất cho fan Gundam SEED.',
    850000, 780000, 25, 'Bandai',
    'https://placehold.co/600x600/1a6b8a/ffffff?text=MG+Freedom+2.0',
    '["https://placehold.co/600x600/1a6b8a/dddddd?text=MG+Freedom+Open","https://placehold.co/600x600/1a6b8a/dddddd?text=MG+Freedom+Inner"]',
    7, TRUE, TRUE
),
(
    'MG 1/100 RX-78-2 Gundam Ver. 3.0',
    'mg-1-100-rx-78-2-ver-3',
    'Phiên bản MG 1/100 RX-78-2 Ver. 3.0 với khung nội thất hoàn toàn mới, chi tiết hơn bất kỳ phiên bản nào trước đó. Bao gồm pilot Amuro Ray và buồng lái Gundam.',
    950000, NULL, 20, 'Bandai',
    'https://placehold.co/600x600/1a1a2e/ffffff?text=MG+RX78-2+v3',
    '["https://placehold.co/600x600/1a1a2e/dddddd?text=MG+RX78+Inner","https://placehold.co/600x600/1a1a2e/dddddd?text=MG+RX78+Pilot"]',
    7, TRUE, TRUE
),
(
    'MG 1/100 Gundam Exia Ignition Mode',
    'mg-1-100-gundam-exia-ignition-mode',
    'MG 1/100 Gundam Exia Ignition Mode từ series Gundam 00. Bao gồm GN Sword, GN Blade, và LED Unit tùy chọn để phát sáng GN Drive. Chi tiết kết cấu tuyệt vời.',
    1100000, 990000, 15, 'Bandai',
    'https://placehold.co/600x600/27ae60/ffffff?text=MG+Exia',
    '["https://placehold.co/600x600/27ae60/dddddd?text=Exia+LED","https://placehold.co/600x600/27ae60/dddddd?text=Exia+Sword"]',
    7, TRUE, FALSE
),
(
    'MG 1/100 Strike Freedom Gundam',
    'mg-1-100-strike-freedom-gundam',
    'MG 1/100 Strike Freedom Gundam với khung vàng Dragoon System và cánh METEOR mở rộng. Một trong những bộ mô hình MG ấn tượng nhất với màu sắc trắng vàng tươi sáng.',
    1200000, NULL, 18, 'Bandai',
    'https://placehold.co/600x600/f39c12/ffffff?text=MG+Strike+Freedom',
    '["https://placehold.co/600x600/f39c12/dddddd?text=SF+Wings","https://placehold.co/600x600/f39c12/dddddd?text=SF+Dragoon"]',
    7, TRUE, TRUE
);

-- ─── GUNDAM RG (category_id = 8) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'RG 1/144 RX-78-2 Gundam',
    'rg-1-144-rx-78-2-gundam',
    'Mô hình RG 1/144 RX-78-2 Gundam với Advanced MS Joint cho phép linh hoạt như MG nhưng ở tỷ lệ 1/144. Chi tiết panel line tinh xảo, màu sắc đa tầng thực tế.',
    490000, 450000, 40, 'Bandai',
    'https://placehold.co/600x600/1a1a2e/ffffff?text=RG+RX78-2',
    '["https://placehold.co/600x600/1a1a2e/dddddd?text=RG+RX78+Joint"]',
    8, TRUE, FALSE
),
(
    'RG 1/144 Nu Gundam',
    'rg-1-144-nu-gundam',
    'RG 1/144 Nu Gundam với hệ thống fin funnel có thể triển khai, màu sắc xanh trắng đặc trưng. Khung nội thất tinh xảo và chi tiết cao nhất trong dòng RG.',
    650000, NULL, 22, 'Bandai',
    'https://placehold.co/600x600/2980b9/ffffff?text=RG+Nu+Gundam',
    '["https://placehold.co/600x600/2980b9/dddddd?text=Nu+Funnel","https://placehold.co/600x600/2980b9/dddddd?text=Nu+Back"]',
    8, TRUE, TRUE
),
(
    'RG 1/144 Evangelion Unit-01',
    'rg-1-144-evangelion-unit-01',
    'RG 1/144 Evangelion Unit-01 từ series Neon Genesis Evangelion. Màu tím xanh đặc trưng của Eva-01, chi tiết tuyệt vời với các khớp linh hoạt. Phiên bản giới hạn.',
    720000, 650000, 12, 'Bandai',
    'https://placehold.co/600x600/6c3483/ffffff?text=RG+Eva-01',
    '["https://placehold.co/600x600/6c3483/dddddd?text=Eva-01+Side","https://placehold.co/600x600/6c3483/dddddd?text=Eva-01+Prog+Knife"]',
    8, TRUE, TRUE
);

-- ─── GUNDAM PG (category_id = 9) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'PG 1/60 RX-78-2 Gundam',
    'pg-1-60-rx-78-2-gundam',
    'Mô hình PG 1/60 RX-78-2 Gundam — đỉnh cao của dòng Perfect Grade. Kích thước 30cm, khung nội thất siêu chi tiết, buồng lái có thể mở, LED tùy chọn. Dành cho người chơi cao cấp.',
    3500000, NULL, 8, 'Bandai',
    'https://placehold.co/600x600/1a1a2e/ffffff?text=PG+RX78-2',
    '["https://placehold.co/600x600/1a1a2e/dddddd?text=PG+Cockpit","https://placehold.co/600x600/1a1a2e/dddddd?text=PG+Inner"]',
    9, TRUE, TRUE
),
(
    'PG 1/60 Unicorn Gundam',
    'pg-1-60-unicorn-gundam',
    'PG 1/60 Unicorn Gundam với hệ thống LED tích hợp cho cả chế độ Unicorn và Destroy. Kích thước 35cm, hơn 400 chi tiết, khung xương phát sáng psychoframe. Đỉnh cao của dòng PG.',
    5200000, 4800000, 5, 'Bandai',
    'https://placehold.co/600x600/e8e8e8/333333?text=PG+Unicorn',
    '["https://placehold.co/600x600/e8e8e8/333333?text=Unicorn+Destroy","https://placehold.co/600x600/e8e8e8/333333?text=Unicorn+LED"]',
    9, TRUE, TRUE
);

-- ─── XE ĐUUA F1 (category_id = 10) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'Tamiya 1/20 Ferrari SF90 F1',
    'tamiya-1-20-ferrari-sf90-f1',
    'Mô hình xe đua F1 1/20 Ferrari SF90 của đội Scuderia Ferrari Missione Winnow. Chi tiết cực cao với các phụ kiện decal chính hãng, lốp xe thực tế và chi tiết khí động học. Dành cho người chơi lắp ráp nghiêm túc.',
    1450000, 1300000, 18, 'Tamiya',
    'https://placehold.co/600x600/c0392b/ffffff?text=Ferrari+SF90',
    '["https://placehold.co/600x600/c0392b/dddddd?text=SF90+Side","https://placehold.co/600x600/c0392b/dddddd?text=SF90+Engine"]',
    10, TRUE, TRUE
),
(
    'Tamiya 1/20 Red Bull Racing RB16B',
    'tamiya-1-20-red-bull-rb16b',
    'Mô hình 1/20 Red Bull Racing RB16B từ mùa giải F1 2021 — chiếc xe đã giúp Max Verstappen giành chức vô địch đầu tiên. Màu xanh navy và vàng đặc trưng của Red Bull.',
    1550000, NULL, 12, 'Tamiya',
    'https://placehold.co/600x600/1a237e/ffffff?text=Red+Bull+RB16B',
    '["https://placehold.co/600x600/1a237e/dddddd?text=RB16B+Top","https://placehold.co/600x600/1a237e/dddddd?text=RB16B+Front"]',
    10, TRUE, FALSE
),
(
    'Tamiya 1/24 Toyota GR Supra Racing',
    'tamiya-1-24-toyota-gr-supra-racing',
    'Mô hình 1/24 Toyota GR Supra phiên bản Racing với thiết kế GT4. Bao gồm chi tiết nội thất roll cage, ghế racing và volant. Dễ lắp ráp hơn so với dòng 1/20 F1.',
    680000, 620000, 35, 'Tamiya',
    'https://placehold.co/600x600/e74c3c/ffffff?text=GR+Supra',
    '["https://placehold.co/600x600/e74c3c/dddddd?text=Supra+Side","https://placehold.co/600x600/e74c3c/dddddd?text=Supra+Interior"]',
    10, TRUE, FALSE
);

-- ─── XE QUÂN SỰ (category_id = 11) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'Tamiya 1/35 German Tiger I Tank',
    'tamiya-1-35-german-tiger-i-tank',
    'Mô hình xe tăng 1/35 Tiger I của Đức trong Thế chiến II. Chi tiết cực cao với xích xe có thể liên kết từng mắt, tháp pháo xoay 360°, nòng pháo 88mm và 5 hình lính tùy chọn.',
    890000, NULL, 22, 'Tamiya',
    'https://placehold.co/600x600/5d4037/ffffff?text=Tiger+I',
    '["https://placehold.co/600x600/5d4037/dddddd?text=Tiger+Side","https://placehold.co/600x600/5d4037/dddddd?text=Tiger+Track"]',
    11, TRUE, TRUE
),
(
    'Tamiya 1/35 M4A3 Sherman Tank',
    'tamiya-1-35-m4a3-sherman-tank',
    'Mô hình xe tăng 1/35 Sherman M4A3 của Mỹ, biểu tượng của Thế chiến II. Chi tiết lịch sử chính xác với xích xe có thể di chuyển và 4 hình lính kèm theo.',
    760000, 700000, 28, 'Tamiya',
    'https://placehold.co/600x600/4a7c59/ffffff?text=Sherman+M4A3',
    '["https://placehold.co/600x600/4a7c59/dddddd?text=Sherman+Side"]',
    11, TRUE, FALSE
),
(
    'Trumpeter 1/35 Russian T-34/76 1942',
    'trumpeter-1-35-russian-t34-76-1942',
    'Mô hình xe tăng 1/35 T-34/76 của Liên Xô năm 1942. Sản phẩm của Trumpeter với chi tiết kỹ lưỡng, bao gồm nội thất cabin, động cơ V-2 và nhiều lựa chọn trang trí decal khác nhau.',
    650000, NULL, 15, 'Trumpeter',
    'https://placehold.co/600x600/78909c/ffffff?text=T-34-76',
    '["https://placehold.co/600x600/78909c/dddddd?text=T34+Side","https://placehold.co/600x600/78909c/dddddd?text=T34+Engine"]',
    11, TRUE, FALSE
);

-- ─── MÔ HÌNH MÁY BAY (category_id = 3) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'Tamiya 1/48 Mitsubishi Zero Type 21',
    'tamiya-1-48-mitsubishi-zero-type-21',
    'Mô hình 1/48 máy bay chiến đấu huyền thoại Mitsubishi A6M2 Zero của Hải quân Nhật. Chi tiết buồng lái, cánh có thể gập và phi công kèm theo. Huyền thoại của WWII.',
    520000, 480000, 20, 'Tamiya',
    'https://placehold.co/600x600/b8c4bb/333333?text=Zero+Type+21',
    '["https://placehold.co/600x600/b8c4bb/333333?text=Zero+Side","https://placehold.co/600x600/b8c4bb/333333?text=Zero+Cockpit"]',
    3, TRUE, TRUE
),
(
    'Trumpeter 1/32 F-16C Fighting Falcon',
    'trumpeter-1-32-f16c-fighting-falcon',
    'Mô hình 1/32 máy bay chiến đấu F-16C Fighting Falcon của Không quân Mỹ. Kích thước lớn với chi tiết buồng lái đầy đủ, động cơ và nozzle, cùng nhiều lựa chọn vũ khí.',
    1200000, 1080000, 10, 'Trumpeter',
    'https://placehold.co/600x600/5c6bc0/ffffff?text=F-16C',
    '["https://placehold.co/600x600/5c6bc0/dddddd?text=F16+Side","https://placehold.co/600x600/5c6bc0/dddddd?text=F16+Cockpit"]',
    3, TRUE, FALSE
),
(
    'Hasegawa 1/72 Su-27 Flanker B',
    'hasegawa-1-72-su-27-flanker-b',
    'Mô hình 1/72 tiêm kích Su-27 Flanker B của Nga, được sản xuất bởi Hasegawa Nhật Bản. Chi tiết chính xác với cánh mang nhiều loại tên lửa, buồng lái và chân đáp.',
    380000, NULL, 25, 'Hasegawa',
    'https://placehold.co/600x600/37474f/ffffff?text=Su-27+Flanker',
    '["https://placehold.co/600x600/37474f/dddddd?text=Su27+Top"]',
    3, TRUE, FALSE
);

-- ─── MÔ HÌNH TÀU CHIẾN (category_id = 4) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'Tamiya 1/350 USS Enterprise CV-6',
    'tamiya-1-350-uss-enterprise-cv-6',
    'Mô hình 1/350 tàu sân bay USS Enterprise CV-6 của Hải quân Mỹ trong WWII. Chiều dài 79cm sau khi lắp ráp, đi kèm 67 máy bay thu nhỏ và 240+ chi tiết thủy thủ.',
    2800000, 2500000, 6, 'Tamiya',
    'https://placehold.co/600x600/455a64/ffffff?text=USS+Enterprise',
    '["https://placehold.co/600x600/455a64/dddddd?text=Enterprise+Top","https://placehold.co/600x600/455a64/dddddd?text=Enterprise+Side"]',
    4, TRUE, TRUE
),
(
    'Trumpeter 1/350 Bismarck German Battleship',
    'trumpeter-1-350-bismarck-german-battleship',
    'Mô hình 1/350 thiết giáp hạm Bismarck của Đức — siêu chiến hạm huyền thoại trong WWII. Chiều dài 82cm, chi tiết pháo, thiết bị radar, cano cứu sinh và tháp chỉ huy.',
    2200000, NULL, 8, 'Trumpeter',
    'https://placehold.co/600x600/37474f/dddddd?text=Bismarck',
    '["https://placehold.co/600x600/37474f/ffffff?text=Bismarck+Side","https://placehold.co/600x600/37474f/ffffff?text=Bismarck+Gun"]',
    4, TRUE, FALSE
);

-- ─── NHÂN VẬT & FIGURE (category_id = 5) ───
INSERT INTO products (name, slug, description, price, sale_price, stock, brand, image_url, images, category_id, is_active, is_featured) VALUES
(
    'S.H.Figuarts Iron Man Mark L (Avengers: Infinity War)',
    'shf-iron-man-mark-l-infinity-war',
    'Figure chính hãng Bandai Tamashii Nations S.H.Figuarts Iron Man Mark L từ Avengers: Infinity War. Cao 16cm, hơn 40 điểm khớp, đi kèm nhiều phụ kiện và effect parts.',
    1350000, 1200000, 20, 'Bandai Tamashii',
    'https://placehold.co/600x600/c0392b/ffffff?text=SHF+Iron+Man',
    '["https://placehold.co/600x600/c0392b/dddddd?text=Iron+Man+Effect","https://placehold.co/600x600/c0392b/dddddd?text=Iron+Man+Fist"]',
    5, TRUE, TRUE
),
(
    'Nendoroid Naruto Uzumaki',
    'nendoroid-naruto-uzumaki',
    'Nendoroid #682 Naruto Uzumaki từ series Naruto Shippuden. Cao 10cm, bao gồm 3 bộ mặt biểu cảm, Rasengan effect part, và Kurama (Kyuubi) nhỏ. Đáng yêu và chi tiết.',
    680000, NULL, 35, 'Good Smile Company',
    'https://placehold.co/600x600/f39c12/ffffff?text=Nendoroid+Naruto',
    '["https://placehold.co/600x600/f39c12/dddddd?text=Naruto+Rasengan","https://placehold.co/600x600/f39c12/dddddd?text=Naruto+Faces"]',
    5, TRUE, FALSE
),
(
    'ARTFX J Roronoa Zoro (One Piece)',
    'artfx-j-roronoa-zoro-one-piece',
    'Statue 1/8 Roronoa Zoro từ series One Piece của Kotobukiya ARTFX J. Cao 24cm, ba kiếm đặc trưng, pose chiến đấu dữ dội sau timeskip. Không lắp ráp, chỉ cần gắn vào base.',
    1580000, 1420000, 14, 'Kotobukiya',
    'https://placehold.co/600x600/2ecc71/ffffff?text=ARTFX+Zoro',
    '["https://placehold.co/600x600/2ecc71/dddddd?text=Zoro+Side","https://placehold.co/600x600/2ecc71/dddddd?text=Zoro+Sword"]',
    5, TRUE, TRUE
);
