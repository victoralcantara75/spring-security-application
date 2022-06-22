create table users(
	id uuid not null,
	name varchar(100) not null,
	username varchar(50) not null,
	password varchar(100) not null,
	primary key (id)
);

create table roles(
	id integer not null,
	name varchar(20) not null,
	primary key (id)
);

create table users_roles(
	user_id uuid not null,
	role_id integer not null,
	foreign key (user_id) references users(id),
	foreign key (role_id) references roles(id),
	constraint id primary key(user_id, role_id)
);