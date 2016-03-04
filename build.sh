#!/bin/sh

set -e

PATH="/opt/aptly_utils/bin/:/opt/debian_utils/bin:$PATH"
NAME="crowd-jaas"
VERSION="1.0.1-b${BUILD_NUMBER}"
DEB="${NAME}_${VERSION}.deb"

echo "Install completed, create debian package"

create_debian_package \
-loglevel=DEBUG \
-version=${VERSION} \
-config=create_debian_package_config.json

echo "Create debian package completed, start upload to aptly"

aptly_upload \
-loglevel=DEBUG \
-url=https://www.benjamin-borbe.de/aptly \
-username=api \
-passwordfile=/etc/aptly_api_password \
-file=${DEB} \
-repo=unstable

echo "Upload completed"

exit 0