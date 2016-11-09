#!/bin/sh

set -e

PATH="/opt/aptly_utils/bin/:/opt/debian_utils/bin:$PATH"
NAME="crowd-jaas"
VERSION="1.0.1-b${BUILD_NUMBER}"
DEB="${NAME}_${VERSION}.deb"

echo "Install completed, create debian package"

create_debian_package \
-logtostderr \
-v=2 \
-version=${VERSION} \
-config=create_debian_package_config.json

echo "Create debian package completed, start upload to aptly"

aptly_upload \
-logtostderr \
-v=2 \
-url=http://aptly-api.aptly.svc.cluster.local:3845 \
-file=${DEB} \
-repo=unstable

echo "Upload completed"

exit 0
