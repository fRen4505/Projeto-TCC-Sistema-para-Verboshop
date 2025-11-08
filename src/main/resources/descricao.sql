/*
CREATE TABLE IF NOT EXISTS user(
    nome string,
    email string,
    id string,
    permissao integer
);

CREATE TABLE IF NOT EXISTS livro(
    titulo string,
    autor string,
    editora string,
    isbn string,
    dinheiro double,
    quantidade integer

);
*/

CREATE TABLE IF NOT EXISTS user(
	"nome"	TEXT NOT NULL,
	"email"	TEXT NOT NULL,
	"id"	TEXT NOT NULL UNIQUE,
	"permissao"	INTEGER NOT NULL,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS livro(
    "titulo"	TEXT NOT NULL UNIQUE,
	"autor"	TEXT NOT NULL,
	"editora"	TEXT NOT NULL,
	"isbn"	TEXT NOT NULL UNIQUE,
	"dinheiro"	REAL NOT NULL,
	"quantidade" INTEGER NOT NULL,
	PRIMARY KEY("isbn")
);

CREATE TABLE IF NOT EXISTS "pedido" (
	"criadorID"	TEXT NOT NULL,
	"clienteID"	TEXT NOT NULL,
	"metodoPagamento"	TEXT NOT NULL,
	"data"	TEXT NOT NULL,
	"dataEntrega"	TEXT NOT NULL,
	"livrosCodigo"	TEXT,
	"ID"	TEXT NOT NULL UNIQUE,
	"entregue"	INTEGER,
	PRIMARY KEY("ID"),
	FOREIGN KEY("clienteID") REFERENCES "user(id)" ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY("criadorID") REFERENCES "user(id)" ON DELETE RESTRICT ON UPDATE CASCADE
);