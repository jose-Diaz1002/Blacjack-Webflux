db = db.getSiblingDB("blackjack");

db.players.insertMany([
    {
            name: "John Doe",
            score: 120,
            createdAt: new Date()
        },
        {
            name: "Jane Smith",
            score: 90,
            createdAt: new Date()
        },
        {
            name: "Robert Brown",
            score: 150,
            createdAt: new Date()
        },
        {
            name: "Alice Williams",
            score: 45,
            createdAt: new Date()
        }
]);
