#!/bin/bash
set -x
eval "$(ssh-agent -s)"
chmod 600 ~/.travis/id_rsa
ssh-add ~/.travis/id_rsa

#git config --global push.default matching
#git remote add deploy ssh://git@$IP:$PORT$DEPLOY_DIR
#git push deploy master

ssh deploy@$IP -p $PORT <<EOF
    sudo service tosu-backend stop
    mv -f /var/www/tosu-backend/build/libs/tosu-backend.jar /var/tosu-apps/tosu-backend.jar
    sudo service tosu-backend start
    cd /var/www
    rm -rf tosu-backend

