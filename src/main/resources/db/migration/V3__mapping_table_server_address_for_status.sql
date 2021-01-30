alter table mapping
rename column minecraft_server_url to minecraft_server_api_url;

alter table mapping
add column minecraft_server_address varchar(255);

alter table mapping
add column discord_channel_name varchar(30);
