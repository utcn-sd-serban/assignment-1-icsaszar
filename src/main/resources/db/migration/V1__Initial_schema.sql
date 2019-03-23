create schema if not exists assign1;

set search_path to assign1;

create table if not exists users(
  id bigserial primary key,
  user_name varchar(63)
);

create table if not exists post(
  id bigserial primary key,
  post_text varchar(255),
  posted timestamp,
  author_id bigint references users (id)
    on update cascade
);

create table if not exists question(
  id bigint references post (id)
    on update cascade,
  title varchar(63),
  primary key (id)
);

create table if not exists answer(
  id bigint references post (id)
    on update cascade,
  question_id bigint references question(id)
    on update cascade,
    primary key (id)
);

create table if not exists tag(
  id bigserial primary key,
  name varchar(64)
);

create table if not exists question_tag
(
  tag_id bigint references tag (id),
  question_id bigint references question (id),
  primary key (tag_id, question_id)
);