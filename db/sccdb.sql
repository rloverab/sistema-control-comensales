/*
 * Script de creación de la base de datos para el
 * Sistema de Control de Comensales de la 
 * Escuela Latinoamericana de Medicina Dr. Salvador Allende.
 * 
 * Autor: Roger Lovera
 * 
 * Creado: 09/05/2022
 * Actualizado: 07/12/2022
 */ 

# Borrar base de datos "sccdb" (sólo si existe)
/*
 * NOTA: Esta sentencia borra la base de datos existente
 * con todo y registros. Solo para fines de pruebas y desarrollo.
 * 
 * Usar con precaución.
 * 
 * Está sentencia está comentada por defecto(#)
 */
DROP DATABASE IF EXISTS sccdb;

# Crear base de datos "sccdb"
CREATE SCHEMA IF NOT EXISTS sccdb;

# Seleccionar base de datos "sccdb"
USE sccdb;

# Crear el usuario de acceso de la base de datos
CREATE USER IF NOT EXISTS 'sccusuario'@'%';
CREATE USER IF NOT EXISTS 'sccusuario'@'localhost';
ALTER USER 'sccusuario'@'%'
IDENTIFIED BY '	' ;
ALTER USER 'sccusuario'@'localhost'
IDENTIFIED BY 'cambiame' ;

# Establecer permisos para usuario creado
GRANT Execute ON sccdb.* TO 'sccusuario'@'%';
GRANT Execute ON sccdb.* TO 'sccusuario'@'localhost';

/*
 * Tablas de dimensiones
 */

# Crear tabla "sexos"
CREATE TABLE IF NOT EXISTS sexos (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sexo VARCHAR(9) NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX (sexo)
) ENGINE=InnoDB;

# Crear tabla "comidas"
CREATE TABLE IF NOT EXISTS comidas (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  comida VARCHAR(9) NOT NULL,
  inicio TIME NOT NULL,
  fin TIME NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX (comida)
) ENGINE=InnoDB;

# Crear tabla "tipos_usuario"
CREATE TABLE IF NOT EXISTS tipos_usuario (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  tipo_usuario VARCHAR(15) NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX (tipo_usuario)
) ENGINE=InnoDB;

# Crear tabla "continentes"
CREATE TABLE IF NOT EXISTS continentes (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  continente VARCHAR(15) NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX (continente)
) ENGINE=InnoDB;

# Crear tabla "paises"
CREATE TABLE IF NOT EXISTS paises (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  pais VARCHAR(45) NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX (pais)
) ENGINE=InnoDB;

/*
 * Tablas de hechos
 */

# Crear tabla "paises_continentes"
CREATE TABLE IF NOT EXISTS paises_continentes (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  pais_id INT UNSIGNED NOT NULL,
  continente_id INT UNSIGNED NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (pais_id) REFERENCES paises (id),
  FOREIGN KEY (continente_id) REFERENCES continentes (id)
) ENGINE=InnoDB;

# Crear tabla "personas"
CREATE TABLE IF NOT EXISTS personas (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  documento_identidad VARCHAR(15) NOT NULL,
  nombre1 VARCHAR(25) NOT NULL,
  nombre2 VARCHAR(25) NULL DEFAULT NULL,
  apellido1 VARCHAR(25) NOT NULL,
  apellido2 VARCHAR(25) NULL DEFAULT NULL,
  sexo_id INT UNSIGNED NOT NULL,
  fecha_nacimiento DATE NOT NULL,  
  telefono VARCHAR(16) NULL DEFAULT NULL,
  correo_electronico VARCHAR(320) NULL DEFAULT NULL,
  pais_continente_id INT UNSIGNED DEFAULT 1,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX (documento_identidad),
  FOREIGN KEY (sexo_id) REFERENCES sexos (id),
  FOREIGN KEY (pais_continente_id) REFERENCES paises_continentes (id)
) ENGINE=InnoDB;

# Crear tabla "usuarios"
CREATE TABLE IF NOT EXISTS usuarios (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  persona_id INT UNSIGNED NOT NULL,
  tipo_usuario_id INT UNSIGNED NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (persona_id) REFERENCES personas (id),
  FOREIGN KEY (tipo_usuario_id) REFERENCES tipos_usuario (id)
) ENGINE=InnoDB;

# Crear tabla "servicios"
CREATE TABLE IF NOT EXISTS servicios (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  usuario_id INT UNSIGNED NOT NULL,
  comida_id INT UNSIGNED NOT NULL,
  fecha_hora_atencion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  actualizado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
  FOREIGN KEY (comida_id) REFERENCES comidas (id)
) ENGINE=InnoDB;

/*
 * Vistas
 */

# Crear vista "view_usuarios_registrados"
CREATE OR REPLACE VIEW view_usuarios_registrados AS
SELECT		usuarios.id AS usuario_id,
			personas.documento_identidad,
			personas.nombre1,
			personas.nombre2,
			personas.apellido1,
			personas.apellido2,
			personas.sexo_id,
			sexos.sexo,
			TIMESTAMPDIFF(YEAR, personas.fecha_nacimiento, NOW()) AS edad,
			tipos_usuario.id AS tipo_usuario_id,
			tipos_usuario.tipo_usuario,
			usuarios.activo
FROM 		usuarios,
			personas,
			tipos_usuario,
			sexos
WHERE		usuarios.persona_id = personas.id
AND			usuarios.tipo_usuario_id = tipos_usuario.id
AND			personas.sexo_id = sexos.id;

# Crear vista "view_usuarios_atendidos"
CREATE OR REPLACE VIEW view_usuarios_atendidos AS
SELECT		usuarios.id AS usuario_id,
			personas.documento_identidad,
			personas.nombre1,
			personas.nombre2,
			personas.apellido1,
			personas.apellido2,
			personas.sexo_id,
			sexos.sexo,
			TIMESTAMPDIFF(YEAR, personas.fecha_nacimiento, NOW()) AS edad,
			tipos_usuario.id AS tipo_usuario_id,
			tipos_usuario.tipo_usuario,
			usuarios.activo,
			comidas.id AS comida_id,
			comidas.comida,
			servicios.fecha_hora_atencion
FROM 		usuarios,
			personas,
			tipos_usuario,
			sexos,
			servicios,
			comidas
WHERE		usuarios.persona_id = personas.id
AND			usuarios.tipo_usuario_id = tipos_usuario.id
AND			personas.sexo_id = sexos.id
AND			servicios.usuario_id = usuarios.id
AND			servicios.comida_id = comidas.id;

/*
 * Procedimientos
 */

# Crear procedimiento "insert_sexo"
DROP PROCEDURE IF EXISTS insert_sexo;
DELIMITER $$
CREATE PROCEDURE insert_sexo(
					sexo VARCHAR(9))
BEGIN
	DECLARE sexo_id INT UNSIGNED DEFAULT NULL;

	REPEAT
		SELECT 	sexos.id INTO sexo_id
		FROM	sexos
		WHERE	sexos.sexo = sexo
		LIMIT	1;
	
		IF sexo_id IS NULL THEN
			INSERT INTO sexos(sexo)
			VALUES(sexo);
		END IF;		
	UNTIL sexo_id IS NOT NULL
	END REPEAT;
END$$
DELIMITER ;

# Crear procedimiento "insert_tipo_usuario"
DROP PROCEDURE IF EXISTS insert_tipo_usuario;
DELIMITER $$
$$
CREATE PROCEDURE insert_tipo_usuario(
					tipo_usuario VARCHAR(15))
BEGIN
	DECLARE tipo_usuario_id INT UNSIGNED DEFAULT NULL;

	REPEAT
		SELECT 	tipos_usuario.id INTO tipo_usuario_id
		FROM	tipos_usuario
		WHERE	tipos_usuario.tipo_usuario = tipo_usuario
		LIMIT	1;
	
		IF tipo_usuario_id IS NULL THEN
			INSERT INTO tipos_usuario(tipo_usuario)
			VALUES(tipo_usuario);
		END IF;		
	UNTIL tipo_usuario_id IS NOT NULL
	END REPEAT;
END$$
DELIMITER ;

# Crear procedimiento "insert_comida"
DROP PROCEDURE IF EXISTS insert_comida;
DELIMITER $$
$$
CREATE PROCEDURE insert_comida(
					comida VARCHAR(9),
					inicio TIME,
					fin TIME)
BEGIN
	DECLARE comida_id INT UNSIGNED DEFAULT NULL;	

	REPEAT
		SELECT 	comidas.id INTO comida_id
		FROM	comidas
		WHERE	comidas.comida = comida
		LIMIT	1;
	
		IF comida_id IS NULL THEN
			INSERT INTO comidas(comida, inicio, fin)
			VALUES(comida, inicio, fin);
		END IF;		
	UNTIL comida_id IS NOT NULL
	END REPEAT;
END$$
DELIMITER ;

# Crear procedimiento "update_comida"
DROP PROCEDURE IF EXISTS update_comida;
DELIMITER $$
CREATE PROCEDURE update_comida(
					comida_id INT UNSIGNED,
					inicio TIME,
					fin TIME)
BEGIN
	UPDATE 	comidas
	SET		comidas.inicio = inicio,
			comidas.fin = fin
	WHERE	comidas.id = comida_id
	LIMIT	1;
END$$
DELIMITER ;

# Crear procedimiento "insert_persona"
DROP PROCEDURE IF EXISTS insert_persona;
DELIMITER $$
CREATE PROCEDURE insert_persona (
                    documento_identidad VARCHAR(15), 
                    nombre1 VARCHAR(25),
                    nombre2 VARCHAR(25),
                    apellido1 VARCHAR(25),
                    apellido2 VARCHAR(25),
                    sexo_id INT UNSIGNED,
                    fecha_nacimiento DATE,
                    pais VARCHAR(45),
                    telefono VARCHAR(16),                    
                    correo_electronico VARCHAR(320))
BEGIN
    DECLARE persona_id INT UNSIGNED DEFAULT NULL;
    DECLARE pais_continente_id INT UNSIGNED DEFAULT NULL;
   
   	SELECT 	paises_continentes.id INTO pais_continente_id
	FROM	paises_continentes,
			paises
	WHERE	paises_continentes.pais_id = paises.id
	AND		paises.pais = TRIM(pais)
	LIMIT	1;

	IF pais_continente_id IS NULL THEN
		SET pais_continente_id = 1; 
	END IF;

    REPEAT
        SET persona_id = get_persona_id(TRIM(documento_identidad));
    
        IF persona_id IS NULL THEN
            INSERT INTO personas (
                            documento_identidad, 
                            nombre1, 
                            nombre2, 
                            apellido1, 
                            apellido2, 
                            sexo_id, 
                            fecha_nacimiento,
                            telefono,
                            correo_electronico,
                            pais_continente_id)
            VALUES (
                            TRIM(documento_identidad), 
                            TRIM(nombre1), 
                            TRIM(nombre2), 
                            TRIM(apellido1), 
                            TRIM(apellido2), 
                            sexo_id, 
                            fecha_nacimiento,
                            telefono,
                            correo_electronico,
                            pais_continente_id);				  
        END IF;
    UNTIL persona_id IS NOT NULL
    END REPEAT;
END$$
DELIMITER ;

# Crear procedimiento "update_persona"
DROP PROCEDURE IF EXISTS update_persona;
DELIMITER $$
CREATE PROCEDURE update_persona (
					id INT UNSIGNED,
                    documento_identidad VARCHAR(15), 
                    nombre1 VARCHAR(25),
                    nombre2 VARCHAR(25),
                    apellido1 VARCHAR(25),
                    apellido2 VARCHAR(25),
                    sexo_id INT UNSIGNED,
                    fecha_nacimiento DATE,
                    pais VARCHAR(45),
                    telefono VARCHAR(16),
                    correo_electronico VARCHAR(320))
BEGIN
	DECLARE pais_continente_id INT UNSIGNED DEFAULT NULL;
   
   	SELECT 	paises_continentes.id INTO pais_continente_id
	FROM	paises_continentes,
			paises
	WHERE	paises_continentes.pais_id = paises.id
	AND		paises.pais = TRIM(pais)
	LIMIT	1;

	IF pais_continente_id IS NULL THEN
		SET pais_continente_id = 1; 
	END IF;
	
	UPDATE 	personas
	SET		personas.documento_identidad = documento_identidad,
			personas.nombre1 = nombre1,
			personas.nombre2 = nombre2,
			personas.apellido1 = apellido1,
			personas.apellido2 = apellido2,
			personas.sexo_id = sexo_id,
			personas.fecha_nacimiento = fecha_nacimiento,
			personas.pais_continente_id = pais_continente_id,
			personas.telefono = telefono,
			personas.correo_electronico = correo_electronico
	WHERE	personas.id = id
	LIMIT	1;
END$$
DELIMITER ;

# Crear procedimiento "insert_usuario"
DROP PROCEDURE IF EXISTS insert_usuario;
DELIMITER $$
CREATE PROCEDURE insert_usuario(
					persona_id INT UNSIGNED,
					tipo_usuario_id INT UNSIGNED,
					isActivo BOOLEAN)
BEGIN
	DECLARE usuario_id INT UNSIGNED DEFAULT NULL;		

	REPEAT
		SELECT 	usuarios.id INTO usuario_id
		FROM	usuarios
		WHERE	usuarios.persona_id  = persona_id 
		LIMIT	1;
	
		IF usuario_id IS NULL THEN
			INSERT INTO usuarios(persona_id, tipo_usuario_id, activo)
			VALUES(persona_id, tipo_usuario_id, isActivo);
		END IF;		
	UNTIL usuario_id IS NOT NULL
	END REPEAT;
END$$
DELIMITER ;

# Crear procedimiento "insert_usuario_with_all_data"
DROP PROCEDURE IF EXISTS insert_usuario_with_all_data;
DELIMITER $$
CREATE PROCEDURE insert_usuario_with_all_data(
					documento_identidad VARCHAR(15), 
                    nombre1 VARCHAR(25),
                    nombre2 VARCHAR(25),
                    apellido1 VARCHAR(25),
                    apellido2 VARCHAR(25),
                    sexo VARCHAR(9),
                    fecha_nacimiento DATE,
                    telefono VARCHAR(16),
                    correo_electronico VARCHAR(320),
                    tipo_usuario VARCHAR(15),
                    pais VARCHAR(45),
                    isActivo BOOLEAN)
BEGIN
	DECLARE persona_id INT UNSIGNED DEFAULT NULL;
	DECLARE sexo_id INT UNSIGNED DEFAULT NULL;
	DECLARE tipo_usuario_id INT UNSIGNED DEFAULT NULL;
	DECLARE usuario_id INT UNSIGNED DEFAULT NULL;

	SET sexo_id = get_sexo_id(sexo);
	SET tipo_usuario_id = get_tipo_usuario_id(tipo_usuario);
	
	IF ((sexo_id IS NOT NULL) AND (tipo_usuario_id IS NOT NULL)) THEN
		REPEAT
			SET persona_id = get_persona_id(documento_identidad);
		
			IF persona_id IS NULL THEN
				CALL insert_persona(
					documento_identidad,
					nombre1,
					nombre2,
					apellido1,
					apellido2,
					sexo_id,
					fecha_nacimiento,
					pais,
					telefono,
					correo_electronico);		
			END IF;
		UNTIL persona_id IS NOT NULL
		END REPEAT;
	
		SET usuario_id = get_usuario_id(persona_id, NULL);
	
		IF usuario_id IS NOT NULL THEN
			CALL update_usuario(
				usuario_id, 
				tipo_usuario_id, 
				isActivo);
		ELSE
			CALL insert_usuario(
				persona_id, 
				tipo_usuario_id, 
				isActivo); 
		END IF;
	END IF;			
END$$
DELIMITER ;

# Crear procedimiento "update_usuario"
DROP PROCEDURE IF EXISTS update_usuario;
DELIMITER $$
CREATE PROCEDURE update_usuario(
					usuario_id INT UNSIGNED,
					tipo_usuario_id INT UNSIGNED,
					isActivo BOOLEAN)
BEGIN
	UPDATE 	usuarios 
	SET		usuarios.tipo_usuario_id = tipo_usuario_id,
			usuarios.activo = isActivo
	WHERE	usuarios.id = usuario_id
	LIMIT	1;
END$$
DELIMITER ;

# Crear procedimiento "insert_servicio"
DROP PROCEDURE IF EXISTS insert_servicio;
DELIMITER $$
CREATE PROCEDURE insert_servicio(
					documento_identidad VARCHAR(15),
					comida_id INT UNSIGNED)
BEGIN
	DECLARE usuario_id INT UNSIGNED DEFAULT NULL;
	DECLARE servicio_id INT UNSIGNED DEFAULT NULL;
	DECLARE ahora DATETIME DEFAULT CURRENT_TIMESTAMP();
	DECLARE aux INT DEFAULT 0;
	DECLARE status INT DEFAULT 0;
	# 0: Ok.
	# 1: Usuario no existe (o inactivo)
	# 2: Usuario atendido previamente

	SET usuario_id = get_usuario_id(get_persona_id(documento_identidad),TRUE);

	# Verificar que exista el usuario
	IF usuario_id IS NULL THEN
		SET status = 1; # Usuario no existe
	ELSE
		SELECT 	COUNT(*) INTO aux
		FROM	servicios
		WHERE	servicios.usuario_id = usuario_id 
		AND 	servicios.comida_id = comida_id
		AND 	DATE(servicios.fecha_hora_atencion) = DATE(ahora);		
	
		IF aux > 0 THEN
			SET status = 2;
		END IF;
	END IF;

	IF status = 0 THEN
		REPEAT
			SELECT 	servicios.id INTO servicio_id
			FROM	servicios
			WHERE	servicios.usuario_id = usuario_id 
			AND		servicios.comida_id = comida_id
			AND 	DATE(servicios.fecha_hora_atencion) = DATE(ahora)
			LIMIT	1;
		
			IF servicio_id IS NULL THEN
				INSERT INTO servicios(usuario_id, comida_id, fecha_hora_atencion)
				VALUES(usuario_id, comida_id, ahora);
			END IF;		
		UNTIL servicio_id IS NOT NULL
		END REPEAT;
	END IF;
	
	SELECT status;
END$$
DELIMITER ;

# Crear procedimiento "select_comida"
DROP PROCEDURE IF EXISTS select_comida;
DELIMITER $$
$$
CREATE PROCEDURE select_comida()
BEGIN
	DECLARE ahora TIME DEFAULT CURRENT_TIME();

	SELECT	id,
			comida,
			inicio,
			fin
	FROM	comidas
	WHERE	ahora BETWEEN inicio AND fin
	LIMIT 	1;
END$$
DELIMITER ;

# Crear procedimiento "select_persona"
DROP PROCEDURE IF EXISTS select_persona;
DELIMITER $$
$$
CREATE PROCEDURE select_persona(documento_identidad VARCHAR(15))
BEGIN
	SELECT	personas.id AS persona_id,
			personas.documento_identidad,
			personas.nombre1,
			personas.nombre2,
			personas.apellido1,
			personas.apellido2,
			sexos.id AS sexo_id,
			sexos.sexo,
			personas.fecha_nacimiento,
			personas.telefono,
			personas.correo_electronico
	FROM	personas,
			sexos
	WHERE	personas.documento_identidad = TRIM(documento_identidad)
	AND		personas.sexo_id = sexos.id
	LIMIT	1;
END$$
DELIMITER ;

# Crear procedimiento "select_usuario"
DROP PROCEDURE IF EXISTS select_usuario;
DELIMITER $$
$$
CREATE PROCEDURE select_usuario(documento_identidad VARCHAR(15))
BEGIN
	SELECT	usuarios.id AS usuario_id,
			personas.id AS persona_id,
			personas.documento_identidad,
			personas.nombre1,
			personas.nombre2,
			personas.apellido1,
			personas.apellido2,
			sexos.id AS sexo_id,
			sexos.sexo,
			personas.fecha_nacimiento,
			paises.id AS pais_id,
			paises.pais,
			personas.telefono,
			personas.correo_electronico,
			tipos_usuario.id AS tipos_usuario_id,
			tipos_usuario.tipo_usuario,
			usuarios.activo
	FROM	usuarios,
			personas,
			sexos,
			tipos_usuario,
			paises_continentes,
			paises
	WHERE	personas.documento_identidad = documento_identidad
	AND		usuarios.persona_id = personas.id
	AND		usuarios.tipo_usuario_id = tipos_usuario.id
	AND		personas.sexo_id = sexos.id
	AND		personas.pais_continente_id = paises_continentes.id 
	AND		paises_continentes.pais_id = paises.id 
	LIMIT 	1;
END$$
DELIMITER ;

# Crear procedimiento "select_usuarios_atendidos"
DROP PROCEDURE IF EXISTS select_usuarios_atendidos;
DELIMITER $$
CREATE PROCEDURE select_usuarios_atendidos(
					documento_identidad VARCHAR(15),
					nombres VARCHAR(31),
					apellidos VARCHAR(31),
					tipo_usuario_id INT UNSIGNED,
					comida_id INT UNSIGNED,
					fecha_inicial TIMESTAMP,
					fecha_final TIMESTAMP,
					limite INTEGER UNSIGNED)
BEGIN
	DECLARE _limite INTEGER DEFAULT 0;
	
	CREATE OR REPLACE TEMPORARY TABLE tmp_usuarios_atendidos
	SELECT		usuarios.id AS usuario_id,
				personas.documento_identidad,
				personas.nombre1,
				personas.nombre2,
				personas.apellido1,
				personas.apellido2,
				sexos.sexo,
				tipos_usuario.id AS tipo_usuario_id,
				tipos_usuario.tipo_usuario,
				paises.id AS pais_id,
				paises.pais,
				comidas.id AS comida_id,
				comidas.comida,
				comidas.inicio,
				comidas.fin,
				servicios.fecha_hora_atencion
	FROM		usuarios,
				personas,
				tipos_usuario,
				servicios,
				comidas,
				sexos,
				paises_continentes,
				paises
	WHERE		usuarios.persona_id = personas.id
	AND			usuarios.tipo_usuario_id = tipos_usuario.id
	AND			servicios.usuario_id = usuarios.id
	AND			servicios.comida_id = comidas.id
	AND			personas.sexo_id = sexos.id
	AND			personas.pais_continente_id = paises_continentes.id
	AND			paises_continentes.pais_id = paises.id 
	AND			CASE WHEN documento_identidad IS NOT NULL THEN
					personas.documento_identidad LIKE CONCAT('%',documento_identidad,'%')
				ELSE 
					personas.documento_identidad IS NOT NULL
				END
	AND			CASE WHEN nombres IS NOT NULL THEN
					CONCAT(personas.nombre1,' ',personas.nombre2) LIKE CONCAT('%',nombres,'%')
				ELSE
					personas.nombre1 IS NOT NULL
				END
	AND			CASE WHEN apellidos IS NOT NULL THEN
					CONCAT(personas.apellido1,' ',personas.apellido2) LIKE CONCAT('%',apellidos,'%')
				ELSE
					personas.apellido1 IS NOT NULL
				END
	AND			CASE WHEN tipo_usuario_id > 0 THEN
					usuarios.tipo_usuario_id = tipo_usuario_id
				ELSE
					usuarios.tipo_usuario_id IS NOT NULL
				END
	AND			CASE WHEN comida_id > 0 THEN
					servicios.comida_id = comida_id
				ELSE
					servicios.comida_id IS NOT NULL
				END
	AND			CASE WHEN fecha_inicial IS NOT NULL THEN
					servicios.fecha_hora_atencion >= fecha_inicial
				ELSE
					servicios.fecha_hora_atencion IS NOT NULL
				END
	AND			CASE WHEN fecha_final IS NOT NULL THEN
					servicios.fecha_hora_atencion <= fecha_final
				ELSE
					servicios.fecha_hora_atencion IS NOT NULL
				END
	ORDER BY	servicios.fecha_hora_atencion ASC;
				/*
				comidas.id ASC,
				servicios.fecha_hora_atencion ASC;
				*/
			
	IF limite > 0 THEN
		SET _limite = limite;
	ELSE
		SELECT 	COUNT(*) INTO _limite
		FROM	tmp_usuarios_atendidos;			
	END IF;

	SELECT		*
	FROM		tmp_usuarios_atendidos
	LIMIT		_limite;
			
	DROP TEMPORARY TABLE IF EXISTS tmp_usuarios_atendidos;
END$$
DELIMITER ;

# Crear procedimiento "select_tipos_usuario"
DROP PROCEDURE IF EXISTS select_tipos_usuario;
DELIMITER $$
$$
CREATE PROCEDURE select_tipos_usuario()
BEGIN
	SELECT		*
	FROM		tipos_usuario;
END$$
DELIMITER ;

# Crear procedimiento "select_sexos"
DROP PROCEDURE IF EXISTS select_sexos;
DELIMITER $$
$$
CREATE PROCEDURE select_sexos()
BEGIN
	SELECT		*
	FROM		sexos;
END$$
DELIMITER ;

# Crear procedimiento "select_comidas"
DROP PROCEDURE IF EXISTS select_comidas;
DELIMITER $$
$$
CREATE PROCEDURE select_comidas()
BEGIN
	SELECT		*
	FROM		comidas;
END$$
DELIMITER ;

# Crear procedimiento "select_paises"
DROP PROCEDURE IF EXISTS select_paises;
DELIMITER $$
$$
CREATE PROCEDURE select_paises()
BEGIN
	(SELECT		*
	FROM		paises
	WHERE		id = 1)
	UNION 
	(SELECT		*
	FROM		paises
	WHERE		id > 1
	ORDER BY	pais ASC);
END$$
DELIMITER ;

# Crear procedimiento "select_current_timestamp"
DROP PROCEDURE IF EXISTS select_current_timestamp;
DELIMITER $$
$$
CREATE PROCEDURE select_current_timestamp()
BEGIN
	DECLARE cur_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP();

	SELECT cur_timestamp;
END$$
DELIMITER ;

# Crear procedimiento "select_count_tipos_usuario"
DROP PROCEDURE IF EXISTS select_count_tipos_usuario;
DELIMITER $$
$$
CREATE PROCEDURE select_count_tipos_usuario(
					documento_identidad VARCHAR(15),
					nombres VARCHAR(31),
					apellidos VARCHAR(31),
					tipo_usuario_id INT UNSIGNED,
					comida_id INT UNSIGNED,
					fecha_inicial TIMESTAMP,
					fecha_final TIMESTAMP,
					solo_tipo_usuario BOOLEAN)
BEGIN
	DECLARE total_tipo_usuario INTEGER DEFAULT 0;
	
	DROP TEMPORARY TABLE IF EXISTS tmp_tipos_usuario;
	DROP TEMPORARY TABLE IF EXISTS tmp_count_comidas;
	DROP TEMPORARY TABLE IF EXISTS tmp_count_tipos_usuario;
	
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_tipos_usuario
	SELECT		comidas.id AS comida_id,
				comidas.comida,
				tipos_usuario.id AS tipo_usuario_id,
				tipos_usuario.tipo_usuario				
	FROM		usuarios,
				personas,
				tipos_usuario,
				servicios,
				comidas,
				sexos
	WHERE		usuarios.persona_id = personas.id
	AND			usuarios.tipo_usuario_id = tipos_usuario.id
	AND			servicios.usuario_id = usuarios.id
	AND			servicios.comida_id = comidas.id
	AND			personas.sexo_id = sexos.id
	AND			CASE WHEN documento_identidad IS NOT NULL THEN
					personas.documento_identidad LIKE CONCAT('%',documento_identidad,'%')
				ELSE 
					personas.documento_identidad IS NOT NULL
				END
	AND			CASE WHEN nombres IS NOT NULL THEN
					CONCAT(personas.nombre1,' ',personas.nombre2) LIKE CONCAT('%',nombres,'%')
				ELSE
					personas.nombre1 IS NOT NULL
				END
	AND			CASE WHEN apellidos IS NOT NULL THEN
					CONCAT(personas.apellido1,' ',personas.apellido2) LIKE CONCAT('%',apellidos,'%')
				ELSE
					personas.apellido1 IS NOT NULL
				END
	AND			CASE WHEN tipo_usuario_id > 0 THEN
					usuarios.tipo_usuario_id = tipo_usuario_id
				ELSE
					usuarios.tipo_usuario_id IS NOT NULL
				END
	AND			CASE WHEN comida_id > 0 THEN
					servicios.comida_id = comida_id
				ELSE
					servicios.comida_id IS NOT NULL
				END
	AND			CASE WHEN fecha_inicial IS NOT NULL THEN
					servicios.fecha_hora_atencion >= fecha_inicial
				ELSE
					servicios.fecha_hora_atencion IS NOT NULL
				END
	AND			CASE WHEN fecha_final IS NOT NULL THEN
					servicios.fecha_hora_atencion <= fecha_final
				ELSE
					servicios.fecha_hora_atencion IS NOT NULL
				END;
			
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_count_comidas
	SELECT		tmp_tipos_usuario.comida_id,
				tmp_tipos_usuario.comida,
				COUNT(*) AS comida_count
	FROM		tmp_tipos_usuario
	GROUP BY	tmp_tipos_usuario.comida;

	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_count_tipos_usuario
	SELECT		tmp_tipos_usuario.comida_id,
				tmp_tipos_usuario.comida,
				tmp_tipos_usuario.tipo_usuario_id,
				tmp_tipos_usuario.tipo_usuario,
				COUNT(*) AS tipo_usuario_count
	FROM		tmp_tipos_usuario
	GROUP BY	tmp_tipos_usuario.comida,
				tmp_tipos_usuario.tipo_usuario;
			
	IF solo_tipo_usuario THEN
		SELECT		SUM(tipo_usuario_count) INTO total_tipo_usuario
		FROM		tmp_count_tipos_usuario;
	
		SELECT		tmp_count_tipos_usuario.tipo_usuario,
					SUM(tmp_count_tipos_usuario.tipo_usuario_count) AS tipo_usuario_count,
					SUM(tmp_count_tipos_usuario.tipo_usuario_count)/total_tipo_usuario AS tipo_usuario_percentage
		FROM		tmp_count_tipos_usuario,
					tmp_count_comidas
		WHERE		tmp_count_tipos_usuario.comida_id = tmp_count_comidas.comida_id
		GROUP BY	tmp_count_tipos_usuario.tipo_usuario;
	ELSE
		
		SELECT		tmp_count_comidas.comida_id,
					tmp_count_comidas.comida,
					tmp_count_comidas.comida_count,
					tmp_count_tipos_usuario.tipo_usuario_id,
					tmp_count_tipos_usuario.tipo_usuario,
					tmp_count_tipos_usuario.tipo_usuario_count,
					tmp_count_tipos_usuario.tipo_usuario_count/tmp_count_comidas.comida_count AS tipo_usuario_percentage
		FROM		tmp_count_tipos_usuario,
					tmp_count_comidas
		WHERE		tmp_count_tipos_usuario.comida_id = tmp_count_comidas.comida_id
		ORDER BY	tmp_count_comidas.comida_id ASC,
					tmp_count_tipos_usuario.tipo_usuario_id ASC;
	END IF;

		
END$$
DELIMITER ;

# Crear procedimiento "select_count_comidas"
DROP PROCEDURE IF EXISTS select_count_comidas;
DELIMITER $$
$$
CREATE PROCEDURE select_count_comidas(
					documento_identidad VARCHAR(15),
					nombres VARCHAR(31),
					apellidos VARCHAR(31),
					tipo_usuario_id INT UNSIGNED,
					comida_id INT UNSIGNED,
					fecha_inicial TIMESTAMP,
					fecha_final TIMESTAMP)
BEGIN
	DECLARE total_usuarios_atendidos INTEGER DEFAULT 0;
		
	CREATE OR REPLACE TEMPORARY TABLE tmp_usuarios_atendidos
	SELECT		view_usuarios_atendidos.comida_id,
				view_usuarios_atendidos.comida,
				COUNT(*) AS usuarios_atendidos
	FROM 		view_usuarios_atendidos
	WHERE		CASE WHEN documento_identidad IS NOT NULL THEN
					view_usuarios_atendidos.documento_identidad LIKE CONCAT('%',documento_identidad,'%')
				ELSE 
					view_usuarios_atendidos.documento_identidad IS NOT NULL
				END
	AND			CASE WHEN nombres IS NOT NULL THEN
					CONCAT(view_usuarios_atendidos.nombre1,' ',view_usuarios_atendidos.nombre2) LIKE CONCAT('%',nombres,'%')
				ELSE
					view_usuarios_atendidos.nombre1 IS NOT NULL
				END
	AND			CASE WHEN apellidos IS NOT NULL THEN
					CONCAT(view_usuarios_atendidos.apellido1,' ',view_usuarios_atendidos.apellido2) LIKE CONCAT('%',apellidos,'%')
				ELSE
					view_usuarios_atendidos.apellido1 IS NOT NULL
				END
	AND			CASE WHEN tipo_usuario_id > 0 THEN
					view_usuarios_atendidos.tipo_usuario_id = tipo_usuario_id
				ELSE
					view_usuarios_atendidos.tipo_usuario_id IS NOT NULL
				END
	AND			CASE WHEN comida_id > 0 THEN
					view_usuarios_atendidos.comida_id = comida_id
				ELSE
					view_usuarios_atendidos.comida_id IS NOT NULL
				END
	AND			CASE WHEN fecha_inicial IS NOT NULL THEN
					view_usuarios_atendidos.fecha_hora_atencion >= fecha_inicial
				ELSE
					view_usuarios_atendidos.fecha_hora_atencion IS NOT NULL
				END
	AND			CASE WHEN fecha_final IS NOT NULL THEN
					view_usuarios_atendidos.fecha_hora_atencion <= fecha_final
				ELSE
					view_usuarios_atendidos.fecha_hora_atencion IS NOT NULL
				END
	GROUP BY 	view_usuarios_atendidos.comida_id;

	SELECT	SUM(tmp_usuarios_atendidos.usuarios_atendidos) INTO total_usuarios_atendidos
	FROM	tmp_usuarios_atendidos;

	SELECT	tmp_usuarios_atendidos.comida_id,
			tmp_usuarios_atendidos.comida,
			tmp_usuarios_atendidos.usuarios_atendidos,
			tmp_usuarios_atendidos.usuarios_atendidos/total_usuarios_atendidos AS usuarios_atendidos_percentage
	FROM	tmp_usuarios_atendidos;

	DROP TEMPORARY TABLE IF EXISTS tmp_usuarios_atendidos;
END$$
DELIMITER ;

# Crear procedimiento "select_count_poblacion"
DROP PROCEDURE IF EXISTS select_count_poblacion;
DELIMITER $$
$$
CREATE PROCEDURE select_count_poblacion(
					fecha_inicial TIMESTAMP,
					fecha_final TIMESTAMP,
					isTotal BOOLEAN)
BEGIN
	DECLARE total_poblacion INTEGER DEFAULT 0;

	CREATE OR REPLACE TEMPORARY TABLE tmp_poblacion
	SELECT		*
	FROM		view_usuarios_registrados
	WHERE		view_usuarios_registrados.activo = TRUE
	OR			(
				view_usuarios_registrados.activo = FALSE AND
				view_usuarios_registrados.usuario_id IN (
				 	SELECT	view_usuarios_atendidos.usuario_id
				 	FROM	view_usuarios_atendidos
				 	WHERE	CASE WHEN fecha_inicial IS NOT NULL THEN 
								view_usuarios_atendidos.fecha_hora_atencion >= fecha_inicial
							ELSE 
								view_usuarios_atendidos.fecha_hora_atencion IS NOT NULL
							END
					AND		CASE WHEN fecha_final IS NOT NULL THEN 
								view_usuarios_atendidos.fecha_hora_atencion <= fecha_final
							ELSE 
								view_usuarios_atendidos.fecha_hora_atencion IS NOT NULL
							END)
				)
	GROUP BY	view_usuarios_registrados.usuario_id;		

	CREATE OR REPLACE TEMPORARY TABLE tmp_count_poblacion
	SELECT		tmp_poblacion.tipo_usuario_id,
				tmp_poblacion.tipo_usuario,
				COUNT(*) AS cantidad
	FROM 		tmp_poblacion
	GROUP BY	tmp_poblacion.tipo_usuario_id
	UNION
	SELECT		tipos_usuario.id AS tipo_usuario_id,
				tipos_usuario.tipo_usuario,
				0 AS cantidad
	FROM		tipos_usuario
	WHERE		tipos_usuario.id NOT IN (SELECT tipo_usuario_id FROM tmp_poblacion);

	SELECT		SUM(tmp_count_poblacion.cantidad) INTO total_poblacion
	FROM		tmp_count_poblacion;

	CREATE OR REPLACE TEMPORARY TABLE tmp_count_poblacion_atendida
	SELECT 		tbl.tipo_usuario_id,
				tbl.tipo_usuario,
				COUNT(*) AS cantidad
	FROM		(SELECT		*
				 FROM		view_usuarios_atendidos
				 GROUP BY	view_usuarios_atendidos.usuario_id) AS tbl
	WHERE		CASE WHEN fecha_inicial IS NOT NULL THEN 
					tbl.fecha_hora_atencion >= fecha_inicial
				ELSE 
					tbl.fecha_hora_atencion IS NOT NULL
				END
	AND			CASE WHEN fecha_final IS NOT NULL THEN 
					tbl.fecha_hora_atencion <= fecha_final
				ELSE 
					tbl.fecha_hora_atencion IS NOT NULL
				END	
	GROUP BY	tbl.tipo_usuario_id
	UNION
	SELECT		tipos_usuario.id AS tipo_usuario_id,
				tipos_usuario.tipo_usuario,
				0 AS cantidad
	FROM		tipos_usuario
	WHERE		tipos_usuario.id NOT IN (SELECT 	view_usuarios_atendidos.tipo_usuario_id 
										 FROM 		view_usuarios_atendidos
										 WHERE		CASE WHEN fecha_inicial IS NOT NULL THEN 
														view_usuarios_atendidos.fecha_hora_atencion >= fecha_inicial
													ELSE 
														view_usuarios_atendidos.fecha_hora_atencion IS NOT NULL
													END
										 AND		CASE WHEN fecha_final IS NOT NULL THEN 
														view_usuarios_atendidos.fecha_hora_atencion <= fecha_final
													ELSE 
														view_usuarios_atendidos.fecha_hora_atencion IS NOT NULL
													END
										 GROUP BY	view_usuarios_atendidos.tipo_usuario_id);
		
	CREATE OR REPLACE TEMPORARY TABLE tmp_result
	SELECT		tcpa.tipo_usuario_id,
				tcpa.tipo_usuario,
				tcpa.cantidad AS atendidos,
				tcp.cantidad - tcpa.cantidad AS no_atendidos,
				tcp.cantidad AS poblacion,
				total_poblacion
	FROM		tmp_count_poblacion_atendida tcpa
	JOIN		tmp_count_poblacion tcp
	ON			tcpa.tipo_usuario_id = tcp.tipo_usuario_id;

	IF isTotal THEN
		SELECT 	SUM(atendidos) AS atendidos,
				SUM(no_atendidos) AS no_atendidos,
				SUM(poblacion) AS poblacion
		FROM	tmp_result;
	ELSE
		SELECT 	*
		FROM	tmp_result;
	END IF;

	DROP TEMPORARY TABLE IF EXISTS tmp_poblacion;
	DROP TEMPORARY TABLE IF EXISTS tmp_count_poblacion;
	DROP TEMPORARY TABLE IF EXISTS tmp_count_poblacion_atendida;
	DROP TEMPORARY TABLE IF EXISTS tmp_result;
END$$
DELIMITER ;

# Crear procedimiento "insert_continente"
DROP PROCEDURE IF EXISTS insert_continente;
DELIMITER $$
$$
CREATE PROCEDURE insert_continente(continente VARCHAR(15))
BEGIN
	DECLARE continente_id INT UNSIGNED DEFAULT NULL;

	SET continente_id = get_continente_id(TRIM(continente));

	IF continente_id IS NULL THEN
		INSERT INTO continentes(continente)
		VALUES(TRIM(continente));
	END IF;
END$$
DELIMITER ;

# Crear procedimiento "insert_pais"
DROP PROCEDURE IF EXISTS insert_pais;
DELIMITER $$
$$
CREATE PROCEDURE insert_pais(pais VARCHAR(45))
BEGIN
	DECLARE pais_id INT UNSIGNED DEFAULT NULL;

	SET pais_id = get_pais_id(TRIM(pais));

	IF pais_id IS NULL THEN
		INSERT INTO paises(pais)
		VALUES(TRIM(pais));
	END IF;
END$$
DELIMITER ;

# Crear procedimiento "insert_pais_continente"
DROP PROCEDURE IF EXISTS insert_pais_continente;
DELIMITER $$
$$
CREATE PROCEDURE insert_pais_continente(pais VARCHAR(45), continente VARCHAR(15))
BEGIN
	DECLARE pais_id INT UNSIGNED DEFAULT NULL;
	DECLARE continente_id INT UNSIGNED DEFAULT NULL;
	DECLARE pais_continente_id INT UNSIGNED DEFAULT NULL;

	CALL insert_pais(TRIM(pais));
	CALL insert_continente(TRIM(continente));

	SET pais_id	= get_pais_id(TRIM(pais));	
	SET continente_id = get_continente_id(TRIM(continente));	

	SELECT 	id INTO pais_continente_id
	FROM	paises_continentes
	WHERE	paises_continentes.pais_id = pais_id
	AND		paises_continentes.continente_id = continente_id
	LIMIT	1;

	IF pais_continente_id IS NULL THEN
		INSERT INTO paises_continentes(pais_id, continente_id)
		VALUES(pais_id, continente_id);
	END IF;
END$$
DELIMITER ;

/*
 * Funciones
 */
# Crear función "get_sexo_id"
DROP FUNCTION IF EXISTS get_sexo_id;
DELIMITER $$
CREATE FUNCTION get_sexo_id (sexo VARCHAR(9))
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE sexo_id INT UNSIGNED DEFAULT NULL;
    
    SELECT  sexos.id INTO sexo_id
    FROM    sexos
    WHERE   sexos.sexo = TRIM(sexo);    
RETURN sexo_id;
END$$
DELIMITER ;

# Crear función "get_persona_id"
DROP FUNCTION IF EXISTS get_persona_id;
DELIMITER $$
CREATE FUNCTION get_persona_id (documento_identidad VARCHAR(15))
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE persona_id INT UNSIGNED DEFAULT NULL;
     
    SELECT  personas.id INTO persona_id
    FROM    personas
    WHERE   personas.documento_identidad = TRIM(documento_identidad)
    LIMIT   1;
RETURN persona_id;
END$$
DELIMITER ;

# Crear función "get_usuario_id"
DROP FUNCTION IF EXISTS get_usuario_id;
DELIMITER $$
CREATE FUNCTION get_usuario_id (
					persona_id INT UNSIGNED,
					isActivo BOOLEAN)
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE usuario_id INT UNSIGNED DEFAULT NULL;
    
    SELECT  usuarios.id INTO usuario_id
    FROM    usuarios
    WHERE   usuarios.persona_id = persona_id
    AND		CASE WHEN isActivo IS NOT NULL THEN
    			usuarios.activo = isActivo
			ELSE
				usuarios.activo IS NOT NULL
			END
    LIMIT   1;
RETURN usuario_id;
END$$
DELIMITER ;

# Crear función "get_comida_id"
DROP FUNCTION IF EXISTS get_comida_id;
DELIMITER $$
CREATE FUNCTION get_comida_id (comida VARCHAR(9))
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE comida_id INT UNSIGNED DEFAULT NULL;
   	DECLARE _comida VARCHAR(9) DEFAULT NULL;
      	    
    SELECT  comidas.id INTO comida_id
    FROM    comidas
    WHERE   comidas.comida = TRIM(comida)
    LIMIT   1;
RETURN comida_id;
END$$
DELIMITER ;

# Crear función "get_tipo_usuario_id"
DROP FUNCTION IF EXISTS get_tipo_usuario_id;
DELIMITER $$
CREATE FUNCTION get_tipo_usuario_id (tipo_usuario VARCHAR(15))
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE tipo_usuario_id INT UNSIGNED DEFAULT NULL;
   	DECLARE _tipo_usuario VARCHAR(15) DEFAULT NULL;
    
    SELECT  tipos_usuario.id INTO tipo_usuario_id
    FROM    tipos_usuario
    WHERE   tipos_usuario.tipo_usuario = TRIM(tipo_usuario);    
RETURN tipo_usuario_id;
END$$
DELIMITER ;

# Crear función "get_continente_id"
DROP FUNCTION IF EXISTS get_continente_id;
DELIMITER $$
CREATE FUNCTION get_continente_id (continente VARCHAR(15))
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE continente_id INT UNSIGNED DEFAULT NULL;
    
    SELECT  continentes.id INTO continente_id
    FROM    continentes
    WHERE   continentes.continente = TRIM(continente)
   	LIMIT	1;        
RETURN continente_id;
END$$
DELIMITER ;

# Crear función "get_pais_id"
DROP FUNCTION IF EXISTS get_pais_id;
DELIMITER $$
CREATE FUNCTION get_pais_id (pais VARCHAR(45))
RETURNS INTEGER
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE pais_id INT UNSIGNED DEFAULT NULL;
    
    SELECT  paises.id INTO pais_id
    FROM    paises
    WHERE   paises.pais = TRIM(pais)
   	LIMIT	1;    
RETURN pais_id;
END$$
DELIMITER ;

/*
 * Precarga de datos
 */

START TRANSACTION;

# Sexos
CALL insert_sexo("Masculino");
CALL insert_sexo("Femenino");

# Comensales
CALL insert_tipo_usuario("Estudiante");
CALL insert_tipo_usuario("Trabajador");
CALL insert_tipo_usuario("Usuario externo");

# Comidas
CALL insert_comida("Desayuno", '06:00:00', '07:00:00');
CALL insert_comida("Almuerzo", '12:00:00', '13:00:00');
CALL insert_comida("Cena", '18:00:00', '19:00:00');

# Paises y continentes
CALL insert_pais_continente('Por determinar','Por determinar');
CALL insert_pais_continente('Angola','África');
CALL insert_pais_continente('Argelia','África');
CALL insert_pais_continente('Benin','África');
CALL insert_pais_continente('Botsuana','África');
CALL insert_pais_continente('Burkina Faso','África');
CALL insert_pais_continente('Burundi','África');
CALL insert_pais_continente('Cabo Verde','África');
CALL insert_pais_continente('Camerún','África');
CALL insert_pais_continente('Chad','África');
CALL insert_pais_continente('Comoras','África');
CALL insert_pais_continente('Costa De Marfil','África');
CALL insert_pais_continente('Egipto','África');
CALL insert_pais_continente('Eritrea','África');
CALL insert_pais_continente('Etiopía','África');
CALL insert_pais_continente('Gabón','África');
CALL insert_pais_continente('Gambia','África');
CALL insert_pais_continente('Ghana','África');
CALL insert_pais_continente('Guinea','África');
CALL insert_pais_continente('Guinea Ecuatorial','África');
CALL insert_pais_continente('Guinea-Bissau','África');
CALL insert_pais_continente('Kenia','África');
CALL insert_pais_continente('Lesoto','África');
CALL insert_pais_continente('Liberia','África');
CALL insert_pais_continente('Libia','África');
CALL insert_pais_continente('Madagascar','África');
CALL insert_pais_continente('Malaui','África');
CALL insert_pais_continente('Mali','África');
CALL insert_pais_continente('Marruecos','África');
CALL insert_pais_continente('Mauricio','África');
CALL insert_pais_continente('Mauritania','África');
CALL insert_pais_continente('Mozambique','África');
CALL insert_pais_continente('Namibia','África');
CALL insert_pais_continente('Níger','África');
CALL insert_pais_continente('Nigeria','África');
CALL insert_pais_continente('República Centroafricana','África');
CALL insert_pais_continente('República Del Congo','África');
CALL insert_pais_continente('República Democrática Del Congo','África');
CALL insert_pais_continente('Ruanda','África');
CALL insert_pais_continente('Santo Tomé Y Príncipe','África');
CALL insert_pais_continente('Senegal','África');
CALL insert_pais_continente('Seychelles','África');
CALL insert_pais_continente('Sierra Leona','África');
CALL insert_pais_continente('Somalia','África');
CALL insert_pais_continente('Suazilandia','África');
CALL insert_pais_continente('Sudáfrica','África');
CALL insert_pais_continente('Sudán','África');
CALL insert_pais_continente('Sudán Del Sur','África');
CALL insert_pais_continente('Tanzania','África');
CALL insert_pais_continente('Togo','África');
CALL insert_pais_continente('Túnez','África');
CALL insert_pais_continente('Uganda','África');
CALL insert_pais_continente('Yibuti','África');
CALL insert_pais_continente('Zambia','África');
CALL insert_pais_continente('Zimbabue','África');
CALL insert_pais_continente('Antigua Y Barbuda','América');
CALL insert_pais_continente('Argentina','América');
CALL insert_pais_continente('Bahamas','América');
CALL insert_pais_continente('Barbados','América');
CALL insert_pais_continente('Belice','América');
CALL insert_pais_continente('Bolivia','América');
CALL insert_pais_continente('Brasil','América');
CALL insert_pais_continente('Canadá','América');
CALL insert_pais_continente('Chile','América');
CALL insert_pais_continente('Colombia','América');
CALL insert_pais_continente('Costa Rica','América');
CALL insert_pais_continente('Cuba','América');
CALL insert_pais_continente('Dominica','América');
CALL insert_pais_continente('Ecuador','América');
CALL insert_pais_continente('El Salvador','América');
CALL insert_pais_continente('Estados Unidos','América');
CALL insert_pais_continente('Granada','América');
CALL insert_pais_continente('Guatemala','América');
CALL insert_pais_continente('Guyana','América');
CALL insert_pais_continente('Haití','América');
CALL insert_pais_continente('Honduras','América');
CALL insert_pais_continente('Jamaica','América');
CALL insert_pais_continente('México','América');
CALL insert_pais_continente('Nicaragua','América');
CALL insert_pais_continente('Panamá','América');
CALL insert_pais_continente('Paraguay','América');
CALL insert_pais_continente('Perú','América');
CALL insert_pais_continente('Puerto Rico','América');
CALL insert_pais_continente('República Dominicana','América');
CALL insert_pais_continente('San Cristóbal Y Nieves','América');
CALL insert_pais_continente('San Vicente Y Las Granadinas','América');
CALL insert_pais_continente('Santa Lucía','América');
CALL insert_pais_continente('Surinam','América');
CALL insert_pais_continente('Trinidad Y Tobago','América');
CALL insert_pais_continente('Uruguay','América');
CALL insert_pais_continente('Venezuela','América');
CALL insert_pais_continente('Afganistán','Asia');
CALL insert_pais_continente('Arabia Saudita','Asia');
CALL insert_pais_continente('Bangladés','Asia');
CALL insert_pais_continente('Baréin','Asia');
CALL insert_pais_continente('Brunei','Asia');
CALL insert_pais_continente('Bután','Asia');
CALL insert_pais_continente('Camboya','Asia');
CALL insert_pais_continente('Catar','Asia');
CALL insert_pais_continente('China','Asia');
CALL insert_pais_continente('Chipre','Asia');
CALL insert_pais_continente('Corea Del Norte','Asia');
CALL insert_pais_continente('Corea Del Sur','Asia');
CALL insert_pais_continente('Emiratos Arabes Unidos','Asia');
CALL insert_pais_continente('Filipinas','Asia');
CALL insert_pais_continente('India','Asia');
CALL insert_pais_continente('Indonesia','Asia');
CALL insert_pais_continente('Irán','Asia');
CALL insert_pais_continente('Iraq','Asia');
CALL insert_pais_continente('Israel','Asia');
CALL insert_pais_continente('Japón','Asia');
CALL insert_pais_continente('Jordania','Asia');
CALL insert_pais_continente('Kazajistán','Asia');
CALL insert_pais_continente('Kirguistán','Asia');
CALL insert_pais_continente('Kuwait','Asia');
CALL insert_pais_continente('Laos','Asia');
CALL insert_pais_continente('Líbano','Asia');
CALL insert_pais_continente('Malasia','Asia');
CALL insert_pais_continente('Maldivas','Asia');
CALL insert_pais_continente('Mongolia','Asia');
CALL insert_pais_continente('Myanmar (Birmania)','Asia');
CALL insert_pais_continente('Nepal','Asia');
CALL insert_pais_continente('Omán','Asia');
CALL insert_pais_continente('Pakistán','Asia');
CALL insert_pais_continente('Palestina','Asia');
CALL insert_pais_continente('Siria','Asia');
CALL insert_pais_continente('Sri Lanka','Asia');
CALL insert_pais_continente('Tailandia','Asia');
CALL insert_pais_continente('Tayikistán','Asia');
CALL insert_pais_continente('Timor Oriental','Asia');
CALL insert_pais_continente('Turkmenistán','Asia');
CALL insert_pais_continente('Turquía','Asia');
CALL insert_pais_continente('Uzbekistán','Asia');
CALL insert_pais_continente('Vietnam','Asia');
CALL insert_pais_continente('Yemen','Asia');
CALL insert_pais_continente('Albania','Europa');
CALL insert_pais_continente('Alemania','Europa');
CALL insert_pais_continente('Andorra','Europa');
CALL insert_pais_continente('Armenia','Europa');
CALL insert_pais_continente('Austria','Europa');
CALL insert_pais_continente('Azerbaiyán','Europa');
CALL insert_pais_continente('Bélgica','Europa');
CALL insert_pais_continente('Bielorrusia','Europa');
CALL insert_pais_continente('Bosnia Y Herzegovina','Europa');
CALL insert_pais_continente('Bulgaria','Europa');
CALL insert_pais_continente('Croacia','Europa');
CALL insert_pais_continente('Dinamarca','Europa');
CALL insert_pais_continente('Eslovaquia','Europa');
CALL insert_pais_continente('Eslovenia','Europa');
CALL insert_pais_continente('España','Europa');
CALL insert_pais_continente('Estonia','Europa');
CALL insert_pais_continente('Finlandia','Europa');
CALL insert_pais_continente('Francia','Europa');
CALL insert_pais_continente('Georgia','Europa');
CALL insert_pais_continente('Grecia','Europa');
CALL insert_pais_continente('Hungría','Europa');
CALL insert_pais_continente('Irlanda','Europa');
CALL insert_pais_continente('Islandia','Europa');
CALL insert_pais_continente('Italia','Europa');
CALL insert_pais_continente('Letonia','Europa');
CALL insert_pais_continente('Liechtenstein','Europa');
CALL insert_pais_continente('Lituania','Europa');
CALL insert_pais_continente('Luxemburgo','Europa');
CALL insert_pais_continente('Malta','Europa');
CALL insert_pais_continente('Moldavia','Europa');
CALL insert_pais_continente('Mónaco','Europa');
CALL insert_pais_continente('Montenegro','Europa');
CALL insert_pais_continente('Noruega','Europa');
CALL insert_pais_continente('Países Bajos','Europa');
CALL insert_pais_continente('Polonia','Europa');
CALL insert_pais_continente('Portugal','Europa');
CALL insert_pais_continente('Reino Unido','Europa');
CALL insert_pais_continente('República Checa','Europa');
CALL insert_pais_continente('República De Macedonia','Europa');
CALL insert_pais_continente('Rumania','Europa');
CALL insert_pais_continente('Rusia','Europa');
CALL insert_pais_continente('San Marino','Europa');
CALL insert_pais_continente('Serbia','Europa');
CALL insert_pais_continente('Suecia','Europa');
CALL insert_pais_continente('Suiza','Europa');
CALL insert_pais_continente('Ucrania','Europa');
CALL insert_pais_continente('Australia','Oceanía');
CALL insert_pais_continente('Fiyi','Oceanía');
CALL insert_pais_continente('Islas Marshall','Oceanía');
CALL insert_pais_continente('Islas Salomón','Oceanía');
CALL insert_pais_continente('Kiribati','Oceanía');
CALL insert_pais_continente('Micronesia','Oceanía');
CALL insert_pais_continente('Nauru','Oceanía');
CALL insert_pais_continente('Nueva Zelanda','Oceanía');
CALL insert_pais_continente('Palaos','Oceanía');
CALL insert_pais_continente('Papúa Nueva Guinea','Oceanía');
CALL insert_pais_continente('Samoa','Oceanía');
CALL insert_pais_continente('Tonga','Oceanía');
CALL insert_pais_continente('Tuvalu','Oceanía');
CALL insert_pais_continente('Vanuatu','Oceanía');

# Prueba. No es permanente
# Personas 
/*
CALL insert_persona("V11111111","Hermógenes","Daniel","Cuellar","Regalado",get_sexo_id("Masculino"),"1984-12-08","04141111111","correo1@gmail.com");
CALL insert_persona("V22222222","Ercilia","Nohemira","Fajardo","Torres",get_sexo_id("Femenino"),"1964-09-18","04242222222","correo2@gmail.com");
CALL insert_persona("V33333333","Odín","Alonso","Archuleta","Zaragoza",get_sexo_id("Masculino"),"1961-01-04","04163333333","correo3@yahoo.com");
CALL insert_persona("V44444444","Xenia","Margarita","Solórzano","Villagómez",get_sexo_id("Femenino"),"1964-09-18","04244444444","correo4@gmail.com");
CALL insert_persona("V55555555","Ezechiel","Pantoño","Lara","Ballesteros",get_sexo_id("Masculino"),"1961-01-04","04165555555","correo5@yahoo.com");
CALL insert_persona("V66666666","Chloe","Del Valle","Velasco","Jáquez",get_sexo_id("Femenino"),"1981-01-05","04166666666","correo6@yahoo.com");
# Usuarios
CALL insert_usuario(get_persona_id("V11111111"), get_tipo_usuario_id("Trabajador"), TRUE);
CALL insert_usuario(get_persona_id("V22222222"), get_tipo_usuario_id("Trabajador"), TRUE);
CALL insert_usuario(get_persona_id("V33333333"), get_tipo_usuario_id("Estudiante"), TRUE);
CALL insert_usuario(get_persona_id("V44444444"), get_tipo_usuario_id("Estudiante"), TRUE);
CALL insert_usuario(get_persona_id("V55555555"), get_tipo_usuario_id("Usuario externo"), TRUE);
CALL insert_usuario(get_persona_id("V66666666"), get_tipo_usuario_id("Usuario externo"), TRUE);

CALL insert_usuario_with_all_data("V11111111","Hermógenes","Daniel","Cuellar","Regalado","Masculino","1984-12-08","04141111111","correo1@gmail.com","Trabajador",TRUE);
CALL insert_usuario_with_all_data("V22222222","Ercilia","Nohemira","Fajardo","Torres","Femenino","1964-09-18","04242222222","correo2@gmail.com","Trabajador",TRUE);
CALL insert_usuario_with_all_data("V33333333","Odín","Alonso","Archuleta","Zaragoza","Masculino","1961-01-04","04163333333","correo3@yahoo.com","Estudiante",TRUE);
CALL insert_usuario_with_all_data("V44444444","Xenia","Margarita","Solórzano","Villagómez","Femenino","1964-09-18","04244444444","correo4@gmail.com","Estudiante",TRUE);
CALL insert_usuario_with_all_data("V55555555","Ezechiel","Pantoño","Lara","Ballesteros","Masculino","1961-01-04","04165555555","correo5@yahoo.com","Usuario externo",TRUE);
CALL insert_usuario_with_all_data("V66666666","Chloe","Del Valle","Velasco","Jáquez","Femenino","1981-01-05","04166666666","correo6@yahoo.com","Usuario externo",TRUE);

# Servicios
CALL insert_servicio("V11111111", get_comida_id("Desayuno"));
CALL insert_servicio("V11111111", get_comida_id("Almuerzo"));
CALL insert_servicio("V11111111", get_comida_id("Cena"));
CALL insert_servicio("V22222222", get_comida_id("Desayuno"));
CALL insert_servicio("V22222222", get_comida_id("Almuerzo"));
CALL insert_servicio("V22222222", get_comida_id("Cena"));
CALL insert_servicio("V33333333", get_comida_id("Almuerzo"));
CALL insert_servicio("V33333333", get_comida_id("Cena"));
CALL insert_servicio("V55555555", get_comida_id("Almuerzo"));


UPDATE 	servicios
SET		servicios.fecha_hora_atencion = TIMESTAMP(DATE_FORMAT(servicios.fecha_hora_atencion, '%Y-%m-%d 06:%i:%s'))
WHERE	servicios.comida_id = 1;

UPDATE 	servicios
SET		servicios.fecha_hora_atencion = TIMESTAMP(DATE_FORMAT(servicios.fecha_hora_atencion, '%Y-%m-%d 12:%i:%s'))
WHERE	servicios.comida_id = 2;

UPDATE 	servicios
SET		servicios.fecha_hora_atencion = TIMESTAMP(DATE_FORMAT(servicios.fecha_hora_atencion, '%Y-%m-%d 18:%i:%s'))
WHERE	servicios.comida_id = 3;
*/

# Trabajadores
CALL insert_usuario_with_all_data("V1157765","Alcángel","","Tonito","","Masculino","1937-04-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V3557644","Ángel","Custodio","Rodríguez","Varela","Masculino","1949-02-21","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V3589963","Sara","Coromoto","Puentes","De Cristiani","Femenino","1953-06-25","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V3731827","José","De Jesús","Navas","García","Masculino","1951-12-24","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V3888049","Henriqueta","Josefina","Estrada","Ovalles","Femenino","1949-04-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V3973379","Armando","Enrique","Sequera","Sánchez","Masculino","1952-12-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V4430887","Pedro","José","González","","Masculino","1951-11-17","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V4678225","José","De Los Santos","Teran","Trompetero","Masculino","1958-04-04","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V4841407","Alicia","Mercedes","Guerra","Rodríguez","Femenino","1955-06-23","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V4895024","Freddi","Rafael","Fuentes","","Masculino","1957-11-18","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5145795","Gladys","Mireya","Luna","","Femenino","1957-11-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5427671","Ramónalberto","Marquez","Pinango","","Masculino","1955-04-12","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5439923","Manuel","Felipe","Guedez","Aguilar","Masculino","1959-12-20","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5450512","Juana","Isabel","Blanco","Aragort","Femenino","1959-01-06","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5454988","Benito","José","Colina","","Masculino","1957-11-24","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5519935","Marvy","Del Carmen","Torres","Tejera","Femenino","1959-03-30","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5609770","María","","Azcoaga","Elizondo","Femenino","1968-02-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5883468","Nicasia","Del Valle","Reyes","","Femenino","1959-12-14","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V5967209","Yajaira","Coromoto","Caldarulo","Torres","Femenino","1959-10-31","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6045949","Elizabeth","Maria","Marcano","De Marin","Femenino","1961-12-27","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6079459","Alberto","","Jaimes","Niño","Masculino","1962-01-28","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6134618","Alexis","Aguilera","Leon","","Masculino","1962-05-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6191060","Ana","Yadira","Montenegro","","Femenino","1966-10-10","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6213243","Genaro","","Guillen","","Masculino","1961-06-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6220113","Roberto","José","Zamora","Rivero","Masculino","1967-04-17","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6250181","Ingrid","Mariela","González","Herrera","Femenino","1964-07-30","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6252139","Freddy","Alexis","Castro","Machado","Masculino","1965-04-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6282628","Javier","Antonio","Madrid","Vásquez","Masculino","1963-10-14","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6304193","Eddy","Josefina","León","De Zarrameda","Femenino","1968-05-20","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6307026","José","Gregorio","Alcalá","Matobre","Masculino","1966-05-04","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6333981","Olga","Teresa","Requena","Bravo","Femenino","1965-04-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6348198","Ángel","Rafael","Cabrices","García","Masculino","1966-04-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6353742","Adelino","Antonio","Oliveira","","Masculino","1961-03-31","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6392430","Juan","Carlos","Lovera","Castillo","Masculino","1961-01-04","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6464145","Daniel","Ignacio","Grimán","Peña","Masculino","1963-02-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6482787","Miguel","Antonio","Alfonzo","Díaz","Masculino","1963-08-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6814955","Ivonne","Mariela","Serrano","Rodríguez","Femenino","1964-12-11","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6845014","Henry","Rafael","Monzon","","Masculino","1963-08-19","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6862244","Reinaldo","Alexis","Escobar","Camejo","Masculino","1965-11-19","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6871230","Daniel","Vicente","Luque","Ordoñez","Masculino","1965-08-22","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6878810","Carolina","Briggith","Avilán","","Femenino","1966-02-04","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6888471","José","Luis","Bolíivar","Camacho","Masculino","1964-09-18","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6892952","Frank","Eduardo","González","","Masculino","1966-06-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6906049","María","Del Carmen","Toyo","Guanares","Femenino","1966-05-27","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V6960538","Wilfredo","Martín","García","Oquendo","Masculino","1966-05-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V7599158","Ligia","Eneida","Vargas","De Vargas","Femenino","1965-01-18","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V7663593","Ana","Yelena","Guarate","Echenique","Femenino","1965-04-16","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V7879953","Paula","Teresa","Canelones","Segovia","Femenino","1959-06-29","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V8042564","Josefina","Marquina","Alarcon","","Femenino","1962-11-11","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V9001880","Gerson","José","Bozo","","Masculino","1960-07-21","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V9353860","Carmen","Alicia","Ramírez","Redondo","Femenino","1965-10-29","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V9376611","Perpetua","Del Socorro","Urbina","Urbina","Femenino","1963-06-27","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V9895657","Migdalys","Del Valle","Bonilla","Romero","Femenino","1966-12-30","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V9895658","Rosirys","Dolores","Bonilla","Romero","Femenino","1964-09-18","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V9979018","Eddys","Teresa","Barreto","","Femenino","1962-10-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10067146","Oswaldo","Antonio","Osto","","Masculino","1964-12-22","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10217538","Marcial","Agustín","Ramírez","Ramírez","Masculino","1969-07-03","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10217725","Ysledys","Beatriz","Rodríguez","Villarroel","Femenino","1970-06-10","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10328667","Isabe","Uvina","Merlo","Varona","Femenino","1972-01-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10351435","Johnny","Leonardo","Prieto","Castro","Masculino","1969-08-19","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10375823","Luz","Alba","León","Carrillo","Femenino","1969-03-13","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10484061","Milagros","Josefina","Tayupo","Liendo","Femenino","1971-01-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10522910","Neida","Aleida","Natera","Vallenilla","Femenino","1969-08-27","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10538266","Alix","Gabriela","Tejada","Amado","Femenino","1971-06-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10756281","Gustavo","Adolfo","Contreras","","Masculino","1971-04-22","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10780981","José","Jhonny","Peña","Rojas","Masculino","1972-01-29","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10811120","Rafael","","Aguilera","León","Masculino","1971-12-31","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10830955","Ricarda","Tibisay","Sequea","Rodríguez","Femenino","1967-03-25","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10881105","Willians","De Jesús","Sánchez","Brito","Masculino","1969-04-11","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10977771","José","Rafael","González","Herrera","Masculino","1965-09-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10988174","Ángel","","Quintero","","Masculino","1969-01-18","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V10991838","Marisela","","Velasquez","Farfan","Femenino","1973-06-11","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11028696","Ana","Luisa","Urbina","","Femenino","1969-02-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11029165","Roberto","Antonio","Naranjo","Hernández","Masculino","1965-09-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11043448","Carlos","José","Valladares","Leal","Masculino","1972-03-12","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11160875","Mileidy","Lourdes","Navas","","Femenino","1971-02-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11182061","Magaly","Coromoto","Colmenares","Sanoja","Femenino","1971-08-17","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11200636","Janeth","Josefina","Camacho","Gomez","Femenino","1971-09-25","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11615789","Mélida","Del Carmen","Peña","","Femenino","1975-09-04","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11666437","Obdulio","Pineda","Duran","","Masculino","1970-08-21","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11674922","Williams","Antonio","Galíndez","Meléndez","Masculino","1971-10-18","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11703692","Gregorio","José","Briceño","","Masculino","1969-12-31","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11819604","Juan","Antonio","Escalona","Molina","Masculino","1972-12-10","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11833824","Saida","Josefina","Rapozo","","Femenino","1966-01-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11923384","Francia","Eduvigis","Martínezromero","","Femenino","1975-10-16","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V11958366","Ramón","Emiro","Rojas","Toro","Masculino","1974-08-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V12296438","Érika","Mayita","Palacios","Salazar","Femenino","1973-05-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V12298577","Ana","Mercedes","Valladares","Paredes","Femenino","1962-09-24","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V12397092","Hector","Augusto","Gámez","Pérez","Masculino","1977-09-09","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V12397168","Cruz","Alejandro","Castellano","Rodríguez","Masculino","1972-03-07","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V12809069","Grabiel","José","Velasquez","Martinez","Masculino","1977-03-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V12881335","Antonio","Guzmán","Flores","","Masculino","1975-10-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13138506","Stiven","Ramón","Maita","González","Masculino","1977-07-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13275248","Petra","Josefina","Suárez","Gómez","Femenino","1973-09-10","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13308438","Gilberto","Aldana","Sierralta","","Masculino","2022-01-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13432314","Franny","Joel","Sánchez","Arcia","Masculino","1977-08-10","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13564974","Carmen","Katiusca","Hernández","Campos","Femenino","1978-10-28","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13687164","Henrry","Augusto","Galíndez","Meléndez","Masculino","1973-10-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13727891","María","Elena","Alvarado","Márquez","Femenino","1977-09-20","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V13886252","Virginia","Ibeth","Castro","López","Femenino","1962-05-21","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14059420","Santiago","Ramón","Mata","Carrizales","Masculino","1979-12-21","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14157165","Eucaris","Mayorleny","Villamizar","Ramírez","Femenino","1975-04-09","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14157171","Francisco","José","Oropeza","Sulbarán","Masculino","1976-08-25","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14232783","Humberto","José","Caña","Rodríguez","Masculino","1977-12-07","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14260100","Hilda","Mercedes","Medina","De López","Femenino","1979-01-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14368915","Jairo","Aroldo","Contreras","Corredor","Masculino","1980-02-16","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14558855","Emilse","Isabel","Nieves","De Galvis","Femenino","1962-03-09","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14559857","Marvin","Augusta","Romero","Liendo","Femenino","1979-11-17","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14852634","Isabel","Cristina","Vargas","De Rodríguez","Femenino","1980-10-07","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14852672","Yulitsi","Josefina","Rivas","Molero","Femenino","1982-10-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14903397","Franklin","Segundo","Ramírez","Velázco","Masculino","1980-01-04","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V14955042","Carlos","Jesús","Pineda","Farias","Masculino","1977-11-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15026060","Loiwuis","Francisco","Iriarte","Brito","Masculino","1981-12-28","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15067159","Jenny","Thais","Polanco","Navas","Femenino","1978-07-22","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15118579","Kristian","Josefina","Alvarado","Márquez","Femenino","1980-12-16","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15133158","Juan","Carlos","Sanz","Portilla","Masculino","1977-05-20","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15151052","Alejandro","José","Blanco","Fagúndez","Masculino","1982-10-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15179508","María","Consuelo","Raddatz","Gatica","Femenino","1967-07-21","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15519720","Ranmier","Arturo","Bogado","","Masculino","1979-09-23","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15589979","Lorenza","","Barroeta","","Femenino","1979-08-11","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15794334","Tomás","Enrique","Pernalete","Hortegano","Masculino","1979-02-07","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V15804431","Ledis","Yobeysis","Castillo","Mijares","Femenino","1981-04-13","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16006728","Merly","Yaritza","Ravelo","Mayor","Femenino","1982-06-29","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16135528","Deisy","Carolina","Nieves","Flores","Femenino","1983-12-22","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16284547","Adelis","Antonio","Rodríguez","Izaguirre","Masculino","1982-01-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16671352","Roger","Alejandro","Lovera","Bonilla","Masculino","1984-12-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16671493","Ely","María","Rosal","Machado","Femenino","1976-07-07","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16723024","Yraima","Berenice","Oliveros","","Femenino","1979-10-30","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16887950","Tania","Margarita","Colina","Sotillet","Femenino","1984-09-05","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16889840","Andreina","Yamilet","Peña","González","Femenino","1982-03-10","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16909607","Arides","Miguel","Rivera","Silva","Masculino","1973-04-19","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V16952066","Alejandra","Renee","Vivas","González","Femenino","1984-06-03","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V17168493","Yaseimy","Carolina","Nadal","Berroterán","Femenino","1985-10-14","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V17303987","Karin","Yuleima","Parra","López","Femenino","1984-07-06","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V17478308","José","Gregorio","Maya","Cabrices","Masculino","1986-10-31","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V17509351","María","Zenaida","Angulo","Vásquez","Femenino","1986-07-27","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V17578593","María","Luisa","Chacón","De Aldana","Femenino","2022-01-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18026197","Ingrid","Carolina","Pérez","Mora","Femenino","1987-10-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18027022","Marci","Enedina","Rodríguez","Bueno","Femenino","1987-04-25","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18040540","William","Alejandro","Marín","Berroterán","Masculino","1987-03-06","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18245060","Dayan","Ayarit","Leal","Vásquez","Femenino","1982-03-29","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18461389","Ernesto","José","Rapozo","","Masculino","1985-11-19","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18602106","Maryelis","Karina","Rosales","Muñoz","Femenino","1987-01-23","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V18740823","Kiury","Carina","Hernández","Ulacio","Femenino","1985-03-13","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V19649797","Johana","Raquel","Barrios","Gómez","Femenino","1989-07-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V20637184","Karlin","Edday","Echeverria","Luna","Femenino","1993-02-25","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V20753522","Alejandro","Ríos","Castellanos","","Masculino","1992-04-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V21252664","Mayerlin","Lilibeth","Raga","Ortiz","Femenino","1991-09-20","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V21469352","Gisgrey","Andreina","Barrera","Salcedo","Femenino","1994-01-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V21469837","Roberto","Carlos","Fajardo","Arredondo","Masculino","1987-08-28","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V21603351","Bárbara","Vanessa","Barrios","García","Femenino","1993-12-11","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V21724434","María","Fernanda","Castillo","","Femenino","1993-05-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V22359675","María","Margoth","López","Fuentes","Femenino","1959-12-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V23526866","Yairlin","Milagros","Rojas","Rojas","Femenino","1995-11-15","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V23949833","Katherin","Victoria","Ricci","Velázquez","Femenino","1994-06-08","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V24192731","Yohana","Carolina","Arteaga","Reinoza","Femenino","1988-10-02","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V24224528","Eduardo","Javin","Luna","Olivero","Masculino","1975-10-27","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V24876925","Neucaris","Carolina","Velásquez","Brito","Femenino","1991-05-16","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V25228606","Pedro","Manuel","García","Natera","Masculino","1996-09-24","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V25866569","Yeferson","Oswaldo","Ochoa","Navas","Masculino","1995-02-01","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V26478966","Franklin","Elías","Solano","Sarmiento","Masculino","1996-11-23","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V27448954","Gabriela","Del Valle","Pérez","Flores","Femenino","1999-09-06","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V27597677","Diego","Alejandro","Yayes","Mendez","Masculino","2000-05-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V27770558","Briyitt","Del Carmen","Licett","Sánchez","Femenino","2000-12-31","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V27908813","Richard","David","Rojas","Dugarte","Masculino","2000-06-03","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V27916002","Adelina","","Hernández","","Femenino","2001-04-03","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V28330053","Damiri","Yannil","Barreto","Molina","Femenino","1997-06-26","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V30335209","Raicelys","Carolina","Caraucan","Caraucan","Femenino","2001-09-03","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V30455512","David","Josué","Bozo","González","Masculino","2001-08-17","","","Trabajador","Venezuela", TRUE);
CALL insert_usuario_with_all_data("V30636629","Mersy","Candelaria","Vargas","Reyes","Femenino","2001-09-13","","","Trabajador","Venezuela", TRUE);
# Estudiante
CALL insert_usuario_with_all_data("ZN713842","Able","","Chilambe","","Masculino","1997-07-10","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ048299","André","Felipe","Da Silva","Martins","Masculino","2000-10-21","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ147940","Antonia","Clarisse","Vitor","Da Silva","Femenino","2001-07-20","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("G2271192","Benjamin","Emmanuel","Otoo","","Masculino","1999-06-15","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("PP4984987","Bernadin","","Clermont","","Masculino","1996-03-20","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX852589","Brigida Terra","","Moreira","Rozeno","Femenino","1999-05-02","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX852585","Bruna","","Andrade Ribeiro","Da Silva","Femenino","1999-06-15","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("GA336383","Camila","Davila","Azevedo","Costa","Femenino","1994-11-28","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX965751","Carla","","Guedes","Da Silvas Santos","Femenino","1998-01-08","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN666013","Chinyama","","Mulenga","","Masculino","2000-09-11","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN660563","Christone","Sukali","Katema","","Masculino","1998-01-01","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX907398","Claudiane","","Rodrigues","Reis","Femenino","1998-08-26","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX053297","Damires","","Soares","De Sousa","Femenino","2000-03-22","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FP647279","Danilo","","Silva Santos","De Jesus","Masculino","1998-02-08","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX990974","Danubia","Cristina","Braga","","Femenino","1999-11-05","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("CY3939512","Emmanuel","","Louis","Jean","Masculino","1994-12-22","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY573072","Erwani","","Lopes","De Macedo","Femenino","1999-03-20","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX868474","Gabriel","Luis","Carvalho","Silva","Masculino","1998-06-18","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX955288","Gabrieli","","Blau","","Femenino","2001-03-03","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ086758","Geysson","","De Oliveira","Rocha","Masculino","1999-07-16","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN533415","Gift","","Samazaka","","Masculino","1996-04-14","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ128181","Ianka","Sayonara","Da Silva","","Femenino","1996-03-21","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FW334098","Idania","Maria","De Araujo","Costa","Femenino","1997-09-04","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY267061","Igor","","Alves","Dos Santos","Masculino","1996-03-21","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN645785","Janet","","Tembo","","Femenino","2000-04-08","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("PP4866506","Jean","Ricot","Petit","Homme","Masculino","1995-09-10","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY352694","Juliana","","Leite","Da Silva","Femenino","1999-06-13","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ086759","Kalline","","Assunçao","Souza","Femenino","1998-05-01","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ048283","Karolainy","","Lisboa","Bita","Femenino","1999-04-10","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY009688","Keli","Dandara","Lins","","Femenino","2001-06-15","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX486676","Lenin","Kauê","Lerias","De Oliveira Dias","Masculino","1999-02-03","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ048296","Leticia","","De Sousa","Alves","Femenino","1996-12-22","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX969218","Leticia","Maria","Gonçalves","Dos Santos","Femenino","1999-04-16","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX990972","Luana","Teresinha","Andrioli","Ehrenbrink","Femenino","2000-04-27","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY002964","Luzia","","Lima","De Jesus","Femenino","1996-11-13","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN655969","Maambo","Mwanza","Kazoka","","Masculino","1999-08-01","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN581953","Macleod","","Lunkoto","","Masculino","1997-07-17","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY380066","Marcos","","Souza","De Amorim","Masculino","1995-06-04","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN713839","Martha","","Musongo","","Femenino","1999-06-03","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY267992","Mayara","","De Araújo","Souza","Femenino","1996-01-14","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN669267","Moffat","Mulangali","Mwale","","Masculino","1998-07-13","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN713846","Nalumino","","Lienda","","Masculino","1999-08-26","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN678663","Naomi","","Kamukule","","Femenino","2000-07-26","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("CH3530728","Price-Kerlie","Francesca","Lazare","","Femenino","1999-02-23","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX965817","Raimundo","","De Oliveira","Sousa","Masculino","2001-02-23","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FW404600","Raíza","","Witcel","Gasparin","Femenino","1999-07-19","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN651025","Robert","","Mulenga","","Masculino","1997-10-18","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("TN4804276","Rodachca","","St-Juste","","Femenino","2000-01-02","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FZ148484","Romario","","Pereira","Da Silva","Masculino","1997-01-26","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX934658","Roseli","","Silva Vieira","De Sousa","Femenino","2001-02-13","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN713866","Sebastian","","Mulenga","","Masculino","1997-05-12","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FW418815","Sindy","Allen","Cordena","Gauber","Femenino","1997-03-04","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("ZN713800","Teddy","","Sianinda","","Masculino","1999-01-22","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX964045","Thais","Aparecida","Da Silva","","Femenino","2000-01-17","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FY267991","Thalya","Carla","Vieira","De Lima","Femenino","1999-08-31","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX808380","Vanessa","","Almeida","Costa","Femenino","1999-05-08","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("PV4427750","Vincy","","Bijoux","","Femenino","1997-01-19","","","Estudiante","Por determinar", TRUE);
CALL insert_usuario_with_all_data("FX921036","Wenya","","Cruz","Alves","Femenino","1999-11-12","","","Estudiante","Por determinar", TRUE);

# Nuevos estudiantes
CALL insert_usuario_with_all_data("B0700250","Gandaho","","Adricelle","Melaine","Femenino","2001-12-1","","","Estudiante","Benín",TRUE);
CALL insert_usuario_with_all_data("B0725474","Abi","","Olouwakemi","Benedicte","Femenino","2001-12-1","","","Estudiante","Benín",TRUE);
CALL insert_usuario_with_all_data("B0725703","Amali","","Clitandre","","Masculino","1997-12-1","","","Estudiante","Benín",TRUE);
CALL insert_usuario_with_all_data("A3121101","Kambou","Siboyen","Ines","Dorcas","Femenino","2001-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3121899","Yalpougdou","","Amandine","Wendlasida","Femenino","2001-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3123448","Zongo","","Feizatou","","Femenino","2001-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3121486","Rouamba","","Mahamadi","","Masculino","2001-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3121856","Ouedraogo","","Baowendabo","Andre","Masculino","2001-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3121702","Ki","Dintama","Abdel","Nasser","Masculino","2002-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3121484","Ouedraogo","","Pingdawinde","Daniel","Masculino","2001-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("A3121764","Zida","","Mohamed","Moha-Barique","Masculino","2002-12-1","","","Estudiante","Burkina Faso",TRUE);
CALL insert_usuario_with_all_data("RW1056696","Abdelaziz","","Moussa","Bahar","Masculino","2002-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RF5770575","Ahmat","","Dourbane","Ali","Masculino","2000-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RN1391778","Alfadil","","Hamatta","Achene","Masculino","2002-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RX1642555","Amira","","Mahamat","Hassan","Femenino","2000-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("R0477490","Rannenda","","Sirandi","","Femenino","2000-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RI2166802","Souleymane","Abdramane","Adji","Mai","Masculino","2000-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RO7077378","Moufti","","Abdelmouni","","Masculino","2001-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RD9241321","Abdelmahmoud","","Khoudar","Mahamat","Masculino","2001-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("R0478537","Ali","","Egrey","Goukouni","Masculino","1997-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RC2056066","Mahamat","","Ousmane","Haroune","Masculino","1999-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RT7019131","Mahamat","Nour","Brahim","","Masculino","1998-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("RI4089165","Adoum","Saleh","Adoum","Siam","Masculino","1999-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("R0481481","Abakar","","Tahir","Aumar","Masculino","1999-12-1","","","Estudiante","Chad",TRUE);
CALL insert_usuario_with_all_data("P16433884","Muñoz","Montenegro","Anthares","Amanda","Femenino","2002-12-1","","","Estudiante","Chile",TRUE);
CALL insert_usuario_with_all_data("206697660","Rojas","Panay","Haziel","Ángel","Masculino","2001-12-1","","","Estudiante","Chile",TRUE);
CALL insert_usuario_with_all_data("AZ083001","Montoya","Henao","Danna","Mishel","Femenino","2004-12-1","","","Estudiante","Colombia",TRUE);
CALL insert_usuario_with_all_data("C00306616","Abam","Quadé","Dorca","","Femenino","2000-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00309056","Bsumande","Siga","Djiro","Osvaldo","Masculino","2003-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00305385","Cassamá","Djaló","Laidi","Sanu","Femenino","2002-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00279164","Correia","Balaqué","Claudete","","Femenino","2003-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00308935","Turé","","Malam","","Masculino","2000-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00308940","Baldé","","Mara","","Masculino","2001-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00305474","Djú","","Pursina","","Femenino","2001-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00241618","Injai","","Tatiana","Umaro","Femenino","2004-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("C00306306","Nhaga","","Quita","Vaneza","Femenino","2002-12-1","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("P10010352","Ongo","Ayang","Maria","Moneta","Femenino","2000-12-1","","","Estudiante","Guinea Ecuatorial",TRUE);
CALL insert_usuario_with_all_data("F0241260","Nkulu","Bindang","Ceniza","Mbengono","Femenino","1999-12-1","","","Estudiante","Guinea Ecuatorial",TRUE);
CALL insert_usuario_with_all_data("P10023130","Ndong","Nchama","Aurora","Sabina Kiebiyene","Femenino","2000-12-1","","","Estudiante","Guinea Ecuatorial",TRUE);
CALL insert_usuario_with_all_data("P10015658","Ndong","Mangue","Ines","Nzang","Femenino","1998-12-1","","","Estudiante","Guinea Ecuatorial",TRUE);
CALL insert_usuario_with_all_data("P10023127","Enguru","Medja","Alicia","Nchama","Femenino","2000-12-1","","","Estudiante","Guinea Ecuatorial",TRUE);
CALL insert_usuario_with_all_data("TB5459340","Augustin","","Adams","","Masculino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10822772","Paul","","Jean","Edmondson","Masculino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R11482575","Pierre","","Dinaud","","Masculino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PP5515086","Mon","Gerard","Marc","Donald","Masculino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10271140","Dorelus","","Emmanuel","Christopher","Masculino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PP4808098","Santana","","Schnaica","-Celena","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("TB5692789","Artis","","Alissa","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10230164","Compere","","Dachka","","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10275492","Erminus","Yourie","Anne","Saelle","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PL5663327","Noel","","Florence","","Femenino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10826594","Petit","Ned","Hendelle","Slineed","Femenino","2003-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10246586","Pierre","Noel","Geraldine","","Femenino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10001262","Pierre","","Bethnikove","Mirlanda","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R11491783","Simeon","","Rose","Samuelle","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("TB4828536","Moise","","Valery-Anaise","","Femenino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("RM5144018","Decius","","Laure-Nika","","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10051909","Ariste","","Lauriechamma","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("RM5145932","Biem-Aime","","Jeanne","D'Arc-Anna","Femenino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10074703","Simeon","","Faella","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10273160","Pierre","","Oldinx","","Masculino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10000173","Duverne","","Stencia","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10046916","Laine","","Nathaelle","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PL4785091","Alexandre","","Frantz Ii","","Masculino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10840048","Odigé","","Pierre","Richard","Masculino","2002-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("DL5678066","Lormistyl","","Skim’S Jean","Amorse Fils","Masculino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10053062","Vilma","","Roldson","","Masculino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10259157","Jean","Charles","Dacline","","Masculino","2002-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10011791","Janvier","","Katline","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10268118","Fremond","","Natarlie","","Femenino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("GV5674501","Joseph","","Kenchard","","Masculino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10224430","Ostin","","Ney","Mekarly","Masculino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10846501","Civil","","Sindy-Love","","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10872750","Bernard","","Roselaure","","Femenino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("RM5072235","Saintilnor","Witmayeld","Jose","Prepty","Masculino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PV5598969","Joseph","","Telemarque","","Masculino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10018960","Jean","Pierre","Olivier","","Masculino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PL5460863","Brenord","","Samson","","Masculino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10858113","Saint-Julien","","Beverly","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("RM5119182","Dorelien","","Jana","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PP4679743","Jean","","Wilmide","Wilza","Femenino","2002-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10857297","Romual","","Sterline","","Femenino","2002-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10840240","Jean","Baptiste","Sherlandine","","Femenino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10825941","Jean-Baptiste","","Sabina","R.","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10858874","Paul","","Kendaise","","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("RM5143802","Dolcine","","Claudin","","Masculino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10938765","Antoine","","Jenny","","Femenino","2002-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10081025","Guerrier","","Marie","Christ Joelle","Femenino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R11412813","Ferdinand","","Chrishna","","Femenino","2002-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10031988","Jules","","Maquingson","","Masculino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10937520","Petion","","Landie","","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10004389","Merisca","","Wisfla","","Femenino","2000-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10017236","Saintil","","Medjina","","Femenino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10238195","Luc","","Mondy","","Masculino","2001-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10269805","Senatus","","Louna","","Femenino","1998-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("R10103646","St Hilaire","","Jenny-Flore","","Femenino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("PL5707722","Donnat","","Wesly","","Masculino","1999-12-1","","","Estudiante","Haití",TRUE);
CALL insert_usuario_with_all_data("AK1001298","Adano","","Talaso","Salesa","Femenino","2003-12-1","","","Estudiante","Kenia",TRUE);
CALL insert_usuario_with_all_data("AK1052681","Gone","","Bonaya","Adano","Masculino","2002-12-1","","","Estudiante","Kenia",TRUE);
CALL insert_usuario_with_all_data("BK385708","Lemalon","","David","Kareto","Masculino","2000-12-1","","","Estudiante","Kenia",TRUE);
CALL insert_usuario_with_all_data("AB1086278","Macuacua","","Leonilde","Marcelo","Femenino","1997-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB0756931","Mucavele","","Lily","Cardoso","Femenino","1997-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("15AM44092","Rodrigues","","Maira","Magid Pedro","Femenino","2002-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB1077556","Nhambe","","Moisés","Francisco","Masculino","2002-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB0852948","Muterima","","Samir","Abede","Masculino","2000-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB0826679","Carvalho","","Laura","Adriano Lourenco","Femenino","2002-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("15AN63390","Ponte","Bambino De Assunção","Daniel","Limpo","Masculino","1998-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB1035527","Chitula","","Cynthia","Gabriela Armando","Femenino","1997-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB1082998","Janúario","","Janúario","Felisberto","Masculino","2003-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("AB1081409","Baicha","","Fátima","Alí","Femenino","1999-12-1","","","Estudiante","Mozambique",TRUE);
CALL insert_usuario_with_all_data("B00791733","Ajiri","","Fortune","","Femenino","1998-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A11516052","Akawu","","Alex","","Masculino","2000-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("B00227096","Bako","","Fatumi","Mazhi","Femenino","2000-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A12054161","Chukwu","","Favour","Chimeremeze","Femenino","2001-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A12071283","Kanu","Ubah","Ikenna","Marvelous","Masculino","2001-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A10866269","Onwuka","","Chika","Mark","Femenino","2004-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("B00192738","Onwumere","","Oluoma","Judith","Femenino","2001-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("B00129674","Orji","","Emmanuel","Chibueze","Masculino","2000-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("B00567623","Orji","","Ifunanya","Victory","Femenino","2004-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A11345735","Prince","","Kingseed","Chiziterem","Masculino","2005-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A11305898","Arogundade","","Shalom","Omolayo","Femenino","2002-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A11386645","Ewung","","Una-Stella","Bukie","Femenino","2002-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A11373873","Sani","","Zubair","Umar","Masculino","2001-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("B00469104","Yakubu","","Wavah","Vaudi","Masculino","2001-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A11703132","Abbas","","Adam","","Masculino","2002-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A10778740","Ahmad","","Ahmad","Abdurrahman","Masculino","2002-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A12071432","Onwuchekwa","","Michael","Chukwuemeka","Masculino","2003-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("A12054169","Dike","","Peace","Amarachi","Femenino","1999-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("B50125695","Akinloye","","Ridoyat","Aramide","Femenino","2003-12-1","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("ER143425","Vandi","","Fatmata","","Femenino","1998-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER212936","Komeh","","John","","Masculino","1994-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER248766","Thoronka","","John","Lamin","Masculino","2002-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER207749","Deen-Sesay","","Ibrahim","","Masculino","2003-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER234886","Kuyembeh","","Fuad","","Masculino","1999-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER250059","Kamara","","Agness","Salikatu","Femenino","2000-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER220558","Ibrahim","","Saffiatu","Banjoko","Femenino","2004-12-1","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("P00932499","Birmad","","Fadumo","Ahmed","Femenino","1998-12-1","","","Estudiante","Somalia",TRUE);
CALL insert_usuario_with_all_data("P00856277","Dahir","","Bashir","Mohamoud","Masculino","1998-12-1","","","Estudiante","Somalia",TRUE);
CALL insert_usuario_with_all_data("C00293993","Na","Bicamba","Judite","José","Femenino","2002-5-11","","","Estudiante","Guinea Bissau",TRUE);
CALL insert_usuario_with_all_data("P01213931","Sheikh","Ali","Bishar","Ahmed","Masculino","1996-1-1","","","Estudiante","Somalia",TRUE);
CALL insert_usuario_with_all_data("OA0541645","Ebengue - Ngala","","Marise","","Femenino","2000-12-6","","","Estudiante","Congo",TRUE);
CALL insert_usuario_with_all_data("OA0568679","Eta","Stephanie","Astrise","Miria","Femenino","1999-12-6","","","Estudiante","Congo",TRUE);
CALL insert_usuario_with_all_data("OA0441069","Madzou","Yavline","DaãAna","Gloria","Femenino","2003-12-6","","","Estudiante","Congo",TRUE);
CALL insert_usuario_with_all_data("OA0531379","Ngabio","Charles","Axel","Monfils","Masculino","2002-12-6","","","Estudiante","Congo",TRUE);
CALL insert_usuario_with_all_data("OA0573168","Poue","Ndzale","Imys","Byrlon","Masculino","2000-12-6","","","Estudiante","Congo",TRUE);
CALL insert_usuario_with_all_data("OA0512508","Youla","Nsimba","Don-Beni","","Masculino","2002-12-6","","","Estudiante","Congo",TRUE);
CALL insert_usuario_with_all_data("P10004644","Ndong","Avomo","Leoncio","Jordan Ebana","Masculino","1999-12-6","","","Estudiante","Guinea Ecuatorial",TRUE);
CALL insert_usuario_with_all_data("TB4908936","Jean","","Danis","","Masculino","2000-12-6","","","Estudiante","Haitã",TRUE);
CALL insert_usuario_with_all_data("A12055280","Ogbu","","John","Chibuike","Masculino","2003-12-6","","","Estudiante","Nigeria",TRUE);
CALL insert_usuario_with_all_data("ER258604","Fannah","Sylvanus","Major","Joe","Masculino","2002-12-6","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER221551","Gbao","","Abdul","","Masculino","1997-12-6","","","Estudiante","Sierra Leona",TRUE);
CALL insert_usuario_with_all_data("ER154125","Max-Morgan","","Gideon","Elisha","Masculino","1999-12-6","","","Estudiante","Sierra Leona",TRUE);

COMMIT;