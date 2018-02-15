#!/bin/bash
set -x
eval "$(ssh-agent -s)"
chmod 600 ~/.travis/id_rsa
ssh-add ~/.travis/id_rsa

#git config --global push.default matching
#git remote add deploy ssh://git@$IP:$PORT$DEPLOY_DIR
#git push deploy master
ssh deploy@$IP -p $PORT <<EOF
    if [[ $TRAVIS_BRANCH = 'master' ]]
    then
        sudo cp -f /var/www/tosu-backend/build/libs/tosu-backend-0.1.0.jar /var/tosu-apps/tosu-backend.jar
        sudo systemctl daemon-reload
        sudo chmod +x /var/tosu-apps/tosu-backend.jar
        sudo service tosu-backend restart
        rm -rf /var/www/tosu-backend
    fi
EOF
