-- database name: skateparks;
START TRANSACTION;
ALTER TABLE review
ALTER COLUMN visit_date TYPE varchar;
COMMIT;
ROLLBACK;

START TRANSACTION;
ALTER TABLE skater DROP COLUMN IF EXISTS email;
COMMIT;


START TRANSACTION;
DROP TABLE IF EXISTS skater;
DROP TABLE IF EXISTS park;
DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS review;

DROP SEQUENCE IF EXISTS skater_skater_id_seq;
DROP SEQUENCE IF EXISTS park_park_id_seq;
DROP SEQUENCE IF EXISTS visit_visit_id_seq;
DROP SEQUENCE IF EXISTS review_review_id_seq;

CREATE SEQUENCE park_park_id_seq
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;

CREATE TABLE park (
  park_id integer DEFAULT nextval('park_park_id_seq'::regclass) NOT NULL,
  park_name varchar(80) NOT NULL,          -- Name of the skatepark
  park_state varchar(50) NOT NULL,      -- State name(s) where skatepark is located
  park_city varchar(50) NOT NULL,      -- City name where skatepark is located
  indoor_outdoor varchar(50) NOT NULL,  --is the park indoor or outdoor
  pads varchar(50),          -- can be empty, are pads required?
  notes varchar(500),  --can be empty
  CONSTRAINT pk_park_park_id PRIMARY KEY (park_id)      -- park_id is the primary key of park
);

CREATE SEQUENCE skater_skater_id_seq
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;

CREATE TABLE skater (
  skater_id integer DEFAULT nextval('skater_skater_id_seq'::regclass) NOT NULL,
  skater_username varchar(80) NOT NULL,   -- Skater username
  password_hash varchar(200) NOT NULL,   -- Skater password hash
  CONSTRAINT pk_skater_skater_id PRIMARY KEY (skater_id)
);

CREATE SEQUENCE visit_visit_id_seq
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;

CREATE TABLE visit (
  visit_id integer DEFAULT nextval('visit_visit_id_seq'::regclass) NOT NULL,
  park_id integer NOT NULL,                       -- skatepark id
  skater_id integer NOT NULL,                       -- skater id
  visit_date date NOT NULL,
  CONSTRAINT pk_visit_visit_id PRIMARY KEY (visit_id),
  CONSTRAINT fk_visit_park_id FOREIGN KEY (park_id) REFERENCES park(park_id),
  CONSTRAINT fk_visit_skater_id FOREIGN KEY (skater_id) REFERENCES skater(skater_id) 
);

CREATE SEQUENCE review_review_id_seq
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;

CREATE TABLE review (
  review_id integer DEFAULT nextval('review_review_id_seq'::regclass) NOT NULL,
  review varchar(1000) NOT NULL,                -- review, limit 1000 characters, may need to change
  review_score integer NOT NULL,                        --review score
  park_id integer NOT NULL,                       -- skatepark id
  skater_id integer NOT NULL,                       -- skater id
  visit_date date NOT NULL,
  CONSTRAINT pk_review_review_id PRIMARY KEY (review_id),
  CONSTRAINT fk_review_park_id FOREIGN KEY (park_id) REFERENCES park(park_id),
  CONSTRAINT fk_review_skater_id FOREIGN KEY (skater_id) REFERENCES skater(skater_id)
);

INSERT INTO park(park_name, park_state, park_city, indoor_outdoor, pads, notes)
VALUES ('Lakewood Skatepark', 'Ohio', 'Lakewood', 'Outdoor', 'No', 'Concrete park with heavy street elements. Limited transition.');

ROLLBACK;
COMMIT;