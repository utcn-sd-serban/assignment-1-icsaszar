create schema if not exists assign1;

set search_path to assign1;

create table if not exists users(
  id bigserial primary key,
  user_name varchar(63) not null unique check ( length(user_name) > 2 )
);

create table if not exists post(
  id bigserial primary key,
  post_text varchar(255) check ( length(post_text) > 0 ),
  posted timestamp default now(),
  author_id bigint not null references users (id)
    on update cascade
    on delete cascade
);

create table if not exists question(
  id bigint references post (id)
    on update cascade
    on delete cascade,
  title varchar(63) check ( length(title) > 2 ),
  primary key (id)
);

create table if not exists answer(
  id bigint references post (id)
    on update cascade
    on delete cascade,
  question_id bigint not null references question(id)
    on update cascade
    on delete cascade,
  primary key (id)
);

create table if not exists tags(
  id bigserial primary key,
  tag_name varchar(63) unique check ( length(tag_name) > 2 )
);

create table if not exists question_tag
(
  tag_id bigint references tags (id)
    on update cascade
    on delete cascade,
  question_id bigint references question (id)
    on update cascade
    on delete cascade,
  primary key (tag_id, question_id)
);