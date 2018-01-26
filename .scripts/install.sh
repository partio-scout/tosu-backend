#!/bin/bash
set -x
openssl aes-256-cbc -K $encrypted_0521f70a21ab_key -iv $encrypted_0521f70a21ab_iv -in deploy.pem.enc -out deploy.pem -d
rm deploy.pem.enc
chmod 600 deploy.pem
mv deploy.pem ~/.travis/id_rsa
