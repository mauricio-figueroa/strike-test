create table users
(
    id                bigint auto_increment primary key,
    version           bigint null,
    creation_date     datetime(6) not null,
    modification_date datetime(6) not null,
    app_user_role     varchar(80) null,
    enabled           bit,
    locked            bit,
    password          varchar(255) not null,
    username          varchar(255) not null);

create index user_username_idx on users (username);

create table file
(
    id                bigint auto_increment primary key,
    version           bigint null,
    creation_date     datetime(6) not null,
    modification_date datetime(6) not null,
    name              varchar(255) not null,
    file_blob         LONGBLOB     not null,
    user_id           bigint not null,
    CONSTRAINT file_user__fk
        FOREIGN KEY (user_id) REFERENCES users (id));


CREATE TABLE file_user
(
    file_id bigint NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (file_id, user_id),
    CONSTRAINT file_user_file_fk
        FOREIGN KEY (file_id) REFERENCES file (id),
    CONSTRAINT file_user_user_fk
        FOREIGN KEY (user_id) REFERENCES users (id));