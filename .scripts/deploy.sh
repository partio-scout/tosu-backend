'#!/bin/bash
set -x
eval "$(ssh-agent -s)"
chmod 600 ~/.travis/id_rsa
ssh-add ~/.travis/id_rsa

#git config --global push.default matching
#git remote add deploy ssh://git@$IP:$PORT$DEPLOY_DIR
#git push deploy master

ssh deploy@$IP -p $PORT <<EOF
    cd /var/www
    git clone https://github.com/partio-scout/tosu-backend.git
    cd tosu-backend
    gradle bootRepackage
    mv build/libs/gs-rest-service-0.1.0.jar /var/www/html/backend.jar
    cd /var/www/html
    java -jar backend.jar &
    cd /var/www
    rm -rf tosu-backend
EOF
