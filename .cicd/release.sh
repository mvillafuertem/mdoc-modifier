#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

BASE="$(cd "$(dirname "${0}")" && pwd)"
source "${BASE}"/../.env

echo Running release.sh

CURRENT_BRANCH="$(git rev-parse --abbrev-ref HEAD)"
CURRENT_VERSION="$(cat version.sbt | sed 's/[^"]*"\([^"]*\)".*/\1/')"

if [[ ${CURRENT_BRANCH} == develop && ${CURRENT_VERSION} =~ "-SNAPSHOT" ]];
then
    git tag -a v${CURRENT_VERSION} -m v${CURRENT_VERSION}
    git push origin --tags
elif [[ ${CURRENT_BRANCH} == master ]];
then
    CURRENT_VERSION="$(cat version.sbt | sed 's/[^"]*"\([^"]*\)-SNAPSHOT".*/\1/')"
    git tag -a v${CURRENT_VERSION} -m v${CURRENT_VERSION}
    git push origin --tags
else
    echo "This change doesn't generate new version"
    exit 1
fi