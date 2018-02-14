#!/bin/bash
set -x
eval "$(ssh-agent -s)"
chmod 600 ~/.travis/id_rsa
ssh-add ~/.travis/id_rsa

#git config --global push.default matching
#git remote add deploy ssh://git@$IP:$PORT$DEPLOY_DIR
#git push deploy master
if [[ $TRAVIS_BRANCH = 'master' ]]
    ssh deploy@$IP -p $PORT <<EOF
        cd /var/www/
        git clone https://github.com/partio-scout/tosu-backend.git
        cp certificate.p12 /var/www/tosu-backend/src/main/resources/certificate.p12
        cp -f application-production.properties /var/www/tosu-backend/src/main/resources/application-production.properties
        cd tosu-backend
        gradle bootRepackage
    EOF
fi
