CREATE DATABASE thuexe_xe_db;
CREATE DATABASE thuexe_khach_db;
CREATE DATABASE thuexe_hopdong_db;
CREATE DATABASE thuexe_bangiao_db;

CREATE USER xe_user WITH PASSWORD 'xe_pass';
GRANT ALL PRIVILEGES ON DATABASE thuexe_xe_db TO xe_user;

CREATE USER khach_user WITH PASSWORD 'khach_pass';
GRANT ALL PRIVILEGES ON DATABASE thuexe_khach_db TO khach_user;

CREATE USER hopdong_user WITH PASSWORD 'hopdong_pass';
GRANT ALL PRIVILEGES ON DATABASE thuexe_hopdong_db TO hopdong_user;

CREATE USER bangiao_user WITH PASSWORD 'bangiao_pass';
GRANT ALL PRIVILEGES ON DATABASE thuexe_bangiao_db TO bangiao_user;

-- Khóa quyền truy cập chéo: mỗi user chỉ vào được database của mình
REVOKE CONNECT ON DATABASE thuexe_xe_db FROM PUBLIC;
GRANT CONNECT ON DATABASE thuexe_xe_db TO xe_user;

REVOKE CONNECT ON DATABASE thuexe_khach_db FROM PUBLIC;
GRANT CONNECT ON DATABASE thuexe_khach_db TO khach_user;

REVOKE CONNECT ON DATABASE thuexe_hopdong_db FROM PUBLIC;
GRANT CONNECT ON DATABASE thuexe_hopdong_db TO hopdong_user;

REVOKE CONNECT ON DATABASE thuexe_bangiao_db FROM PUBLIC;
GRANT CONNECT ON DATABASE thuexe_bangiao_db TO bangiao_user;

-- CẤP QUYỀN TẠO BẢNG TRONG SCHEMA PUBLIC (BỔ SUNG)
\c thuexe_xe_db
GRANT ALL ON SCHEMA public TO xe_user;

\c thuexe_khach_db
GRANT ALL ON SCHEMA public TO khach_user;

\c thuexe_hopdong_db
GRANT ALL ON SCHEMA public TO hopdong_user;

\c thuexe_bangiao_db
GRANT ALL ON SCHEMA public TO bangiao_user;