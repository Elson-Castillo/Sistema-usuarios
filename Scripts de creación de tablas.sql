/*Estos scripst serian los ideales para la descripcion de proyecto, 
aunque por falta de tiempo y para cuestiones de la prueba realizada solo se ponen de ejemplo del analisis realizado, porque no fueron empleados en el pryecto
CREATE TABLE CLIENTE (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NOMBRE VARCHAR(100) NOT NULL,
    DIRECCION VARCHAR(255),
    EMAIL VARCHAR(50),
    TELEFONO VARCHAR(20)
);

CREATE TABLE AREA (
    ID INT PRIMARY KEY,
    NOMBRE VARCHAR(50) NOT NULL
);

CREATE TABLE STATUS (
    ID CHAR(1) PRIMARY KEY,
    DESCRIPCION VARCHAR(50) NOT NULL
);

CREATE TABLE USUARIO (
    LOGIN VARCHAR(20) NOT NULL PRIMARY KEY,
    PASSWORD VARCHAR(30) NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    CLIENTE INT NOT NULL,
    EMAIL VARCHAR(50),
    FECHAALTA DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FECHABAJA DATE,
    STATUS CHAR(1) NOT NULL DEFAULT 'A',
    INTENTOS FLOAT NOT NULL DEFAULT 0,
    FECHAREVOCADO DATE,
    FECHA_VIGENCIA DATE,
    NO_ACCESO INT,
    APELLIDO_PATERNO VARCHAR(50),
    APELLIDO_MATERNO VARCHAR(50),
    AREA INT,
    FECHAMODIFICACION DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CLIENTE) REFERENCES CLIENTE(ID),
    FOREIGN KEY (AREA) REFERENCES AREA(ID),
    FOREIGN KEY (STATUS) REFERENCES STATUS(ID)
);

CREATE TABLE ACCESO (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    USUARIO_ID VARCHAR(20),
    FECHA_ACCESO TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    EXITO BOOLEAN NOT NULL,
    FOREIGN KEY (USUARIO_ID) REFERENCES USUARIO(LOGIN)
);
*/
--fin de los scripts





--Inicio de los que utiliza el reamelmente el proyecto
/*Este script seria el ideal para crear la tabla del poryecto si se quisiera hacer de manera manual*/
CREATE TABLE USUARIO (
    NUM_REGISTRO INT AUTO_INCREMENT PRIMARY KEY,
    LOGIN VARCHAR(20) NOT NULL,
    PASSWORD VARCHAR(100) NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(50),
    FECHAALTA DATE NOT NULL,
    FECHABAJA DATE,
    STATUS CHAR(1) NOT NULL,
    INTENTOS FLOAT NOT NULL DEFAULT 0.0,
    FECHAREVOCADO DATE,
    FECHA_VIGENCIA DATE,
    NO_ACCESO INT,
    APELLIDO_PATERNO VARCHAR(50),
    APELLIDO_MATERNO VARCHAR(50),
    AREA INT,
    FECHAMODIFICACION DATE NOT NULL
);





-- Insertar un nuevo usuario con contraseña encriptada y otros valores obligatorios para probar el sistema por priemera ves
INSERT INTO USUARIO (
    LOGIN, 
	EMAIL, 
    FECHAALTA, 
	FECHAMODIFICACION,
	INTENTOS,
	NOMBRE, 
    PASSWORD, 
    STATUS
) VALUES (
    'Ruya', -- Reemplazar con el valor real
	'ruy@gmail.com', -- Reemplazar con el valor real
	NOW(), 
	NOW(),
    0.0,
	'Ramon juna', -- Reemplazar con el valor real
    TO_BASE64(UNHEX(SHA1('yoli12'))),
    'A'
);


select * from usuario;

delete from usuario;

update usuario set FECHA_VIGENCIA = '2024-08-23' where num_registro = 3;
commit;