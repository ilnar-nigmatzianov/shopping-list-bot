create table hibernate_sequence
(
    next_val bigint
) engine = InnoDB;
insert into hibernate_sequence
values (1);

create table message
(
    id          bigint not null,
    external_id bigint,
    text        longtext,
    user_id     bigint,
    primary key (id)
) engine = InnoDB;
create table shopping_list
(
    id       bigint not null,
    status   varchar(255),
    owner_id bigint,
    primary key (id)
) engine = InnoDB;
create table shopping_list_item
(
    id               bigint not null,
    status           varchar(255),
    text             varchar(255),
    shopping_list_id bigint,
    primary key (id)
) engine = InnoDB;
create table usr
(
    id          bigint not null,
    chat_id     varchar(255),
    external_id varchar(255),
    last_name   varchar(255),
    name        varchar(255),
    nick        varchar(255),
    primary key (id)
) engine = InnoDB;

alter table message
    add constraint message_user_fk foreign key (user_id) references usr (id);
alter table shopping_list
    add constraint shopping_list_user_fk foreign key (owner_id) references usr (id);
alter table shopping_list_item
    add constraint shopping_list_item_shopping_list_fk foreign key (shopping_list_id) references shopping_list (id);