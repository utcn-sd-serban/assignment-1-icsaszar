set search_path to assign1;

alter table users add is_mod boolean default false;

alter table users add is_banned boolean default false;

create table if not exists votes(
    post_id bigint references post(id)
        on UPDATE cascade
        on delete cascade,
    user_id bigint references users(id)
        on update cascade
        on delete cascade,
    vote smallint,
    primary key(post_id, user_id)
);
