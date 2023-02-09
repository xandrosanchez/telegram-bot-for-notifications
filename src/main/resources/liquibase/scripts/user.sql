-- liquibase formatted sql

-- changeset aSnegireff:1
create table notification_task
(
    id serial primary key,
    chat_id int not null,
    message_text varchar(264) not null ,
    date date not null,
    time time not null
);

-- changeset aSnegireff:2
alter table notification_task drop column date;
alter table notification_task drop column time;

-- changeset aSnegireff:3
alter table notification_task add column DateTime TIMESTAMP not null;