-- TABLES CREATION

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username varchar(50) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role varchar(20) NOT NULL DEFAULT 'User',
    name varchar(100),
    dob date,
    address varchar(200),
    email varchar(100) NOT NULL UNIQUE,
    phone varchar(15) NOT NULL UNIQUE,
    state varchar(50)
);

CREATE TABLE vehicle_category (
    category_id SERIAL PRIMARY KEY,
    category_name varchar(50) NOT NULL UNIQUE,
    description varchar(200)
);

CREATE TABLE user_log (
    log_id SERIAL PRIMARY KEY,
    user_id integer,
    username varchar(50),
    password_hash varchar(255),
    role varchar(20),
    name varchar(100),
    dob date,
    address varchar(200),
    email varchar(100),
    phone varchar(15),
    delete_date date NOT NULL,
    state varchar(50)
);

CREATE TABLE driving_license (
    license_number varchar(20) PRIMARY KEY,
    user_id integer NOT NULL UNIQUE,
    issue_date date NOT NULL,
    expiry_date date NOT NULL,
    status varchar(20) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE driving_exam_questions (
    question_id SERIAL PRIMARY KEY,
    category_id integer NOT NULL,
    question_text varchar NOT NULL,
    option_1 varchar NOT NULL,
    option_2 varchar NOT NULL,
    option_3 varchar NOT NULL,
    option_4 varchar NOT NULL,
    correct_answer integer NOT NULL,
    FOREIGN KEY(category_id) REFERENCES vehicle_category(category_id)
);

CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    payment_for varchar(20) NOT NULL,
    ref_id varchar(20) NOT NULL,
    amount numeric(10,2) NOT NULL,
    payment_date date NOT NULL,
    user_id integer NOT NULL,
    mode varchar(30),
    FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE driving_exam (
    exam_id SERIAL PRIMARY KEY,
    user_id integer NOT NULL,
    category_id integer NOT NULL,
    exam_date date NOT NULL,
    fees numeric(10,2) NOT NULL,
    scheduled_by integer NOT NULL,
    payment_status varchar(10) NOT NULL DEFAULT 'Pending',
    result varchar(20),
    FOREIGN KEY(user_id) REFERENCES users(user_id),
    FOREIGN KEY(category_id) REFERENCES vehicle_category(category_id),
    FOREIGN KEY(scheduled_by) REFERENCES users(user_id)
);

CREATE TABLE driving_license_category (
    license_number varchar(20) NOT NULL,
    category_id integer NOT NULL,
    PRIMARY KEY (license_number, category_id),
    FOREIGN KEY(license_number) REFERENCES driving_license(license_number),
    FOREIGN KEY(category_id) REFERENCES vehicle_category(category_id)
);

CREATE TABLE vehicle (
    license_plate varchar(15) PRIMARY KEY,
    user_id integer NOT NULL,
    category_id integer NOT NULL,
    engine_number varchar(50) NOT NULL UNIQUE,
    chasis_number varchar(50) NOT NULL UNIQUE,
    insurance_expiry_date date NOT NULL,
    puc_expiry_date date NOT NULL,
    rc_expiry_date date NOT NULL,
    status varchar(20) NOT NULL DEFAULT 'Active',
    vname varchar(30),
    license_number varchar(20),
    state varchar(50) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(user_id),
    FOREIGN KEY(license_number) REFERENCES driving_license(license_number),
    FOREIGN KEY(category_id) REFERENCES vehicle_category(category_id)
);

CREATE TABLE e_challan (
    challan_id SERIAL PRIMARY KEY,
    license_plate varchar(15) NOT NULL,
    issued_date date NOT NULL,
    due_date date NOT NULL,
    amount numeric(10,2) NOT NULL,
    reason varchar(255) NOT NULL,
    status varchar(20) NOT NULL DEFAULT 'Pending',
    issued_by integer,
    FOREIGN KEY(license_plate) REFERENCES vehicle(license_plate),
    FOREIGN KEY(issued_by) REFERENCES users(user_id)
);

-- Creating Procedures, functions and triggers
-- creating a trigger function and trigger that inserts user data into user_log when user is deleted
Create or replace function
	log_user_data() returns trigger
	language plpgsql as $$

	Begin
		Insert into user_log(user_id, username, password_hash, role, name, dob, address, email, phone, delete_date, state) values (OLD.user_id, OLD.username, OLD.password_hash, OLD.role, OLD.name, OLD.dob, OLD.address, OLD.email, OLD.phone, CURRENT_DATE, OLD.state);
		return OLD;
	End;
	$$

Create trigger log_user_data_trigger after delete on users for each row execute procedure log_user_data();


-- procedure to insert license into driving_license if not exists and add the category in driving_license_category
Create or replace procedure
	issue_license(license_num in varchar(20), u_id in int, c_id in int)
	language plpgsql as $$

	Declare
	tmp boolean;

	Begin
		Select NOT EXISTS(Select * from driving_license where license_number=license_num) into tmp;
		If tmp then
			Insert into driving_license values (license_num, u_id, CURRENT_DATE, get_date_after_20_years(), 'Active');
		else
			Update driving_license set issue_date=current_date, expiry_date=get_date_after_20_years(), status='Active' where license_number=license_num;
		end if;
		Insert into driving_license_category values (license_num, c_id);
	end;
	$$



-- procedure to get date after 20 years from today
Create or replace function
	get_date_after_20_years() returns date
	language plpgsql as $$

	Declare
	current_year int:=Extract(year from CURRENT_DATE);
	current_month int:=Extract(month from CURRENT_DATE);
	current_day int:=Extract(day from CURRENT_DATE);
	future_year int:=current_year+20;

	Begin
		return make_date(future_year, current_month, current_day);
	end;
	$$