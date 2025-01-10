CREATE TABLE IF NOT EXISTS INVOICES(
	id SERIAL PRIMARY KEY,
	amount REAL,
	due_date varchar(20),
	paid_amount real DEFAULT 0.0,
	status varchar(30) DEFAULT 'PENDING'
);