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
    "titulo"	TEXT NOT NULL,
	"autor"	TEXT NOT NULL,
	"editora"	TEXT NOT NULL,
	"isbn"	TEXT NOT NULL UNIQUE,
	"dinheiro"	REAL NOT NULL,
	"quantidade"	INTEGER,
	PRIMARY KEY("isbn")
);

CREATE TABLE IF NOT EXISTS pedido(
    "criadorID"	TEXT NOT NULL UNIQUE,
	"clienteID"	TEXT NOT NULL UNIQUE,
	"metodoPagamento"	TEXT NOT NULL,
	"data"	TEXT NOT NULL,
	"livrosCodigo"	TEXT,
	PRIMARY KEY("clienteID","criadorID"),
	FOREIGN KEY("livrosCodigo") REFERENCES "livro"("isbn")
);
