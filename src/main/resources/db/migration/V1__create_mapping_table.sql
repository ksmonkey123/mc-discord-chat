create table mapping (
    id serial,
    discord_channel_id varchar(32) not null unique,
    discord_webhook_url varchar(255) not null,
    minecraft_server_token varchar(32) not null unique,
    minecraft_server_url varchar(255) not null
);
