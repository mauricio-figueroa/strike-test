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
