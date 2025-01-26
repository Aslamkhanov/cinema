create table if not exists movie(
	id serial primary key,
	"name" varchar(200),
	description varchar(500));

create table if not exists place(
	id serial primary key,
	"number" varchar(10));

create table if not exists "session"(
	id serial primary key,
	movie_id bigint,
	date_time timestamp,
	price numeric(10, 2),
	constraint fk_movie foreign key (movie_id) references movie (id));

create table if not exists ticket(
	id serial primary key,
	place_id bigint,
	session_id bigint,
	is_bought boolean not null,
	constraint fk_place foreign key (place_id) references place (id),
	constraint fk_session foreign key (session_id) references "session" (id));
