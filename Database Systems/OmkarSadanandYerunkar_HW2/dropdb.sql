drop index building_idx force;
drop index hydrant_idx force;
drop index fb_building_idx force;
delete from USER_SDO_GEOM_METADATA;


drop table building cascade constraints;
drop table hydrant;
drop table firebuilding;

