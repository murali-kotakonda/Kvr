INSERT INTO users (username, first_name, last_name, password, fail_count, is_deleted, insert_tms, updated_tms, user_type,email_id)
VALUES ('admin', 'Admin', 'User', 'admin', 0, false, CURRENT_TIMESTAMP, NULL, 'admin','');

INSERT INTO users (username, first_name, last_name, password, fail_count, is_deleted, insert_tms, updated_tms, user_type,email_id)
VALUES ('john.doe', 'John', 'Doe', 'admin', 0, false, CURRENT_TIMESTAMP, NULL, 'user','');

INSERT INTO users (username, first_name, last_name, password, fail_count, is_deleted, insert_tms, updated_tms, user_type,email_id)
VALUES ('jane.smith', 'Jane', 'Smith', 'admin', 0, false, CURRENT_TIMESTAMP, NULL, 'client','');
