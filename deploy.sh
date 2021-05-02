#!/bin/bash
# aqui se puede poner la logica de despliegue dependiendo del entorno
# que se va a seleccionar para el mismo. Estas lineas son un boceto de
# despliegue pero es para facilidad de peruebas del funcionamiento del
# aplicativo, se requieren otros componentes adicionales dependiendo del
# entorno de despliegues

# creamos el archivo de contraseña en el vuelo, si ya existe lo mantenemos
if [ ! -w "redis_password.txt" ] ; then
    (< /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${1:-32};) > redis_password.txt;
fi

# creamos las imagenes
docker-compose build

# desplegamos en local para verificación, esto no es producción
docker-compose up -d
