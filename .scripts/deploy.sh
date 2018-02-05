#!/bin/bash
set -x
eval "$(ssh-agent -s)"
chmod 600 ~/.travis/id_rsa
ssh-add ~/.travis/id_rsa

#git config --global push.default matching
#git remote add deploy ssh://git@$IP:$PORT$DEPLOY_DIR
#git push deploy master

ssh deploy@$IP -p $PORT <<EOF
#    eval "$(ssh-agent -s)"
#    ssh-add /home/deploy/.ssh/deploy.pem
    cd /var/www/
    git clone https://github.com/partio-scout/tosu-backend.git
    gradle bootRepackage
EOF
