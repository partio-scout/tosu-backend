#!/bin/bash
set -x
eval "$(ssh-agent -s)"
chmod 600 ~/.travis/id_rsa
ssh-add ~/.travis/id_rsa

#git config --global push.default matching
#git remote add deploy ssh://git@$IP:$PORT$DEPLOY_DIR
#git push deploy master

ssh deploy@$IP -p $PORT <<EOF
    mv /var/www/tosu-backend/build/libs/gs-rest-service-0.1.0.jar /var/www/html/backend.jar
    java -jar /var/www/html/backend.jar &
    cd /var/www
    rm -rf tosu-backend

