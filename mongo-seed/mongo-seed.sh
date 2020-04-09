mongoimport --host mongo --db db --collection partner --type json --file /data.json --jsonArray
mongo --host mongo db --eval 'db.partner.ensureIndex({ "document": 1}, { "unique": true })'
mongo --host mongo db --eval 'db.partner.ensureIndex({ "address": "2dsphere" })'