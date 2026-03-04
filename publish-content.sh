#!/usr/bin/env bash
# Publishes a ContentPublishedEvent to the content-published Kafka topic.
# Event payload matches the spec in docs/specification.md §5.1.
# Requires the Kafka broker to be reachable at localhost:9094 (docker compose external listener).
#
# Usage:
#   ./publish-content.sh <1-20>
#   KAFKA_BOOTSTRAP=localhost:9092 ./publish-content.sh <1-20>
#
# Content options by number:
#   1  Romance        - Forever in Lisbon
#   2  Romance        - Letters from Kyoto
#   3  Comedy         - My Terrible Roommate
#   4  Comedy         - Office Chaos
#   5  Kids           - Captain Stardust and the Moon Pirates
#   6  Kids           - The Tiny Dragon of Greenwood Forest
#   7  Police         - Iron Coast
#   8  Police         - Night Shift: Seoul
#   9  Documentary    - The Last Glacier
#   10 Documentary    - Inside the Algorithm
#   11 Sci-Fi         - The Last Signal
#   12 Sci-Fi         - Colony Zero
#   13 Drama          - Shadow District
#   14 Drama          - The Weight of Rain
#   15 Thriller       - Forty-Eight Hours
#   16 Historical     - The Silk Road Conspiracy
#   17 Animation      - Pebble & Friends
#   18 Musical        - Broadway or Bust
#   19 Mystery        - The Vanishing Hour
#   20 Fantasy        - Realm of the Forgotten Kings

set -euo pipefail

KAFKA_BOOTSTRAP="${KAFKA_BOOTSTRAP:-localhost:9094}"
TOPIC="content-published"
CONTENT_ID="${1:-}"
TS="2026-03-04T14:00:00Z"

if [[ -z "${CONTENT_ID}" ]]; then
  echo "Usage: $0 <1-20>"
  echo "Run '$0 --list' to see all options."
  exit 1
fi

if [[ "${CONTENT_ID}" == "--list" ]]; then
  grep "^#   [0-9]" "$0"
  exit 0
fi

case "${CONTENT_ID}" in
  1)  MESSAGE='{"contentId":"forever-lisbon-001","title":"Forever in Lisbon","description":"Two strangers meet during a week-long music festival in Lisbon and must decide whether a fleeting connection is worth risking everything they have back home.","genre":"Romance","region":"EU","timestamp":"'"${TS}"'"}';;
  2)  MESSAGE='{"contentId":"letters-kyoto-002","title":"Letters from Kyoto","description":"A Japanese-American woman returns to Kyoto to settle her grandmother'\''s estate and discovers a decades-old love story hidden inside a box of handwritten letters.","genre":"Romance","region":"APAC","timestamp":"'"${TS}"'"}';;
  3)  MESSAGE='{"contentId":"terrible-roommate-003","title":"My Terrible Roommate","description":"A neat-freak architect is forced to share her apartment with her free-spirited cousin for the summer, leading to increasingly absurd domestic disasters.","genre":"Comedy","region":"US","timestamp":"'"${TS}"'"}';;
  4)  MESSAGE='{"contentId":"office-chaos-004","title":"Office Chaos","description":"When a tech startup accidentally hires two people for the same job, both refuse to quit — and the entire office must pick a side.","genre":"Comedy","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  5)  MESSAGE='{"contentId":"captain-stardust-005","title":"Captain Stardust and the Moon Pirates","description":"A brave young girl and her robot dog join the legendary Captain Stardust on a race across the solar system to recover a stolen moon crystal before darkness falls.","genre":"Kids","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  6)  MESSAGE='{"contentId":"tiny-dragon-006","title":"The Tiny Dragon of Greenwood Forest","description":"A very small dragon who cannot breathe fire sets off on a journey to find his roar, making unlikely friends along the way.","genre":"Kids","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  7)  MESSAGE='{"contentId":"iron-coast-007","title":"Iron Coast","description":"A disgraced detective in 1970s Rio de Janeiro uncovers a conspiracy that reaches the highest levels of a military regime, forcing him to choose between survival and justice.","genre":"Police","region":"LATAM","timestamp":"'"${TS}"'"}';;
  8)  MESSAGE='{"contentId":"night-shift-seoul-008","title":"Night Shift: Seoul","description":"An exhausted narcotics detective working the night shift in Seoul stumbles onto a human trafficking ring with ties to a powerful political family.","genre":"Police","region":"APAC","timestamp":"'"${TS}"'"}';;
  9)  MESSAGE='{"contentId":"last-glacier-009","title":"The Last Glacier","description":"Filmed over three years in Greenland and Antarctica, this documentary follows the scientists racing to understand what the disappearance of the world'\''s glaciers means for billions of people.","genre":"Documentary","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  10) MESSAGE='{"contentId":"inside-algorithm-010","title":"Inside the Algorithm","description":"A former tech executive goes on record to expose how recommendation algorithms are engineered to maximize outrage and what it is costing society.","genre":"Documentary","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  11) MESSAGE='{"contentId":"last-signal-011","title":"The Last Signal","description":"When a deep-space probe returns an unexpected transmission, a team of scientists must decide whether humanity is ready for the answer.","genre":"Sci-Fi","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  12) MESSAGE='{"contentId":"colony-zero-012","title":"Colony Zero","description":"The last surviving colony ship loses contact with Earth and its passengers must govern themselves across a 200-year journey to a planet nobody has seen.","genre":"Sci-Fi","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  13) MESSAGE='{"contentId":"shadow-district-013","title":"Shadow District","description":"A political thriller set in a dystopian city where a journalist risks her life to publish the one story the government has spent decades burying.","genre":"Drama","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  14) MESSAGE='{"contentId":"weight-of-rain-014","title":"The Weight of Rain","description":"After a catastrophic flood destroys a small Irish farming community, three generations of the same family must confront long-buried secrets to rebuild their lives.","genre":"Drama","region":"EU","timestamp":"'"${TS}"'"}';;
  15) MESSAGE='{"contentId":"forty-eight-hours-015","title":"Forty-Eight Hours","description":"A hostage negotiator has 48 hours to find a missing child before the trail goes cold — and every suspect she clears leads her closer to someone she trusts.","genre":"Thriller","region":"US","timestamp":"'"${TS}"'"}';;
  16) MESSAGE='{"contentId":"silk-road-016","title":"The Silk Road Conspiracy","description":"Set in 14th-century Central Asia, a young Chinese diplomat uncovers a plot to assassinate trade envoys from five rival empires and must stop a war before it begins.","genre":"Historical","region":"APAC","timestamp":"'"${TS}"'"}';;
  17) MESSAGE='{"contentId":"pebble-friends-017","title":"Pebble & Friends","description":"Pebble the penguin and her colourful friends solve a new mystery every episode in their cozy Antarctic village, learning about kindness and curiosity along the way.","genre":"Animation","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  18) MESSAGE='{"contentId":"broadway-or-bust-018","title":"Broadway or Bust","description":"A small-town choir teacher drags her reluctant students to a national competition in New York City, where big dreams collide with even bigger stage fright.","genre":"Musical","region":"US","timestamp":"'"${TS}"'"}';;
  19) MESSAGE='{"contentId":"vanishing-hour-019","title":"The Vanishing Hour","description":"In a remote Scottish village, a clockmaker disappears at midnight every year on the same date. A forensic archivist arrives to solve the pattern — and vanishes herself.","genre":"Mystery","region":"EU","timestamp":"'"${TS}"'"}';;
  20) MESSAGE='{"contentId":"forgotten-kings-020","title":"Realm of the Forgotten Kings","description":"When a cartographer stumbles upon a map that should not exist, she is pulled into a dying magical empire whose last rulers are fighting to reclaim a throne lost to myth.","genre":"Fantasy","region":"GLOBAL","timestamp":"'"${TS}"'"}';;
  *)
    echo "Error: invalid option '${CONTENT_ID}'. Choose a number between 1 and 20."
    exit 1
    ;;
esac

echo "Publishing content #${CONTENT_ID} to topic '${TOPIC}' on ${KAFKA_BOOTSTRAP}..."
echo "${MESSAGE}" | docker run --rm -i --network host apache/kafka:latest \
  /opt/kafka/bin/kafka-console-producer.sh \
  --bootstrap-server "${KAFKA_BOOTSTRAP}" \
  --topic "${TOPIC}"

echo "Done."
