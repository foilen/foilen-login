
    create table auth_password (
       id bigint not null auto_increment,
        expire datetime,
        hashed_password varchar(255) not null,
        single_use bit not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table login_token (
       id bigint not null auto_increment,
        expire datetime,
        login_token varchar(255) not null,
        redirect_url varchar(255),
        user_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table user (
       id bigint not null auto_increment,
        address varchar(255),
        email varchar(255) not null,
        first_name varchar(255),
        is_admin bit not null,
        lang varchar(255),
        last_name varchar(255),
        user_id varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    alter table login_token 
       add constraint UK_i8trs0h8oiwb6xt4a5f11itvx unique (login_token);

    alter table user 
       add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);

    alter table user 
       add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id);

    alter table auth_password 
       add constraint FKlqpwgvphb43obpv621d0c25g 
       foreign key (user_id) 
       references user (id);

    alter table login_token 
       add constraint FK9jmx51cxchcug7a96k7x6658k 
       foreign key (user_id) 
       references user (id);
