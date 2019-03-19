create schema if not exists assign1;
set search_path to assign1;

create table if not exists users(
  id serial primary key,
  name varchar(64)
);

create table if not exists post(
  id SERIAL primary key,
  post_text text,
  posted timestamp,
  id_author integer references users (id)
    on update cascade
);

create table if not exists question(
  id_post integer references post (id)
    on update cascade,
  title varchar(64),
  primary key (id_post)
);

create table if not exists answer(
  id_post integer references post (id)
    on update cascade,
  id_response_to integer references question(id_post)
    on update cascade,
    primary key (id_post)
);

create table if not exists tag(
  id serial primary key,
  name varchar(64)
);

create table if not exists question_tag
(
  tag_id integer references tag (id),
  question_id integer references question (id_post),
  primary key (tag_id, question_id)
);